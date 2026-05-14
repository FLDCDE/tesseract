package com.fieldcode.tesseract.map;

import org.junit.jupiter.api.Test;

import com.fieldcode.tesseract.Moment;
import com.fieldcode.tesseract.Signal;
import com.fieldcode.tesseract.interval.IntervalTestSupport;
import com.fieldcode.tesseract.utils.iterator.Iterators;

import static com.fieldcode.tesseract.signal.Signals.signal;
import static org.assertj.core.api.Assertions.assertThat;

class BackwardBoundsIteratorTest extends IntervalTestSupport {

  @Test
  void reverse_Empty_Empty() {
    var signals = Iterators.<Signal<Moment, Boolean>>empty();

    var rev = BackwardBoundsIterator.of(signals);
    assertThat(rev.list())
        .isEmpty();

  }

  @Test
  void reverse_() {

    var signals = Iterators.iterator(
        signal(inf(), false),
        signal(moment(30), false),
        signal(moment(20), true),
        signal(moment(10), false),
        signal(moment(0), true),
        signal(ninf(), false)
    );

    var rev = BackwardBoundsIterator.of(signals);

    assertThat(rev.list())
        .containsExactly(
            signal(inf(), false),
            signal(moment(30), true),
            signal(moment(20), false),
            signal(moment(10), true),
            signal(moment(0), false),
            signal(ninf(), false)
        );

  }

}
