package com.fieldcode.tesseract.interval;

import org.junit.jupiter.api.Test;

import java.time.Duration;
import static com.fieldcode.tesseract.Constants.NEGATIVE_INFINITE_SIGN;
import static com.fieldcode.tesseract.IntervalAlignment.ALIGN_AFTER;
import static com.fieldcode.tesseract.IntervalAlignment.ALIGN_BEFORE;
import static com.fieldcode.tesseract.IntervalAlignment.ALIGN_ENDS;
import static com.fieldcode.tesseract.IntervalAlignment.ALIGN_STARTS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class IntervalToTest extends IntervalTestSupport {

  @Test
  void create_Normal_Success() {
    var interval = Intervals.intervalTo(at(12));

    assertThat(interval)
        .is(unbounded(ninf(), moment(12)));
  }

  @Test
  void toString_Success() {
    var interval = Intervals.intervalTo(at(12));
    var expectedToString = "[" + NEGATIVE_INFINITE_SIGN + ".." + at(12) + ")";

    assertThat(interval)
        .is(representedBy(expectedToString));
  }

  @Test
  void contains_Success() {
    var interval = Intervals.intervalTo(at(0));

    assertContains(interval, ninf(), true);
    assertContains(interval, moment(-1), true);
    assertContains(interval, moment(0), false);
    assertContains(interval, moment(1), false);
    assertContains(interval, inf(), false);

    assertContains(interval, at(-1), true);
    assertContains(interval, at(0), false);
    assertContains(interval, at(1), false);
  }

  @Test
  void complement_To() {

    assertThat(intervalTo(0).complement())
        .toIterable()
        .containsExactly(
            intervalFrom(0)
        );

    assertThat(intervalTo(0).complement())
        .toIterable()
        .containsExactly(
            intervalFrom(0)
        );

    assertThat(intervalTo(10).complement())
        .toIterable()
        .containsExactly(
            intervalFrom(10)
        );

  }

  @Test
  void shift_To() {

    assertShift(intervalTo(-10), Duration.ofHours(5), intervalTo(-5));
    assertShift(intervalTo(0), Duration.ofHours(5), intervalTo(5));
    assertShift(intervalTo(10), Duration.ofHours(5), intervalTo(15));

  }

  @Test
  void withLower_To_Success() {

    assertWithLower(intervalTo(-10), moment(-20), interval(-20, -10));
    assertWithLower(intervalTo(0), moment(-10), interval(-10, 0));
    assertWithLower(intervalTo(10), moment(0), interval(0, 10));

  }

  @Test
  void withUpper_To_Success() {

    assertWithUpper(intervalTo(-10), moment(0), intervalTo(0));
    assertWithUpper(intervalTo(0), moment(10), intervalTo(10));
    assertWithUpper(intervalTo(10), moment(20), intervalTo(20));

  }

  @Test
  void withDuration_All_Success() {

    var interval = Intervals.intervalTo(moment(0));

    assertThatThrownBy(() -> interval.withDuration(ONE_HOUR, ALIGN_BEFORE))
        .isInstanceOf(IllegalArgumentException.class);

    assertThatThrownBy(() -> interval.withDuration(ONE_HOUR, ALIGN_STARTS))
        .isInstanceOf(IllegalArgumentException.class);

    assertThat(interval.withDuration(ONE_HOUR, ALIGN_ENDS))
        .isEqualTo(interval(-1, 0));

    assertThat(interval.withDuration(ONE_HOUR, ALIGN_AFTER))
        .isEqualTo(interval(0, 1));
  }

  @Test
  void getDuration_Empty() {
    var interval = Intervals.intervalTo(moment(0));

    assertThat(interval.getDuration())
        .isEmpty();
  }

  @Test
  void withZoneIdSameInterval_Success() {
    var interval1 = Intervals.intervalTo(at(0));
    var interval2 = interval1.withZoneIdSameInterval("America/New_York");
    assertThat(interval2).isEqualTo(interval1);
    assertThat(interval2).isNotSameAs(interval1);
  }

  @Test
  void span_Success() {
    var interval = Intervals.intervalTo(at(10));

    // Span with itself - should return the same interval
    assertSpan(interval, interval, interval);

    // Span with Always - always wins (covers entire timeline)
    assertSpan(interval, Intervals.always(), Intervals.always());

    // Span with other IntervalTo instances
    // When spanning two IntervalTo, result has -∞ start and max of both ends
    assertSpan(interval, Intervals.intervalTo(at(-10)), Intervals.intervalTo(at(10)));
    assertSpan(interval, Intervals.intervalTo(at(0)), Intervals.intervalTo(at(10)));
    assertSpan(interval, Intervals.intervalTo(at(5)), Intervals.intervalTo(at(10)));
    assertSpan(interval, Intervals.intervalTo(at(10)), Intervals.intervalTo(at(10)));
    assertSpan(interval, Intervals.intervalTo(at(24)), Intervals.intervalTo(at(24)));

    // Span with IntervalFrom instances
    // IntervalTo(-∞..10) span IntervalFrom(x..∞) = Always(-∞..∞)
    assertSpan(interval, Intervals.intervalFrom(at(-10)), Intervals.always());
    assertSpan(interval, Intervals.intervalFrom(at(0)), Intervals.always());
    assertSpan(interval, Intervals.intervalFrom(at(5)), Intervals.always());
    assertSpan(interval, Intervals.intervalFrom(at(10)), Intervals.always());
    assertSpan(interval, Intervals.intervalFrom(at(20)), Intervals.always());

    // Span with bounded intervals
    // IntervalTo(-∞..10) span Interval(x..y) = IntervalTo(-∞..max(10,y))
    assertSpan(interval, Intervals.interval(at(-20), at(-10)), Intervals.intervalTo(at(10)));
    assertSpan(interval, Intervals.interval(at(-20), at(0)), Intervals.intervalTo(at(10)));
    assertSpan(interval, Intervals.interval(at(-20), at(5)), Intervals.intervalTo(at(10)));
    assertSpan(interval, Intervals.interval(at(-20), at(10)), Intervals.intervalTo(at(10)));
    assertSpan(interval, Intervals.interval(at(-20), at(20)), Intervals.intervalTo(at(20)));
    assertSpan(interval, Intervals.interval(at(0), at(5)), Intervals.intervalTo(at(10)));
    assertSpan(interval, Intervals.interval(at(0), at(10)), Intervals.intervalTo(at(10)));
    assertSpan(interval, Intervals.interval(at(0), at(20)), Intervals.intervalTo(at(20)));
    assertSpan(interval, Intervals.interval(at(5), at(9)), Intervals.intervalTo(at(10)));
    assertSpan(interval, Intervals.interval(at(5), at(10)), Intervals.intervalTo(at(10)));
    assertSpan(interval, Intervals.interval(at(5), at(20)), Intervals.intervalTo(at(20)));
    assertSpan(interval, Intervals.interval(at(10), at(20)), Intervals.intervalTo(at(20)));
  }


}
