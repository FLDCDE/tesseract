package com.fieldcode.tesseract.utils.iterator;

import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.assertj.core.util.Streams;
import org.junit.jupiter.api.Test;

import com.fieldcode.tesseract.interval.IntervalSets;
import com.fieldcode.tesseract.interval.IntervalTestSupport;

import static org.assertj.core.api.Assertions.assertThat;

class FlatMapIteratorTest extends IntervalTestSupport {


  @Test
  void flatMap_Simple_Success() {

    var numbers = List.of(1, 2, 3, 4).iterator();
    var flat = FlatMapIterator.of(numbers, i -> Iterators.iterator(i, i * 10));

    assertThat(flat)
        .toIterable()
        .containsExactly(1, 10, 2, 20, 3, 30, 4, 40);

  }

  @Test
  void flatMap_Empty_Success() {

    var numbers = Collections.<Integer>emptyIterator();
    var flat = FlatMapIterator.of(numbers, i -> Iterators.iterator(i, i * 10));

    assertThat(flat)
        .toIterable()
        .isEmpty();
  }


  @Test
  void flatMap_intervals_success() {

    var map = FlatMapIterator.of(
        Iterators.iterator(
            interval(0, 1),
            interval(2, 3),
            interval(4, 5)
        ), i -> Iterators.iterator(i, i.shift(Duration.ofDays(1)))
    );

    assertThat(map)
        .toIterable()
        .containsExactly
            (
                interval(0, 1),
                interval(24, 25),
                interval(2, 3),
                interval(26, 27),
                interval(4, 5),
                interval(28, 29)
            );
  }

  @Test
  void flatMap_emptyIntervalSet_containsEmpty_success() {
    var set = IntervalSets.disjoint();

    var items = Streams.stream(
        set.intervals()
    ).collect(Collectors.toList());

    var map = FlatMapIterator.of(
        Iterators.iterator(
            items
        ), i -> Iterators.iterator(i, i.shift(Duration.ofDays(1)))
    );
    assertThat(map)
        .toIterable()
        .containsExactly
            (
            );
  }

}
