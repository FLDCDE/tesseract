package com.fieldcode.tesseract;

import java.time.Duration;

public interface MatrixStoreConfig {

  long getStoreCacheMaxSize();

  Duration getStoreCacheExpireAfterAccess();

}
