package com.fieldcode.tesseract.matrix;

import com.fieldcode.tesseract.DirectionMatrix;
import com.fieldcode.tesseract.DirectionMatrixDimensions;
import com.fieldcode.tesseract.Distance;
import com.fieldcode.tesseract.Location;
import com.fieldcode.tesseract.PartialDirectionMatrix;
import com.fieldcode.tesseract.distance.Distances;
import com.fieldcode.tesseract.location.LocationDistances;
import com.google.common.collect.ImmutableSortedSet;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import java.time.Duration;
import java.util.Collection;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.function.BiFunction;
import java.util.stream.Stream;
import static com.google.common.base.Preconditions.checkArgument;

public class DirectionMatrices {

  private DirectionMatrices() {
  }

  public static DirectionMatrix matrix(SortedSet<Location> locations, long[][] distancedInMeter, long[][] durationsInSeconds) {
    return ImmutableDirectionMatrix.builder()
        .locations(locations)
        .distancesInMeter(distancedInMeter)
        .durationsInSeconds(durationsInSeconds)
        .build();
  }

  public static PartialDirectionMatrix partial(SortedSet<Location> origins, SortedSet<Location> destinations, long[][] distancesInMeter, long[][] durationsInSeconds) {

    if (origins.equals(destinations)) {
      return matrix(origins, distancesInMeter, durationsInSeconds);
    }

    return ImmutablePartialDirectionMatrix.builder()
        .origins(origins)
        .destinations(destinations)
        .distancesInMeter(distancesInMeter)
        .durationsInSeconds(durationsInSeconds)
        .build();
  }

  public static DirectionMatrix toFullMatrix(PartialDirectionMatrix matrix) {

    if (matrix instanceof DirectionMatrix) {
      return (DirectionMatrix) matrix;
    }

    var origins = matrix.getOrigins();
    var destinations = matrix.getDestinations();
    checkArgument(
        origins.equals(destinations),
        "Partial matrix origins and destinations must be equal to convert direction matrix. [origins=%s, destinations=%s]", origins, destinations
    );

    return matrix(origins, matrix.getDistancesInMeter(), matrix.getDurationsInSeconds());
  }

  public static DirectionMatrix spherical(Collection<Location> locations, Duration durationUnit) {
    var durationFactor = durationUnit.toSeconds();
    return toFullMatrix(calculate(locations, locations, DirectionMatrices::getSphericalDistance, 1, durationFactor));
  }


  public static DirectionMatrix pythagorean(Collection<Location> locations) {
    return pythagorean(locations, Distances.ONE_KILOMETER_INSTANCE, Duration.ofHours(1));
  }

  public static PartialDirectionMatrix pythagorean(DirectionMatrixDimensions request) {
    return pythagorean(request.getOrigins(), request.getDestinations(), Distances.ONE_KILOMETER_INSTANCE, Duration.ofHours(1));
  }

  public static PartialDirectionMatrix pythagorean(Collection<Location> origins, Collection<Location> destinations) {
    return pythagorean(origins, destinations, Distances.ONE_KILOMETER_INSTANCE, Duration.ofHours(1));
  }

  public static DirectionMatrix pythagorean(Collection<Location> locations, Distance distanceUnit, Duration durationUnit) {
    var distanceFactor = distanceUnit.toMeters();
    var durationFactor = durationUnit.toSeconds();
    return toFullMatrix(calculate(locations, locations, DirectionMatrices::getPythagoreanDistance, distanceFactor, durationFactor));
  }

  public static PartialDirectionMatrix pythagorean(Collection<Location> origins, Collection<Location> destinations, Distance distanceUnit, Duration durationUnit) {
    var distanceFactor = distanceUnit.toMeters();
    var durationFactor = durationUnit.toSeconds();
    return calculate(origins, destinations, DirectionMatrices::getPythagoreanDistance, distanceFactor, durationFactor);
  }

  private static double getPythagoreanDistance(Location origin, Location destination) {
    return LocationDistances.pythagoreanDistance(origin, destination);
  }

  private static double getSphericalDistance(Location origin, Location destination) {
    return LocationDistances.sphericalDistance(origin, destination).toMeters();
  }

  private static PartialDirectionMatrix calculate(Collection<Location> origins, Collection<Location> destinations, BiFunction<Location, Location, Double> resolver, long distanceFactor, long durationFactor) {

    var orderedOrigins = new TreeSet<>(origins);
    var orderedDestinations = new TreeSet<>(destinations);

    var originsArray = orderedOrigins.toArray(Location[]::new);
    var destinationArray = orderedDestinations.toArray(Location[]::new);

    var n = orderedOrigins.size();
    var m = orderedDestinations.size();

    var distances = new long[n][m];
    var durations = new long[n][m];

    for (int i = 0; i < n; i++) {
      for (int j = 0; j < m; j++) {

        var origin = originsArray[i];
        var destination = destinationArray[j];

        var d = resolver.apply(origin, destination);

        var distance = (long) (d * distanceFactor);
        var duration = (long) (d * durationFactor);

        distances[i][j] = distance;
        durations[i][j] = duration;
      }
    }

    return partial(orderedOrigins, orderedDestinations, distances, durations);

  }

  public static DirectionMatrixDimensions dimensions(SortedSet<Location> origins, SortedSet<Location> destinations) {
    return ImmutableDirectionMatrixDimensions.of(origins, destinations);
  }

  public static Stream<DirectionMatrixDimensions> split(DirectionMatrixDimensions locations, int max) {
    return split(locations, max, max);
  }

  public static Stream<DirectionMatrixDimensions> split(DirectionMatrixDimensions locations, int maxOrigins, int maxDestinations) {

    var o = Lists.newArrayList(locations.getOrigins());
    var d = Lists.newArrayList(locations.getDestinations());

    var op = Lists.partition(o, maxOrigins);
    var od = Lists.partition(d, maxDestinations);

    return Lists.cartesianProduct(op, od)
        .stream()
        .map(
            c -> dimensions(
                Sets.newTreeSet(c.get(0)),
                Sets.newTreeSet(c.get(1))
            )
        );
  }

  public static PartialDirectionMatrix self(Location location) {
    return matrix(
        ImmutableSortedSet.of(location),
        new long[1][1],
        new long[1][1]
    );
  }

  public static DirectionMatrix empty() {
    return ImmutableEmptyDirectionMatrix.of();
  }

}
