package com.fieldcode.tesseract.location;

import java.util.Optional;

import com.fieldcode.tesseract.Location;


public final class Locations {

  private Locations() {}


  public static Location parse(String code) {
    return LocationParser.parse(code);
  }

  public static Location location(String code) {
    return LocationParser.parse(code);
  }

  public static Optional<Location> tryParse(String code) {
    return LocationParser.tryParse(code);
  }

  public static Location location(double latitude, double longitude) {
    return ImmutableLocation.of(latitude, longitude);
  }

  public static Location anywhere() {
    return ImmutableLocationAnywhere.of();
  }

}
