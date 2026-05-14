package com.fieldcode.tesseract.matrix.cache;

import com.fieldcode.tesseract.DirectionMatrix;
import com.fieldcode.tesseract.DirectionMatrixCacheConfig;
import com.fieldcode.tesseract.Location;
import com.fieldcode.tesseract.location.Locations;
import com.fieldcode.tesseract.matrix.DirectionMatrices;
import com.google.common.base.Ticker;

import java.time.Duration;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.function.IntFunction;
import java.util.stream.IntStream;

public class DirectionMatrixTestSupport extends MatrixTestSupport {

  protected static DirectionMatrixCacheController emptyController() {
    var config = getDefaultConfig();

    return DirectionMatrixCacheController.of(config, DirectionMatrices::pythagorean);
  }

  protected static MatrixStore emptyMatrixStore() {
    var config = getDefaultConfig();
    return MatrixStore.of(config);
  }

  protected static MatrixStore emptyMatrixStore(Ticker ticker) {
    var config = getDefaultConfig();
    return MatrixStore.of(config, ticker);
  }

  private static DirectionMatrixCacheConfig getDefaultConfig() {
    return new DirectionMatrixCacheConfig() {
      @Override
      public long getStoreCacheMaxSize() {
        return 5000;
      }

      @Override
      public Duration getStoreCacheExpireAfterAccess() {
        return Duration.ofHours(1);
      }

      @Override
      public long getQuickCacheMaxSize() {
        return 5000;
      }

      @Override
      public Duration getQuickCacheExpireAfterAccess() {
        return Duration.ofHours(1);
      }
    };
  }

  protected Location[] generateLocationArray(int number) {
    return IntStream.range(0, number)
        .mapToObj(this::locationI)
        .toArray(Location[]::new);
  }

  private Location locationI(int i) {
    return Locations.location(0, i);
  }

  protected SortedSet<Location> locations(int size) {
    return locations(0, size, i -> Locations.location(0, i));
  }

  protected SortedSet<Location> locations(int startInclusive, int endExclusive) {
    return locations(startInclusive, endExclusive, i -> Locations.location(0, i));
  }

  protected SortedSet<Location> locations(int size, IntFunction<Location> mapper) {
    return locations(0, size, mapper);
  }

  protected SortedSet<Location> locations(int startInclusive, int endExclusive, IntFunction<Location> mapper) {
    return IntStream.range(startInclusive, endExclusive)
        .mapToObj(mapper)
        .collect(TreeSet::new, TreeSet::add, TreeSet::addAll);
  }

  protected DirectionMatrix matrix(int size) {
    return DirectionMatrices.pythagorean(locations(size));
  }

  protected DirectionMatrix matrix(int startInclusive, int endExclusive) {
    return DirectionMatrices.pythagorean(locations(startInclusive, endExclusive));
  }


}
