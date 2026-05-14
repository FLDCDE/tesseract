package com.fieldcode.tesseract.matrix;

import java.util.SortedSet;

import org.immutables.value.Value.Check;
import org.immutables.value.Value.Immutable;
import org.immutables.value.Value.Parameter;

import com.fieldcode.tesseract.DirectionMatrixDimensions;
import com.fieldcode.tesseract.Location;

import static com.google.common.base.Preconditions.checkArgument;

@Immutable
public abstract class DirectionMatrixDimensionsSkeleton implements DirectionMatrixDimensions {

  @Override
  @Parameter
  public abstract SortedSet<Location> getOrigins();

  @Override
  @Parameter
  public abstract SortedSet<Location> getDestinations();

  @Check
  protected void check() {
    checkArgument(!getOrigins().isEmpty(), "Origins must not be empty!");
    checkArgument(!getDestinations().isEmpty(), "Destinations must not be empty!");
  }

}
