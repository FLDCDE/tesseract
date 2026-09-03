package com.fieldcode.tesseract.matrix.cache;

import com.fieldcode.tesseract.DirectionMatrix;
import com.fieldcode.tesseract.MatrixStoreConfig;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Ticker;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.RemovalNotification;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

import static com.google.common.base.Preconditions.checkState;
import static java.util.Objects.nonNull;

class MatrixStore {

  private final Cache<String, QueryableDirectionMatrix> store;

  private final Listener<String> evictCallback = Listener.of();
  private final Listener<QueryableDirectionMatrix> storeCallback = Listener.of();

  private MatrixStore(MatrixStoreConfig config) {
    this.store = buildCache(config).build();
  }

  private MatrixStore(MatrixStoreConfig config, Ticker ticker) {
    this.store = buildCache(config)
        .ticker(ticker)
        .build();
  }

  public static MatrixStore of(MatrixStoreConfig config) {
    return new MatrixStore(config);
  }

  @VisibleForTesting
  static MatrixStore of(MatrixStoreConfig config, Ticker ticker) {
    return new MatrixStore(config, ticker);
  }

  private CacheBuilder<String, QueryableDirectionMatrix> buildCache(MatrixStoreConfig config) {
    return CacheBuilder.newBuilder()
        .maximumSize(config.getStoreCacheMaxSize())
        .expireAfterAccess(config.getStoreCacheExpireAfterAccess())
        .concurrencyLevel(5)
        .removalListener(this::onRemoval);
  }

  private void onRemoval(RemovalNotification<String, QueryableDirectionMatrix> notification) {
    evictCallback.callback(notification.getKey());
  }

  void register(Consumer<QueryableDirectionMatrix> onStore, Consumer<String> onEvict) {
    evictCallback.register(onEvict);
    storeCallback.register(onStore);
  }

  QueryableDirectionMatrix findByHash(String hash) {
    var matrix = store.getIfPresent(hash);
    checkState(nonNull(matrix), "Store element not found. [hash=%s]", hash);
    return matrix;
  }

  Optional<CandidateDirectionMatrix> findByRequest(MatrixRequest request) {
    return List.copyOf(store.asMap().values())
        .stream()
        .map(matrix -> matrix.candidate(request))
        .sorted()
        .findFirst();
  }

  String store(DirectionMatrix matrix) {
    var element = QueryableDirectionMatrix.of(matrix);
    var key = element.getHash();
    store.put(key, element);
    storeCallback.callback(element);
    store.cleanUp();
    return key;
  }

  long size() {
    return store.size();
  }

}
