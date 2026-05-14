package com.fieldcode.tesseract;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.Optional;

/**
 * The IntervalSet represents a data structure for storing and retrieving non overlapping intervals.
 * <p>
 * {@link com.fieldcode.tesseract.Interval}
 *
 * @version 1.3.0
 */
public interface IntervalSet extends IntervalSetReader, IntervalCollection {

  /**
   * <pre>
   * Inserts an interval into the set.
   *
   * Given an interval set and a defined interval:
   *
   * {@code IntervalSet set = intervalset.include(interval); }
   *
   * will return the <strong>set</strong> containing the provided interval.
   *
   * <strong>CASE:</strong> Merging non overlapping intervals
   *
   * Supposing that the set contains two intervals:
   * <strong>A(a1, a2)</strong> and <strong>B(b1, b2)</strong> where
   * <strong>a1 < b1</strong> and <strong>a2 < b1</strong>. In case of including
   * an interval <strong>C(c1, c2)</strong> where <strong>a1 <= c1 <= a2</strong> and
   * <strong>b1 <= c2 <= b2</strong> .
   *
   *   a1        a2    b1       b2
   *   -----------     ----------
   *           c1      c2
   *         C ----------
   *
   * Then the two non-overlapping intervals will be merged with the newly added
   * interval C to a single interval with lower moment bound a1 and upper moment
   * bound b2
   *
   *   a1                      b2
   *   --------------------------
   *
   * @param interval Interval
   *
   * @return the set after the interval is included
   *
   * @since 1.3.0
   * </pre>
   */
  IntervalSet include(Interval interval);

  /**
   * <pre>
   * Inserts an interval into the set defined by a lower and an upper bound moment.
   * In case of overlapping intervals the intervals will be merged.
   *
   * Given an interval set and two defined moments:
   *
   * {@code IntervalSet set = intervalset.include(momentLower ,
   * momentUpper); }
   *
   * will return the <strong>set</strong> containing the provided interval.
   *
   * In case of merging example see {@link #include(Interval)}
   *
   * @param lower moment
   * @param upper moment
   *
   * @return the set after the interval is included
   *
   * @since 1.3.0
   *
   * </pre>
   */
  IntervalSet include(Moment lower, Moment upper);

  /**
   * <pre>
   * Inserts an interval into the set defined by a lower and an upper bound offsetDateTime.
   * In case of overlapping intervals the intervals will be merged.
   *
   * Given an interval set and two defined offsetDateTimes:
   *
   * {@code IntervalSet set = intervalset.include(offsetDateTimeLower ,
   * offsetDateTimeUpper); }
   *
   * will return the <strong>set</strong> containing the provided interval.
   *
   * In case of merging example see {@link #include(Interval)}
   *
   * @param lower offsetDateTime
   * @param upper offsetDateTime
   *
   * @return the set after the interval is included
   *
   * @since 1.3.0
   * </pre>
   */

  IntervalSet include(OffsetDateTime lower, OffsetDateTime upper);

  /**
   * <pre>
   * Excludes a defined interval from the set. In case the interval is a subset
   * of a set's interval then the set interval will be split into two separate
   * intervals.
   *
   * Given an interval set and a defined interval:
   *
   * {@code IntervalSet set = intervalset.exclude(interval); }
   *
   * will return the <strong>set</strong> excluding the requested interval.
   *
   * <strong>CASE:</strong> Excluding subset of an interval
   *
   * Supposing that the set contains one interval <strong>A(a1, a2)</strong>.
   * In case of excluding the interval <strong>B(b1, b2)</strong> where
   * <strong>a1 < b1 < a2 </strong> and <strong> a1 < b2 < a2</strong>, then:
   *
   *                     a1                 a2
   *                   A ---------------------
   *
   *                           b1      b2
   *                         B ----------
   *
   *                     c1  c2          d1 d2
   *                   C ------        D -----
   *
   * The original interval <strong>A</strong> will be split into two different intervals
   * <strong>C(c1, c2)</strong> and <strong>D(d1,d2)</strong>
   *
   * @param interval to be excluded from the set
   *
   * @return the set after the interval is excluded
   *
   * @since 1.3.0
   *
   * </pre>
   */
  IntervalSet exclude(Interval interval);

