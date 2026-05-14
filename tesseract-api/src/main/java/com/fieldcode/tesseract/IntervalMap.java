package com.fieldcode.tesseract;

import java.util.function.BiPredicate;

public interface IntervalMap<T> {

  /**
   * Put a value into the map for the given interval. If the map contains intersected intervals with the same value, the intervals will be merged.
   *
   * @param interval The interval to put the value into
   * @param value The value to put into the interval
   * @return this map
   */
  IntervalMap<T> put(Interval interval, T value);

  /**
   * Returns the interval entry for the given moment. The returned entry is the entry with the interval that contains the given moment.
   *
   * @param moment The moment to get the interval entry for
   * @return the interval entry for the given moment
   */
  IntervalEntry<T> get(Moment moment);

  /**
   * Sets the default value for the map in the given interval.
   *
   * @param interval The interval to set the default value for
   * @return this map
   */
  IntervalMap<T> remove(Interval interval);

  /**
   * Returns the default value for this map.
   *
   * @return the default value for this map
   */
  T getDefaultValue();

  /**
   * Returns an iterator over the entries of this map. The entries are returned in the order of their intervals.
   *
   * @return an iterator over the entries of this map
   */
  FluentIterator<IntervalEntry<T>> entries();

  /**
   * Returns an iterator over the entries of this map in the given window and direction. The entries are returned in the order of their intervals. Intervals are cropped to the
   * window.
   *
   * @param window The window to iterate over
   * @param direction The direction to iterate in
   * @return an iterator over the entries of this map in the given window and direction
   */
  FluentIterator<IntervalEntry<T>> entries(Interval window, QueryDirection direction);

  /**
   * Returns an IntervalCollection view of this map with all intervals that have a value different from the default value.
   * @return an IntervalCollection
   */
  IntervalCollection intervals();

  /**
   * Returns an IntervalCollection view of this map with all intervals that satisfy the given predicate.
   * @param intervalPredicate The predicate to satisfy
   * @return an IntervalCollection
   */
  IntervalCollection intervals(BiPredicate<Interval, T> intervalPredicate);

  interface IntervalEntry<T> {

    Interval getInterval();

    T getValue();

    IntervalEntry<T> withValue(T value);

    IntervalEntry<T> withInterval(Interval interval);

  }

}
