package com.fieldcode.tesseract.interval;

import java.time.Duration;

import com.fieldcode.tesseract.Interval;
import com.fieldcode.tesseract.IntervalAlignment.IntervalAlignmentVisitor;

public class IntervalDurationAligner implements IntervalAlignmentVisitor<Interval> {

  private final Interval original;
  private final Duration duration;

  private IntervalDurationAligner(Interval original, Duration duration) {
    this.original = original;
    this.duration = duration;
  }

  public static IntervalDurationAligner of(Interval original, Duration duration) {
    return new IntervalDurationAligner(original, duration);
  }

  @Override
  public Interval alignBefore() {
    var lower = original.getLower().shift(duration.negated());
    var upper = original.getLower();
    return Intervals.interval(lower, upper);
  }

  @Override
  public Interval alignStarts() {
    var lower = original.getLower();
    var upper = lower.shift(duration);
    return Intervals.interval(lower, upper);
  }

  @Override
  public Interval alignEnds() {
    var lower = original.getUpper().shift(duration.negated());
    var upper = original.getUpper();
    return Intervals.interval(lower, upper);
  }

  @Override
  public Interval alignAfter() {
    var lower = original.getUpper();
    var upper = lower.shift(duration);

    return Intervals.interval(lower, upper);
  }

}