  /**
   * <pre>
   * excludes an interval from the set defined by a lower and an upper bound moment.
   * In case the interval is a subset of a set's interval then the set interval
   * will be split into two separate intervals.
   *
   * Given an interval set and a defined interval:
   *
   * {@code IntervalSet set = intervalset.exclude(momentLower, momentUpper); }
   *
   * will return the <strong>set</strong> excluding the requested interval.
   *
   * For the splitting case see {@link #exclude(Interval)}
   *
   * @param lower moment
   * @param upper moment
   *
   * @return the set after the interval is excluded
   *
   * @since 1.3.0
   * </pre>
   */

  IntervalSet exclude(Moment lower, Moment upper);

  /**
   * <pre>
   * excludes an interval from the set defined by a lower and an upper bound offsetDateTime.
   * In case the interval is a subset of a set's interval then the set interval
   * will be split into two separate intervals.
   *
   * Given an interval set and a defined interval:
   *
   * {@code IntervalSet set = intervalset.exclude(offsetDateTimeLower, offsetDateTimeUpper); }
   *
   * will return the <strong>set</strong> excluding the requested interval.
   *
   * For the splitting case see {@link #exclude(Interval)}
   *
   * @param lower offsetDateTime
   * @param upper offsetDateTime
   *
   * @return the set after the interval is excluded
   *
   * @since 1.3.0
   * </pre>
   */
  IntervalSet exclude(OffsetDateTime lower, OffsetDateTime upper);

  /**
   * <pre>
   * This function searches throughout the set using the given moment as a
   * reference point for the closest interval that has a duration longer or
   * equal than the requested one. The interval in question will be searched from
   * the given moment onwards or backwards depending on the value of the direction
   * argument.
   *
   * Given a defined set containing the intervals <strong>A(a1, a2)</strong> and <strong>B(b1, b2)</strong>
   * where <strong>durationB = 2 * durationA</strong> and <strong>a*,b*</strong> are moments.
   *
   *                                 a1  a2    b1       b2
   *                                 -----     ----------
   *
   * i) CASE FORWARD:
   *
   * {@code Optional<Interval> interval = interval.find(b1,durationA,IterationDirection.FORWARD) }
   *
   *                                 a1  a2    b1       b2
   *                                 -----     ----------
   *                                         C -----
   *
   * will return an interval C with lower bound defined by the <strong>b1</strong> moment
   * and upper defined by <strong>b1</strong> plus the requested duration <strong>(durationA)</strong>.
   *
   * ii) CASE BACKWARD:
   *
   * {@code Optional<Interval> interval = interval.find(b1,durationA,IterationDirection.BACKWARD) }
   *
   *                                  a1  a2    b1       b2
   *                                  -----     ----------
   *                                C -----
   *
   * the first iteration will fetch the interval <strong>B</strong> which will not comply with
   * the duration requirement as the search is conducted backwards. In
   * that case the next available interval backwards will be interval <strong>A</strong> which satisfies
   * the duration requirement.
   *
   * The result will be an interval C with a lower bound defined by the upper moment <strong>a2</strong>
   * minus the provided duration <strong>(durationA)</strong> and the upper bound
   * defined by <strong>a2</strong>.
   *
   * @param window the time interval in which the search will begin
   * @param duration of the requested interval
   * @param direction of the search window
   * @return an optional containing either the requested interval or empty
   * @since 1.3.0
   *
   * </pre>
   */
  Optional<Interval> find(Interval window, Duration duration, QueryDirection direction);

  Optional<Interval> find(Moment start, Duration duration, QueryDirection direction);

  /**
   * Creates a snapshot of the current interval set.
   *
   * @return a snapshot instance of the current interval set
   */
  IntervalSet snapshot();

}
