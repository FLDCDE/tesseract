package com.fieldcode.tesseract.collection;

import com.fieldcode.tesseract.Constants;
import com.fieldcode.tesseract.Interval;
import com.fieldcode.tesseract.IntervalCollection;
import com.fieldcode.tesseract.interval.IntervalSets;
import com.fieldcode.tesseract.interval.Intervals;

import java.util.Collection;
import java.util.Optional;
import static com.fieldcode.tesseract.QueryDirection.FORWARD;
import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.stream.Collectors.joining;

/**
 * Utility class for creating {@link IntervalCollection}s from other collections using logical operations or often used combinations.
 */
public class IntervalCollections {

  private IntervalCollections() {
  }

  private static IntervalCollection[] asArray(Collection<IntervalCollection> collections) {
    var array = new IntervalCollection[collections.size()];
    int i = 0;
    for (var collection : collections) {
      array[i++] = collection;
    }

    return array;
  }

  /**
   * Creates a new collection that is the logical AND of the given collections.
   * <p>
   * <strong>Note:</strong> If no collections are provided, returns an empty collection.
   *
   * @param collections collections to AND
   * @return new collection
   */
  public static IntervalCollection and(IntervalCollection... collections) {
    if (collections.length == 0) {
      return empty();
    }
    return AndIntervalCollection.of(collections);
  }

  /**
   * Creates a new collection that is the logical AND of the given collections.
   * <p>
   * <strong>Note:</strong> If no collections are provided (the input collection is empty), returns an empty collection.
   *
   * @param collections collections to AND
   * @return new collection
   */
  public static IntervalCollection and(Collection<IntervalCollection> collections) {
    checkNotNull(collections, "collections cannot be null");
    if (collections.isEmpty()) {
      return empty();
    }
    return AndIntervalCollection.of(asArray(collections));
  }

  /**
   * Creates a new collection that is the logical OR of the given collections.
   * <p>
   * <strong>Note:</strong> If no collections are provided, returns an empty collection.
   *
   * @param collections collections to OR
   * @return new collection
   */
  public static IntervalCollection or(IntervalCollection... collections) {
    if (collections.length == 0) {
      return empty();
    }
    return OrIntervalCollection.of(collections);
  }

  /**
   * Creates a new collection that is the logical OR of the given collections.
   * <p>
   * <strong>Note:</strong> If no collections are provided (the input collection is empty), returns an empty collection.
   *
   * @param collections collections to OR
   * @return new collection
   */
  public static IntervalCollection or(Collection<IntervalCollection> collections) {
    checkNotNull(collections, "collections cannot be null");
    if (collections.isEmpty()) {
      return empty();
    }
    return OrIntervalCollection.of(asArray(collections));
  }

  /**
   * Creates a new collection that is the logical NOT of the given collections.
   * <p>
   * <strong>Note:</strong> If no collections are provided, returns a collection covering the entire timeline (always).
   *
   * @param collections collections to NOT
   * @return new collection
   */
  public static IntervalCollection not(IntervalCollection... collections) {
    if (collections.length == 0) {
      return always();
    }
    return NotIntervalCollection.of(collections);
  }

  /**
   * Creates a new collection that is the logical NOT of the given collections.
   * <p>
   * <strong>Note:</strong> If no collections are provided (the input collection is empty), returns a collection covering the entire timeline (always).
   *
   * @param collections collections to NOT
   * @return new collection
   */
  public static IntervalCollection not(Collection<IntervalCollection> collections) {
    checkNotNull(collections, "collections cannot be null");
    if (collections.isEmpty()) {
      return always();
    }
    return NotIntervalCollection.of(asArray(collections));
  }

  /**
   * Creates a new collection that contains only those moments where exactly one input collection is active.
   * <p>
   * <strong>Note:</strong> If no collections are provided, returns an empty collection.
   *
   * @param collections collections to combine
   * @return new collection
   */
  public static IntervalCollection exclusive(IntervalCollection... collections) {
    if (collections.length == 0) {
      return empty();
    }
    return ExclusiveIntervalCollection.of(collections);
  }

