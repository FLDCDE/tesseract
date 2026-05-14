package com.fieldcode.tesseract.interval;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.Optional;

import org.immutables.value.Value.Check;
import org.immutables.value.Value.Derived;
import org.immutables.value.Value.Immutable;
import org.immutables.value.Value.Parameter;

import com.fieldcode.tesseract.BoundedInterval;
import com.fieldcode.tesseract.FluentIterator;
import com.fieldcode.tesseract.Interval;
import com.fieldcode.tesseract.IntervalAlignment;
import com.fieldcode.tesseract.Moment;
import com.fieldcode.tesseract.utils.iterator.Iterators;

import static com.google.common.base.Preconditions.checkArgument;

@Immutable
abstract class BoundedIntervalSkeleton extends AbstractInterval implements BoundedInterval {

  @Check
  protected void check() {
    checkArgument(getLower().lt(getUpper()), "Lower bound must be less then upper bound [lower=%s, upper=%s]", getLower(), getUpper());
    checkArgument(getLower().isFinite(), "Lower bound must be finite [lower=%s]", getLower());
    checkArgument(getUpper().isFinite(), "Upper bound must be finite [upper=%s]", getUpper());
  }

  @Override
  @Parameter
  public abstract Moment getLower();

  @Override
  @Parameter
  public abstract Moment getUpper();

  @Override
  @Derived
  public Optional<Duration> getDuration() {
    var duration = Duration.between(getLower().getAt(), getUpper().getAt());
    return Optional.of(duration);
  }

  @Override
  public boolean isBounded() {
    return true;
  }

  @Override
  public boolean isUnbounded() {
    return false;
  }

  @Override
  public boolean isAlways() {
    return false;
  }

  @Override
  public boolean contains(Moment at) {
    return at.ge(getLower()) && at.lt(getUpper());
  }

  @Override
  public boolean encloses(Interval interval) {
    return getRelationTo(interval).encloses();
  }

  @Override
  public boolean encloses(Duration duration) {
    return Duration.between(getStart(), getEnd()).compareTo(duration) >= 0;
  }

  @Override
  public Interval span(Interval other) {
    var lower = getLower().min(other.getLower());
    var upper = getUpper().max(other.getUpper());
    return Intervals.interval(lower, upper);
  }

  @Override
  public FluentIterator<Interval> complement() {
    return Iterators.iterator(
        Intervals.intervalTo(getStart()),
        Intervals.intervalFrom(getEnd())
    );
  }

  @Override
  public Optional<Interval> tryWithDuration(Duration duration, IntervalAlignment alignment) {
    return Optional.of(super.withDuration(duration, alignment));
  }

  @Override
  public boolean isNever() {
    return false;
  }

  @Override
  public OffsetDateTime getStart() {
    return getLower().getAt();
  }

  @Override
  public OffsetDateTime getEnd() {
    return getUpper().getAt();
  }

  @Override
  public String toString() {
    return "[" + getLower() + ".." + getUpper() + ")";
  }

}
