package com.fieldcode.tesseract.location;

import java.util.Optional;

import com.fieldcode.tesseract.Location;

import static java.lang.Double.parseDouble;

final class LocationParser {

  public static final String ANYWHERE_LITERAL_CODE = "anywhere";
  private static final String SEPARATOR = ",";

  private LocationParser() {
  }

  static String generateCode(Double latitude, Double longitude) {
    return latitude + SEPARATOR + longitude;
  }

  static String code(double latitude, double longitude) {
    return generateCode(latitude, longitude);
  }

  static Location parse(String code) {
    return tryParse(code).orElseThrow(() -> new IllegalArgumentException("Unable to parse location code: " + code));
  }

  static Optional<Location> tryParse(String code) {
    if (ANYWHERE_LITERAL_CODE.equals(code)) {
      return Optional.ofNullable(Locations.anywhere());
    }

    try {
      var split = code.split(SEPARATOR);
      var latitude = parseDouble(split[0]);
      var longitude = parseDouble(split[1]);
      return Optional.of(create(latitude, longitude));
    } catch (Exception e) {
      return Optional.empty();
    }
  }

  private static Location create(double latitude, double longitude) {
    return Locations.location(latitude, longitude);
  }

}
