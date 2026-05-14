package com.fieldcode.tesseract.matrix;

import com.fieldcode.tesseract.ClusteredDirectionMatrixResolver;
import com.fieldcode.tesseract.DirectionMatrix;
import com.fieldcode.tesseract.DirectionMatrixResolver;
import com.fieldcode.tesseract.Distance;
import com.fieldcode.tesseract.Location;
import com.fieldcode.tesseract.distance.Distances;
import com.fieldcode.tesseract.location.LocationCluster;
import com.fieldcode.tesseract.location.LocationDistances;
import com.google.common.collect.Multimap;

import java.time.Duration;
import java.util.Collection;
import java.util.SortedSet;
import java.util.TreeSet;
import static java.util.stream.Collectors.toList;

/**
 * Factory class for creating {@link DirectionMatrixResolver} and {@link ClusteredDirectionMatrixResolver} instances using various calculation models.
 */
public class DirectionResolverFactory {

  private DirectionResolverFactory() {
  }

  /**
   * Creates a new {@link DirectionMatrixResolver} instance based on the given {@link DirectionMatrix}.
   *
   * @param directionMatrix the direction matrix
   * @return a new {@link DirectionMatrixResolver} instance
   * @deprecated Use {@link DirectionResolverFactory#resolver(DirectionMatrix)} instead as it provides a more consistent naming convention
   */
  @Deprecated(forRemoval = true)
  public static DirectionMatrixResolver create(DirectionMatrix directionMatrix) {
    return Resolver.of(directionMatrix);
  }


  /**
   * Creates a new {@link DirectionMatrixResolver} instance based on the given {@link DirectionMatrix}.
   *
   * @param matrix the direction matrix
   * @return a new {@link DirectionMatrixResolver} instance
   */
  public static DirectionMatrixResolver resolver(DirectionMatrix matrix) {
    return Resolver.of(matrix);
  }

  /**
   * Creates a new {@link ClusteredDirectionMatrixResolver} instance based on the given {@link DirectionMatrix} and a radius. The radius is used to cluster locations that are
   * within the specified distance using {@link LocationCluster}.
   *
   * <p>See {@link ClusteredResolver#neighbors(Location, Location)} for more information on determining
   * when locations are considered neighbors.
   *
   * @param matrix the direction matrix
   * @param radius the radius for clustering locations
   * @return a new {@link ClusteredDirectionMatrixResolver} instance
   */
  public static ClusteredDirectionMatrixResolver resolver(DirectionMatrix matrix, Distance radius) {
    var locations = matrix.getLocations();
    var clusters = LocationCluster.spherical(locations, radius);
    return resolver(matrix, clusters);
  }

  /**
   * Creates a new {@link ClusteredDirectionMatrixResolver} instance based on the given {@link DirectionMatrix} and a map of clusters. The clusters map must not contain duplicate
   * locations. If duplicates are found, an {@link IllegalArgumentException} is thrown.
   *
   * <p>See {@link ClusteredDirectionMatrixResolver#neighbors(Location, Location)} for more information on determining
   * when locations are considered neighbors.
   *
   * @param matrix the direction matrix
   * @param clusters the clusters map
   * @return a new {@link ClusteredDirectionMatrixResolver} instance
   */
  public static ClusteredDirectionMatrixResolver resolver(DirectionMatrix matrix, Multimap<Integer, Location> clusters) {
    var resolver = Resolver.of(matrix);
    return ClusteredResolver.of(resolver, clusters);
  }

  /**
   * Creates a new {@link DirectionMatrixResolver} instance using the spherical model for calculating distances. Durations between locations are calculated based on the distance
   * and the given speed in kilometers per hour.
   *
   * <p><strong>Warning:</strong> This method is useful for testing and prototyping, but it is not
   * recommended for production use.
   *
   * @param speedInKmPerHour the speed in kilometers per hour
   * @return a new {@link DirectionMatrixResolver} instance
   */
  public static DirectionMatrixResolver spherical(double speedInKmPerHour) {
    return SphericalResolver.of(speedInKmPerHour);
  }


  /**
   * Creates a new {@link ClusteredDirectionMatrixResolver} instance using the spherical model for calculating distances. Durations between locations are calculated based on the
   * distance and the given speed in kilometers per hour. The neighbors method will return true if the locations belong to the same cluster.
   *
   * <p><strong>Warning:</strong> This method is useful for testing and prototyping, but it is not
   * recommended for production use.
   *
   * @param speedInKmPerHour the speed in kilometers per hour
   * @param clusters the clusters map
   * @return a new {@link ClusteredDirectionMatrixResolver} instance
   */
  public static ClusteredDirectionMatrixResolver spherical(double speedInKmPerHour, Multimap<Integer, Location> clusters) {
    return ClusteredResolver.of(spherical(speedInKmPerHour), clusters);
  }

  /**
   * Creates a new {@link DirectionMatrixResolver} instance based on the given locations, distance matrix, and duration matrix. The locations are ordered in the same order as the
   * matrices.
   *
   * @param orderedLocations the ordered locations
   * @param distanceInMeters the distance matrix in meters
   * @param durationsInSeconds the duration matrix in seconds
   * @return a new {@link DirectionMatrixResolver} instance
   * @deprecated Use {@link DirectionResolverFactory#resolver(DirectionMatrix)} with a matrix created via {@link DirectionMatrices#matrix(SortedSet, long[][], long[][])} instead
   */
  @Deprecated(forRemoval = true)
  public static DirectionMatrixResolver create(SortedSet<Location> orderedLocations, long[][] distanceInMeters, long[][] durationsInSeconds) {
    var matrix = DirectionMatrices.matrix(orderedLocations, distanceInMeters, durationsInSeconds);
    return Resolver.of(matrix);
  }

