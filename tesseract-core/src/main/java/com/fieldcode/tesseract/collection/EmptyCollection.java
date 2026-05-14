package com.fieldcode.tesseract.collection;

import java.time.Duration;
import java.util.Optional;

import com.fieldcode.tesseract.DirectedInterval;
import com.fieldcode.tesseract.FluentIterator;
import com.fieldcode.tesseract.Interval;
import com.fieldcode.tesseract.IntervalCollection;
import com.fieldcode.tesseract.Moment;
import com.fieldcode.tesseract.QueryDirection;
import com.fieldcode.tesseract.Signal;
import com.fieldcode.tesseract.moment.Moments;
import com.fieldcode.tesseract.signal.Signals;
import com.fieldcode.tesseract.utils.iterator.Iterators;

/**
 * An empty collection. This collection is immutable, singleton and thread-safe.
 */
class EmptyCollection extends AbstractIntervalCollection {

  private static final Signal<Moment, Boolean> BASE = Signals.signal(Moments.ninf(), false);

  private static final IntervalCollection INSTANCE = new EmptyCollection();

  private EmptyCollection() {}

  /**
   * Returns the singleton instance of the empty collection.
   *
   * @return the singleton instance of the empty collection
   */
  static IntervalCollection of() {
    return INSTANCE;
  }

  /**
   * Returns an empty iterator.
   *
   * @return an empty iterator
   */
  @Override
  public FluentIterator<Interval> intervals() {
    return Iterators.empty();
  }

  /**
   * Returns an empty iterator.
   *
   * @return an empty iterator
   */
  @Override
  public FluentIterator<Interval> intervals(Interval window, QueryDirection direction) {
    return Iterators.empty();
  }

  /**
   * Returns an empty iterator.
   *
   * @return an empty iterator
   */
  @Override
  public FluentIterator<Interval> intervals(DirectedInterval directed) {
    return Iterators.empty();
  }

  /**
   * Returns and iterator with two signals, one at the lower bound and one at the upper bound, values are false. The direction determines the order of the signals.
   *
   * @param window the window to query
   * @param direction the direction to query
   * @return an iterator with two signals, one at the lower bound and one at the upper bound, values are false
   */
  @Override
  public FluentIterator<Signal<Moment, Boolean>> bounds(Interval window, QueryDirection direction) {
    return direction.get(
        () -> Iterators.iterator(BASE.withKey(window.getLower()), BASE.withKey(window.getUpper())),
        () -> Iterators.iterator(BASE.withKey(window.getUpper()), BASE.withKey(window.getLower()))
    );
  }

  /**
   * Returns an empty optional.
   *
   * @param window the window to query
   * @param direction the direction to query
   * @param duration the duration to query
   * @return an empty optional
   */
  @Override
  public Optional<Interval> find(Interval window, QueryDirection direction, Duration duration) {
    return Optional.empty();
  }

  @Override
  public boolean isNever() {
    return true;
  }

  @Override
  public boolean isAlways() {
    return false;
  }

  @Override
  public Optional<Interval> span() {
    return Optional.empty();
  }

}
