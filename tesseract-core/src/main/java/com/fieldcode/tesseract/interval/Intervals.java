package com.fieldcode.tesseract.interval;

import com.fieldcode.tesseract.BoundedInterval;
import com.fieldcode.tesseract.DirectedInterval;
import com.fieldcode.tesseract.FluentIterator;
import com.fieldcode.tesseract.Interval;
import com.fieldcode.tesseract.Moment;
import com.fieldcode.tesseract.QueryDirection;
import com.fieldcode.tesseract.utils.iterator.Iterators;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Stream;
import static com.fieldcode.tesseract.QueryDirection.FORWARD;
import static com.fieldcode.tesseract.moment.Moments.moment;
import static com.google.common.base.Preconditions.checkArgument;

public class Intervals {

  private static final Duration ONE_DAY = Duration.ofDays(1);

  private Intervals() {}

  /**
   * Parse an interval from a string. The string must be in the format {@code [lower..upper)}. Upper and lower bounds must be Moment-parsable.
   * <p>
   * Examples:
   *   <ul>
   *     <li>{@code [2019-01-01T00:00:00Z..2019-01-02T00:00:00Z)}</li>
   *     <li>{@code [2019-01-01T00:00:00Z..∞)}</li>
   *     <li>{@code [-∞..2019-01-02T00:00:00Z)}</li>
   *     <li>{@code [-∞..∞)}</li>
   *   </ul>
   * </p>
   *
   * @param input the string to parse
   * @return the parsed interval
   * @throws IllegalArgumentException if the string is not in the correct format
   */
  public static Interval parse(String input) {
    return IntervalParser.parse(input);
  }


  /**
   * Create an interval from a lower bound and an upper bound. The lower bound must be less than the upper bound.
   *
   * @param lower the lower bound
   * @param upper the upper bound
   * @return the interval
   * @throws IllegalArgumentException if the lower bound is not less than the upper bound
   */
  public static Interval interval(Moment lower, Moment upper) {

    checkArgument(lower.lt(upper), "Lower bound must be less than upper bound [lower=%s, upper=%s]", lower, upper);

    if (lower.isFinite() && upper.isFinite()) {
      return ImmutableBoundedInterval.of(lower, upper);
    }

    if (lower.isInfinite() && upper.isFinite()) {
      return ImmutableIntervalTo.of(upper);
    }

    if (lower.isFinite() && upper.isInfinite()) {
      return ImmutableIntervalFrom.of(lower);
    }

    return always();

  }

  /**
   * Create an interval from a lower bound and an upper bound. The lower bound must be less than the upper bound.
   *
   * @param lower the lower bound
   * @param upper the upper bound
   * @return the interval
   * @throws IllegalArgumentException if the lower bound is not less than the upper bound
   */
  public static BoundedInterval interval(OffsetDateTime lower, OffsetDateTime upper) {
    return ImmutableBoundedInterval.of(
        moment(lower),
        moment(upper)
    );
  }

  /**
   * Creates an interval.
   * <p>
   * Equivalent to:
   * <pre>
   *   interval(at, FORWARD)  -> intervalFrom(at) -> [at..∞)
   *   interval(at, BACKWARD) -> intervalTo(at)   -> (-∞..at]
   *   interval(-∞, FORWARD)  -> always()         -> (-∞..∞)
   *   interval(∞,  BACKWARD) -> always()         -> (-∞..∞)
   *   interval(∞,  FORWARD)  -> Exception        -> n/a
   *   interval-(∞, BACKWARD) -> Exception        -> n/a
   * </pre>
   * </p>
   *
   * @param at the moment at which the interval should be created
   * @param direction the direction of the interval
   * @return the interval
   * @throws IllegalArgumentException if the direction is FORWARD and the moment is infinite or if the direction is BACKWARD and the moment is negative infinite
   */
  public static Interval interval(Moment at, QueryDirection direction) {
    return (direction == FORWARD)
        ? intervalFrom(at)
        : intervalTo(at);
  }

  /**
   * Creates an interval from a lower bound and a duration. The lower bound must be less than the upper bound.
   *
   * @param at the lower bound
   * @param duration the duration
   * @return the interval
   */
  public static Interval interval(OffsetDateTime at, Duration duration) {
    return interval(at, at.plus(duration));
  }

  /**
   * Creates an interval from a moment and a duration. If the duration is negative, the duration will shift the lower bound, otherwise the duration will shift the upper bound.
   *
   * @param bound the lower/upper bound
   * @param duration the duration
   * @return the interval
   */
  public static Interval interval(Moment bound, Duration duration) {
    return duration.isNegative()
        ? interval(bound.shift(duration), bound)
        : interval(bound, bound.shift(duration));
  }

