package com.fieldcode.tesseract.collection;

import java.util.Arrays;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.fieldcode.tesseract.Moment;
import com.fieldcode.tesseract.Signal;
import com.fieldcode.tesseract.signal.Signals;
import com.fieldcode.tesseract.test_utils.TimeTestSupport;
import com.google.common.collect.Lists;

import static com.fieldcode.tesseract.QueryDirection.BACKWARD;
import static com.fieldcode.tesseract.QueryDirection.FORWARD;
import static java.util.stream.Collectors.toUnmodifiableList;
import static org.assertj.core.api.Assertions.assertThat;

class SignalExpanderIteratorTest extends TimeTestSupport {

  private List<Signal<Moment, Boolean>> signals;
  private List<Signal<Moment, Boolean>> signalsReversed;

  private Signal<Moment, Boolean> signal(int at, boolean value) {
    return Signals.signal(moment(at), value);
  }

  @BeforeEach
  void setUp() {
    signals = List.of(
        signal(0, false),
        signal(2, true),
        signal(4, false),
        signal(6, true),
        signal(8, false),
        signal(10, false)
    );

    signalsReversed = Stream
        .generate(Lists.newLinkedList(signals)::removeLast)
        .limit(signals.size())
        .collect(toUnmodifiableList());
  }

  private SortedSet<Moment> moments(int... at) {
    return Arrays.stream(at)
        .boxed()
        .map(this::moment)
        .collect(TreeSet::new, TreeSet::add, TreeSet::addAll);
  }

  @Test
  void iterator_Forward_success() {

    var additional = moments(-2, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13);

    var iterator = SignalExpanderIterator.of(signals.iterator(), additional, FORWARD);

    assertThat(iterator)
        .toIterable()
        .containsExactly(
            signal(-2, false),
            signal(-1, false),
            signal(0, false),
            signal(1, false),
            signal(2, true),
            signal(3, true),
            signal(4, false),
            signal(5, false),
            signal(6, true),
            signal(7, true),
            signal(8, false),
            signal(9, false),
            signal(10, false),
            signal(11, false),
            signal(12, false),
            signal(13, false)
        );

  }

  @Test
  void iterator_Backward_success() {

    var additional = moments(-2, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13);

    var iterator = SignalExpanderIterator.of(signalsReversed.iterator(), additional, BACKWARD);

    assertThat(iterator)
        .toIterable()
        .containsExactly(
            signal(13, false),
            signal(12, false),
            signal(11, false),
            signal(10, false),
            signal(9, false),
            signal(8, false),
            signal(7, false),
            signal(6, true),
            signal(5, true),
            signal(4, false),
            signal(3, false),
            signal(2, true),
            signal(1, true),
            signal(0, false),
            signal(-1, false),
            signal(-2, false)
        );

  }


  @Test
  void window_Forward_success() {

    var window = interval(3, 7);

    var iterator = SignalExpanderIterator.crop(signals.iterator(), window, FORWARD);

    assertThat(iterator)
        .toIterable()
        .containsExactly(
            signal(3, true),      // true because 2:true
            signal(4, false),
            signal(6, true),
            signal(7, true)       // true because 6:true
        );

  }

  @Test
  void window_Backward_success() {

    var window = interval(3, 7);

    var iterator = SignalExpanderIterator.crop(signalsReversed.iterator(), window, BACKWARD);

    assertThat(iterator)
        .toIterable()
        .containsExactly(
            signal(7, false),   // false because 8:false
            signal(6, true),
            signal(4, false),
            signal(3, false)    // false because 4:false
        );

  }

}

