package com.fieldcode.tesseract.matrix;

import com.fieldcode.tesseract.DirectionMatrix;
import com.fieldcode.tesseract.Location;
import com.google.common.collect.ImmutableSortedSet;
import org.immutables.value.Value.Derived;
import org.immutables.value.Value.Immutable;

import java.util.SortedSet;

@Immutable(singleton = true)
public abstract class EmptyDirectionMatrixSkeleton extends AbstractDirectionMatrix implements DirectionMatrix {

  @Override
  @Derived
  public SortedSet<Location> getOrigins() {
    return ImmutableSortedSet.of();
  }

  @Override
  @Derived
  public SortedSet<Location> getDestinations() {
    return ImmutableSortedSet.of();
  }

  @Override
  @Derived
  public long[][] getDistancesInMeter() {
    return new long[0][];
  }

  @Override
  @Derived
  public long[][] getDurationsInSeconds() {
    return new long[0][];
  }

  @Override
  @Derived
  public SortedSet<Location> getLocations() {
    return ImmutableSortedSet.of();
  }


}
