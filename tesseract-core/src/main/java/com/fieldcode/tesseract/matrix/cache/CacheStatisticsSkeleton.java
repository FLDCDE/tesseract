package com.fieldcode.tesseract.matrix.cache;

import com.fieldcode.tesseract.CacheStatistics;
import org.immutables.value.Value.Default;
import org.immutables.value.Value.Immutable;
import org.immutables.value.Value.Parameter;

@Immutable
abstract class CacheStatisticsSkeleton implements CacheStatistics {

  @Override
  @Parameter
  @Default
  public long getCacheSize() {
    return 0;
  }

  @Override
  @Parameter
  public abstract long getRequestCount();

  @Override
  @Parameter
  public abstract long getQuickHitCount();

  @Override
  @Parameter
  public abstract long getQuickMissCount();

  @Override
  @Parameter
  public abstract long getStoreHitCount();

  @Override
  @Parameter
  public abstract long getStoreMissCount();

  @Override
  @Parameter
  public abstract long getPartialFetchCount();

  @Override
  @Parameter
  public abstract long getAllPartialFetchCount();

  @Override
  @Parameter
  public abstract long getFullFetchCount();

  @Override
  @Parameter
  public abstract long getStoreCount();

  @Override
  @Parameter
  public abstract long getEvictCount();

}
