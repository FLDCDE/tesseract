package com.fieldcode.tesseract.matrix;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

import com.fieldcode.tesseract.DirectionMatrix;
import com.fieldcode.tesseract.Location;
import com.google.common.base.Preconditions;

import lombok.Value;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;
import static java.util.stream.Collectors.toSet;

public class DirectionMatrixBuilder {

  private final List<DirectionFeatures> features = new ArrayList<>();
  private final SortedSet<Location> locations = new TreeSet<>();

  private DirectionMatrixBuilder() {
  }

  public static DirectionMatrixBuilder of() {
    return new DirectionMatrixBuilder();
  }

  public static DirectionMatrixBuilder of(DirectionMatrix matrix) {
    var builder = new DirectionMatrixBuilder();
    fill(builder, matrix);
    return builder;
  }

  private static void fill(DirectionMatrixBuilder builder, DirectionMatrix matrix) {
    var locations = new ArrayList<>(matrix.getLocations());
    var size = locations.size();

    for (int i = 0; i < size; i++) {
      for (int j = 0; j < size; j++) {

        var origin = locations.get(i);
        var destination = locations.get(j);
        var distance = matrix.getDistancesInMeter()[i][j];
        var duration = matrix.getDurationsInSeconds()[i][j];

        builder.set(origin, destination, distance, duration);
      }
    }
  }

  private static Function<Location, Integer> getIndexMapper(SortedSet<Location> locations) {
    var counter = new AtomicInteger(0);

    var locationIndexes = locations
        .stream()
        .collect(toMap(identity(), location -> counter.getAndIncrement()));

    return location -> Optional.ofNullable(locationIndexes.get(location))
        .orElseThrow(() -> new IllegalStateException("No index found for location: " + location));
  }

  public void set(DirectionMatrix matrix) {
    clear();
    fill(this, matrix);
  }

  public DirectionMatrixBuilder set(Location origin, Location destination, long distanceInMeters, long durationInSeconds) {

    var feature = DirectionFeatures.of(origin, destination, distanceInMeters, durationInSeconds);
    features.add(feature);
    locations.add(origin);
    locations.add(destination);

    return this;
  }

  public void clear() {
    features.clear();
    locations.clear();
  }

  public DirectionMatrix build() {
    checkState();

    var orderedLocations = getOrderedLocations();

    var indexMapper = getIndexMapper(locations);

    var size = locations.size();
    var distances = new long[size][size];
    var durations = new long[size][size];

    features.forEach(feature -> {

      var originIndex = indexMapper.apply(feature.origin);
      var destinationIndex = indexMapper.apply(feature.destination);
      var distance = feature.getDistanceInMeters();
      var duration = feature.getDurationInSeconds();

      distances[originIndex][destinationIndex] = distance;
      durations[originIndex][destinationIndex] = duration;

    });

    return ImmutableDirectionMatrix.of(distances, durations, orderedLocations);
  }

  private void checkState() {
    Preconditions.checkState(
        getOrigins().equals(locations),
        "Origin location set must be equal to all locations"
    );
    Preconditions.checkState(
        getDestinations().equals(locations),
        "Destination location set must be equal to all locations"
    );
    Preconditions.checkState(locations.size() * locations.size() == features.size(),
        "Number of features must be equal to number of locations raised to the second power [features=%d, locations=%d]",
        features.size(),
        locations.size()
    );
  }

  private SortedSet<Location> getOrderedLocations() {
    return new TreeSet<>(locations);
  }

  private Set<Location> getOrigins() {
    return features.stream()
        .map(DirectionFeatures::getOrigin)
        .collect(toSet());
  }

  private Set<Location> getDestinations() {
    return features.stream()
        .map(DirectionFeatures::getDestination)
        .collect(toSet());
  }

  @Value(staticConstructor = "of")
  private static class DirectionFeatures {

    Location origin;
    Location destination;
    long distanceInMeters;
    long durationInSeconds;

  }

}
