package com.fieldcode.tesseract.matrix.cache;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.fieldcode.tesseract.QuickCacheConfig;

import java.time.Duration;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("QuickCache")
public class QuickCacheTest extends DirectionMatrixTestSupport {

  private QuickCache quick;

  @BeforeEach
  public void setUp() throws Exception {

    // Arrange: an empty quick cache
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
  @DisplayName("should return the store hash for a search hash that was put")
  public void getStoreKey_Present_Success() {

    // Arrange: a matrix's store hash, registered under its own request hash
    var matrix = matrix(5);
    var storeHash = Hash.hash(matrix.getLocations());
    quick.putSelf(storeHash);

    var request = ImmutableMatrixRequest.of(locations(5));
    var hash = request.getHash();

    // Act: look up the request's search hash
    var result = quick.getStoreKey(request.getHash());

    // Assert: it resolves to the stored hash
    assertThat(result).contains(hash);
  }

  @Test
  @DisplayName("should return empty for a search hash that was never put")
  public void getStoreKey_Empty_Success() {

    // Arrange: a request whose hash was never registered
    var request = ImmutableMatrixRequest.of(locations(5));

    // Act: look up the request's search hash
    var result = quick.getStoreKey(request.getHash());

    // Assert: nothing is found
    assertThat(result).isEmpty();
  }

  @Test
  @DisplayName("should remove every search hash pointing at an invalidated store hash")
  public void invalidateStoreKey_Success() {

    // Act: register four search hashes ("a","b","c","d") all pointing at store hash "a"
    quick.put("a", "a");
    quick.put("b", "a");
    quick.put("c", "a");
    quick.put("d", "a");

    // Assert: each one resolves to "a"
    assertThat(quick.getStoreKey("a")).contains("a");
    assertThat(quick.getStoreKey("b")).contains("a");
    assertThat(quick.getStoreKey("c")).contains("a");
    assertThat(quick.getStoreKey("d")).contains("a");

    // Act: invalidate store hash "a"
    quick.invalidateStoreKey("a");

    // Assert: every search hash that pointed at it is now gone
    assertThat(quick.getStoreKey("a")).isEmpty();
    assertThat(quick.getStoreKey("b")).isEmpty();
    assertThat(quick.getStoreKey("c")).isEmpty();
    assertThat(quick.getStoreKey("d")).isEmpty();
  }

}