  /**
   * Creates an interval from a moment and a duration. Equivalent to {@code interval(at, duration.negated())}. If the duration is positive, the bound is the lower bound, otherwise
   * the bound is the upper bound.
   *
   * @param bound the upper/lower bound
   * @param duration the duration
   * @return the interval
   */
  public static Interval interval(Duration duration, OffsetDateTime bound) {
    return interval(bound, duration.negated());
  }

  /**
   * Creates an interval from a moment and a duration. Equivalent to {@code interval(at, duration.negated())}. If the duration is positive, the bound is the lower bound, otherwise
   * the bound is the upper bound.
   *
   * @param bound the upper/lower bound
   * @param duration the duration
   * @return the interval
   */
  public static Interval interval(Duration duration, Moment bound) {
    return interval(bound, duration.negated());
  }

  /**
   * Creates an interval where the lower bound is negative infinite and the upper bound is the given moment.
   *
   * @param to the upper bound
   * @return the interval
   * @throws IllegalArgumentException if the moment is negative infinite
   */
  public static Interval intervalTo(Moment to) {
    checkArgument(!to.isNegativeInfinite(), "Cannot create an interval from a negative infinite moment [to=%s]", to);
    return to.isFinite()
        ? ImmutableIntervalTo.of(to)
        : always();
  }

  /**
   * Creates an interval where the lower bound is negative infinite and the upper bound is the given moment.
   *
   * @param to the upper bound
   * @return the interval
   */
  public static Interval intervalTo(OffsetDateTime to) {
    return ImmutableIntervalTo.of(moment(to));
  }

  /**
   * Creates an interval where the lower bound is the given moment and the upper bound is positive infinite.
   *
   * @param from the lower bound
   * @return the interval
   * @throws IllegalArgumentException if the moment is positive infinite
   */
  public static Interval intervalFrom(Moment from) {
    checkArgument(!from.isPositiveInfinite(), "Cannot create an interval from a positive infinite moment [from=%s]", from);
    return from.isFinite()
        ? ImmutableIntervalFrom.of(from)
        : always();
  }

  /**
   * Creates an interval where the lower bound is the given moment and the upper bound is positive infinite.
   *
   * @param from the lower bound
   * @return the interval
   */
  public static Interval intervalFrom(OffsetDateTime from) {
    return ImmutableIntervalFrom.of(moment(from));
  }

  /**
   * Creates an optional interval based on the given start and end times.
   * <p>
   * Returns empty Optional:
   *   <ul>
   *    <li>If either of the times is null</li>
   *    <li>If start >= end</li>
   *  </ul>
   * </p>>
   *
   * @param from the start time
   * @param to the end time
   * @return the interval
   */
  public static Optional<Interval> tryInterval(OffsetDateTime from, OffsetDateTime to) {
    // TODO: 2023. 09. 18. Add tests
    if (from == null || to == null) {
      return Optional.empty();
    }
    return tryInterval(moment(from), moment(to));
  }

  /**
   * Creates an optional interval based on the given start and end times.
   * <p>
   * Returns empty Optional:
   *   <ul>
   *    <li>If either of the times is null</li>
   *    <li>If start >= end</li>
   *  </ul>
   * </p>>
   *
   * @param lower the start time
   * @param upper the end time
   * @return the interval
   */
  public static Optional<Interval> tryInterval(Moment lower, Moment upper) {
    // TODO: 2023. 09. 18. Add tests
    try {
      if (lower == null || upper == null) {
        return Optional.empty();
      }
      return Optional.ofNullable(interval(lower, upper));
    } catch (Exception e) {
      return Optional.empty();
    }
  }


  /**
   * Creates an interval where the lower is negative infinite and the upper bound is positive infinite.
   *
   * @return the interval
   */
  public static Interval always() {
    return ImmutableAlwaysInterval.of();
  }

  public static Interval empty() {
    return ImmutableEmptyInterval.of();
  }

  /**
   * Creates a directed interval. The interval is directed in the given direction in time. Mostly used for querying intervals in a specific direction.
   *
   * @param interval the interval
   * @param direction the direction
   * @return the directed interval
   */
  public static DirectedInterval directed(Interval interval, QueryDirection direction) {
    return ImmutableDirectedInterval.of(interval, direction);
  }

