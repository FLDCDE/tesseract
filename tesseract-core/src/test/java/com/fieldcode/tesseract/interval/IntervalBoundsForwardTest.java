package com.fieldcode.tesseract.interval;

import java.util.Iterator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.fieldcode.tesseract.Interval;
import com.fieldcode.tesseract.Moment;
import com.fieldcode.tesseract.Signal;
import com.fieldcode.tesseract.moment.Moments;
import com.fieldcode.tesseract.signal.Signals;

import static com.fieldcode.tesseract.QueryDirection.FORWARD;
import static org.assertj.core.api.Assertions.assertThat;

class IntervalBoundsForwardTest extends IntervalTestSupport {

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
  void bounds_Type1Forward_Success() {
    assertBounds(
        interval.bounds(interval(0, 5), FORWARD),
        signal(0, false),
        signal(5, false)
    );
  }

  @Test
  void bounds_Type2Forward_Success() {
    assertBounds(
        interval.bounds(interval(0, 10), FORWARD),
        signal(0, false),
        signal(10, true)
    );
  }

  @Test
  void bounds_Type3Forward_Success() {
    assertBounds(
        interval.bounds(interval(0, 15), FORWARD),
        signal(0, false),
        signal(10, true),
        signal(15, true)
    );
  }

  @Test
  void bounds_Type4Forward_Success() {
    assertBounds(
        interval.bounds(interval(0, 20), FORWARD),
        signal(0, false),
        signal(10, true),
        signal(20, false)
    );
  }

  @Test
  void bounds_Type5Forward_Success() {
    assertBounds(
        interval.bounds(interval(0, 25), FORWARD),
        signal(0, false),
        signal(10, true),
        signal(20, false),
        signal(25, false)
    );
  }

  @Test
  void bounds_Type6Forward_Success() {
    assertBounds(
        interval.bounds(interval(10, 15), FORWARD),
        signal(10, true),
        signal(15, true)
    );
  }

  @Test
  void bounds_Type7Forward_Success() {
    assertBounds(
        interval.bounds(interval(10, 20), FORWARD),
        signal(10, true),
        signal(20, false)
    );
  }

  @Test
  void bounds_Type8Forward_Success() {
    assertBounds(
        interval.bounds(interval(10, 25), FORWARD),
        signal(10, true),
        signal(20, false),
        signal(25, false)
    );
  }

  @Test
  void bounds_Type9Forward_Success() {
    assertBounds(
        interval.bounds(interval(15, 16), FORWARD),
        signal(15, true),
        signal(16, true)
    );
  }

  @Test
  void bounds_Type10Forward_Success() {
    assertBounds(
        interval.bounds(interval(15, 20), FORWARD),
        signal(15, true),
        signal(20, false)
    );
  }

  @Test
  void bounds_Type11Forward_Success() {
    assertBounds(
        interval.bounds(interval(15, 25), FORWARD),
        signal(15, true),
        signal(20, false),
        signal(25, false)
    );
  }

  @Test
  void bounds_Type12Forward_Success() {
    assertBounds(
        interval.bounds(interval(20, 25), FORWARD),
        signal(20, false),
        signal(25, false)
    );
  }

  @Test
  void bounds_Type13Forward_Success() {
    assertBounds(
        interval.bounds(interval(25, 30), FORWARD),
        signal(25, false),
        signal(30, false)
    );
  }

}
