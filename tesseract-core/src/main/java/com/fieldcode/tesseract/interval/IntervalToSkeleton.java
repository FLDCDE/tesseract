package com.fieldcode.tesseract.interval;

import org.immutables.value.Value.Check;
import org.immutables.value.Value.Immutable;
import org.immutables.value.Value.Parameter;

import com.fieldcode.tesseract.Constants;
import com.fieldcode.tesseract.FluentIterator;
import com.fieldcode.tesseract.Interval;
import com.fieldcode.tesseract.IntervalAlignment;
import com.fieldcode.tesseract.Moment;
import com.fieldcode.tesseract.moment.Moments;
import com.fieldcode.tesseract.utils.iterator.Iterators;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.Optional;
import static com.fieldcode.tesseract.Constants.NEGATIVE_INFINITE_MOMENT_INSTANCE;
import static com.fieldcode.tesseract.IntervalAlignment.ALIGN_STARTS;
import static com.google.common.base.Preconditions.checkArgument;

@Immutable
abstract class IntervalToSkeleton extends AbstractInterval {

  @Check
  public void check() {
    checkArgument(getUpper().isFinite(), "IntervalTo upper bound must be finite [lower=%s, upper=%s]", getLower(), getUpper());
  }

  @Override
  public Moment getLower() {
    return NEGATIVE_INFINITE_MOMENT_INSTANCE;
  }

  @Override
  @Parameter
  public abstract Moment getUpper();

  @Override
  public OffsetDateTime getStart() {
    throw new IllegalStateException("Interval is unbounded");
  }

  @Override
  public OffsetDateTime getEnd() {
    return getUpper().getAt();
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
    return at.lt(getUpper());
  }

  @Override
  public boolean encloses(Interval interval) {
    return getUpper().ge(interval.getUpper());
  }

  @Override
  public boolean encloses(Duration duration) {
    return true;
  }

  @Override
  public Interval span(Interval other) {
    var upper = getUpper().max(other.getUpper());
    return Intervals.interval(Moments.ninf(), upper);
  }

  @Override
  public FluentIterator<Interval> complement() {
    return Iterators.iterator(Intervals.intervalFrom(getUpper().getAt()));
  }

  @Override
  public Optional<Interval> tryWithDuration(Duration duration, IntervalAlignment alignment) {
    if (alignment == ALIGN_STARTS || alignment == IntervalAlignment.ALIGN_BEFORE) {
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
    return Intervals.interval(lower, getUpper());
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
    return "[" + Constants.NEGATIVE_INFINITE_SIGN + ".." + getUpper() + ")";
  }

  @Override
  public boolean isNever() {
    return false;
  }

}