  /**
   * Creates a new {@link DirectionMatrixResolver} instance based on the given locations. The distances and durations between locations are calculated using the Pythagorean
   * theorem. The distance and duration units are used to convert the results to the desired units.
   *
   * <p>For example, to calculate the distances in meters and durations in minutes:
   * <pre>
   *   var resolver = DirectionResolverFactory.pythagorean(locations, Distances.ofMeters(10), Durations.ofMinutes(10));
   *
   *   var origin = location("0.0,0.0");
   *   var destination = location("5.0,0.0");
   *
   *   var distance = resolver.getDistance(origin, destination);
   *   var duration = resolver.getDuration(origin, destination);
   *
   *   System.out.println(distance); // 50m
   *   System.out.println(duration); // PT50M
   * </pre>
   *
   * <p><strong>Warning:</strong> This method is useful for testing and prototyping, but it is not
   * recommended for production use.
   *
   * @param locations the locations
   * @param distanceUnit the distance unit
   * @param durationUnit the duration unit
   * @return a new {@link DirectionMatrixResolver} instance
   */
  public static DirectionMatrixResolver pythagorean(Collection<Location> locations, Distance distanceUnit, Duration durationUnit) {
    var matrix = matrix(locations, distanceUnit, durationUnit);
    return create(matrix);
  }

  /**
   * Creates a new {@link DirectionMatrixResolver} instance based on the given distance and duration units. The distances and durations between locations are calculated using the
   * Pythagorean theorem.
   *
   * @param distanceUnit the distance unit
   * @param durationUnit the duration unit
   * @return a new {@link DirectionMatrixResolver} instance
   */
  public static DirectionMatrixResolver pythagorean(Distance distanceUnit, Duration durationUnit) {
    return PythagoreanResolver.of(distanceUnit, durationUnit, Distances.zero());
  }

  /**
   * Creates a new {@link ClusteredDirectionMatrixResolver} instance based on the given distance and duration units. The distances and durations between locations are calculated
   * using the Pythagorean theorem. The neighbors method will return true if the locations belong to the same cluster.
   *
   * @param distanceUnit the distance unit
   * @param durationUnit the duration unit
   * @param clusters the clusters map
   * @return a new {@link ClusteredDirectionMatrixResolver} instance
   */
  public static ClusteredDirectionMatrixResolver pythagorean(Distance distanceUnit, Duration durationUnit, Multimap<Integer, Location> clusters) {
    var resolver = PythagoreanResolver.of(distanceUnit, durationUnit, Distances.zero());
    return ClusteredResolver.of(resolver, clusters);
  }

  /**
   * Creates a new {@link DirectionMatrixResolver} instance based on the given distance and duration units. The distances and durations between locations are calculated using the
   * Pythagorean theorem. The radius is used to cluster locations that are within the specified distance.
   *
   * @param distanceUnit the distance unit
   * @param durationUnit the duration unit
   * @param clusterRadius the radius for clustering locations
   * @return a new {@link DirectionMatrixResolver} instance
   */
  public static ClusteredDirectionMatrixResolver pythagorean(Distance distanceUnit, Duration durationUnit, Distance clusterRadius) {
    return PythagoreanResolver.of(distanceUnit, durationUnit, clusterRadius);
  }

  /**
   * Creates a new {@link DirectionMatrixResolver} instance based on the given locations. The distances and durations between locations are calculated using the Pythagorean
   * theorem. The distance and duration units are used to convert the results to the desired units. The radius is used to cluster locations that are within the specified distance.
   *
   * <p>For example, to calculate the distances in meters and durations in minutes:
   * <pre>
   *   var resolver = DirectionResolverFactory.pythagorean(locations, Distances.ofMeters(1), Durations.ofMinutes(1), Distances.ofMeters(100));
   *
   *   var origin = location("0.0,0.0");
   *   var destination = location("1.0,0.0");
   *
   *   var distance = resolver.getDistance(origin, destination);
   *   var duration = resolver.getDuration(origin, destination);
   *
   *   System.out.println(distance); // 1m
   *   System.out.println(duration); // PT1M
   *
   *   System.out.println(resolver.neighbors(origin, destination)); // true
   *   System.out.println(resolver.neighbors(origin, location("0.1,0.1"))); // false
   * </pre>
   *
   * <p><strong>Warning:</strong> This method is useful for testing and prototyping, but it is not
   * recommended for production use.
   *
   * @param locations the locations
   * @param distanceUnit the distance unit
   * @param durationUnit the duration unit
   * @param radius the radius for clustering locations
   * @return a new {@link DirectionMatrixResolver} instance
   */
  public static DirectionMatrixResolver pythagorean(Collection<Location> locations, Distance distanceUnit, Duration durationUnit, Distance radius) {
    return resolver(
        matrix(locations, distanceUnit, durationUnit),
        radius
    );
  }

  private static DirectionMatrix matrix(Collection<Location> locations, Distance distanceUnit, Duration durationUnit) {
    var distanceFactor = distanceUnit.toMeters();
    var durationFactor = durationUnit.toSeconds();

    var orderedLocations = locations.stream()
        .sorted()
        .distinct()
        .collect(toList());

    var size = orderedLocations.size();

    var distances = new long[size][size];
    var durations = new long[size][size];

    for (int i = 0; i < size; i++) {
      for (int j = 0; j < size; j++) {

        var origin = orderedLocations.get(i);
        var destination = orderedLocations.get(j);

        var d = LocationDistances.pythagoreanDistance(origin, destination);

        var distance = (long) (d * distanceFactor);
        var duration = (long) (d * durationFactor);

        distances[i][j] = distance;
        durations[i][j] = duration;

      }
    }
    return DirectionMatrices.matrix(new TreeSet<>(orderedLocations), distances, durations);
  }

}
