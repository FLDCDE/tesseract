package com.fieldcode.tesseract.interval;

import com.fieldcode.tesseract.Interval;
import com.fieldcode.tesseract.IntervalCollection;
import com.fieldcode.tesseract.IntervalSet;
import com.fieldcode.tesseract.utils.iterator.Iterators;

import java.time.OffsetDateTime;
import java.util.Collection;
import java.util.Iterator;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * <p>Factory for creating {@link IntervalSet} instances. Note: The returned sets are modifiable.</p>
 *
 * Future versions will provide joined (can contain non-overlapping but connected intervals). Also plan to provide immutable sets for all types.
 */
public class IntervalSets {

  private IntervalSets() {}

  /**
   * Creates a modifiable empty disjoint interval set.
   *
   * @return new disjoint interval set
   */
  public static IntervalSet disjoint() {
    return DisjointIntervalSet.of();
  }

  /**
   * Creates a modifiable disjoint interval set with the given intervals.
   *
   * @param intervals intervals to include
   * @return new disjoint interval set
   */
  public static IntervalSet disjoint(Interval... intervals) {
    var set = DisjointIntervalSet.of();
    for (Interval interval : intervals) {
      set.include(interval);
    }
    return set;
  }

  /**
   * Creates a modifiable disjoint interval set with the given intervals.
   *
   * @param intervals intervals to include
   * @return new disjoint interval set
   */
  public static IntervalSet disjoint(Collection<? extends Interval> intervals) {
    var set = DisjointIntervalSet.of();
    for (Interval interval : intervals) {
      set.include(interval);
    }
    return set;
  }

  /**
   * Creates a modifiable disjoint interval set with the given intervals.
   *
   * @param intervals intervals to include
   * @return new disjoint interval set
   */
  public static IntervalSet disjoint(Stream<? extends Interval> intervals) {
    var set = DisjointIntervalSet.of();
    intervals.forEach(set::include);
    return set;
  }

  /**
   * Creates a modifiable disjoint interval set with the given intervals.
   *
   * @param intervals intervals to include
   * @return new disjoint interval set
   */
  public static IntervalSet disjoint(Iterator<? extends Interval> intervals) {
    var set = DisjointIntervalSet.of();
    Iterators.fluent(intervals).forEach(set::include);
    return set;
  }

  /**
   * Creates a modifiable disjoint interval set from the given input collection using the given mapper function. The returned set will be a detached copy of the input collection.
   * Any modifications on the input collection will not be reflected in the returned set after creation.
   *
   * @param collection input collection
   * @return new disjoint interval set
   */
  public static IntervalSet disjoint(IntervalCollection collection) {
    var set = DisjointIntervalSet.of();
    collection.intervals().forEach(set::include);
    return set;
  }

  /**
   * Creates a modifiable disjoint interval set from the given input collection using the given mapper function.
   *
   * @param input input collection
   * @param lowerMapper mapper function for lower bound
   * @param upperMapper mapper function for upper bound
   * @return new disjoint interval set
   */
  public static <T> IntervalSet disjoint(Collection<T> input, Function<T, OffsetDateTime> lowerMapper, Function<T, OffsetDateTime> upperMapper) {
    var set = DisjointIntervalSet.of();
    for (T t : input) {
      var lower = lowerMapper.apply(t);
      var upper = upperMapper.apply(t);
      set.include(Intervals.interval(lower, upper));
    }
    return set;
  }

}
