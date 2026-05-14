package com.fieldcode.tesseract.location;

import com.fieldcode.tesseract.Distance;
import com.fieldcode.tesseract.Location;
import com.fieldcode.tesseract.distance.Distances;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Utility class providing distance calculation methods between geographical locations. This class cannot be instantiated.
 */
public class LocationDistances {

  /**
   * Earth's mean radius in meters.
   */
  public static final double EARTH_RADIUS_IN_METERS = 6_371_000.0;

  private LocationDistances() {
    throw new AssertionError("No instances of LocationDistances allowed");
  }

  /**
   * Calculate the spherical distance between two locations using the Haversine formula. This method assumes a spherical Earth model with radius {@link #EARTH_RADIUS_IN_METERS}.
   *
   * @param origin the origin location
   * @param destination the destination location
   * @return the distance between the two locations in meters
   * @throws NullPointerException if either location is null
   */
  public static Distance sphericalDistance(Location origin, Location destination) {

    checkNotNull(origin, "The origin location must not be null.");
    checkNotNull(destination, "The destination location must not be null.");

    var latitudeFrom = Math.toRadians(origin.getLatitude());
    var longitudeFrom = Math.toRadians(origin.getLongitude());

    var latitudeTo = Math.toRadians(destination.getLatitude());
    var longitudeTo = Math.toRadians(destination.getLongitude());

    // Haversine formula
    var longitudeDelta = longitudeTo - longitudeFrom;
    var latitudeDelta = latitudeTo - latitudeFrom;

    var a = Math.pow(Math.sin(latitudeDelta / 2), 2)
            + Math.cos(latitudeFrom) * Math.cos(latitudeTo) * Math.pow(Math.sin(longitudeDelta / 2), 2);

    var distanceInMeters = 2 * Math.asin(Math.sqrt(a)) * EARTH_RADIUS_IN_METERS;

    // calculate the result
    return Distances.ofMeters((long) distanceInMeters);
  }

  /**
   * Calculate the Pythagorean distance between two locations as a coordinate pair. There result has no physical meaning, but can be used to compare distances between locations. In
   * case of physical distances, use {@link #sphericalDistance(Location, Location)} instead.
   *
   * @param origin the origin location
   * @param destination the destination location
   * @return the distance between the two locations
   */
  public static double pythagoreanDistance(Location origin, Location destination) {
    double dx = destination.getLongitude() - origin.getLongitude();
    double dy = destination.getLatitude() - origin.getLatitude();
    return Math.sqrt(dx * dx + dy * dy);
  }

}
