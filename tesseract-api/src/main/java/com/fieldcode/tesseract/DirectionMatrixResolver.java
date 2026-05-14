package com.fieldcode.tesseract;


import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.function.BiFunction;

public interface DirectionMatrixResolver extends BiFunction<Location, Location, DirectionFeature> {

  /**
   * Returns the distance in meters between two locations.
   *
   * @param origin is the starting point
   * @param destination is the end point
   * @return the distance in meters
   * @deprecated Use {@link #getDistance(Location, Location)} instead
   */
  @Deprecated(forRemoval = true)
  long getDistanceInMeter(Location origin, Location destination);

  /**
   * Returns the distance between two locations.
   *
   * @param origin is the starting point
   * @param destination is the end point
   * @return the distance
   */
  Distance getDistance(Location origin, Location destination);

  /**
   * Returns the distances in meters between all locations.
   *
   * @return the distances in meters
   * @deprecated Use the original {@link DirectionMatrix} if you need the raw data
   */
  @Deprecated(forRemoval = true)
  long[][] getDistancesInMeter();


  /**
   * Returns the duration in seconds between two locations. The duration is the time it takes to travel from the origin to the destination.
   *
   * @param origin is the starting point
   * @param destination is the end point
   * @return the duration in seconds
   * @deprecated Use {@link #getDuration(Location, Location)} instead
   */
  @Deprecated(forRemoval = true)
  long getDurationInSeconds(Location origin, Location destination);

  /**
   * Returns the duration between two locations. The duration is the time it takes to travel from the origin to the destination.
   *
   * @param origin is the starting point
   * @param destination is the end point
   * @return the duration
   */
  Duration getDuration(Location origin, Location destination);

  /**
   * Returns the durations in seconds between all locations.
   *
   * @return the durations in seconds
   * @deprecated Use the original {@link DirectionMatrix} if you need the raw data
   */
  @Deprecated(forRemoval = true)
  long[][] getDurationsInSecond();

  /**
   * Returns the locations handled by the resolver. If the resolver is not based on a {@link DirectionMatrix} an empty array is returned.
   *
   * @return the locations
   * @deprecated Use the original {@link DirectionMatrix} if you need the raw data
   */
  @Deprecated(forRemoval = true)
  Location[] getLocations();

  /**
   * Returns the track between two locations starting at the specified time. See {@link Track} for more information.
   *
   * @param origin is the starting point
   * @param destination is the end point
   * @param start is the starting time
   * @return the track
   */
  Track getTrack(Location origin, Location destination, OffsetDateTime start);

  /**
   * Returns the track between two locations starting at the specified time. See {@link DirectionFeature} for more information.
   *
   * @param origin is the starting point
   * @param destination is the end point
   * @return the track
   */
  DirectionFeature getDirectionFeature(Location origin, Location destination);

}
