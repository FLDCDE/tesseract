package com.fieldcode.tesseract.query;

import org.immutables.value.Value.Check;
import org.immutables.value.Value.Default;
import org.immutables.value.Value.Immutable;

import com.fieldcode.tesseract.Interval;
import com.fieldcode.tesseract.IntervalQuery;
import com.fieldcode.tesseract.interval.Intervals;

import static com.google.common.base.Preconditions.checkArgument;

@Immutable
abstract class IntervalQuerySkeleton implements IntervalQuery {

  @Override
  @Default
  public Interval getWindow() {
    return Intervals.always();
  }

  @Override
  public IntervalQuery window(Interval window) {
    return withWindow(window);
  }

  @Check
  protected void check() {
    var numberOfLocks = getIntervalDefinitions()
        .stream()
        .filter(intervalDurationQuery -> intervalDurationQuery.isLocked() || intervalDurationQuery.isCenter())
        .count();

    checkArgument(numberOfLocks < 2, "Only one locked or centered duration query is allowed");
  }

  protected abstract ImmutableIntervalQuery withWindow(Interval value);

}
