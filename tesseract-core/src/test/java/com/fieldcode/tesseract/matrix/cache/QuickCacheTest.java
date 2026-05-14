package com.fieldcode.tesseract.matrix.cache;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.fieldcode.tesseract.QuickCacheConfig;

import java.time.Duration;
import static org.assertj.core.api.Assertions.assertThat;

public class QuickCacheTest extends DirectionMatrixTestSupport {

  private QuickCache quick;

  @BeforeEach
  public void setUp() throws Exception {
    var config = new QuickCacheConfig() {
      @Override
      public long getQuickCacheMaxSize() {
        return 5000;
      }

      @Override
      public Duration getQuickCacheExpireAfterAccess() {
        return Duration.ofDays(1);
      }
    };
    quick = QuickCache.of(config);
  }

  @Test
  public void getStoreKey_Present_Success() {

    var matrix = matrix(5);
    var storeHash = Hash.hash(matrix);

    quick.putSelf(storeHash);

    var request = ImmutableMatrixRequest.of(locations(5));
    var hash = request.getHash();

    var result = quick.getStoreKey(request.getHash());

    assertThat(result).contains(hash);
  }

  @Test
  public void getStoreKey_Empty_Success() {

    var request = ImmutableMatrixRequest.of(locations(5));

    var result = quick.getStoreKey(request.getHash());

    assertThat(result).isEmpty();

  }

  @Test
  public void invalidateStoreKey_Success() {

    quick.put("a", "a");
    quick.put("b", "a");
    quick.put("c", "a");
    quick.put("d", "a");

    assertThat(quick.getStoreKey("a")).contains("a");
    assertThat(quick.getStoreKey("b")).contains("a");
    assertThat(quick.getStoreKey("c")).contains("a");
    assertThat(quick.getStoreKey("d")).contains("a");

    quick.invalidateStoreKey("a");

    assertThat(quick.getStoreKey("a")).isEmpty();
    assertThat(quick.getStoreKey("b")).isEmpty();
    assertThat(quick.getStoreKey("c")).isEmpty();
    assertThat(quick.getStoreKey("d")).isEmpty();

  }

}
