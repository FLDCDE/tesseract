package com.fieldcode.tesseract.location;

import com.fieldcode.tesseract.Distance;
import com.fieldcode.tesseract.Location;
import com.fieldcode.tesseract.utils.QuickUnion;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.SortedSet;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.IntStream;
import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.stream.Collectors.toMap;

/**
 * The `LocationCluster` class is responsible for clustering locations based on their geographical proximity. It uses a union-find data structure to group locations that are
 * similar and within a specified distance.
 *
 * @param <T> the type of elements to be clustered
 */
public class LocationCluster<T> {

  public final Collector<Entry<T, Integer>, ?, HashMultimap<Integer, T>> toMultimap = Multimaps.toMultimap(
      Entry::getValue,
      Entry::getKey,
      HashMultimap::create
  );

  private final int n;
  private final List<T> input;
  private final BiPredicate<T, T> similar;
  private final BiPredicate<T, T> neighbor;

  private LocationCluster(Collection<T> input, BiPredicate<T, T> similar, BiPredicate<T, T> neighbor) {

    checkNotNull(input, "The input collection must not be null.");
    checkNotNull(similar, "The similarity predicate must not be null.");
    checkNotNull(neighbor, "The neighbor predicate must not be null.");

    this.n = input.size();
    this.input = List.copyOf(input);
    this.similar = similar;
    this.neighbor = neighbor;
  }

  /**
   * Creates a new `LocationCluster` for clustering locations based on spherical distance.
   *
   * @param <T> the type of elements to be clustered
   * @param input the collection of elements to be clustered
   * @param locationMapper a function that maps an element to its geographical location
   * @param similarity a predicate that tests if two elements are similar
   * @param radius the maximum distance within which elements are considered neighbors
   * @return a `Multimap` where the keys are cluster IDs and the values are the clustered elements
   */
  public static <T> Multimap<Integer, T> spherical(Collection<T> input, Function<T, Location> locationMapper, BiPredicate<T, T> similarity, Distance radius) {
    return new LocationCluster<>(
        input,
        similarity,
        (a, b) -> LocationDistances.sphericalDistance(locationMapper.apply(a), locationMapper.apply(b)).le(radius)
    ).cluster();
  }

  /**
   * Creates a new `LocationCluster` for clustering locations based on spherical distance.
   *
   * @param input the sorted set of locations to be clustered
   * @param radius the maximum distance within which locations are considered neighbors
   * @return a `Multimap` where the keys are cluster IDs and the values are the clustered locations
   */
  public static Multimap<Integer, Location> spherical(SortedSet<Location> input, Distance radius) {
    return new LocationCluster<>(
        input,
        (a, b) -> true,
        (a, b) -> LocationDistances.sphericalDistance(a, b).le(radius)
    ).cluster();
  }

  /**
   * Clusters the input elements based on their similarity and proximity.
   *
   * This method performs the following steps:
   * <ol>
   *   <li>Initializes a {@code QuickUnion} instance to manage the union-find operations.</li>
   *   <li>Iterates over each pair of elements in the input list.</li>
   *   <li>Performs a union operation if the elements are similar and neighbors.</li>
   *   <li>Creates a map from elements to their cluster IDs.</li>
   *   <li>Renumbers the clusters to ensure contiguous cluster IDs.</li>
   * </ol>
   *
   * @return a {@code Multimap} where the keys are cluster IDs and the values are the clustered elements
   */
  private Multimap<Integer, T> cluster() {
    var qu = QuickUnion.of(n);

    for (int i = 0; i < n; i++) {
      var a = input.get(i);

      for (int j = i; j < n; j++) {
        var b = input.get(j);

        if (similar.test(a, b) && neighbor.test(a, b)) {
          qu.union(i, j);
        }

      }
    }

    // This map contains a T -> cluster ID mapping. Cluster IDs are not necessarily contiguous.
    var objectToClusterId = IntStream.range(0, n)
        .boxed()
        .collect(toMap(input::get, qu::find));

    return renumber(invert(objectToClusterId));
  }

  /**
   * Inverts a map of elements to cluster IDs into a multimap of cluster IDs to elements.
   *
   * <p>This method transforms a map where keys are elements and values are their assigned
   * cluster IDs into a multimap where keys are cluster IDs and values are collections of elements belonging to each cluster.
   *
   * @param objectToClusterId a map containing element to cluster ID mappings
   * @return a multimap where keys are cluster IDs and values are the elements in each cluster
   */
  private Multimap<Integer, T> invert(Map<T, Integer> objectToClusterId) {
    return objectToClusterId
        .entrySet()
        .stream()
        .collect(toMultimap);
  }

  /**
   * Renumbers the cluster IDs to ensure they are contiguous and start from zero.
   *
   * <p>This method takes a multimap with arbitrary integer keys (cluster IDs) and transforms it
   * into a new multimap where the keys are renumbered sequentially starting from 0. This ensures that cluster IDs in the returned multimap are contiguous integers.
   *
   * <p>The resulting multimap also has its keys ordered in ascending order.
   *
   * @param input a multimap with arbitrary cluster IDs as keys and clustered elements as values
   * @return a new multimap with contiguous cluster IDs starting from 0
   */
  private Multimap<Integer, T> renumber(Multimap<Integer, T> input) {
    var counter = new AtomicInteger(0);

    var builder = ImmutableMultimap
        .<Integer, T>builder()
        .orderKeysBy(Integer::compare);

    input.asMap()
        .forEach((key, value) -> builder.putAll(counter.getAndIncrement(), value));

    return builder.build();
  }

}
