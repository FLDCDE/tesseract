package com.fieldcode.tesseract.matrix;

import com.fieldcode.tesseract.DirectionFeature;
import com.fieldcode.tesseract.DirectionMatrixResolver;
import com.fieldcode.tesseract.Distance;
import com.fieldcode.tesseract.Location;
import com.fieldcode.tesseract.Track;
import com.fieldcode.tesseract.direction.Directions;
import com.fieldcode.tesseract.location.LocationDistances;
import com.fieldcode.tesseract.track.Tracks;

import java.time.Duration;
import java.time.OffsetDateTime;

class SphericalResolver implements DirectionMatrixResolver {

  private final double speedInMeterPerHour;

  protected SphericalResolver(double speedInKmPerHour) {
    this.speedInMeterPerHour = speedInKmPerHour * 1000;
  }

  static SphericalResolver of(double speedInKmPerHour) {
    return new SphericalResolver(speedInKmPerHour);
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
    var distance = LocationDistances.sphericalDistance(origin, destination);

    var distanceInMeters = distance.toMeters();
    var durationInSeconds = (long) (3600 * distanceInMeters / speedInMeterPerHour);

    var duration = Duration.ofSeconds(durationInSeconds);
    return Directions.feature(origin, destination, distance, duration);
  }

  @Override
  public DirectionFeature apply(Location origin, Location destination) {
    return getDirectionFeature(origin, destination);
  }

}
