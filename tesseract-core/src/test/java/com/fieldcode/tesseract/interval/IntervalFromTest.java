package com.fieldcode.tesseract.interval;

import java.time.Duration;

import org.junit.jupiter.api.Test;

import static com.fieldcode.tesseract.Constants.POSITIVE_INFINITE_SIGN;
import static com.fieldcode.tesseract.IntervalAlignment.ALIGN_AFTER;
import static com.fieldcode.tesseract.IntervalAlignment.ALIGN_BEFORE;
import static com.fieldcode.tesseract.IntervalAlignment.ALIGN_ENDS;
import static com.fieldcode.tesseract.IntervalAlignment.ALIGN_STARTS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class IntervalFromTest extends IntervalTestSupport {


  @Test
  void create_Normal_Success() {
    var interval = intervalFrom(12);

    assertThat(interval)
        .is(unbounded(moment(12), inf()));
  }

  @Test
  void toString_Success() {
    var interval = intervalFrom(12);
    var expectedToString = "[" + at(12) + ".." + POSITIVE_INFINITE_SIGN + ")";

    assertThat(interval)
        .is(representedBy(expectedToString));
  }

  @Test
  void contains_Success() {
    var interval = intervalFrom(0);

    assertContains(interval, ninf(), false);
    assertContains(interval, moment(-1), false);
    assertContains(interval, moment(0), true);
    assertContains(interval, moment(1), true);
    assertContains(interval, inf(), true);

    assertContains(interval, at(-1), false);
    assertContains(interval, at(0), true);
    assertContains(interval, at(1), true);

  }

  @Test
  void span_Success() {
    var interval = intervalFrom(0);

    assertSpan(interval, interval, interval);
    assertSpan(interval, Intervals.always(), Intervals.always());

    assertSpan(interval, intervalTo(-10), Intervals.always());
    assertSpan(interval, intervalTo(0), Intervals.always());
    assertSpan(interval, intervalTo(5), Intervals.always());
    assertSpan(interval, intervalTo(10), Intervals.always());
    assertSpan(interval, intervalTo(24), Intervals.always());

    assertSpan(interval, intervalFrom(-10), intervalFrom(-10));
    assertSpan(interval, intervalFrom(0), intervalFrom(0));
    assertSpan(interval, intervalFrom(5), intervalFrom(0));
    assertSpan(interval, intervalFrom(10), intervalFrom(0));
    assertSpan(interval, intervalFrom(20), intervalFrom(0));

    assertSpan(interval, Intervals.interval(at(-20), at(-10)), intervalFrom(-20));
    assertSpan(interval, Intervals.interval(at(-20), at(0)), intervalFrom(-20));
    assertSpan(interval, Intervals.interval(at(-20), at(5)), intervalFrom(-20));
    assertSpan(interval, Intervals.interval(at(-20), at(10)), intervalFrom(-20));
    assertSpan(interval, Intervals.interval(at(-20), at(20)), intervalFrom(-20));

    assertSpan(interval, Intervals.interval(at(0), at(5)), intervalFrom(0));
    assertSpan(interval, Intervals.interval(at(0), at(10)), intervalFrom(0));
    assertSpan(interval, Intervals.interval(at(0), at(20)), intervalFrom(0));

    assertSpan(interval, Intervals.interval(at(5), at(9)), intervalFrom(0));
    assertSpan(interval, Intervals.interval(at(5), at(10)), intervalFrom(0));
    assertSpan(interval, Intervals.interval(at(5), at(20)), intervalFrom(0));

    assertSpan(interval, Intervals.interval(at(10), at(20)), intervalFrom(0));

  }

  @Test
  void complement_From() {

    assertThat(intervalFrom(0).complement())
        .toIterable()
        .containsExactly(
            intervalTo(0)
        );
    assertThat(intervalFrom(-1).complement())
        .toIterable()
        .containsExactly(
            intervalTo(-1)
        );

    assertThat(intervalFrom(10).complement())
        .toIterable()
        .containsExactly(
            intervalTo(10)
        );

  }

  @Test
  void shift_From() {

    assertShift(intervalFrom(-10), Duration.ofHours(5), intervalFrom(-5));
    assertShift(intervalFrom(0), Duration.ofHours(5), intervalFrom(5));
    assertShift(intervalFrom(10), Duration.ofHours(5), intervalFrom(15));

  }

  @Test
  void withLower_From_Success() {

    assertWithLower(intervalFrom(-10), moment(0), intervalFrom(0));
    assertWithLower(intervalFrom(0), moment(10), intervalFrom(10));
    assertWithLower(intervalFrom(10), moment(20), intervalFrom(20));

  }

  @Test
  void withUpper_From_Success() {

    assertWithUpper(intervalFrom(-10), moment(0), interval(-10, 0));
    assertWithUpper(intervalFrom(0), moment(10), interval(0, 10));
    assertWithUpper(intervalFrom(10), moment(20), interval(10, 20));

  }

  @Test
  void withDuration_All_Success() {

    var interval = Intervals.intervalFrom(moment(0));

    assertThat(interval.withDuration(ONE_HOUR, ALIGN_BEFORE))
        .isEqualTo(interval(-1, 0));

    assertThat(interval.withDuration(ONE_HOUR, ALIGN_STARTS))
        .isEqualTo(interval(0, 1));

    assertThatThrownBy(() -> interval.withDuration(ONE_HOUR, ALIGN_ENDS))
        .isInstanceOf(IllegalArgumentException.class);

    assertThatThrownBy(() -> interval.withDuration(ONE_HOUR, ALIGN_AFTER))
        .isInstanceOf(IllegalArgumentException.class);
  }


  @Test
  void getDuration_Empty() {
    var interval = Intervals.intervalFrom(moment(0));

    assertThat(interval.getDuration())
        .isEmpty();
  }

  @Test
  void withZoneIdSameInterval_Success() {
    var interval1 = Intervals.intervalFrom(at(0));
    var interval2 = interval1.withZoneIdSameInterval("America/New_York");
    assertThat(interval2).isEqualTo(interval1);
    assertThat(interval2).isNotSameAs(interval1);
  }


}
