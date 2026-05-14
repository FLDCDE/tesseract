package com.fieldcode.tesseract.utils.iterator;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import com.fieldcode.tesseract.interval.IntervalTestSupport;

class DropWhileIteratorTest extends IntervalTestSupport {

  @Test
  void dropWholeIterator_simple() {

    var it = Iterators.iterator(1, 2, 3, 4, 5, 6, 7);

    var i2 = DropWhileIterator.of(it, i -> i <= 2 || i >= 5);

    Assertions.assertThat(i2).toIterable()
        .containsExactly(
            3, 4, 5, 6, 7
        );

  }

  @Test
  void dropWhileIterator_intervals_success() {

    var it = Iterators.iterator
        (
            interval(-5, -1),
            interval(-1, 0),
            interval(1, 5),
            interval(5, 10)
        );

    var dropWhileIterator = DropWhileIterator.of(
        it, interval ->
            interval.getLower().lt(moment(5))
    );

    Assertions.assertThat(
            dropWhileIterator
        ).toIterable()
        .containsExactly(
            interval(5, 10)
        );

  }

}
