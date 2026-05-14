package com.fieldcode.tesseract.collection;

import java.util.Iterator;

import com.fieldcode.tesseract.DirectedInterval;
import com.fieldcode.tesseract.FluentIterator;
import com.fieldcode.tesseract.Moment;
import com.fieldcode.tesseract.Signal;
import com.fieldcode.tesseract.utils.iterator.Iterators;

/**
 * This class provides factory methods for creating boundary limiter iterators for forward and backward directions.
 *
 * <h3>Boundary</h3>
 *
 * <p>
 * Boundary is a signal with Moment key and Boolean value. The boundary is a signal that represents the boundary of an interval.
 * </p>
 *
 * <h3>Goal</h3>
 *
 * <p>
 * The main goal of the BoundaryLimiterIterator is to provide a generic way boundary iterators for fast and efficient temporal logic operations {@link IntervalCollections}.
 * </p>
 *
 * <h3>Limiting</h3>
 *
 * <p>
 * Boundary limiting is a process of limiting the signals to a specific interval. The limiting process is done by providing a directed interval (describes a window and direction)
 * and an iterator of input signals. The limiting process is done by creating a new iterator that only emits signals from the input iterator that are within the interval provided
 * by the directed interval. The resulting iterator is always contains the window boundaries even.
 * </p>
 *
 * <h3>Examples</h3>
 *
 * <pre>
 *  Forward direction:
 *
 *  Given signals s = [{0,true}, {5,false}, {10,true}, {15,false}] (this defines two intervals: [0..5] and [10..15])
 *
 *  limit(s, [0..15], FORWARD)  => [{0,true}, {5,false}, {10,true}, {15,false}]
 *  limit(s, [3..12], FORWARD)  => [{3,true}, {5,false}, {10,true}, {12,false}]
 *  limit(s, [-5..20], FORWARD) => [{-5, false}, {0,true}, {5,false}, {10,true}, {15,false}, {20, false}]
 *  limit(s, [30..40], FORWARD) => [{30, false}, {40, false}]
 *
 *  Backward direction:
 *
 *  Given signals s = [{15,true}, {10,false}, {5,true}, {0,false}] (this defines two intervals: [10..15], [0..5])
 *
 *  limit(s, [0..15], BACKWARD)  => [{15,true}, {10,false}, {5,true}, {0,false}]
 *  limit(s, [3..12], BACKWARD)  => [{12,true}, {10,false}, {5,true}, {3,false}]
 *  limit(s, [-5..20], BACKWARD) => [{20, false}, {15,true}, {10,false}, {5,true}, {0,false}, {-5, false}]
 *  limit(s, [30..40], BACKWARD) => [{40, false}, {30, false}]
 * </pre>
 *
 * <p>
 * <b>REMINDER: </b>The implementation assumes that the signals are ordered in a backward direction by keys. Wrong order can lead to unexpected behavior.
 * </p>
 */
public class BoundaryLimiterIterator {

  private BoundaryLimiterIterator() {}

  /**
   * Creates a fluent iterator that limits the boundaries. Equivalent to <pre>{@code Iterators.fluent(BoundaryLimiterIterator.of(delegate, directed))}</pre>
   *
   * @param delegate the input iterator
   * @param directed the directed interval
   * @return a fluent iterator that limits the boundaries
   */
  public static FluentIterator<Signal<Moment, Boolean>> fluent(Iterator<Signal<Moment, Boolean>> delegate, DirectedInterval directed) {
    return Iterators.fluent(of(delegate, directed));
  }

  /**
   * Creates an iterator that limits the boundaries.
   *
   * @param delegate the input iterator
   * @param directed the directed interval
   * @return an iterator that limits the boundaries
   */
  public static Iterator<Signal<Moment, Boolean>> of(Iterator<Signal<Moment, Boolean>> delegate, DirectedInterval directed) {
    var interval = directed.getInterval();
    return directed.getDirection().get(
        () -> BoundaryLimiterForwardIterator.of(delegate, interval),
        () -> BoundaryLimiterBackwardIterator.of(delegate, interval)
    );
  }

}
