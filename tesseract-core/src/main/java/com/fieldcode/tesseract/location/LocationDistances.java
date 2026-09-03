package com.fieldcode.tesseract.location;

import com.fieldcode.tesseract.DirectionMatrixResolver;
import com.fieldcode.tesseract.Distance;
import com.fieldcode.tesseract.Location;
import com.fieldcode.tesseract.distance.Distances;

import java.util.function.Function;
import java.util.function.ToDoubleBiFunction;
import java.util.stream.Stream;
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

  /**
   * Sort items by their distance from a reference location using a custom distance function.
   *
   * @param input the items to sort
   * @param reference the reference location to measure distance from
   * @param locationMapper extracts a {@link Location} from each item
   * @param distance computes the distance between two locations
   * @param reverse if {@code true}, sorts farthest first; if {@code false}, sorts nearest first
   * @param <T> the item type
   * @return a stream of items sorted by distance from the reference location
   * @throws NullPointerException if any of the input parameters is null
   */
  public static <T> Stream<T> sort(Iterable<T> input, Location reference, Function<T, ? extends Location> locationMapper, ToDoubleBiFunction<Location, Location> distance, boolean reverse) {
    checkNotNull(input, "Input items must not be null");
    checkNotNull(reference, "Reference location must not be null");
    checkNotNull(locationMapper, "Location mapper function must not be null");
    checkNotNull(distance, "Distance function must not be null");
    return ProximitySort.sort(input, reference, locationMapper, distance, reverse);
  }

  /**
   * Sort items nearest first using a {@link DirectionMatrixResolver} for distance calculation.
   *
   * @param input the items to sort
   * @param reference the reference location to measure distance from
   * @param locationMapper extracts a {@link Location} from each item
   * @param resolver the direction matrix resolver to compute distances
   * @param <T> the item type
   * @return a stream of items sorted nearest first
   * @throws NullPointerException if any of the input parameters is null
   */
  public static <T> Stream<T> sortByNearest(Iterable<T> input, Location reference, Function<T, ? extends Location> locationMapper, DirectionMatrixResolver resolver) {
    checkNotNull(resolver, "Direction matrix resolver must not be null");
    return sort(input, reference, locationMapper, (a, b) -> (double) resolver.getDistance(a, b).toMeters(), false);
  }

  /**
   * Sort items nearest first using {@link #pythagoreanDistance(Location, Location) Pythagorean distance}.
   *
   * @param input the items to sort
   * @param reference the reference location to measure distance from
   * @param locationMapper extracts a {@link Location} from each item
   * @param <T> the item type
   * @return a stream of items sorted nearest first
   * @throws NullPointerException if any of the input parameters is null
   */
  public static <T> Stream<T> sortByNearestPythagorean(Iterable<T> input, Location reference, Function<T, ? extends Location> locationMapper) {
    return sort(input, reference, locationMapper, LocationDistances::pythagoreanDistance, false);
  }

  /**
   * Sort items nearest first using {@link #sphericalDistance(Location, Location) spherical (Haversine) distance}.
   *
   * @param input the items to sort
   * @param reference the reference location to measure distance from
   * @param locationMapper extracts a {@link Location} from each item
   * @param <T> the item type
   * @return a stream of items sorted nearest first
   * @throws NullPointerException if any of the input parameters is null
   */
  public static <T> Stream<T> sortByNearestSpherical(Iterable<T> input, Location reference, Function<T, ? extends Location> locationMapper) {
    return sort(input, reference, locationMapper, (a, b) -> (double) LocationDistances.sphericalDistance(a, b).toMeters(), false);
  }

  /**
   * Sort items farthest first using a {@link DirectionMatrixResolver} for distance calculation.
   *
   * @param input the items to sort
   * @param reference the reference location to measure distance from
   * @param locationMapper extracts a {@link Location} from each item
   * @param resolver the direction matrix resolver to compute distances
   * @param <T> the item type
   * @return a stream of items sorted farthest first
   * @throws NullPointerException if any of the input parameters is null
   */
  public static <T> Stream<T> sortByFarthest(Iterable<T> input, Location reference, Function<T, ? extends Location> locationMapper, DirectionMatrixResolver resolver) {
    checkNotNull(resolver, "Direction matrix resolver must not be null");
    return sort(input, reference, locationMapper, (a, b) -> (double) resolver.getDistance(a, b).toMeters(), true);
  }

  /**
   * Sort items farthest first using {@link #pythagoreanDistance(Location, Location) Pythagorean distance}.
   *
   * @param input the items to sort
   * @param reference the reference location to measure distance from
   * @param locationMapper extracts a {@link Location} from each item
   * @param <T> the item type
   * @return a stream of items sorted farthest first
   * @throws NullPointerException if any of the input parameters is null
   */
  public static <T> Stream<T> sortByFarthestPythagorean(Iterable<T> input, Location reference, Function<T, ? extends Location> locationMapper) {
    return sort(input, reference, locationMapper, LocationDistances::pythagoreanDistance, true);
  }

  /**
   * Sort items farthest first using {@link #sphericalDistance(Location, Location) spherical (Haversine) distance}.
   *
   * @param input the items to sort
   * @param reference the reference location to measure distance from
   * @param locationMapper extracts a {@link Location} from each item
   * @param <T> the item type
   * @return a stream of items sorted farthest first
   * @throws NullPointerException if any of the input parameters is null
   */
  public static <T> Stream<T> sortByFarthestSpherical(Iterable<T> input, Location reference, Function<T, ? extends Location> locationMapper) {
    return sort(input, reference, locationMapper, (a, b) -> (double) LocationDistances.sphericalDistance(a, b).toMeters(), true);
  }

}
