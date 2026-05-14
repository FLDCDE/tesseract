package com.fieldcode.tesseract.movement;

import org.immutables.value.Value.Check;
import org.immutables.value.Value.Immutable;
import org.immutables.value.Value.Parameter;

import com.fieldcode.tesseract.Interval;
import com.fieldcode.tesseract.Moment;
import com.fieldcode.tesseract.Movement;
import com.fieldcode.tesseract.Presence;
import com.fieldcode.tesseract.interval.Intervals;

import static com.google.common.base.Preconditions.checkArgument;

@Immutable
public abstract class MovementSkeleton implements Movement {

  @Override
  @Parameter
  public abstract Presence getOrigin();

  @Override
  @Parameter
  public abstract Presence getDestination();

  @Override
  public Interval getInterval() {
    return Intervals.interval(getOrigin().getMoment(), getDestination().getMoment());
  }

  @Override
  public Movement withLower(Moment lower) {
    var origin = getOrigin().withMoment(lower);
    return withOrigin(origin);
  }

  @Override
  public Movement withUpper(Moment upper) {
    var destination = getDestination().withMoment(upper);
    return withDestination(destination);
  }

  protected abstract Movement withOrigin(Presence origin);

  protected abstract Movement withDestination(Presence destination);

  @Check
  protected void check() {
    var start = getOrigin().getMoment();
    var end = getDestination().getMoment();
    checkArgument(start.lt(end), "Origin moment must be before destination moment [start=%s, end=%s]", start, end);
  }

}
