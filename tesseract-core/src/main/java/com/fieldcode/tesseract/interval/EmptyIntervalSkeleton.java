package com.fieldcode.tesseract.interval;

import org.immutables.value.Value.Immutable;

import com.fieldcode.tesseract.FluentIterator;
import com.fieldcode.tesseract.Interval;
import com.fieldcode.tesseract.IntervalAlignment;
import com.fieldcode.tesseract.Moment;
import com.fieldcode.tesseract.utils.iterator.Iterators;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.Optional;
import static com.fieldcode.tesseract.Constants.INTERVAL_EMPTY_SIGN;

@Immutable(singleton = true)
abstract class EmptyIntervalSkeleton extends AbstractInterval {

  @Override
  public Moment getLower() {
    return unsupported();
  }

  @Override
  public Moment getUpper() {
    return unsupported();
  }

  @Override
  public OffsetDateTime getStart() {
    return unsupported();
  }

  @Override
  public OffsetDateTime getEnd() {
    return unsupported();
  }

  @Override
  public Optional<Duration> getDuration() {
    return Optional.of(Duration.ZERO);
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
    return false;
  }

  @Override
  public boolean contains(Moment at) {
    return false;
  }

  @Override
  public boolean encloses(Interval interval) {
    return false;
  }

  @Override
  public boolean encloses(Duration duration) {
    return false;
  }

  @Override
  public Interval span(Interval other) {
    return this;
  }

  @Override
  public FluentIterator<Interval> complement() {
    return Iterators.iterator(Intervals.always());
  }

  @Override
  public Optional<Interval> tryWithDuration(Duration duration, IntervalAlignment alignment) {
    return Optional.empty();
  }

  @Override
  public boolean isEmpty() {
    return true;
  }

  @Override
  public FluentIterator<Interval> union(Interval other) {
    return Iterators.iterator(other);
  }

  @Override
  public FluentIterator<Interval> intersect(Interval other) {
    return Iterators.iterator(this);
  }

  @Override
  public Interval shift(Duration shift) {
    return Intervals.empty();
  }

  @Override
  public Interval withLower(OffsetDateTime from) {
    return unsupported();
  }

  @Override
  public Interval withLower(Moment from) {
    return unsupported();
  }

  @Override
  public Interval withUpper(Moment to) {
    return unsupported();
  }

  @Override
  public Interval withUpper(OffsetDateTime end) {
    return unsupported();
  }

  @Override
  public Interval withDuration(Duration duration, IntervalAlignment alignment) {
    return unsupported();
  }

  @Override
  public String toString() {
    return INTERVAL_EMPTY_SIGN;
  }

  @Override
  public boolean isNever() {
    return true;
  }

  @Override
  public Interval withZoneIdSameInterval(ZoneId zoneId) {
    return this;
  }

}
