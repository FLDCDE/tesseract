package com.fieldcode.tesseract.direction;

import org.immutables.value.Value.Immutable;
import org.immutables.value.Value.Parameter;

import com.fieldcode.tesseract.Direction;
import com.fieldcode.tesseract.Location;


@Immutable
abstract class DirectionSkeleton implements Direction {

  @Override
  @Parameter
  public abstract Location getOrigin();

  @Override
  @Parameter
  public abstract Location getDestination();

  @Override
  public String toString() {
    return String.format("[%s->%s]", getOrigin(), getDestination());
  }

}
