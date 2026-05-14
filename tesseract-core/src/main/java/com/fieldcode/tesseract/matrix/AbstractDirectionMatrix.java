package com.fieldcode.tesseract.matrix;

import com.fieldcode.tesseract.Direction;
import com.fieldcode.tesseract.DirectionFeature;
import com.fieldcode.tesseract.Location;
import com.fieldcode.tesseract.PartialDirectionMatrix;
import com.fieldcode.tesseract.direction.Directions;
import com.fieldcode.tesseract.distance.Distances;
import org.immutables.value.Value.Lazy;

import java.time.Duration;
import java.util.SortedSet;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.ToIntFunction;
import java.util.stream.Stream;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;

public abstract class AbstractDirectionMatrix implements PartialDirectionMatrix {

  @Override
  public Stream<DirectionFeature> getAllFeatures() {
    return Directions
        .getAllDirections(getOrigins(), getDestinations())
        .map(this::getTrackFeature);
  }

  @Override
  public long getDistanceInMeters(Location origin, Location destination) {
    return getValue(getDistancesInMeter(), origin, destination);
  }

  @Override
  public long getDistanceInMeters(Direction direction) {
    return getDistanceInMeters(direction.getOrigin(), direction.getDestination());
  }

  @Override
  public long getDurationInSeconds(Location origin, Location destination) {
    return getValue(getDurationsInSeconds(), origin, destination);
  }

  @Override
  public long getDurationInSeconds(Direction direction) {
    return getDurationInSeconds(direction.getOrigin(), direction.getDestination());
  }

  @Lazy
  protected ToIntFunction<Location> getOriginIndexMapper() {
    return getIndexMapper(getOrigins());
  }

  @Lazy
  protected ToIntFunction<Location> getDestinationIndexMapper() {
    return getIndexMapper(getDestinations());
  }

  private ToIntFunction<Location> getIndexMapper(SortedSet<Location> locations) {
    var index = new AtomicInteger();
    var map = locations
        .stream()
        .collect(toMap(identity(), l -> index.getAndIncrement()));
    return map::get;
  }

  private long getValue(long[][] matrix, Location origin, Location destination) {
    var row = getOriginIndexMapper().applyAsInt(origin);
    var column = getDestinationIndexMapper().applyAsInt(destination);
    return matrix[row][column];
  }

  private DirectionFeature getTrackFeature(Direction direction) {
    var distance = Distances.ofMeters(getDistanceInMeters(direction));
    var duration = Duration.ofSeconds(getDurationInSeconds(direction));
    var origin = direction.getOrigin();
    var destination = direction.getDestination();

    return Directions.feature(origin, destination, distance, duration);
  }

}
