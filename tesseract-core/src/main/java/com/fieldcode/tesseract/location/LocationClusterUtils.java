package com.fieldcode.tesseract.location;

import com.fieldcode.tesseract.Location;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Multimap;

import java.util.Map;
import java.util.Map.Entry;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.stream.Collectors;
import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.stream.Collectors.toList;

/**
 * Utility class providing methods for clustering {@link Location} objects based on different distance criteria.
 * <p>
 * This class supports clustering locations that are in close proximity to each other, which can be useful for route optimization, group processing, or visualization of location
 * data.
 */
public class LocationClusterUtils {

  private LocationClusterUtils() {
    // Private constructor to prevent instantiation
  }

  /**
   * Inverts the clusters map. The keys become the values and the values become the keys.
   * <p>
   * The clusters map must not contain duplicate locations. If duplicates are found, an {@link IllegalArgumentException} is thrown.
   *
   * @param clusters the clusters map
   * @return the inverted clusters map
   * @throws IllegalArgumentException if the clusters map contains duplicate locations
   */
  public static Map<Location, Integer> invert(Multimap<Integer, Location> clusters) {
    validate(clusters);
    return clusters
        .entries()
        .stream()
        .collect(ImmutableMap.toImmutableMap(Entry::getValue, Entry::getKey));
  }

  /**
   * Validates the clusters map by checking for duplicate locations.
   *
   * @param clusters the clusters map to validate
   * @throws NullPointerException if the clusters map is null
   * @throws IllegalArgumentException if the clusters map contains duplicate locations
   */
  public static void validate(Multimap<Integer, Location> clusters) {
    checkNotNull(clusters, "The clusters must not be null.");

    var duplicates = clusters
        .entries()
        .stream()
        .collect(Collectors.groupingBy(Entry::getValue, Collectors.counting()))
        .entrySet().stream()
        .filter(entry -> entry.getValue() > 1)
        .map(Map.Entry::getKey)
        .collect(toList());

    checkArgument(duplicates.isEmpty(), "Duplicate locations found: " + duplicates);

  }

  /**
   * Creates a predicate that checks if two locations are neighbors based on the provided clusters multimap.
   * <p>
   * The predicate returns true if the locations belong to the same cluster.
   * <p>
   * If the clusters map is empty, the predicate always returns false.
   * <p>
   * The clusters map must not contain duplicate locations. If duplicates are found, an {@link IllegalArgumentException} is thrown.
   *
   * @param clusters the clusters map
   * @return a predicate that checks if two locations are neighbors
   * @throws IllegalArgumentException if the clusters map contains duplicate locations
   */
  public static BiPredicate<Location, Location> neighbors(Multimap<Integer, Location> clusters) {

    if (clusters.isEmpty()) {
      return (a, b) -> false;
    }

    var inverted = invert(clusters);
    return neighbors(inverted);
  }

  /**
   * Creates a predicate that checks if two locations are neighbors based on the provided clusters map.
   * <p>
   * The predicate returns true if the locations belong to the same cluster.
   * <p>
   * If the clusters map is empty, the predicate always returns false.
   * @param clusters the clusters map
   * @return a predicate that checks if two locations are neighbors
   */
  public static BiPredicate<Location, Location> neighbors(Map<Location, Integer> clusters) {

    if (clusters.isEmpty()) {
      return (a, b) -> false;
    }

    return (a, b) -> {
      if (clusters.containsKey(a) && clusters.containsKey(b)) {
        var o = clusters.get(a);
        var d = clusters.get(b);
        return o.equals(d);
      }
      return false;
    };
  }

  /**
   * Creates a predicate that checks if two elements are neighbors based on the provided clusters multimap.
   * <p>
   * The predicate returns true if:
   * <ul>
   *   <li>Both elements map to valid locations in the clusters</li>
   *   <li>The mapped locations belong to the same cluster</li>
   *   <li>The similarity predicate also returns true for the elements</li>
   * </ul>
   * <p>
   * The clusters map must not contain duplicate locations. If duplicates are found, an
   * {@link IllegalArgumentException} is thrown.
   *
   * @param clusters the clusters map
   * @param mapper the function that maps elements to locations (must not return null)
   * @param similarity the similarity predicate for additional comparison
   * @param <T> the type of elements to be compared
   * @return a predicate that checks if two elements are neighbors
   * @throws IllegalArgumentException if the clusters map contains duplicate locations
   */
  public static <T> BiPredicate<T, T> neighbors(Multimap<Integer, Location> clusters, Function<T, Location> mapper, BiPredicate<T, T> similarity) {
    var inverted = invert(clusters);
    return (a, b) -> {
      var locationA = mapper.apply(a);
      var locationB = mapper.apply(b);

      if (locationA == null || locationB == null) {return false;}

      if (inverted.containsKey(locationA) && inverted.containsKey(locationB)) {
        var o = inverted.get(locationA);
        var d = inverted.get(locationB);
        return o.equals(d) && similarity.test(a, b);
      }
      return false;
    };

  }


}
