package com.fieldcode.tesseract;

import java.time.Duration;

public interface QuickCacheConfig {

  long getQuickCacheMaxSize();

  Duration getQuickCacheExpireAfterAccess();

}