  /**
   * Creates a new collection that contains only those moments where exactly one input collection is active.
   * <p>
   * <strong>Note:</strong> If no collections are provided (the input collection is empty), returns an empty collection.
   *
   * @param collections collections to combine
   * @return new collection
   */
  public static IntervalCollection exclusive(Collection<IntervalCollection> collections) {
    checkNotNull(collections, "collections cannot be null");
    if (collections.isEmpty()) {
      return empty();
    }
    return ExclusiveIntervalCollection.of(asArray(collections));
  }

  /**
   * Excludes the given collection (exclude) from the input collection.
   * <p>
   * This is equivalent to {@code and(input, not(exclude))}.
   *
   * @param input input collection
   * @param exclude collection to exclude
   */
  public static IntervalCollection exclude(IntervalCollection input, IntervalCollection exclude) {
    checkNotNull(input, "input cannot be null");
    checkNotNull(exclude, "exclude cannot be null");
    return and(input, not(exclude));
  }

  /**
   * Creates a collection that covers the whole timeline.
   * <p>
   * <strong>Note:</strong> Currently, this is implemented as a disjoint interval set containing a single interval that covers the whole timeline. However, it may be optimized in
   * the future by creating a specific implementation for always interval collection, like EmptyCollection.
   *
   * @return collection that covers the whole timeline
   */
  public static IntervalCollection always() {
    // TODO: 2023. 11. 08. Create specific implementation for always interval collection, like EmptyCollection
    return IntervalSets.disjoint(Intervals.always());
  }

  /**
   * Creates an empty collection. This is a singleton.
   *
   * @return empty collection
   */
  public static IntervalCollection empty() {
    return EmptyCollection.of();
  }

  /**
   * Returns the span of the given collection.
   * <p>
   * <strong>Note:</strong> If the collection is empty, returns {@link Optional#empty()}.
   *
   * @param collection collection to get the span of
   * @return span of the collection
   */
  public static Optional<Interval> span(IntervalCollection collection) {
    checkNotNull(collection, "collection cannot be null");
    var intervals = collection.intervals().list();
    if (intervals.isEmpty()) {
      return Optional.empty();
    }

    var first = intervals.get(0);
    var last = intervals.get(intervals.size() - 1);

    return Optional.of(Intervals.interval(first.getLower(), last.getUpper()));
  }

  /**
   * Returns whether the given collection encloses the given interval.
   * <p>
   * <strong>Note:</strong> If the collection is empty, always returns {@code false}.
   *
   * @param collection collection to check
   * @param interval interval to check
   * @return whether the collection encloses the interval
   */
  public static boolean encloses(IntervalCollection collection, Interval interval) {
    checkNotNull(collection, "collection cannot be null");
    checkNotNull(interval, "interval cannot be null");
    return collection.intervals(interval, FORWARD)
        .findFirst()
        .map(i -> i.equals(interval))
        .orElse(false);
  }

  /**
   * Returns true if the given collection and the given object are equal. Two collections are equal if they contain the same intervals.
   *
   * @param a collection to compare
   * @param b object to compare
   * @return true if the given collection and the given object are equal
   */
  public static boolean equals(IntervalCollection a, Object b) {
    if (b == null) {return false;}
    if (b == a) {return true;}
    if (b instanceof IntervalCollection) {
      var intervalsA = a.intervals().list();
      var intervalsB = ((IntervalCollection) b).intervals().list();
      return intervalsA.equals(intervalsB);
    }
    return false;
  }

  /**
   * Returns the string representation of the given collection.
   *
   * @param collection collection to get the string representation of
   * @return string representation of the collection
   */
  public static String toString(IntervalCollection collection) {
    var intervals = collection.intervals()
        .map(Object::toString)
        .stream()
        .collect(joining(",", "[", "]"));

    return "[]".equals(intervals)
        ? Constants.INTERVAL_EMPTY_SIGN
        : intervals;
  }

  /**
   * Returns the hash code of the given collection.
   *
   * @param collection collection to get the hash code of
   * @return hash code of the collection
   */
  public static int hashCode(IntervalCollection collection) {
    return collection.intervals()
        .stream()
        .mapToInt(Interval::hashCode)
        .sum();
  }

}
