package com.fieldcode.tesseract.matrix.cache;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("DirectionMatrixCacheController")
public class DirectionMatrixCacheControllerTest extends DirectionMatrixTestSupport {

  @Test
  @DisplayName("should discard a poorly-overlapping candidate and fetch fresh when the suggestion is FULL")
  void get_FullSuggestion_FetchesFresh() {

    // Arrange: store locations 0-9, then request locations 9-19 — only location 9
    // overlaps, so extending the stored matrix costs more than a fresh fetch
    // (a FULL suggestion, per QueryableDirectionMatrixTest.cost()).
    var cache = emptyController();
    cache.get(locations(0, 10));
    var statsBeforeRequest = cache.stat();

    // Act: request the poorly-overlapping location set
    var result = cache.get(locations(9, 20));

    // Assert: the result covers exactly the requested locations, and a fresh full
    // fetch was performed instead of reusing or partially extending the candidate
    assertThat(result.getLocations()).isEqualTo(locations(9, 20));
    assertThat(cache.stat().getFullFetchCount()).isEqualTo(statsBeforeRequest.getFullFetchCount() + 1);
    assertThat(cache.stat().getPartialFetchCount()).isEqualTo(statsBeforeRequest.getPartialFetchCount());
  }

  @Test
  @DisplayName("should track cache statistics correctly across multiple searches")
  void stat_MultipleSearches_Success() {

    // Arrange: an empty cache
    var cache = emptyController();

    // Assert: every counter starts at zero
    assertThat(cache.stat()).isEqualTo(ImmutableCacheStatistics.of(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0));

    // Act: first-ever request, nothing stored yet
    cache.get(locations(5));
    // Assert: a full fetch, storing the first matrix
    assertThat(cache.stat()).isEqualTo(ImmutableCacheStatistics.of(1, 1, 0, 1, 0, 1, 0, 0, 1, 1, 0));

    // Act: request a subset of the stored matrix's locations
    cache.get(locations(4));
    // Assert: fully covered by the stored matrix, no new fetch
    assertThat(cache.stat()).isEqualTo(ImmutableCacheStatistics.of(1, 2, 0, 2, 1, 1, 0, 0, 1, 1, 0));

    // Act: request a superset missing only one location
    cache.get(locations(6));
    // Assert: a partial fetch extends and merges the stored matrix
    assertThat(cache.stat()).isEqualTo(ImmutableCacheStatistics.of(2, 3, 0, 3, 1, 1, 1, 3, 1, 2, 0));

    // Act: request a location set poorly covered by anything stored
    cache.get(locations(5, 7));
    // Assert: too costly to extend, so a fresh full fetch is stored instead
    assertThat(cache.stat()).isEqualTo(ImmutableCacheStatistics.of(3, 4, 0, 4, 1, 2, 1, 3, 2, 3, 0));

    // Act: request a single location disjoint from everything stored
    cache.get(locations(10, 11));
    // Assert: another full fetch, since extending would cost more than fetching it alone
    assertThat(cache.stat()).isEqualTo(ImmutableCacheStatistics.of(4, 5, 0, 5, 1, 3, 1, 3, 3, 4, 0));

    // Act: repeat the exact same request as the earlier full fetch
    cache.get(locations(5, 7));
    // Assert: an exact hash match resolves through the quick cache, skipping the store entirely
    assertThat(cache.stat()).isEqualTo(ImmutableCacheStatistics.of(4, 6, 1, 5, 1, 3, 1, 3, 3, 4, 0));

  }

}
