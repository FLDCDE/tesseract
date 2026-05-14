package com.fieldcode.tesseract.location;

import java.util.List;

import org.immutables.value.Value.Immutable;

import com.fieldcode.tesseract.Location;


@Immutable(singleton = true)
abstract class LocationAnywhereSkeleton implements Location {

  @Override
  public String getCode() {
    return "anywhere";
  }

  @Override
  public double getLatitude() {
    throw new UnsupportedOperationException("Anywhere location has no latitude");
  }

  @Override
  public double getLongitude() {
    throw new UnsupportedOperationException("Anywhere location has no longitude");
  }

  @Override
  public boolean isAnywhere() {
    return true;
  }

  @Override
  public List<Double> asLonLatList() {
    throw new UnsupportedOperationException("Anywhere location no coordinates");
  }

  @Override
  public String toString() {
    return getCode();
  }

  @Override
  public int compareTo(Location other) {
    return 0;
  }

}
