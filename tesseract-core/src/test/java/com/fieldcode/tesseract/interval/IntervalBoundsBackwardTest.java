package com.fieldcode.tesseract.interval;

import java.util.Iterator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.fieldcode.tesseract.Interval;
import com.fieldcode.tesseract.Moment;
import com.fieldcode.tesseract.Signal;
import com.fieldcode.tesseract.moment.Moments;
import com.fieldcode.tesseract.signal.Signals;

import static com.fieldcode.tesseract.QueryDirection.BACKWARD;
import static org.assertj.core.api.Assertions.assertThat;

class IntervalBoundsBackwardTest extends IntervalTestSupport {

  private Interval interval;

  @BeforeEach
  void setUp() {
    interval = interval(10, 20);
  }

  private Signal<Moment, Boolean> signal(int hour, boolean value) {
    return Signals.signal(Moments.moment(at(hour)), value);
  }

  @SafeVarargs
  private void assertBounds(Iterator<Signal<Moment, Boolean>> iterator, Signal<Moment, Boolean>... expected) {
    assertThat(iterator)
        .toIterable()
        .containsExactly(expected);
  }

  @Test
  void bounds_Type1Backward_Success() {
    assertBounds(
        interval.bounds(interval(0, 5), BACKWARD),
        signal(5, false),
        signal(0, false)
    );
  }

  @Test
  void bounds_Type2Backward_Success() {
    assertBounds(
        interval.bounds(interval(0, 10), BACKWARD),
        signal(10, false),
        signal(0, false)
    );
  }

  @Test
  void bounds_Type3Backward_Success() {
    assertBounds(
        interval.bounds(interval(0, 15), BACKWARD),
        signal(15, true),
        signal(10, false),
        signal(0, false)
    );
  }

  @Test
  void bounds_Type4Backward_Success() {
    assertBounds(
        interval.bounds(interval(0, 20), BACKWARD),
        signal(20, true),
        signal(10, false),
        signal(0, false)
    );
  }

  @Test
  void bounds_Type5Backward_Success() {
    assertBounds(
        interval.bounds(interval(0, 25), BACKWARD),
        signal(25, false),
        signal(20, true),
        signal(10, false),
        signal(0, false)
    );
  }

  @Test
  void bounds_Type6Backward_Success() {
    assertBounds(
        interval.bounds(interval(10, 15), BACKWARD),
        signal(15, true),
        signal(10, false)
    );
  }

  @Test
  void bounds_Type7Backward_Success() {
    assertBounds(
        interval.bounds(interval(10, 20), BACKWARD),
        signal(20, true),
        signal(10, false)
    );
  }

  @Test
  void bounds_Type8Backward_Success() {
    assertBounds(
        interval.bounds(interval(10, 25), BACKWARD),
        signal(25, false),
        signal(20, true),
        signal(10, false)
    );
  }

  @Test
  void bounds_Type9Backward_Success() {
    assertBounds(
        interval.bounds(interval(15, 16), BACKWARD),
        signal(16, true),
        signal(15, true)
    );
  }

  @Test
  void bounds_Type10Backward_Success() {
    assertBounds(
        interval.bounds(interval(15, 20), BACKWARD),
        signal(20, true),
        signal(15, true)
    );
  }

  @Test
  void bounds_Type11Backward_Success() {
    assertBounds(
        interval.bounds(interval(15, 25), BACKWARD),
        signal(25, false),
        signal(20, true),
        signal(15, true)
    );
  }

  @Test
  void bounds_Type12Backward_Success() {
    assertBounds(
        interval.bounds(interval(20, 25), BACKWARD),
        signal(25, false),
        signal(20, true)
    );
  }

  @Test
  void bounds_Type13Backward_Success() {
    assertBounds(
        interval.bounds(interval(25, 30), BACKWARD),
        signal(30, false),
        signal(25, false)
    );
  }

}
