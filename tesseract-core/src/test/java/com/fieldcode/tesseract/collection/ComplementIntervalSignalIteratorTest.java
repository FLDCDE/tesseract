package com.fieldcode.tesseract.collection;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;

import com.fieldcode.tesseract.Moment;
import com.fieldcode.tesseract.Signal;
import com.fieldcode.tesseract.interval.IntervalSets;
import com.fieldcode.tesseract.interval.IntervalTestSupport;
import com.fieldcode.tesseract.interval.Intervals;
import com.fieldcode.tesseract.map.SignalMaps;
import com.fieldcode.tesseract.moment.Moments;

import static com.fieldcode.tesseract.QueryDirection.FORWARD;
import static com.fieldcode.tesseract.signal.Signals.signal;
import static org.assertj.core.api.Assertions.assertThat;

class ComplementIntervalSignalIteratorTest extends IntervalTestSupport {


  @Test
  void complementIntervalIterator_signalList_success() {

    Iterator<Signal<Moment, Boolean>> iterator = List.of(
        signal(inf(), false),
        signal(moment(15), false),
        signal(moment(10), true),
        signal(moment(5), false),
        signal(moment(0), true),
        signal(ninf(), false)
    ).iterator();

    var complement = ComplementIntervalSignalIterator.of(iterator);
    assertThat(complement).toIterable().containsExactly(
        signal(inf(), true),
        signal(moment(15), true),
        signal(moment(10), false),
        signal(moment(5), true),
        signal(moment(0), false),
        signal(ninf(), true)
    );

  }

  @Test
  void complementIntervalIterator_empty_success() {
    Iterator<Signal<Moment, Boolean>> iterator = Collections.emptyIterator();

    var complement = ComplementIntervalSignalIterator.of(iterator);
    assertThat(complement).toIterable()
        .containsExactly();
  }

  @Test
  void complementIntervalIterator_infinites_success() {
    Iterator<Signal<Moment, Boolean>> iterator = List.of(
        signal(Moments.ninf(), true),
        signal(Moments.inf(), false)
    ).iterator();

    var complement = ComplementIntervalSignalIterator.of(iterator);
    assertThat(complement).toIterable()
        .containsExactly(
            signal(Moments.ninf(), false),
            signal(Moments.inf(), true)
        );
  }

  @Test
  void complementIntervalIterator_allFalse_success() {
    Iterator<Signal<Moment, Boolean>> iterator = List.of
        (
            signal(inf(), false),
            signal(moment(15), false),
            signal(moment(10), false),
            signal(moment(5), false),
            signal(moment(0), false),
            signal(ninf(), false)
        ).iterator();
    var complement = ComplementIntervalSignalIterator.of(iterator);
    assertThat(complement).toIterable()
        .containsExactly(
            signal(inf(), true),
            signal(moment(15), true),
            signal(moment(10), true),
            signal(moment(5), true),
            signal(moment(0), true),
            signal(ninf(), true)
        );
  }

  @Test
  void complementIntervalIterator_allTrue_success() {
    Iterator<Signal<Moment, Boolean>> iterator = List.of
            (
                signal(inf(), true),
                signal(moment(15), true),
                signal(moment(10), true),
                signal(moment(5), true),
                signal(moment(0), true),
                signal(ninf(), true)
            )
        .iterator();

    var complement = ComplementIntervalSignalIterator.of(iterator);
    assertThat(complement).toIterable()
        .containsExactly(
            signal(inf(), false),
            signal(moment(15), false),
            signal(moment(10), false),
            signal(moment(5), false),
            signal(moment(0), false),
            signal(ninf(), false)
        );

  }

  @Test
  void complementIntervalIterator_signalMap_success() {
    var set = SignalMaps.modifiable(Moments.ninf(), Moments.inf(), false);
    set.putAll(
        Stream.of
            (
                signal(inf(), false),
                signal(moment(15), false),
                signal(moment(10), true),
                signal(moment(5), false),
                signal(moment(0), true),
                signal(ninf(), false)
            )
    );
    var complement = ComplementIntervalSignalIterator.of(set.signals());
    assertThat(complement).toIterable().containsExactly
        (
            signal(ninf(), true),
            signal(moment(0), false),
            signal(moment(5), true),
            signal(moment(10), false),
            signal(moment(15), true),
            signal(inf(), true)
        );
  }

  @Test
  void complementIntervalIterator_intervalSet_success() {
    var set = IntervalSets.disjoint
        (
            interval(-10, 0),
            interval(5, 10),
            interval(15, 20)
        );

    var complement = ComplementIntervalSignalIterator.of(set.bounds(Intervals.always(), FORWARD));
    assertThat(complement).toIterable().containsExactly
        (
            signal(ninf(), true),
            signal(moment(-10), false),
            signal(moment(0), true),
            signal(moment(5), false),
            signal(moment(10), true),
            signal(moment(15), false),
            signal(moment(20), true),
            signal(inf(), true)
        );
  }
}
