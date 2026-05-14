package com.fieldcode.tesseract.matrix.cache;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class DirectionMatrixCacheControllerTest extends DirectionMatrixTestSupport {

  @Test
  @DisplayName("Should track cache statistics correctly across multiple searches")
  void stat_MultipleSearches_Success() {

    var cache = emptyController();

    assertThat(cache.stat()).isEqualTo(ImmutableCacheStatistics.of(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0));

    cache.get(locations(5));
    assertThat(cache.stat()).isEqualTo(ImmutableCacheStatistics.of(1, 1, 0, 1, 0, 1, 0, 0, 1, 1, 0));

    cache.get(locations(4));
    assertThat(cache.stat()).isEqualTo(ImmutableCacheStatistics.of(1, 2, 0, 2, 1, 1, 0, 0, 1, 1, 0));

    cache.get(locations(6));
    assertThat(cache.stat()).isEqualTo(ImmutableCacheStatistics.of(2, 3, 0, 3, 1, 1, 1, 3, 1, 2, 0));

    cache.get(locations(5, 7));
    assertThat(cache.stat()).isEqualTo(ImmutableCacheStatistics.of(3, 4, 0, 4, 1, 2, 1, 3, 2, 3, 0));

    cache.get(locations(10, 11));
    assertThat(cache.stat()).isEqualTo(ImmutableCacheStatistics.of(4, 5, 0, 5, 1, 3, 1, 3, 3, 4, 0));

    cache.get(locations(5, 7));
    assertThat(cache.stat()).isEqualTo(ImmutableCacheStatistics.of(4, 6, 1, 5, 1, 3, 1, 3, 3, 4, 0));

  }

}
