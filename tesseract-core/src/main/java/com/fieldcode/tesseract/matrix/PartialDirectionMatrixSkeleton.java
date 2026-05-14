package com.fieldcode.tesseract.matrix;

import com.fieldcode.tesseract.Location;
import org.immutables.value.Value.Check;
import org.immutables.value.Value.Immutable;
import org.immutables.value.Value.Parameter;

import java.util.SortedSet;

import static com.google.common.base.Preconditions.checkArgument;

@Immutable
abstract class PartialDirectionMatrixSkeleton extends AbstractDirectionMatrix {

  @Override
  @Parameter
  public abstract SortedSet<Location> getOrigins();

  @Override
  @Parameter
  public abstract SortedSet<Location> getDestinations();

  @Override
  @Parameter
  public abstract long[][] getDistancesInMeter();

  @Override
  @Parameter
  public abstract long[][] getDurationsInSeconds();

  @Check
  protected void check() {
    checkArgument(!getOrigins().isEmpty(), "Origins must not be empty!");
    checkArgument(!getDestinations().isEmpty(), "Destinations must not be empty!");
  }

}
