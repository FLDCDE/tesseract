package com.fieldcode.tesseract;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.Optional;
import java.util.function.BiFunction;

/**
 * An interval is a representation of a time span. It is defined by a lower and an upper bound.
 * <p>
 * The lower bound is inclusive, the upper bound is exclusive.
 * <p>
 * Bounds are represented by {@link Moment}s. Because of this, an interval can be unbounded or bounded.
 */
public interface Interval extends IntervalCollection {

  /**
   * Returns the lower bound of the interval.
   *
   * @return the lower bound of the interval
   */
  Moment getLower();

  /**
   * Returns the upper bound of the interval.
   *
   * @return the upper bound of the interval
   */
  Moment getUpper();

  /**
   * Returns the lower bound of the interval as an OffsetDateTime. If the interval is unbounded, an IllegalStateException is thrown.
   *
   * @return the lower bound of the interval as an OffsetDateTime
   */
  OffsetDateTime getStart();

  /**
   * Returns the upper bound of the interval as an OffsetDateTime. If the interval is unbounded, an IllegalStateException is thrown.
   *
   * @return the upper bound of the interval as an OffsetDateTime
   */
  OffsetDateTime getEnd();

  /**
   * Returns the duration of the bounded interval. The duration is the difference between the upper and lower bound. If the interval is unbounded, an empty Optional is returned.
   *
   * @return optional duration of the interval
   */
  Optional<Duration> getDuration();

  /**
   * Returns the lower and upper bound of the interval as an iterator.
   *
   * @return the lower and upper bound of the interval as an iterator
   */
  FluentIterator<Moment> getBounds();

  /**
   * True if the interval is bounded, false if it is unbounded. An interval is bounded if both its lower and upper bound are finite Moments.
   *
   * @return true if the interval is bounded, false if it is unbounded
   */
  boolean isBounded();

  /**
   * True if the interval is unbounded, false if it is bounded. An interval is unbounded if either its lower or upper bound is infinite.
   *
   * @return true if the interval is unbounded, false if it is bounded
   */
  boolean isUnbounded();

  /**
   * True if the interval is empty, false if it is not. This method will be removed in a future version.
   *
   * @return true if the interval is empty, false if it is not
   */
  boolean isEmpty();

  /**
   * True if the interval is always, false if it is not. An interval is always if lower bound is negative infinite and upper bound is positive infinite.
   *
   * @return true if the interval is always, false if it is not
   */
  boolean isAlways();

  /**
   * True if the interval contains the given time. A time is contained in an interval if it is greater than or equal to the lower bound and less than the upper bound.
   *
   * @param at the moment to check
   * @return true if the interval contains the given moment
   */
  boolean contains(OffsetDateTime at);

  /**
   * True if the interval contains the given moment. A moment is contained in an interval if it is greater than or equal to the lower bound and less than the upper bound.
   *
   * @param at the moment to check
   * @return true if the interval contains the given moment
   */
  boolean contains(Moment at);

  /**
   * If the interval contains the given moment, returns the result of the mapper function applied to the interval. Otherwise, returns the interval itself.
   *
   * @param at the moment to check
   * @param mapper the mapper function
   * @return the result of the mapper function applied to the interval if the interval contains the given moment, otherwise the interval itself
   */
  Interval ifContains(Moment at, BiFunction<Interval, Moment, Interval> mapper);

  /**
   * Maps the interval to a new interval relative to the given moment. The provided functions are called depending on the relation between the interval and the given moment.
   *
   * @param at the moment to map the interval relative to
   * @param beforeAction the function to call if the interval is before the given moment
   * @param containsAction the function to call if the interval contains the given moment
   * @param afterAction the function to call if the interval is after the given moment
   * @return the result of the function called depending on the relation between the interval and the given moment
   */
  Interval mapRelativeTo(
      OffsetDateTime at,
      BiFunction<Interval, Moment, Interval> beforeAction,
      BiFunction<Interval, Moment, Interval> containsAction,
      BiFunction<Interval, Moment, Interval> afterAction
  );

  /**
   * Maps the interval to a new interval relative to the given moment. The provided functions are called depending on the relation between the interval and the given moment.
   *
   * @param at the moment to map the interval relative to
   * @param beforeAction the function to call if the interval is before the given moment
   * @param containsAction the function to call if the interval contains the given moment
   * @param afterAction the function to call if the interval is after the given moment
   * @return the result of the function called depending on the relation between the interval and the given moment
   */
  Interval mapRelativeTo(
      Moment at,
      BiFunction<Interval, Moment, Interval> beforeAction,
      BiFunction<Interval, Moment, Interval> containsAction,
      BiFunction<Interval, Moment, Interval> afterAction
  );


  /**
   * Returns a relation between this interval and the given interval. see {@link IntervalsRelation}
   *
   * @param interval the interval to compare to
   * @return the relation between this interval and the given interval
   */
  IntervalsRelation getRelationTo(Interval interval);

  /**
   * True if the two intervals have no common sub-intervals.
   *
   * @param other the interval to check
   * @return true if the two intervals have no common sub-intervals
   */
  boolean distinct(Interval other);

  /**
   * True if the two intervals have common sub-intervals.
   *
   * @param other the interval to check
   * @return true if the two intervals have common sub-intervals
   */
  boolean intersects(Interval other);

  /**
   * True if the two intervals have common sub-intervals or are adjacent.
   *
   * @param other the interval to check
   * @return true if the two intervals have no common sub-intervals
   */
  boolean connects(Interval other);

