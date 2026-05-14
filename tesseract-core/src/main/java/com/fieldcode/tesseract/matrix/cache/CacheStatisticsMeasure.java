package com.fieldcode.tesseract.matrix.cache;

import java.util.concurrent.atomic.AtomicLong;


public class CacheStatisticsMeasure {

  private final AtomicLong evictCount = new AtomicLong();
  private final AtomicLong storeCount = new AtomicLong();
  private final AtomicLong requestCount = new AtomicLong();
  private final AtomicLong storeHitCount = new AtomicLong();
  private final AtomicLong quickHitCount = new AtomicLong();
  private final AtomicLong storeMissCount = new AtomicLong();
  private final AtomicLong fullFetchCount = new AtomicLong();
  private final AtomicLong quickMissCount = new AtomicLong();
  private final AtomicLong partialFetchCount = new AtomicLong();
  private final AtomicLong allPartialFetchCount = new AtomicLong();

  private CacheStatisticsMeasure() {
  }

  public static CacheStatisticsMeasure of() {
    return new CacheStatisticsMeasure();
  }

  public void incrementEvict() {
    evictCount.getAndIncrement();
  }

  public void incrementStore() {
    storeCount.getAndIncrement();
  }

  public void incrementRequest() {
    requestCount.getAndIncrement();
  }

  public void incrementStoreHit() {
    storeHitCount.getAndIncrement();
  }

  public void incrementQuickHit() {
    quickHitCount.getAndIncrement();
  }

  public void incrementStoreMiss() {
    storeMissCount.getAndIncrement();
  }

  public void incrementFullFetch() {
    fullFetchCount.getAndIncrement();
  }

  public void incrementQuickMiss() {
    quickMissCount.getAndIncrement();
  }

  public void incrementPartialFetch() {
    partialFetchCount.incrementAndGet();
  }

  public void incrementAllPartialFetch() {
    allPartialFetchCount.incrementAndGet();
  }

  public ImmutableCacheStatistics getStatistics() {
    return ImmutableCacheStatistics.builder()
        .storeCount(storeCount.get())
        .evictCount(evictCount.get())
        .requestCount(requestCount.get())
        .storeHitCount(storeHitCount.get())
        .quickHitCount(quickHitCount.get())
        .storeMissCount(storeMissCount.get())
        .fullFetchCount(fullFetchCount.get())
        .quickMissCount(quickMissCount.get())
        .partialFetchCount(partialFetchCount.get())
        .allPartialFetchCount(allPartialFetchCount.get())
        .build();
  }

}
