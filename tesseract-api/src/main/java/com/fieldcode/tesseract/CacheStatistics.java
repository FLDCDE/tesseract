package com.fieldcode.tesseract;

public interface CacheStatistics {

  long getCacheSize();

  long getRequestCount();

  long getQuickHitCount();

  long getQuickMissCount();

  long getStoreHitCount();

  long getStoreMissCount();

  long getPartialFetchCount();

  long getAllPartialFetchCount();

  long getFullFetchCount();

  long getStoreCount();

  long getEvictCount();

}
