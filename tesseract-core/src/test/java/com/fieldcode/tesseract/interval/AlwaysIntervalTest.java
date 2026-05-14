package com.fieldcode.tesseract.interval;

import java.time.Duration;
import java.time.OffsetDateTime;

import org.junit.jupiter.api.Test;

import com.fieldcode.tesseract.Interval;

import static com.fieldcode.tesseract.Constants.INTERVAL_ALWAYS_SIGN;
import static com.fieldcode.tesseract.IntervalAlignment.ALIGN_AFTER;
import static com.fieldcode.tesseract.IntervalAlignment.ALIGN_BEFORE;
import static com.fieldcode.tesseract.IntervalAlignment.ALIGN_ENDS;
import static com.fieldcode.tesseract.IntervalAlignment.ALIGN_STARTS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class AlwaysIntervalTest extends IntervalTestSupport {

  @Test
  void create_Normal_Success() {
    var interval = Intervals.always();

    assertThat(interval)
        .is(always())
        .isSameAs(Intervals.always());
  }

  @Test
  void toString_Success() {
    var interval = Intervals.always();

    assertThat(interval)
        .is(representedBy(INTERVAL_ALWAYS_SIGN));
  }

  @Test
  void contains_Success() {
    var interval = Intervals.always();

    assertContains(interval, ninf(), true);
    assertContains(interval, moment(0), true);
    assertContains(interval, at(0), true);
    assertContains(interval, inf(), false);
  }

  @Test
  void span_Success() {
    var always = Intervals.always();

    assertSpan(always, always, always);
    assertSpan(always, Intervals.intervalTo(at(0)), always);
    assertSpan(always, Intervals.intervalFrom(at(0)), always);
    assertSpan(always, Intervals.interval(at(0), at(12)), always);
  }

  @Test
  void union_Success() {
    var always = Intervals.always();

    assertUnion(always, always, always);
    assertUnion(always, Intervals.intervalTo(at(0)), always);
    assertUnion(always, Intervals.intervalFrom(at(0)), always);
    assertUnion(always, Intervals.interval(at(0), at(12)), always);

  }

  @Test
  void complement_Always_Empty_Success() {

    var always = Intervals.always();

    assertThat(always.complement())
        .toIterable()
        .isEmpty();
  }

  @Test
  void shift_To_Always_Success() {

    var interval = Intervals.always();

    assertShift(interval, Duration.ofHours(10), Intervals.always());
    assertShift(interval, Duration.ofDays(10), Intervals.always());
    assertShift(interval, Duration.ofNanos(10), Intervals.always());

  }

  @Test
  void withLower_Success() {

    var interval = Intervals.always();

    assertWithLower(interval, moment(10), Intervals.intervalFrom(moment(10).getAt()));
    assertWithLower(interval, moment(20), Intervals.intervalFrom(moment(20).getAt()));

    var currentODT = OffsetDateTime.now();

    assertWithLower(interval, currentODT, Intervals.intervalFrom(currentODT));

  }

  @Test
  void withUpper_Success() {

    var interval = Intervals.always();

    assertWithUpper(interval, moment(10), Intervals.intervalTo(moment(10).getAt()));
    assertWithUpper(interval, moment(20), Intervals.intervalTo(moment(20).getAt()));

    var currentODT = OffsetDateTime.now();

    assertWithUpper(interval, currentODT, Intervals.intervalTo(currentODT));

  }

  @Test
  void withDuration_All_Success() {

    var interval = Intervals.always();

    assertThatThrownBy(() -> interval.withDuration(ONE_HOUR, ALIGN_BEFORE))
        .isInstanceOf(IllegalArgumentException.class);

    assertThatThrownBy(() -> interval.withDuration(ONE_HOUR, ALIGN_STARTS))
        .isInstanceOf(IllegalArgumentException.class);

    assertThatThrownBy(() -> interval.withDuration(ONE_HOUR, ALIGN_ENDS))
        .isInstanceOf(IllegalArgumentException.class);

    assertThatThrownBy(() -> interval.withDuration(ONE_HOUR, ALIGN_AFTER))
        .isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  void getDuration_Empty() {
    var interval = Intervals.always();

    assertThat(interval.getDuration())
        .isEmpty();
  }

  @Test
  void withZoneIdSameInterval_Success() {
    var interval1 = Intervals.always();
    var interval2 = interval1.withZoneIdSameInterval("Europe/Berlin");
    assertThat(interval2)
        .isSameAs(interval1);
  }

}
