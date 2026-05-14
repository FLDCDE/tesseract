package com.fieldcode.tesseract.interval;

import java.time.Duration;
import java.time.OffsetDateTime;

import org.assertj.core.api.Condition;

import com.fieldcode.tesseract.Interval;
import com.fieldcode.tesseract.Moment;
import com.fieldcode.tesseract.moment.Moments;
import com.fieldcode.tesseract.test_utils.TimeTestSupport;

import static org.assertj.core.api.Assertions.allOf;
import static org.assertj.core.api.Assertions.assertThat;

public class IntervalTestSupport extends TimeTestSupport {

  protected static final Moment MIN = Moments.ninf();
  protected static final Moment MAX = Moments.inf();
  protected static final boolean DEFAULT_VALUE = false;
  protected static final Duration ONE_HOUR = Duration.ofHours(1);
  protected static final Duration ONE_DAY = Duration.ofDays(1);


  protected Duration hours(int hours) {
    return Duration.ofHours(hours);
  }

  protected Interval interval(int lower, int upper) {
    return Intervals.interval(at(lower), at(upper));
  }

  protected Condition<Interval> alwaysFlag(boolean flag) {
    return new Condition<>(i -> i.isAlways() == flag, "interval.isAlways() == %s", flag);
  }

  protected Condition<Interval> boundedFlag(boolean flag) {
    return new Condition<>(i -> i.isBounded() == flag, "interval.isBounded() == %s", flag);
  }

  protected Condition<Interval> unboundedFlag(boolean flag) {
    return new Condition<>(i -> i.isUnbounded() == flag, "interval.isUnbounded() == %s", flag);
  }

  protected Condition<Interval> emptyFlag(boolean flag) {
    return new Condition<>(i -> i.isEmpty() == flag, "interval.isEmpty() == %s", flag);
  }

  protected Condition<Interval> lower(OffsetDateTime lower) {
    return lower(moment(lower));
  }

  protected Condition<Interval> lowerNinf() {
    return lower(ninf());
  }

  protected Condition<Interval> lower(Moment lower) {
    return new Condition<>(i -> i.getLower().equals(lower), "lower == %s", lower);
  }

  protected Condition<Interval> upper(OffsetDateTime upper) {
    return upper(moment(upper));
  }

  protected Condition<Interval> upperInf() {
    return upper(inf());
  }

  protected Condition<Interval> upper(Moment upper) {
    return new Condition<>(i -> i.getUpper().equals(upper), "upper == %s", upper);
  }

  protected Condition<Interval> bounded(OffsetDateTime start, OffsetDateTime end) {
    return allOf(
        lower(start),
        upper(end),
        boundedFlag(true),
        unboundedFlag(false),
        alwaysFlag(false),
        emptyFlag(false)
    );
  }

  protected Condition<Interval> always() {
    return unbounded(ninf(), inf());
  }

  protected Condition<Interval> empty() {
    return emptyFlag(true);
  }

  protected Condition<Interval> unbounded(Moment lower, Moment upper) {
    var alwaysFlag = lower.isNegativeInfinite() && upper.isPositiveInfinite();
    return allOf(
        lower(lower),
        upper(upper),
        boundedFlag(false),
        unboundedFlag(true),
        alwaysFlag(alwaysFlag),
        emptyFlag(false)
    );
  }

  protected Condition<Interval> representedBy(String toString) {
    return new Condition<>(i -> i.toString().equals(toString), "interval.toString() == %s", toString);
  }

  protected void assertContains(Interval interval, Moment moment, boolean expected) {
    assertThat(interval.contains(moment)).isEqualTo(expected);
  }

  protected void assertContains(Interval interval, OffsetDateTime at, boolean expected) {
    assertThat(interval.contains(at)).isEqualTo(expected);
  }

  protected void assertSpan(Interval a, Interval b, Interval expected) {
    assertThat(a.span(b)).isEqualTo(expected);
  }

  protected void assertUnion(Interval a, Interval b, Interval... expected) {
    assertThat(a.union(b))
        .toIterable()
        .containsExactly(expected);
  }

  protected void assertIntersection(Interval a, Interval b, Interval[] expected) {
    assertThat(a.intersect(b))
        .toIterable()
        .containsExactly(expected);
  }

  protected void assertComplement(Interval a, Interval[] expected) {

    assertThat(a.complement())
        .toIterable()
        .containsExactly(expected);
  }

  protected void assertComplement(Interval a, Interval b) {

    assertThat(a.complement())
        .toIterable()
        .containsExactly(b);
  }


  protected void assertShift(Interval interval, Duration shift, Interval expected) {

    assertThat(interval.shift(shift)).isEqualTo(expected);
  }

  protected void assertWithLower(Interval interval, Moment moment, Interval expected) {
    assertThat(interval.withLower(moment)).isEqualTo(expected);
  }

  protected void assertWithLower(Interval interval, OffsetDateTime datetime, Interval expected) {

    assertThat(interval.withLower(datetime)).isEqualTo(expected);
  }


  protected void assertWithUpper(Interval interval, Moment moment, Interval expected) {
    assertThat(interval.withUpper(moment)).isEqualTo(expected);
  }

  protected void assertWithUpper(Interval interval, OffsetDateTime moment, Interval expected) {
    assertThat(interval.withUpper(moment)).isEqualTo(expected);
  }

}