  /**
   * True if the interval contains the given interval. An interval is contained in another interval if its lower bound is greater than or equal to the lower bound of the other
   * interval and its upper bound is less than or equal to the upper bound of the other interval.
   *
   * @param interval the interval to check
   * @return true if the interval contains the given interval
   */
  boolean encloses(Interval interval);

  /**
   * Checks if the interval min duration is greater than or equal to the given duration. In unbounded intervals, this method will always return true.
   *
   * @param duration the duration to check
   * @return true if the interval min duration is greater than or equal to the given duration
   */
  boolean encloses(Duration duration);

  /**
   * <BR/> Returns an Interval of which the starting point is the lowest of the 2 and the upper is the highest.
   * <p>
   * Examples (C -> A span B):
   * <pre>
   * example 1
   *         A|-------|
   *                    B|-------|
   *         C|------------------|
   *
   * example 2
   *         A|-------|
   *              B|-------|
   *         C|------------|
   *
   * </pre>
   *
   * @return an array containing either both the intervals separately (Case 1) or the span of both intervals (Case 2)
   */
  Interval span(Interval other);

  /**
   * <BR/> Returns an iterator that can contain 1 or 2 items
   * <p>
   * Examples (A union B):
   * <pre>
   *  2 Intervals (Case 1: no mutual ground)
   *         A|-------|
   *                    B|-------|
   * 1 Interval (Case 2: mutual ground)
   *
   *        A|-------|
   *              B|-------|
   *
   * </pre>
   *
   * @return an iterator containing either both the intervals separately (Case 1) or the span of both intervals (Case 2)
   */

  FluentIterator<Interval> union(Interval other);

  /**
   * Auxiliary relation. <BR/> Returns an iterator that can contain 0 or 1 items
   * <p>
   * Examples (A B* intersects):
   * <pre>
   *
   *         A|-------|
   *        B1|-------|
   *  B2|-------|
   *              B3|-------|
   *       B4|--|
   *            B5|--|
   *         B6|--|
   *    B7|-------------|
   *
   * </pre>
   *
   * @return an iterator containing the 'intersection' (mutual) area of A and B
   */

  FluentIterator<Interval> intersect(Interval other);

  /**
   * An Interval iterator that can contain 0 to 2 items depending on the finiteness of the bounds
   *
   * <pre>
   *  Finite Bounds. A's complement
   *
   *                  A|---------|
   *                            B|---------|
   *        C|---------|
   *  InFinite Lower Bound. A's complement
   *
   *                 -00 A|---------|
   *                               B|---------|+00
   *  InFinite upper Bound. A's complement
   *
   *                  A|---------| +00
   *    -00 B|---------|
   *
   *
   *  InFinite Bounds. A's complement -> Empty Array
   *
   *                  A -00|---------| +00
   *                          B||
   * </pre>
   *
   * @return an interval iterator that can contain B,C,[B,C] or []
   */
  FluentIterator<Interval> complement();

  /**
   * <pre>
   *
   *                  A|---------|
   *                        B|---------|
   *
   * </pre>
   *
   * @return an interval (B) with both bounds shifted by the specified amount
   */
  Interval shift(Duration shift);

  Interval withLower(OffsetDateTime lower);

  /**
   * <pre>
   *
   *                           A|---------|
   *                   B|-----------------|
   *
   * </pre>
   *
   * @return an interval with B.upper = A.upper and a newly defined lower bound
   * @throws IllegalArgumentException when the lower parameter is greater than A.upper
   */
  Interval withLower(Moment lower);

  /**
   * <pre>
   *
   *                  A|---------|
   *                  B|-----------------|
   *
   * </pre>
   *
   * @return an interval with B.lower = A.lower and a newly defined upper bound
   * @throws IllegalArgumentException when the lower parameter is greater than A.upper
   */

  Interval withUpper(Moment upper);

  Interval withUpper(OffsetDateTime end);

  /**
   * Case 1: Align to lower end
   * <pre>
   *
   *                  A|---------|
   *                  B|----|
   *
   * </pre>
   * <p>
   * return an interval with B.lower = A.lower and B.upper = A.lower shifted by the Duration
   *
   * <p>
   * Case 2: Align to upper end
   * <p>
   * A|---------| B     |----|
   *
   * </pre>
   *
   * @return an interval with B.upper = A.upper and B.lower = A.upper shifted by the Duration negated
   */
  Interval withDuration(Duration duration, IntervalAlignment alignment);

  Optional<Interval> tryWithDuration(Duration duration, IntervalAlignment alignment);

  /**
   * Returns a new instance of this interval with the given zone id.
   *
   * @param zoneId the zone id to apply
   * @return a new instance of this interval with the given zone id
   */
  Interval withZoneIdSameInterval(ZoneId zoneId);

  /**
   * Returns a new instance of this interval with the given zone id.
   *
   * @param zoneId the zone id to apply
   * @return a new instance of this interval with the given zone id
   */
  Interval withZoneIdSameInterval(String zoneId);

  /**
   * Returns a new instance of {see @link DirectedInterval} with the direction {@link QueryDirection#FORWARD}
   *
   * @return a new instance of {see @link DirectedInterval}
   */
  DirectedInterval forward();

  /**
   * Returns a new instance of {see @link DirectedInterval} with the direction {@link QueryDirection#BACKWARD}
   *
   * @return a new instance of {see @link DirectedInterval}
   */
  DirectedInterval backward();

  /**
   * Returns a new instance of {see @link DirectedInterval} with the given direction
   *
   * @param direction the direction to apply
   * @return a new instance of {see @link DirectedInterval} with the given direction
   */
  DirectedInterval directed(QueryDirection direction);

}
