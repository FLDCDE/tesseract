package com.fieldcode.tesseract.collection;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.fieldcode.tesseract.IntervalSet;
import com.fieldcode.tesseract.Moment;
import com.fieldcode.tesseract.QueryDirection;
import com.fieldcode.tesseract.Signal;
import com.fieldcode.tesseract.interval.IntervalSets;
import com.fieldcode.tesseract.interval.Intervals;
import com.fieldcode.tesseract.signal.Signals;
import com.fieldcode.tesseract.test_utils.TimeTestSupport;

import static org.assertj.core.api.Assertions.assertThat;

class IntervalToBoundaryIteratorTest extends TimeTestSupport {

  private IntervalSet set;

  private Signal<Moment, Boolean> signal(int hour, boolean value) {
    return Signals.signal(moment(hour), value);
  }

  @BeforeEach
  void setUp() {
    set = IntervalSets.disjoint(
        interval(0, 5),
        interval(10, 15),
        interval(20, 25)
    );
  }

  @Test
  void iterator_Empty_Success() {

    var direction = QueryDirection.FORWARD;

    var bounds = IntervalToBoundaryIterator.of(set.intervals(interval(30, 40), direction), direction);

    assertThat(bounds)
        .toIterable()
        .isEmpty();
  }

  @Test
  void iterator_SimpleForward_Success() {

    var direction = QueryDirection.FORWARD;

    var bounds = IntervalToBoundaryIterator.of(set.intervals(), direction);

    assertThat(bounds)
        .toIterable()
        .containsExactly(
            signal(0, true),
            signal(5, false),
            signal(10, true),
            signal(15, false),
            signal(20, true),
            signal(25, false)
        );
  }

  @Test
  void iterator_SimpleBackward_Success() {

    var direction = QueryDirection.BACKWARD;

    var bounds = IntervalToBoundaryIterator.of(set.intervals(Intervals.always(), direction), direction);

    assertThat(bounds)
        .toIterable()
        .containsExactly(
            signal(25, true),
            signal(20, false),
            signal(15, true),
            signal(10, false),
            signal(5, true),
            signal(0, false)
        );
  }

}
