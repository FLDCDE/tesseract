package com.fieldcode.tesseract.query;


import org.immutables.value.Value.Check;
import org.immutables.value.Value.Default;
import org.immutables.value.Value.Immutable;

import com.fieldcode.tesseract.Interval;
import com.fieldcode.tesseract.IntervalDurationQuery;
import com.fieldcode.tesseract.interval.Intervals;

import static com.google.common.base.Preconditions.checkArgument;

@Immutable
abstract class IntervalDurationQuerySkeleton implements IntervalDurationQuery {

  @Override
  @Default
  public Interval getWindow() {
    return Intervals.always();
  }

  @Override
  @Default
  public boolean isLocked() {
    return false;
  }

  @Override
  @Default
  public boolean isCenter() {
    return false;
  }

  @Override
  public IntervalDurationQuery center() {
    return center(true);
  }

  @Override
  public IntervalDurationQuery center(boolean center) {
    return withCenter(center);
  }

  @Override
  public IntervalDurationQuery window(Interval window) {
    return withWindow(window);
  }

  @Override
  public IntervalDurationQuery locked() {
    return locked(true);
  }

  @Override
  public IntervalDurationQuery locked(boolean locked) {
    return withLocked(locked);
  }

  @Check
  protected void check() {
    checkArgument(!isLocked() || getWindow().encloses(getDuration()),
        "In case of locked query window duration must be equal to query duration [windows=%s, queryDuration=%s]",
        getWindow(),
        getDuration()
    );
  }

  protected abstract ImmutableIntervalDurationQuery withWindow(Interval value);

  protected abstract ImmutableIntervalDurationQuery withLocked(boolean value);

  protected abstract ImmutableIntervalDurationQuery withCenter(boolean value);

}
