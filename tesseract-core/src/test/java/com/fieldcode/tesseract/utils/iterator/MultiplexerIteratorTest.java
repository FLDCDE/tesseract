package com.fieldcode.tesseract.utils.iterator;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.fieldcode.tesseract.Interval;
import com.fieldcode.tesseract.interval.IntervalSets;
import com.fieldcode.tesseract.interval.IntervalTestSupport;

import static org.assertj.core.api.Assertions.assertThat;

class MultiplexerIteratorTest extends IntervalTestSupport {

  @Test
  void create_Simple_Success() {

    var i0 = List.of(0, 1).iterator();
    var i1 = List.of(0, 2, 3).iterator();
    var i2 = List.of(0, 3, 5).iterator();
    var i3 = List.of(0, 4, 5).iterator();
    var i4 = List.of(0).iterator();
    var i5 = Collections.<Integer>emptyIterator();

    var iterators = List.of(i0, i1, i2, i3, i4, i5);

    var multiplexed = MultiplexerIterator.of(iterators, Integer::compareTo);

    assertThat(multiplexed)
        .toIterable()
        .containsExactly(
            List.of(0, 0, 0, 0, 0),
            List.of(1),
            List.of(2),
            List.of(3, 3),
            List.of(4),
            List.of(5, 5)
        );

  }

  @Test
  void create_Empty_Empty() {

    var iterators = List.of(Iterators.<Integer>empty());

    var multiplexed = MultiplexerIterator.of(iterators, Integer::compareTo);

    assertThat(multiplexed)
        .toIterable()
        .isEmpty();
  }

  @Test
  void intervalSets_multiplexerIterator_Success() {

    var intervalSet = IntervalSets.disjoint(
        interval(0, 1),
        interval(2, 3),
        interval(4, 5)
    );
    var intervalSet2 = IntervalSets.disjoint(
        interval(5, 6),
        interval(7, 8),
        interval(9, 10)
    );
    var intervalSet3 = IntervalSets.disjoint(
        interval(0, 1),
        interval(7, 8),
        interval(2, 3),
        interval(4, 5)
    );

    var intervalSet4 = IntervalSets.disjoint(
        interval(9, 10),
        interval(2, 3),
        interval(7, 8),
        interval(5, 6)
    );

    var i0 = intervalSet.intervals();
    var i1 = intervalSet2.intervals();
    var i2 = intervalSet3.intervals();
    var i3 = intervalSet4.intervals();

    var iterators = List.of(i0, i1, i2, i3);

    var multiplexed = MultiplexerIterator.of(
        iterators,
        Comparator.comparing(Interval::getLower)
    );

    assertThat(multiplexed)
        .toIterable()
        .containsExactly(
            List.of
                (
                    interval(0, 1),
                    interval(0, 1)
                ),
            List.of
                (
                    interval(2, 3),
                    interval(2, 3),
                    interval(2, 3)
                ),
            List.of
                (
                    interval(4, 5),
                    interval(4, 5)
                ),
            List.of
                (
                    interval(5, 6),
                    interval(5, 6)
                ),
            List.of
                (
                    interval(7, 8),
                    interval(7, 8),
                    interval(7, 8)
                ),
            List.of
                (
                    interval(9, 10),
                    interval(9, 10)
                )
        );

  }

}
