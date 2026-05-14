package com.fieldcode.tesseract.location;

import org.immutables.value.Value.Immutable;
import org.immutables.value.Value.Parameter;

import com.fieldcode.tesseract.Location;

import java.util.Comparator;
import java.util.List;


@Immutable
abstract class LocationSkeleton implements Location {

  private static final Comparator<Location> LOCATION_COMPARATOR = Comparator
      .comparingDouble(Location::getLatitude)
      .thenComparingDouble(Location::getLongitude);

  @Override
  public String getCode() {
    return LocationParser.code(getLatitude(), getLongitude());
  }

  @Override
  @Parameter
  public abstract double getLatitude();

  @Override
  @Parameter
  public abstract double getLongitude();

  @Override
  public boolean isAnywhere() {
    return false;
  }

  @Override
  public List<Double> asLonLatList() {
    return List.of(getLongitude(), getLatitude());
  }

  @Override
  public int compareTo(Location other) {
    return LOCATION_COMPARATOR.compare(this, other);
  }

  @Override
  public String toString() {
    return getCode();
  }

}
