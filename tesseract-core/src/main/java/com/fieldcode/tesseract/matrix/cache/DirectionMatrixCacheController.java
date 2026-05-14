package com.fieldcode.tesseract.matrix.cache;

import com.fieldcode.tesseract.*;

import java.util.SortedSet;
import java.util.function.Function;

public interface DirectionMatrixCacheController {

  static DirectionMatrixCacheController of(DirectionMatrixCacheConfig config, Function<DirectionMatrixDimensions, PartialDirectionMatrix> fetch) {
    return CacheController.of(config, fetch);
  }

  DirectionMatrix get(SortedSet<Location> locations);

  CacheStatistics stat();

}