  public static FluentIterator<OffsetDateTime> step(Interval interval, Duration step) {
    checkArgument(interval.isBounded(), "Interval must be bounded", interval);
    var start = interval.getLower().getAt();
    var index = new AtomicInteger();

    var stream = Stream.generate(index::getAndIncrement)
        .map(step::multipliedBy)
        .map(start::plus)
        .takeWhile(interval::contains);

    return Iterators.fluent(stream.iterator());
  }


  /**
   * Calculates a day interval in a specified Zone for a specified date.
   *
   * @param date - reference, not null
   * @param zone – the time-zone to use, not null
   * @return day bounded interval, not null
   */
  public static Interval day(LocalDate date, ZoneId zone) {
    var start = date
        .atStartOfDay()
        .atZone(zone)
        .toOffsetDateTime();

    return Intervals.interval(start, Duration.ofDays(1));
  }

  /**
   * Calculates an interval in a specified Zone which contains the at parameter.
   *
   * @param at - reference, not null
   * @param zone – the time-zone to use, not null
   * @return day-bounded interval, not null
   */
  public static Interval day(OffsetDateTime at, ZoneId zone) {
    var lower = dayStartInZone(at, zone);
    var upper = dayStartInZone(at.plus(ONE_DAY), zone);

    if (lower.toLocalDate().isEqual(upper.toLocalDate())) {
      upper = atZone(upper.plus(ONE_DAY), zone);
    }
    return Intervals.interval(lower, upper);
  }

  /**
   * Returns an iterator of day intervals in a specified Zone which are contained in the given interval. The intervals are cropped to the given interval if crop is true. The method
   * considers the time-zone and the daylight-saving times.
   *
   * @param interval the interval to split into days
   * @param zone the time-zone to use
   * @param crop if true, the intervals are cropped to the given interval
   * @return an iterator of day intervals
   */
  public static FluentIterator<Interval> days(Interval interval, ZoneId zone, boolean crop) {
    checkArgument(interval.isBounded(), "Interval must be bounded", interval);

    var dayStart = day(interval.getStart(), zone).getStart();
    var dayEnd = day(interval.getEnd(), zone).getEnd();

    // fullInterval is an extended version of the input interval
    // moving the lower and upper bounds to the corresponding zone-day lower and upper
    var fullInterval = Intervals.interval(dayStart, dayEnd);

    var lastEnd = new AtomicReference<>(dayStart);

    var iterator = Stream
        .generate(lastEnd::get)
        .map(at -> day(at, zone))
        .peek(i -> lastEnd.set(i.getEnd()))
        .takeWhile(i -> isOverlapping(i, fullInterval))
        .iterator();

    var dayIntervals = Iterators.fluent(iterator);

    return crop
        ? dayIntervals.flatMap(interval::intersect)
        : dayIntervals;
  }

  private static boolean isOverlapping(Interval window, Interval fullDayWindow) {
    return !window.getRelationTo(fullDayWindow).distinct();
  }

  /**
   * Returns an iterator of {see @link Interval}s. Intervals are representing periods per days defined by start and end time. The intervals are cropped to the given interval if
   * crop is true. ZoneId is used to calculate the day boundaries.
   *
   * @param interval the interval to split into days
   * @param start the start time of the period
   * @param end the end time of the period
   * @param zone the time-zone to use
   * @param crop if true, the intervals are cropped to the given interval
   * @return an iterator of day intervals
   */
  public static FluentIterator<Interval> daily(Interval interval, LocalTime start, LocalTime end, ZoneId zone, boolean crop) {
    // TODO: 2023. 09. 18. Add tests

    if (start.equals(end)) {
      return days(interval, zone, crop);
    }

    var startShift = Duration.between(LocalTime.MIN, start);
    var duration = start.isBefore(end)
        ? Duration.between(start, end)
        : ONE_DAY.minus(Duration.between(end, start));

    var intervals = Intervals.days(interval, zone, false)
        .map(Interval::getLower)
        .map(Moment::getAt)
        .map(dayStart -> Intervals.interval(dayStart.plus(startShift), duration));

    return crop
        ? intervals.flatMap(interval::intersect)
        : intervals;
  }

  private static OffsetDateTime dayStartInZone(OffsetDateTime at, ZoneId zone) {
    return atZone(at, zone)
        .truncatedTo(ChronoUnit.DAYS);
  }

  private static OffsetDateTime atZone(OffsetDateTime at, ZoneId zone) {
    return at.atZoneSameInstant(zone)
        .toOffsetDateTime();
  }

}
