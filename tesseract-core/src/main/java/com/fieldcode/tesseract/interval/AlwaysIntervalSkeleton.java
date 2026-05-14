package com.fieldcode.tesseract.interval;

import org.immutables.value.Value.Immutable;

import com.fieldcode.tesseract.FluentIterator;
import com.fieldcode.tesseract.Interval;
import com.fieldcode.tesseract.IntervalAlignment;
import com.fieldcode.tesseract.Moment;
import com.fieldcode.tesseract.moment.Moments;
import com.fieldcode.tesseract.utils.iterator.Iterators;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.Optional;
import static com.fieldcode.tesseract.Constants.INTERVAL_ALWAYS_SIGN;
import static com.fieldcode.tesseract.Constants.NEGATIVE_INFINITE_MOMENT_INSTANCE;
import static com.fieldcode.tesseract.Constants.POSITIVE_INFINITE_MOMENT_INSTANCE;

@Immutable(singleton = true)
abstract class AlwaysIntervalSkeleton extends AbstractInterval {


  @Override
  public Moment getLower() {
    return NEGATIVE_INFINITE_MOMENT_INSTANCE;
  }

  @Override
  public Moment getUpper() {
    return POSITIVE_INFINITE_MOMENT_INSTANCE;
  }

  @Override
  public OffsetDateTime getStart() {
    throw new IllegalStateException("Interval is unbounded");
  }

  @Override
  public OffsetDateTime getEnd() {
    throw new IllegalStateException("Interval is unbounded");
  }

  @Override
  public Optional<Duration> getDuration() {
    return Optional.empty();
  }

  @Override
  public boolean isBounded() {
    return false;
  }

  @Override
  public boolean isUnbounded() {
    return true;
  }

  @Override
  public boolean isAlways() {
    return true;
  }

  @Override
  public boolean contains(Moment at) {
    return !at.isPositiveInfinite();
  }

  @Override
  public boolean encloses(Interval interval) {
    return true;
  }

  @Override
  public boolean encloses(Duration duration) {
    return true;
  }

  @Override
  public Interval span(Interval other) {
    return this;
  }

  @Override
  public FluentIterator<Interval> complement() {
    return Iterators.empty();
  }

  @Override
  public Optional<Interval> tryWithDuration(Duration duration, IntervalAlignment alignment) {
    return Optional.empty();
  }

  @Override
  public Interval withZoneIdSameInterval(ZoneId zoneId) {
    return this;
  }

  @Override
  public boolean isNever() {
    return false;
  }

  @Override
  public boolean contains(OffsetDateTime at) {
    return true;
  }

  @Override
  public FluentIterator<Interval> union(Interval other) {
    return Iterators.iterator(this);
  }

  @Override
  public FluentIterator<Interval> intersect(Interval other) {
    return Iterators.iterator(other);
  }

  @Override
  public Interval withLower(OffsetDateTime from) {
    return withLower(Moments.moment(from));
  }

  @Override
  public Interval withLower(Moment from) {
    return Intervals.intervalFrom(from.getAt());
  }

  @Override
  public Interval withUpper(Moment to) {
    return Intervals.intervalTo(to.getAt());
  }

  @Override
  public Interval withUpper(OffsetDateTime end) {
    return withUpper(Moments.moment(end));
  }

  @Override
  public String toString() {
    return INTERVAL_ALWAYS_SIGN;
  }

}
