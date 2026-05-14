package com.fieldcode.tesseract;

public enum DistanceUnit {
  CENTIMETER("centimeter", "cm", DistanceUnit.CENTIMETER_SCALE),
  METER("meter", "m", DistanceUnit.METER_SCALE),
  KILOMETER("kilometer", "km", DistanceUnit.KILOMETER_SCALE),
  MILE("mile", "mi", DistanceUnit.MILE_SCALE);

  private static final double METER_SCALE = 1;
  private static final double CENTIMETER_SCALE = METER_SCALE / 100;
  private static final double KILOMETER_SCALE = 1000 * METER_SCALE;
  private static final double MILE_SCALE = 1609.34;

  private final String name;
  private final String symbol;
  private final double scale;

  DistanceUnit(String name, String symbol, double scale) {
    this.name = name;
    this.symbol = symbol;
    this.scale = scale;
  }

  public String getName() {
    return name;
  }

  public String getSymbol() {
    return symbol;
  }

  private long cvt(long distance, DistanceUnit destinationUnit) {
    return (long) (distance * scale / destinationUnit.scale);
  }

  public long convert(long meters) {
    return (long) (meters * scale);
  }

  public long toCentimeter(long distance) {
    return cvt(distance, CENTIMETER);
  }

  public long toMeter(long distance) {
    return cvt(distance, METER);
  }

  public long toKilometer(long distance) {
    return cvt(distance, KILOMETER);
  }

  public long toMile(long distance) {
    return cvt(distance, MILE);
  }

}
