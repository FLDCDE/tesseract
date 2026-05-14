package com.fieldcode.tesseract.matrix.cache;

import com.fieldcode.tesseract.QuickCacheConfig;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import java.util.Map.Entry;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

public class QuickCache {

  private final Cache<String, String> cache;

  private QuickCache(QuickCacheConfig config) {
    this.cache = buildCache(config);
  }

  private static Cache<String, String> buildCache(QuickCacheConfig config) {
    return CacheBuilder.newBuilder()
        .maximumSize(config.getQuickCacheMaxSize())
        .expireAfterAccess(config.getQuickCacheExpireAfterAccess())
        .concurrencyLevel(5)
        .build();
  }

  static QuickCache of(QuickCacheConfig config) {
    return new QuickCache(config);
  }

  public void invalidateStoreKey(String key) {
    var invalidateKeys = cache
        .asMap()
        .entrySet()
        .stream()
        .filter(e -> e.getValue().equals(key))
        .map(Entry::getKey)
        .collect(toList());

    cache.invalidateAll(invalidateKeys);
  }

  public Optional<String> getStoreKey(String searchHash) {
    return Optional.ofNullable(cache.getIfPresent(searchHash));
  }

  public void putSelf(String hash) {
    cache.put(hash, hash);
  }

  public void put(String searchHash, String storeHash) {
    cache.put(searchHash, storeHash);
  }

}
