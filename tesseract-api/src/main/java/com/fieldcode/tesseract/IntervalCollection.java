package com.fieldcode.tesseract;

import java.time.Duration;
import java.util.Optional;

public interface IntervalCollection {

  /**
   * Returns all intervals in this collection in forward order.
   *
   * @return an iterator over the intervals in this collection
   */
  FluentIterator<Interval> intervals();

  /**
   * Returns all intervals in this collection in the specified order and within the specified window.
   *
   * @param window the window to query
   * @param direction the direction to query
   * @return an iterator over the intervals in this collection
   */
  FluentIterator<Interval> intervals(Interval window, QueryDirection direction);

  FluentIterator<Interval> intervals(DirectedInterval directed);

  /**
   * Returns all signals in this collection. Direction specifies the order of the signals. In backward direction, the signal direction is also reversed.
   *
   * @param window the window to query
   * @param direction the direction to query
   * @return an iterator over the signals in this collection
   */
  FluentIterator<Signal<Moment, Boolean>> bounds(Interval window, QueryDirection direction);

  /**
   * Searches for the first interval within the provided collection that completely contains the specified duration. The identified interval is aligned either to the beginning or
   * the end of the interval, based on the given search direction. The discovered interval's duration is assured to be at least as long as the specified duration.
   *
   * @param window the window to query
   * @param direction the direction to query
   * @param duration the duration to query
   * @return the first interval in this collection that is at least as long as the specified duration
   */
  Optional<Interval> find(Interval window, QueryDirection direction, Duration duration);

  /**
   * Returns true if this collection contains no intervals.
   *
   * @return true if this collection contains no intervals
   */
  boolean isNever();

  /**
   * Returns true if this collection contains all intervals.
   *
   * @return true if this collection contains all intervals
   */
  boolean isAlways();

  /**
   * Returns the span of this collection.
   *
   * @return the span of this collection
   */
  Optional<Interval> span();

  /**
   * Returns the total durations of the intervals in the given window.
   * If the collection contains unbounded intervals in the given window, illegal state exception is thrown.
   *
   * @return the duration of this collection
   * @throws IllegalStateException if an unbounded interval is encountered
   */
  Duration totalDuration(Interval window);

  /**
   * Creates a snapshot of this collection.
   *
   * @return a snapshot of this collection
   */
  IntervalCollection copy();

}
