package com.fieldcode.tesseract.collection;

import java.util.Iterator;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import com.fieldcode.tesseract.Interval;
import com.fieldcode.tesseract.IntervalSet;
import com.fieldcode.tesseract.QueryDirection;
import com.fieldcode.tesseract.interval.IntervalSets;
import com.fieldcode.tesseract.interval.IntervalTestSupport;
import com.fieldcode.tesseract.interval.Intervals;
import com.fieldcode.tesseract.moment.Moments;

import static com.fieldcode.tesseract.QueryDirection.BACKWARD;
import static com.fieldcode.tesseract.QueryDirection.FORWARD;
import static org.assertj.core.api.Assertions.assertThat;


class BoundariesToIntervalIteratorTest extends IntervalTestSupport {

  private static Iterator<Interval> iterator(IntervalSet set, Interval window, QueryDirection direction) {
    var signals = set.bounds(window, direction);
    return BoundariesToIntervalIterator.of(signals, direction);
  }

  @Test
  void iterate_ForwardEmpty_Empty() {
    var window = Intervals.always();
    var set = IntervalSets.disjoint();

    var iterator = iterator(set, window, FORWARD);

    assertThat(iterator)
        .toIterable()
        .isEmpty();
  }

  @Test
  void iterate_BackwardEmpty_Empty() {
    var window = Intervals.always();

    var set = IntervalSets.disjoint();

    var iterator = iterator(set, window, BACKWARD);

    assertThat(iterator)
        .toIterable()
        .isEmpty();
  }

  @Test
  void iterate_ForwardAll_Success() {
    var window = Intervals.always();

    var set = IntervalSets.disjoint(
        Intervals.intervalTo(moment(0)),
        interval(1, 5),
        interval(10, 15),
        Intervals.intervalFrom(moment(20))
    );

    var iterator = iterator(set, window, FORWARD);

    assertThat(iterator)
        .toIterable()
        .containsExactly(
            Intervals.intervalTo(moment(0)),
            interval(1, 5),
            interval(10, 15),
            Intervals.intervalFrom(moment(20))
        );
  }

  @Test
  void iterate_BackwardAll_Success() {
    var window = Intervals.always();

    var set = IntervalSets.disjoint(
        Intervals.intervalTo(moment(0)),
        interval(1, 5),
        interval(10, 15),
        Intervals.intervalFrom(moment(20))
    );

    var iterator = iterator(set, window, BACKWARD);

    assertThat(iterator)
        .toIterable()
        .containsExactly(
            Intervals.intervalFrom(moment(20)),
            interval(10, 15),
            interval(1, 5),
            Intervals.intervalTo(moment(0))
        );
  }

  @Test
  void iterate_ForwardWindow_Success() {
    var window = interval(3, 14);

    var set = IntervalSets.disjoint(
        Intervals.intervalTo(moment(0)),
        interval(1, 5),
        interval(10, 15),
        Intervals.intervalFrom(moment(20))
    );

    var iterator = iterator(set, window, FORWARD);

    assertThat(iterator)
        .toIterable()
        .containsExactly(
            interval(3, 5),
            interval(10, 14)
        );
  }

  @Test
  void iterate_BackwardWindow_Success() {
    var window = interval(3, 14);

    var set = IntervalSets.disjoint(
        Intervals.intervalTo(moment(0)),
        interval(1, 5),
        interval(10, 15),
        Intervals.intervalFrom(moment(20))
    );

    var iterator = iterator(set, window, BACKWARD);

    assertThat(iterator)
        .toIterable()
        .containsExactly(
            interval(10, 14),
            interval(3, 5)
        );
  }

  @Test
  void IntervalsFromSignalsIterator_disJointSet_emptySet_Forward_Success() {
    var window = Intervals.always();
    var direction = FORWARD;

    var set = IntervalSets.disjoint(

    );

    var complementIterator = ComplementIntervalSignalIterator.of(set.bounds(window, direction));
    var iterator = BoundariesToIntervalIterator.of(complementIterator, direction);

    Assertions.assertThat(iterator)
        .toIterable().containsExactly
            (
                Intervals.interval(Moments.ninf(), Moments.inf())
            );
  }

  @Test
  void IntervalsFromSignalsIterator_disJointSet_emptySet_Backward_Success() {
    var window = Intervals.always();
    var direction = BACKWARD;

    var set = IntervalSets.disjoint(

    );

    var complementIterator = ComplementIntervalSignalIterator.of(set.bounds(window, direction));
    var iterator = BoundariesToIntervalIterator.of(complementIterator, direction);

    Assertions.assertThat(iterator)
        .toIterable().containsExactly
            (
                Intervals.interval(Moments.ninf(), Moments.inf())
            );
  }

  @Test
  void IntervalsFromSignalsIterator_disJointSet_Backward_Success() {
    var window = Intervals.always();
    var direction = BACKWARD;

    var set = IntervalSets.disjoint(
        Intervals.intervalTo(moment(0)),
        interval(1, 5),
        interval(10, 15)
    );

    var complementIterator = ComplementIntervalSignalIterator.of(set.bounds(window, direction));
    var iterator = BoundariesToIntervalIterator.of(complementIterator, direction);

    Assertions.assertThat(iterator)
        .toIterable().containsExactly
            (
                Intervals.intervalFrom(moment(15)),
                interval(5, 10),
                interval(0, 1)
            );
  }

  @Test
  void IntervalsFromSignalsIterator_disJointSet_window_Backward_Success() {
    var window = interval(-5, 8);
    var direction = BACKWARD;

    var set = IntervalSets.disjoint(
        Intervals.intervalTo(moment(0)),
        interval(1, 5),
        interval(10, 15)
    );

    var complementIterator = ComplementIntervalSignalIterator.of(set.bounds(window, direction));
    var iterator = BoundariesToIntervalIterator.of(complementIterator, direction);

    Assertions.assertThat(iterator)
        .toIterable().containsExactly
            (
                interval(5, 8),
                interval(0, 1)
            );
  }

  @Test
  void IntervalsFromSignalsIterator_disJointSet_Forward_Success() {
    var window = Intervals.always();
    var direction = FORWARD;

    var set = IntervalSets.disjoint(
        Intervals.intervalTo(moment(0)),
        interval(1, 5),
        interval(10, 15)
    );

    var complementIterator = ComplementIntervalSignalIterator.of(set.bounds(window, direction));
    var iterator = BoundariesToIntervalIterator.of(complementIterator, direction);

    Assertions.assertThat(iterator)
        .toIterable().containsExactly
            (
                interval(0, 1),
                interval(5, 10),
                Intervals.intervalFrom(moment(15))
            );
  }

  @Test
  void IntervalsFromSignalsIterator_disJointSet_window_Forward_Success() {
    var window = interval(-5, 8);
    var direction = FORWARD;

    var set = IntervalSets.disjoint(
        Intervals.intervalTo(moment(0)),
        interval(1, 5),
        interval(10, 15)
    );

    var complementIterator = ComplementIntervalSignalIterator.of(set.bounds(window, direction));
    var iterator = BoundariesToIntervalIterator.of(complementIterator, direction);

    Assertions.assertThat(iterator)
        .toIterable().containsExactly
            (
                interval(0, 1),
                interval(5, 8)
            );
  }


}
