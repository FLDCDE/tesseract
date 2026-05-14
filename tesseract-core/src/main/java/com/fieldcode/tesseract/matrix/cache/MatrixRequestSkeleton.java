package com.fieldcode.tesseract.matrix.cache;

import com.fieldcode.tesseract.Location;
import org.immutables.value.Value.Derived;
import org.immutables.value.Value.Immutable;
import org.immutables.value.Value.Parameter;

import java.util.SortedSet;


@Immutable
abstract class MatrixRequestSkeleton implements MatrixRequest {

  @Derived
  public String getHash() {
    return Hash.hash(getLocations());
  }

  @Override
  @Parameter
  public abstract SortedSet<Location> getLocations();

  @Override
  public SortedSet<Location> getOrigins() {
    return getLocations();
  }

  @Override
  public SortedSet<Location> getDestinations() {
    return getLocations();
  }

}
