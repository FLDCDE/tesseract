package com.fieldcode.tesseract.distance;

import com.fieldcode.tesseract.Distance;
import com.fieldcode.tesseract.DistanceUnit;

import static com.fieldcode.tesseract.DistanceUnit.KILOMETER;
import static com.fieldcode.tesseract.DistanceUnit.METER;
import static com.fieldcode.tesseract.DistanceUnit.MILE;

public final class Distances {

  public static final Distance ONE_METER_INSTANCE = ImmutableDistance.of(1);
  public static final Distance ONE_KILOMETER_INSTANCE = ImmutableDistance.of(1000);
  private static final Distance ZERO_DISTANCE_INSTANCE = Distances.ofKilometers(0);

  private Distances() {}

  public static Distance zero() {
    return ZERO_DISTANCE_INSTANCE;
  }

  public static Distance oneMeter() {
    return ONE_METER_INSTANCE;
  }

  public static Distance oneKilometer() {
    return ONE_KILOMETER_INSTANCE;
  }

  public static Distance distance(long distance, DistanceUnit unit) {
    return ImmutableDistance.of(unit.convert(distance));
  }

  public static Distance ofMeters(long distance) {
    return distance(distance, METER);
  }

  public static Distance ofKilometers(long distance) {
    return distance(distance, KILOMETER);
  }

  public static Distance ofMile(long distance) {
    return distance(distance, MILE);
  }

}
