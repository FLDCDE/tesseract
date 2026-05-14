package com.fieldcode.tesseract.location;

import com.fieldcode.tesseract.Direction;
import com.fieldcode.tesseract.Distance;
import com.fieldcode.tesseract.Location;

import java.util.Collection;
import static com.fieldcode.tesseract.direction.Directions.direction;
import static com.fieldcode.tesseract.location.Locations.location;


public class LocationUtils {

  public static final long EARTH_RADIUS_IN_METERS = 6371_000;

  private LocationUtils() {
  }

  /**
   * Calculate the spherical distance between two locations.
   *
   * @param from the origin location
   * @param to the destination location
   * @return the distance between the two locations
   * @deprecated Use {@link LocationDistances#sphericalDistance(Location, Location)} instead.
   */
  @Deprecated(forRemoval = true)
  public static Distance sphericalDistance(Location from, Location to) {
    return LocationDistances.sphericalDistance(from, to);
  }

  /**
   * Calculate the Pythagorean distance between two locations as a coordinate pair.
   *
   * @param origin the origin location
   * @param destination the destination location
   * @return the distance between the two locations
   * @deprecated Use {@link LocationDistances#pythagoreanDistance(Location, Location)} instead.
   */
  @Deprecated(forRemoval = true)
  public static double pythagoreanDistance(Location origin, Location destination) {
    return LocationDistances.pythagoreanDistance(origin, destination);
  }

  public static Direction boundingDiagonal(Collection<Location> locations) {

    var latitudes = locations.stream()
        .mapToDouble(Location::getLatitude)
        .summaryStatistics();

    var longitudes = locations.stream()
        .mapToDouble(Location::getLongitude)
        .summaryStatistics();

    var origin = location(latitudes.getMin(), longitudes.getMin());
    var destination = location(latitudes.getMax(), longitudes.getMax());

    return direction(origin, destination);
  }

}
