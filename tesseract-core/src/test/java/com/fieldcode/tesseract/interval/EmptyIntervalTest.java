package com.fieldcode.tesseract.interval;

import org.junit.jupiter.api.Test;

import com.fieldcode.tesseract.Interval;
import com.fieldcode.tesseract.IntervalAlignment;

import java.time.Duration;
import java.time.OffsetDateTime;
import static com.fieldcode.tesseract.Constants.INTERVAL_EMPTY_SIGN;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class EmptyIntervalTest extends IntervalTestSupport {

  @Test
  void create_Normal_Success() {
    var interval = Intervals.empty();

    assertThat(interval)
        .is(empty())
        .isSameAs(Intervals.empty());
  }

  @Test
  void toString_Success() {
    var interval = Intervals.empty();

    assertThat(interval)
        .is(representedBy(INTERVAL_EMPTY_SIGN));
  }

  @Test
  void complement_To_Empty() {
    var interval = Intervals.empty();
    assertComplement(interval, Intervals.always());
  }

  @Test
  void shift_To_Empty() {

    var interval = Intervals.empty();

    assertShift(interval, Duration.ofHours(10), Intervals.empty());
    assertShift(interval, Duration.ofDays(10), Intervals.empty());
    assertShift(interval, Duration.ofNanos(10), Intervals.empty());

  }

  @Test
  void withLower_Unsupported_Success() {

    var interval = Intervals.empty();

    assertThatThrownBy(() -> assertWithLower(interval, OffsetDateTime.now(), interval)).isInstanceOf(UnsupportedOperationException.class);

  }

  @Test
  void withUpper_Unsupported_Success() {

    var interval = Intervals.empty();

    assertThatThrownBy(() -> assertWithUpper(interval, OffsetDateTime.now(), interval)).isInstanceOf(UnsupportedOperationException.class);

  }

  @Test
  void withDuration_forward_Success() {
    var interval = Intervals.empty();

    assertThatThrownBy(
        () -> interval.withDuration(ONE_HOUR, IntervalAlignment.ALIGN_BEFORE)
    ).isInstanceOf(UnsupportedOperationException.class);
  }

  @Test
  void withDuration_backward_Success() {
    var interval = Intervals.empty();

    assertThatThrownBy(
        () -> interval.withDuration(ONE_HOUR, IntervalAlignment.ALIGN_STARTS)
    ).isInstanceOf(UnsupportedOperationException.class);
  }

  @Test
  void getDuration_Empty() {
    var interval = Intervals.empty();

    assertThat(interval.getDuration())
        .contains(Duration.ZERO);
  }

  @Test
  void withZoneIdSameInterval_Success() {
    var interval1 = Intervals.empty();
    var interval2 = interval1.withZoneIdSameInterval("America/New_York");
    assertThat(interval2).isEqualTo(interval1);
    assertThat(interval2).isSameAs(interval1);
  }

  @Test
  void union_Success() {
    var interval = Intervals.empty();

    // Empty is the identity element for union: Empty ∪ X = X

    // Empty ∪ Empty = Empty
    assertUnion(interval, interval, interval);

    // Empty ∪ Always = Always
    assertUnion(interval, Intervals.always(), Intervals.always());

    // Empty ∪ IntervalTo = IntervalTo
    assertUnion(interval, Intervals.intervalTo(at(-10)), Intervals.intervalTo(at(-10)));
    assertUnion(interval, Intervals.intervalTo(at(0)), Intervals.intervalTo(at(0)));
    assertUnion(interval, Intervals.intervalTo(at(10)), Intervals.intervalTo(at(10)));

    // Empty ∪ IntervalFrom = IntervalFrom
    assertUnion(interval, Intervals.intervalFrom(at(-10)), Intervals.intervalFrom(at(-10)));
    assertUnion(interval, Intervals.intervalFrom(at(0)), Intervals.intervalFrom(at(0)));
    assertUnion(interval, Intervals.intervalFrom(at(10)), Intervals.intervalFrom(at(10)));

    // Empty ∪ BoundedInterval = BoundedInterval
    assertUnion(interval, Intervals.interval(at(-20), at(-10)), Intervals.interval(at(-20), at(-10)));
    assertUnion(interval, Intervals.interval(at(0), at(10)), Intervals.interval(at(0), at(10)));
    assertUnion(interval, Intervals.interval(at(10), at(20)), Intervals.interval(at(10), at(20)));
  }

  @Test
  void intersect_Success() {
    var interval = Intervals.empty();

    // Empty is the annihilator for intersection: Empty ∩ X = Empty (no overlap)

    // Empty ∩ Empty = Empty
    assertIntersection(interval, interval, new Interval[]{interval});

    // Empty ∩ Always = Empty
    assertIntersection(interval, Intervals.always(), new Interval[]{interval});

    // Empty ∩ IntervalTo = Empty
    assertIntersection(interval, Intervals.intervalTo(at(-10)), new Interval[]{interval});
    assertIntersection(interval, Intervals.intervalTo(at(0)), new Interval[]{interval});
    assertIntersection(interval, Intervals.intervalTo(at(10)), new Interval[]{interval});

    // Empty ∩ IntervalFrom = Empty
    assertIntersection(interval, Intervals.intervalFrom(at(-10)), new Interval[]{interval});
    assertIntersection(interval, Intervals.intervalFrom(at(0)), new Interval[]{interval});
    assertIntersection(interval, Intervals.intervalFrom(at(10)), new Interval[]{interval});

    // Empty ∩ BoundedInterval = Empty
    assertIntersection(interval, Intervals.interval(at(-20), at(-10)), new Interval[]{interval});
    assertIntersection(interval, Intervals.interval(at(0), at(10)), new Interval[]{interval});
    assertIntersection(interval, Intervals.interval(at(10), at(20)), new Interval[]{interval});
  }

}
