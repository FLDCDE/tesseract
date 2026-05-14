package com.fieldcode.tesseract.interval;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

import org.junit.jupiter.api.Test;

import com.fieldcode.tesseract.Interval;

import static com.fieldcode.tesseract.IntervalAlignment.ALIGN_AFTER;
import static com.fieldcode.tesseract.IntervalAlignment.ALIGN_BEFORE;
import static com.fieldcode.tesseract.IntervalAlignment.ALIGN_ENDS;
import static com.fieldcode.tesseract.IntervalAlignment.ALIGN_STARTS;
import static org.assertj.core.api.Assertions.as;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class BoundedIntervalTest extends IntervalTestSupport {

  @Test
  void create_Normal_Success() {
    var interval = Intervals.interval(at(0), at(12));

    assertThat(interval)
        .is(bounded(at(0), at(12)));
  }

  @Test
  void create_Empty_Exception() {
    assertThatThrownBy(() -> Intervals.interval(at(0), at(0)))
        .isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  void create_Wrong_Exception() {
    assertThatThrownBy(() -> Intervals.interval(at(12), at(0)))
        .isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  void toString_Success() {
    var interval = Intervals.interval(at(0), at(12));
    var expectedToString = "[" + at(0) + ".." + at(12) + ")";

    assertThat(interval)
        .is(representedBy(expectedToString));
  }

  @Test
  void contains_Success() {
    var interval = Intervals.interval(at(0), at(12));

    assertContains(interval, ninf(), false);
    assertContains(interval, moment(-1), false);
    assertContains(interval, moment(0), true);
    assertContains(interval, moment(1), true);
    assertContains(interval, moment(11), true);
    assertContains(interval, moment(12), false);
    assertContains(interval, moment(13), false);
    assertContains(interval, inf(), false);

    assertContains(interval, at(-1), false);
    assertContains(interval, at(0), true);
    assertContains(interval, at(1), true);
    assertContains(interval, at(11), true);
    assertContains(interval, at(12), false);
    assertContains(interval, at(13), false);

  }


  @Test
  void span_Success() {
    var interval = Intervals.interval(at(0), at(10));

    assertSpan(interval, interval, interval);
    assertSpan(interval, Intervals.always(), Intervals.always());

    assertSpan(interval, Intervals.intervalTo(at(-10)), Intervals.intervalTo(at(10)));
    assertSpan(interval, Intervals.intervalTo(at(0)), Intervals.intervalTo(at(10)));
    assertSpan(interval, Intervals.intervalTo(at(5)), Intervals.intervalTo(at(10)));
    assertSpan(interval, Intervals.intervalTo(at(10)), Intervals.intervalTo(at(10)));
    assertSpan(interval, Intervals.intervalTo(at(24)), Intervals.intervalTo(at(24)));

    assertSpan(interval, Intervals.intervalFrom(at(-10)), Intervals.intervalFrom(at(-10)));
    assertSpan(interval, Intervals.intervalFrom(at(0)), Intervals.intervalFrom(at(0)));
    assertSpan(interval, Intervals.intervalFrom(at(5)), Intervals.intervalFrom(at(0)));
    assertSpan(interval, Intervals.intervalFrom(at(10)), Intervals.intervalFrom(at(0)));
    assertSpan(interval, Intervals.intervalFrom(at(20)), Intervals.intervalFrom(at(0)));

    assertSpan(interval, Intervals.interval(at(-20), at(-10)), Intervals.interval(at(-20), at(10)));
    assertSpan(interval, Intervals.interval(at(-20), at(0)), Intervals.interval(at(-20), at(10)));
    assertSpan(interval, Intervals.interval(at(-20), at(5)), Intervals.interval(at(-20), at(10)));
    assertSpan(interval, Intervals.interval(at(-20), at(10)), Intervals.interval(at(-20), at(10)));
    assertSpan(interval, Intervals.interval(at(-20), at(20)), Intervals.interval(at(-20), at(20)));
    assertSpan(interval, Intervals.interval(at(0), at(5)), Intervals.interval(at(0), at(10)));
    assertSpan(interval, Intervals.interval(at(0), at(10)), Intervals.interval(at(0), at(10)));
    assertSpan(interval, Intervals.interval(at(0), at(20)), Intervals.interval(at(0), at(20)));
    assertSpan(interval, Intervals.interval(at(5), at(9)), Intervals.interval(at(0), at(10)));
    assertSpan(interval, Intervals.interval(at(5), at(10)), Intervals.interval(at(0), at(10)));
    assertSpan(interval, Intervals.interval(at(5), at(20)), Intervals.interval(at(0), at(20)));
    assertSpan(interval, Intervals.interval(at(10), at(20)), Intervals.interval(at(0), at(20)));

  }

  @Test
  void union_Success() {
    var interval = interval(0, 10);

    assertUnion(interval, interval, interval);
    assertUnion(interval, Intervals.always(), Intervals.always());

    assertUnion(interval, Intervals.intervalTo(at(-10)), Intervals.intervalTo(at(-10)), interval);

    assertUnion(interval, Intervals.intervalTo(at(0)), Intervals.intervalTo(at(10)));
    assertUnion(interval, Intervals.intervalTo(at(5)), Intervals.intervalTo(at(10)));
    assertUnion(interval, Intervals.intervalTo(at(10)), Intervals.intervalTo(at(10)));
    assertUnion(interval, Intervals.intervalTo(at(24)), Intervals.intervalTo(at(24)));

    assertUnion(interval, Intervals.intervalFrom(at(-10)), Intervals.intervalFrom(at(-10)));
    assertUnion(interval, Intervals.intervalFrom(at(0)), Intervals.intervalFrom(at(0)));
    assertUnion(interval, Intervals.intervalFrom(at(5)), Intervals.intervalFrom(at(0)));
    assertUnion(interval, Intervals.intervalFrom(at(10)), Intervals.intervalFrom(at(0)));
    assertUnion(interval, Intervals.intervalFrom(at(20)), interval, Intervals.intervalFrom(at(20)));

    assertUnion(interval, Intervals.interval(at(-20), at(-10)), Intervals.interval(at(-20), at(-10)), interval);
    assertUnion(interval, Intervals.interval(at(-20), at(0)), Intervals.interval(at(-20), at(10)));
    assertUnion(interval, Intervals.interval(at(-20), at(5)), Intervals.interval(at(-20), at(10)));
    assertUnion(interval, Intervals.interval(at(-20), at(10)), Intervals.interval(at(-20), at(10)));
    assertUnion(interval, Intervals.interval(at(-20), at(20)), Intervals.interval(at(-20), at(20)));

    assertUnion(interval, Intervals.interval(at(0), at(5)), Intervals.interval(at(0), at(10)));
    assertUnion(interval, Intervals.interval(at(0), at(10)), interval);
    assertUnion(interval, Intervals.interval(at(0), at(20)), Intervals.interval(at(0), at(20)));
    assertUnion(interval, Intervals.interval(at(5), at(9)), interval);
    assertUnion(interval, Intervals.interval(at(5), at(10)), interval);
    assertUnion(interval, Intervals.interval(at(5), at(20)), Intervals.interval(at(0), at(20)));
    assertUnion(interval, Intervals.interval(at(10), at(20)), Intervals.interval(at(0), at(20)));
  }

  @Test
  void intersect_Success() {
    var interval = Intervals.interval(at(0), at(10));

    assertIntersection(interval, interval, new Interval[]{interval});
    assertIntersection(interval, Intervals.always(), new Interval[]{interval});

    assertIntersection(interval, Intervals.intervalTo(at(-10)), new Interval[]{});

    assertIntersection(interval, Intervals.intervalTo(at(0)), new Interval[]{});
    assertIntersection(interval, Intervals.intervalTo(at(5)), new Interval[]{Intervals.interval(at(0), at(5))});
    assertIntersection(interval, Intervals.intervalTo(at(10)), new Interval[]{Intervals.interval(at(0), at(10))});
    assertIntersection(interval, Intervals.intervalTo(at(24)), new Interval[]{Intervals.interval(at(0), at(10))});

    assertIntersection(interval, Intervals.intervalFrom(at(-10)), new Interval[]{Intervals.interval(at(0), at(10))});
    assertIntersection(interval, Intervals.intervalFrom(at(0)), new Interval[]{Intervals.interval(at(0), at(10))});
    assertIntersection(interval, Intervals.intervalFrom(at(5)), new Interval[]{Intervals.interval(at(5), at(10))});
    assertIntersection(interval, Intervals.intervalFrom(at(10)), new Interval[]{});
    assertIntersection(interval, Intervals.intervalFrom(at(20)), new Interval[]{});

    assertIntersection(interval, Intervals.interval(at(-20), at(-10)), new Interval[]{});
    assertIntersection(interval, Intervals.interval(at(-20), at(0)), new Interval[]{});
    assertIntersection(interval, Intervals.interval(at(-20), at(5)), new Interval[]{Intervals.interval(at(0), at(5))});
    assertIntersection(interval, Intervals.interval(at(-20), at(10)), new Interval[]{Intervals.interval(at(0), at(10))});
    assertIntersection(interval, Intervals.interval(at(-20), at(20)), new Interval[]{Intervals.interval(at(0), at(10))});

    assertIntersection(interval, Intervals.interval(at(0), at(5)), new Interval[]{Intervals.interval(at(0), at(5))});
    assertIntersection(interval, Intervals.interval(at(0), at(10)), new Interval[]{interval});
    assertIntersection(interval, Intervals.interval(at(0), at(20)), new Interval[]{interval});
    assertIntersection(interval, Intervals.interval(at(5), at(9)), new Interval[]{Intervals.interval(at(5), at(9))});
    assertIntersection(interval, Intervals.interval(at(5), at(10)), new Interval[]{Intervals.interval(at(5), at(10))});
    assertIntersection(interval, Intervals.interval(at(5), at(20)), new Interval[]{Intervals.interval(at(5), at(10))});
    assertIntersection(interval, Intervals.interval(at(10), at(20)), new Interval[]{});
  }

  @Test
  void complement_Bounded_Success() {

    assertComplement(interval(-20, -10), new Interval[]{
        Intervals.intervalTo(at(-20)), Intervals.intervalFrom(at(-10))
    });
    assertComplement(interval(-10, -1), new Interval[]{
        Intervals.intervalTo(at(-10)), Intervals.intervalFrom(at(-1))
    });
    assertComplement(interval(-1, -0), new Interval[]{
        Intervals.intervalTo(at(-1)), Intervals.intervalFrom(at(0))
    });
    assertComplement(interval(0, 1), new Interval[]{
        Intervals.intervalTo(at(0)), Intervals.intervalFrom(at(1))
    });
    assertComplement(interval(10, 20), new Interval[]{
        Intervals.intervalTo(at(10)), Intervals.intervalFrom(at(20))
    });
  }

  @Test
  void shift_Bounded_Success() {

    assertShift(interval(0, 10), Duration.ofHours(10), interval(10, 20));
    assertShift(interval(-10, 10), Duration.ofHours(10), interval(0, 20));

  }

  @Test
  void withLower_Success() {

    assertWithLower(interval(0, 10), moment(9), interval(9, 10));
    assertWithLower(interval(-10, 10), moment(8), interval(8, 10));
    assertWithLower(interval(-50, 50), moment(49), interval(49, 50));

  }

  @Test
  void withUpper_Success() {

    assertWithUpper(interval(0, 10), moment(9), interval(0, 9));
    assertWithUpper(interval(-10, 10), moment(8), interval(-10, 8));
    assertWithUpper(interval(-50, 50), moment(49), interval(-50, 49));
  }

  @Test
  void withDuration_All_Success() {

    var interval = interval(0, 24);

    assertThat(interval.withDuration(ONE_HOUR, ALIGN_BEFORE))
        .isEqualTo(interval(-1, 0));

    assertThat(interval.withDuration(ONE_HOUR, ALIGN_STARTS))
        .isEqualTo(interval(0, 1));

    assertThat(interval.withDuration(ONE_HOUR, ALIGN_ENDS))
        .isEqualTo(interval(23, 24));

    assertThat(interval.withDuration(ONE_HOUR, ALIGN_AFTER))
        .isEqualTo(interval(24, 25));
  }


  @Test
  void getDuration_Empty() {
    var interval = interval(0, 24);

    assertThat(interval.getDuration())
        .contains(Duration.ofHours(24));
  }

  @Test
  void equal_DifferentOffset_Success() {
    var l = OffsetDateTime.parse("2024-01-25T05:00-05:00");
    var u = OffsetDateTime.parse("2024-01-26T05:00-05:00");
    var window1 = Intervals.interval(l, u);
    var window2 = Intervals.interval(
        l.atZoneSameInstant(ZoneOffset.UTC).toOffsetDateTime(),
        u.atZoneSameInstant(ZoneOffset.UTC).toOffsetDateTime()
    );
    assertThat(window1.equals(window2)).isTrue();
  }

  @Test
  void withZoneIdSameInterval_Success() {
    var interval1 = Intervals.interval(at(0), at(10));
    var interval2 = interval1.withZoneIdSameInterval("America/New_York");
    assertThat(interval2).isEqualTo(interval1);
    assertThat(interval2).isNotSameAs(interval1);
  }

}
