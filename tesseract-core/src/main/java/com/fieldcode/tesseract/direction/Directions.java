package com.fieldcode.tesseract.direction;

import java.time.Duration;
import java.util.SortedSet;
import java.util.stream.Stream;

import com.fieldcode.tesseract.Direction;
import com.fieldcode.tesseract.DirectionFeature;
import com.fieldcode.tesseract.Distance;
import com.fieldcode.tesseract.Location;

public class Directions {

  private Directions() {}

  public static Direction direction(Location origin, Location destination) {
    return ImmutableDirection.of(origin, destination);
  }

  public static DirectionFeature feature(Location origin, Location destination, Distance distance, Duration duration) {
    return ImmutableDirectionFeature.of(duration, distance, origin, destination);
  }

  public static Stream<Direction> getAllDirections(SortedSet<Location> origins, SortedSet<Location> destinations) {
    return origins.stream()
        .flatMap(
            origin -> destinations.stream()
                .map(destination -> direction(origin, destination))
        );
  }
}
