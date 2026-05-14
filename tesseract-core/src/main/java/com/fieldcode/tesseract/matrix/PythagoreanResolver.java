package com.fieldcode.tesseract.matrix;

import com.fieldcode.tesseract.ClusteredDirectionMatrixResolver;
import com.fieldcode.tesseract.DirectionFeature;
import com.fieldcode.tesseract.Distance;
import com.fieldcode.tesseract.Location;
import com.fieldcode.tesseract.Track;
import com.fieldcode.tesseract.direction.Directions;
import com.fieldcode.tesseract.distance.Distances;
import com.fieldcode.tesseract.track.Tracks;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.Optional;

class PythagoreanResolver implements ClusteredDirectionMatrixResolver {

  private final long clusterRadius;
  private final Distance distanceUnit;
  private final Duration durationUnit;

  protected PythagoreanResolver(Distance distanceUnit, Duration durationUnit, Distance clusterRadius) {
    this.distanceUnit = distanceUnit;
    this.durationUnit = durationUnit;
    this.clusterRadius = clusterRadius.toMeters();
  }

  static PythagoreanResolver of(Distance distanceUnit, Duration durationUnit, Distance clusterRadius) {
    return new PythagoreanResolver(distanceUnit, durationUnit, clusterRadius);
  }

  @Override
  public long getDistanceInMeter(Location origin, Location destination) {
    return getDistance(origin, destination).toMeters();
  }

  @Override
  public Distance getDistance(Location origin, Location destination) {
    return getDirectionFeature(origin, destination).getDistance();
  }

  @Override
  public long[][] getDistancesInMeter() {
    throw new UnsupportedOperationException("Unsupported operation in " + getClass().getSimpleName());
  }

  @Override
  public long getDurationInSeconds(Location origin, Location destination) {
    return getDuration(origin, destination).toSeconds();
  }

  @Override
  public Duration getDuration(Location origin, Location destination) {
    return getDirectionFeature(origin, destination).getDuration();
  }

  @Override
  public long[][] getDurationsInSecond() {
    throw new UnsupportedOperationException("Unsupported operation in " + getClass().getSimpleName());
  }

  @Override
  public Location[] getLocations() {
    throw new UnsupportedOperationException("Unsupported operation in " + getClass().getSimpleName());
  }

  @Override
  public Track getTrack(Location origin, Location destination, OffsetDateTime start) {
    var feature = getDirectionFeature(origin, destination);
    return Tracks.track(feature, start);
  }

  @Override
  public DirectionFeature getDirectionFeature(Location origin, Location destination) {
    var dLatitude = origin.getLatitude() - destination.getLatitude();
    var dLongitude = origin.getLongitude() - destination.getLongitude();

    var d = Math.sqrt(dLatitude * dLatitude + dLongitude * dLongitude);

    var distanceInMeters = (long) (d * distanceUnit.toMeters());
    var durationInSeconds = (long) (d * durationUnit.toSeconds());

    var duration = Duration.ofSeconds(durationInSeconds);
    var distance = Distances.ofMeters(distanceInMeters);
    return Directions.feature(origin, destination, distance, duration);
  }

  @Override
  public DirectionFeature apply(Location origin, Location destination) {
    return getDirectionFeature(origin, destination);
  }

  @Override
  public boolean neighbors(Location a, Location b) {
    var distance = apply(a,b).getDistance().toMeters();
    return distance <= clusterRadius;
  }

  @Override
  public Optional<Integer> getClusterId(Location location) {
    throw new UnsupportedOperationException("Unsupported operation");
  }

}
