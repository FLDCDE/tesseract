package com.fieldcode.tesseract.interval;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.Optional;

import org.immutables.value.Value.Check;
import org.immutables.value.Value.Immutable;
import org.immutables.value.Value.Parameter;

import com.fieldcode.tesseract.FluentIterator;
import com.fieldcode.tesseract.Interval;
import com.fieldcode.tesseract.IntervalAlignment;
import com.fieldcode.tesseract.Moment;
import com.fieldcode.tesseract.moment.Moments;
import com.fieldcode.tesseract.utils.iterator.Iterators;

import static com.fieldcode.tesseract.Constants.POSITIVE_INFINITE_MOMENT_INSTANCE;
import static com.fieldcode.tesseract.Constants.POSITIVE_INFINITE_SIGN;
import static com.fieldcode.tesseract.IntervalAlignment.ALIGN_ENDS;
import static com.google.common.base.Preconditions.checkArgument;

@Immutable
abstract class IntervalFromSkeleton extends AbstractInterval {

  @Check
  public void check() {
    checkArgument(getLower().isFinite(), "IntervalFrom lower bound must be finite [lower=%s, upper=%s]", getLower(), getUpper());
  }

  @Override
  @Parameter
  public abstract Moment getLower();

  @Override
  public Moment getUpper() {
    return POSITIVE_INFINITE_MOMENT_INSTANCE;
  }

  @Override
  public OffsetDateTime getStart() {
    return getLower().getAt();
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
    return false;
  }

  @Override
  public boolean contains(Moment at) {
    return at.ge(getLower());
  }

  @Override
  public boolean encloses(Interval interval) {
    return getLower().le(interval.getLower());
  }

  @Override
  public boolean encloses(Duration duration) {
    return true;
  }

  @Override
  public Interval span(Interval other) {
    var lower = getLower().min(other.getLower());
    return Intervals.interval(lower, Moments.inf());
  }

  @Override
  public FluentIterator<Interval> complement() {
    return Iterators.iterator(Intervals.intervalTo(getLower().getAt()));
  }

  @Override
  public Optional<Interval> tryWithDuration(Duration duration, IntervalAlignment alignment) {
    if (alignment == ALIGN_ENDS || alignment == IntervalAlignment.ALIGN_AFTER) {
      return Optional.empty();
    }
    return Optional.of(super.withDuration(duration, alignment));
  }

  @Override
  public Interval withLower(OffsetDateTime lower) {
    return withLower(Moments.moment(lower));
  }

  @Override
  public Interval withLower(Moment lower) {
    return Intervals.intervalFrom(lower.getAt());
  }

  @Override
  public Interval withUpper(Moment to) {
    return Intervals.interval(getLower(), to);
  }

  @Override
  public Interval withUpper(OffsetDateTime end) {
    return withUpper(Moments.moment(end));
  }

  @Override
  public String toString() {
    return "[" + getLower() + ".." + POSITIVE_INFINITE_SIGN + ")";
  }

  @Override
  public boolean isNever() {
    return false;
  }

}
