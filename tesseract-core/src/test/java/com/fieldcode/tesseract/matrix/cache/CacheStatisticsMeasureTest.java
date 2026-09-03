package com.fieldcode.tesseract.matrix.cache;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("CacheStatisticsMeasure")
class CacheStatisticsMeasureTest {

  @Test
  @DisplayName("should start every counter at zero")
  void getStatistics_Initial_AllZero() {

    // Arrange: a fresh measure
    var measure = CacheStatisticsMeasure.of();

    // Act: read the statistics without incrementing anything
    var stats = measure.getStatistics();

    // Assert: every counter the measure tracks is zero
    assertThat(stats.getRequestCount()).isZero();
    assertThat(stats.getQuickHitCount()).isZero();
    assertThat(stats.getQuickMissCount()).isZero();
    assertThat(stats.getStoreHitCount()).isZero();
    assertThat(stats.getStoreMissCount()).isZero();
    assertThat(stats.getPartialFetchCount()).isZero();
    assertThat(stats.getAllPartialFetchCount()).isZero();
    assertThat(stats.getFullFetchCount()).isZero();
    assertThat(stats.getStoreCount()).isZero();
    assertThat(stats.getEvictCount()).isZero();
  }

  @Test
  @DisplayName("should accumulate each counter independently, per increment call")
  void increment_EachCounter_AccumulatesIndependently() {

    // Arrange: a fresh measure
    var measure = CacheStatisticsMeasure.of();

    // Act: increment each counter a distinct number of times
    measure.incrementRequest();
    measure.incrementRequest();
    measure.incrementQuickHit();
    measure.incrementQuickMiss();
    measure.incrementQuickMiss();
    measure.incrementQuickMiss();
    measure.incrementStoreHit();
    measure.incrementStoreMiss();
    measure.incrementPartialFetch();
    measure.incrementAllPartialFetch();
    measure.incrementAllPartialFetch();
    measure.incrementFullFetch();
    measure.incrementStore();
    measure.incrementEvict();

    var stats = measure.getStatistics();

    // Assert: each counter reflects exactly its own increments, not any other's
    assertThat(stats.getRequestCount()).isEqualTo(2);
    assertThat(stats.getQuickHitCount()).isEqualTo(1);
    assertThat(stats.getQuickMissCount()).isEqualTo(3);
    assertThat(stats.getStoreHitCount()).isEqualTo(1);
    assertThat(stats.getStoreMissCount()).isEqualTo(1);
    assertThat(stats.getPartialFetchCount()).isEqualTo(1);
    assertThat(stats.getAllPartialFetchCount()).isEqualTo(2);
    assertThat(stats.getFullFetchCount()).isEqualTo(1);
    assertThat(stats.getStoreCount()).isEqualTo(1);
    assertThat(stats.getEvictCount()).isEqualTo(1);
  }

}
