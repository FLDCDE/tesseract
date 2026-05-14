package com.fieldcode.tesseract.matrix.cache;

import com.fieldcode.tesseract.*;
import com.fieldcode.tesseract.matrix.DirectionMatrices;

import java.util.Optional;
import java.util.SortedSet;
import java.util.function.Function;


class CacheController implements DirectionMatrixCacheController {

  private final QuickCache quick;
  private final MatrixStore store;
  private final CacheStatisticsMeasure measure;
  private final Function<DirectionMatrixDimensions, PartialDirectionMatrix> fetch;

  private CacheController(DirectionMatrixCacheConfig config, Function<DirectionMatrixDimensions, PartialDirectionMatrix> fetch) {
    this.store = MatrixStore.of(config);
    this.quick = QuickCache.of(config);
    this.fetch = fetch;
    this.measure = CacheStatisticsMeasure.of();
    store.register(this::onStore, this::onEvict);
  }

  static DirectionMatrixCacheController of(DirectionMatrixCacheConfig config, Function<DirectionMatrixDimensions, PartialDirectionMatrix> fetch) {
    return new CacheController(config, fetch);
  }

  private void onStore(QueryableDirectionMatrix queryableDirectionMatrix) {
    quick.putSelf(queryableDirectionMatrix.getHash());
    measure.incrementStore();
  }

  private void onEvict(String s) {
    quick.invalidateStoreKey(s);
    measure.incrementEvict();
  }

  @Override
  public DirectionMatrix get(SortedSet<Location> locations) {
    measure.incrementRequest();
    var request = ImmutableMatrixRequest.of(locations);
    return quickSearch(request).orElseGet(() -> storeSearchOrFetch(request));
  }

  @Override
  public CacheStatistics stat() {
    return measure.getStatistics()
        .withCacheSize(store.size());
  }

  private Optional<DirectionMatrix> quickSearch(MatrixRequest request) {
    var matrix = quick.getStoreKey(request.getHash())
        .map(store::findByHash)
        .map(QueryableDirectionMatrix::getMatrix);

    matrix.ifPresentOrElse(
        m -> measure.incrementQuickHit(),
        measure::incrementQuickMiss
    );

    return matrix;
  }

  private DirectionMatrix storeSearchOrFetch(MatrixRequest request) {
    return store.findByRequest(request)
        .map(this::select)
        .orElseGet(() -> fetchFull(request));
  }

  private DirectionMatrix fetchFull(MatrixRequest request) {
    var matrix = DirectionMatrices.toFullMatrix(fetch.apply(request));
    var hash = store.store(matrix);
    quick.putSelf(hash);

    measure.incrementStoreMiss();
    measure.incrementFullFetch();
    return matrix;
  }

  private DirectionMatrix select(CandidateDirectionMatrix candidate) {

    var request = candidate.getRequest();

    switch (candidate.getSuggestion()) {
      case FULL:
        return fetchFull(request);
      case COVERED:
        return covered(candidate);
      case PARTIAL:
        return partial(candidate);
      default:
        throw new IllegalStateException("Unknown suggestion: " + candidate.getSuggestion());
    }
  }

  private DirectionMatrix partial(CandidateDirectionMatrix candidate) {
    var fetched = fetchPartial(candidate);
    var hash = store.store(fetched);

    quick.putSelf(hash);

    return fetched;
  }

  private DirectionMatrix covered(CandidateDirectionMatrix candidate) {
    var original = candidate.getStoredMatrix();
    measure.incrementStoreHit();
    quick.put(candidate.getRequestHash(), candidate.getStoredMatrixHash());
    return original.getMatrix();
  }

  private DirectionMatrix fetchPartial(CandidateDirectionMatrix candidate) {
    var fetched = candidate.getExtenderMatrices()
        .map(fetch)
        .peek(m -> measure.incrementAllPartialFetch());

    var original = candidate.getStoredMatrix();

    var merged = MatrixMerger.merge(original.getMatrix(), fetched);

    measure.incrementPartialFetch();
    return DirectionMatrices.toFullMatrix(merged);
  }

}
