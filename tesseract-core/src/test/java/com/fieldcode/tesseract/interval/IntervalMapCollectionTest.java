package com.fieldcode.tesseract.interval;

import java.util.Set;

import org.junit.jupiter.api.Test;

import com.fieldcode.tesseract.test_utils.TimeTestSupport;

import static org.assertj.core.api.Assertions.assertThat;

class IntervalMapCollectionTest extends TimeTestSupport {


  @Test
  void intervals_NonDefaults_Success() {

    var map = IntervalMaps.disjoint("default")
        .put(interval(0, 5), "event1")
        .put(interval(10, 15), "event2")
        .put(interval(20, 25), "event3")
        .put(interval(25, 30), "event4");

    var intervals = map.intervals()
        .intervals()
        .list();

    assertThat(intervals)
        .containsExactly(
            interval(0, 5),
            interval(10, 15),
            interval(20, 30)
        );
  }

  @Test
  void intervals_SpecificPredicate_Success() {

    var map = IntervalMaps.disjoint("default")
        .put(interval(0, 5), "event1")
        .put(interval(10, 15), "event2")
        .put(interval(20, 25), "event3")
        .put(interval(25, 30), "event4");

    var intervals = map.intervals((i, v) -> Set.of("event3", "event4").contains(v))
        .intervals()
        .list();

    assertThat(intervals)
        .containsExactly(
            interval(20, 30)
        );

  }


}
