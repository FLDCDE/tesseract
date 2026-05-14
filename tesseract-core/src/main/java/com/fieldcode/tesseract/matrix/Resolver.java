package com.fieldcode.tesseract.matrix;

import com.fieldcode.tesseract.DirectionFeature;
import com.fieldcode.tesseract.DirectionMatrix;
import com.fieldcode.tesseract.DirectionMatrixResolver;
import com.fieldcode.tesseract.Distance;
import com.fieldcode.tesseract.Location;
import com.fieldcode.tesseract.Track;
import com.fieldcode.tesseract.direction.Directions;
import com.fieldcode.tesseract.distance.Distances;
import com.fieldcode.tesseract.track.Tracks;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.stream.IntStream;

class Resolver implements DirectionMatrixResolver {

  private static final long MAX_DURATION = Duration.ofDays(80).toSeconds();   // Around the World in Eighty Days

  private final long[][] distances;
  private final long[][] durations;
  private final Location[] locations;
  private final Map<String, Integer> index = new TreeMap<>();

  protected Resolver(SortedSet<Location> locations, long[][] distances, long[][] durations) {
    this.distances = distances;
    this.durations = durations;
    this.locations = locations.toArray(new Location[0]);
    IntStream.range(0, locations.size())
        .forEach(i -> index.put(this.locations[i].getCode(), i));
    workaround();
  }

  static Resolver of(DirectionMatrix directionMatrix) {
    return Resolver.of(directionMatrix.getLocations(), directionMatrix.getDistancesInMeter(), directionMatrix.getDurationsInSeconds());
  }

  static Resolver of(SortedSet<Location> locations, long[][] distancesInMeter, long[][] durationsInSecond) {
    return new Resolver(locations, distancesInMeter, durationsInSecond);
  }

  private void workaround() {
    // Workaround for preventing duration max limit
    // exception in route feature calculator
    var n = durations.length;

    for (int i = 0; i < n; i++) {
      for (int j = 0; j < n; j++) {
        reduceMaxDurations(i, j);
      }
    }
  }

  private void reduceMaxDurations(int row, int column) {
    if (durations[row][column] == Integer.MAX_VALUE) {
      durations[row][column] = MAX_DURATION;
    }
  }

  private int getIndex(Location location) {
    try {
      return index.get(location.getCode());
    } catch (NullPointerException e) {
      throw new IllegalArgumentException("Location not found: " + location);
    }
  }

  @Override
  public long getDistanceInMeter(Location origin, Location destination) {
    if (origin.isAnywhere() || destination.isAnywhere()) {
      return 0;
    }
    return distances[getIndex(origin)][getIndex(destination)];
  }

  @Override
  public Distance getDistance(Location origin, Location destination) {
    return Distances.ofMeters(getDistanceInMeter(origin, destination));
  }

  @Override
  public long[][] getDistancesInMeter() {
    return MatrixCopy.copy(distances);
  }

  @Override
  public long getDurationInSeconds(Location origin, Location destination) {
    if (origin.isAnywhere() || destination.isAnywhere()) {
      return 0;
    }
    return durations[getIndex(origin)][getIndex(destination)];
  }

  @Override
  public Duration getDuration(Location origin, Location destination) {
    return Duration.ofSeconds(getDurationInSeconds(origin, destination));
  }

  @Override
  public long[][] getDurationsInSecond() {
    return MatrixCopy.copy(durations);
  }

  @Override
  public Location[] getLocations() {
    var copy = new Location[locations.length];
    System.arraycopy(locations, 0, copy, 0, locations.length);
    return copy;
  }

  @Override
  public Track getTrack(Location origin, Location destination, OffsetDateTime start) {
    var duration = getDuration(origin, destination);
    var distance = getDistance(origin, destination);

    return Tracks.track(origin, destination, distance, duration, start);
  }

  @Override
  public DirectionFeature getDirectionFeature(Location origin, Location destination) {
    var duration = getDuration(origin, destination);
    var distance = getDistance(origin, destination);

    return Directions.feature(origin, destination, distance, duration);
  }

  @Override
  public DirectionFeature apply(Location origin, Location destination) {
    return getDirectionFeature(origin, destination);
  }

}
