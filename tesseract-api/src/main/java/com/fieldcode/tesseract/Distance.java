package com.fieldcode.tesseract;


public interface Distance extends Comparable<Distance> {

  long toMeters();

  default long toCentimeters() {
    return DistanceUnit.METER.toCentimeter(toMeters());
  }

  default long toKilometers() {
    return DistanceUnit.METER.toKilometer(toMeters());
  }

  default long toMiles() {
    return DistanceUnit.METER.toMile(toMeters());
  }

  Distance plus(Distance other);

  boolean lt(Distance other);

  boolean le(Distance other);

  boolean gt(Distance other);

  boolean ge(Distance other);

  boolean eq(Distance other);

  boolean ne(Distance other);

}
