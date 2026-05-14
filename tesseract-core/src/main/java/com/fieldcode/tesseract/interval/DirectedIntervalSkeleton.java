package com.fieldcode.tesseract.interval;

import org.immutables.value.Value.Immutable;
import org.immutables.value.Value.Parameter;

import com.fieldcode.tesseract.DirectedInterval;
import com.fieldcode.tesseract.Interval;
import com.fieldcode.tesseract.QueryDirection;

@Immutable
public abstract class DirectedIntervalSkeleton implements DirectedInterval {

  @Override
  @Parameter
  public abstract Interval getInterval();

  @Override
  @Parameter
  public abstract QueryDirection getDirection();

}
