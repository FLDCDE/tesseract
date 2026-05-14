package com.fieldcode.tesseract.interval;

import com.fieldcode.tesseract.DirectedInterval;
import com.fieldcode.tesseract.FluentIterator;
import com.fieldcode.tesseract.Interval;
import com.fieldcode.tesseract.IntervalAlignment;
import com.fieldcode.tesseract.IntervalCollection;
import com.fieldcode.tesseract.IntervalSet;
import com.fieldcode.tesseract.Moment;
import com.fieldcode.tesseract.QueryDirection;
import com.fieldcode.tesseract.Signal;
import com.fieldcode.tesseract.SignalRange;
import com.fieldcode.tesseract.SignalRangeMap;
import com.fieldcode.tesseract.collection.BoundariesToIntervalIterator;
import com.fieldcode.tesseract.collection.IntervalCollectionFinder;
import com.fieldcode.tesseract.collection.IntervalCollections;
import com.fieldcode.tesseract.duration.Durations;
import com.fieldcode.tesseract.map.SignalRangeMaps;
import com.fieldcode.tesseract.moment.Moments;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.Iterator;
import java.util.Optional;
import static com.fieldcode.tesseract.IntervalAlignment.ALIGN_ENDS;
import static com.fieldcode.tesseract.IntervalAlignment.ALIGN_STARTS;
import static com.fieldcode.tesseract.QueryDirection.FORWARD;
import static com.fieldcode.tesseract.moment.Moments.moment;


public class DisjointIntervalSet implements IntervalSet, Iterable<Interval> {

  private final SignalRangeMap<Moment, Boolean> map = SignalRangeMaps.disjoint(Moments.ninf(), Moments.inf(), false);

  private DisjointIntervalSet() {
  }

  public static IntervalSet of() {
    return new DisjointIntervalSet();
  }

  private static Interval asInterval(SignalRange<Moment, Boolean> range) {
    return Intervals.interval(range.getLowerKey(), range.getUpperKey());
  }

  private static IntervalAlignment directionToAlign(QueryDirection direction) {
    return direction.select(ALIGN_STARTS, ALIGN_ENDS);
  }

  @Override
  public IntervalSet include(Interval interval) {
    if (interval.isNever()) {
      return this;
    }
    return include(interval.getLower(), interval.getUpper());
  }

  @Override
  public IntervalSet include(Moment lower, Moment upper) {
    map.put(lower, upper, true);
    return this;
  }

  @Override
  public IntervalSet include(OffsetDateTime lower, OffsetDateTime upper) {
    return include(moment(lower), moment(upper));
  }

  @Override
  public IntervalSet exclude(Interval interval) {
    if (interval.isNever()) {
      return this;
    }
    map.put(interval.getLower(), interval.getUpper(), false);
    return this;
  }

  @Override
  public IntervalSet exclude(Moment lower, Moment upper) {
    map.put(lower, upper, false);
    return this;
  }

  @Override
  public IntervalSet exclude(OffsetDateTime lower, OffsetDateTime upper) {
    map.put(moment(lower), moment(upper), false);
    return this;
  }

  @Override
  public Optional<Interval> find(Interval window, Duration duration, QueryDirection direction) {
    var alignment = directionToAlign(direction);
    return intervals(window, direction)
        .filter(interval -> interval.encloses(duration))
        .map(interval -> interval.withDuration(duration, alignment))
        .findFirst();
  }

  @Override
  public Optional<Interval> find(Moment start, Duration duration, QueryDirection direction) {
    var window = Intervals.interval(start, direction);
    return find(window, duration, direction);
  }

  @Override
  public IntervalSet snapshot() {
    // TODO: 2023. 09. 18. Must bee immutable
    return IntervalSets.disjoint(this);
  }

  @Override
  public Interval get(Moment moment) {
    var range = map.get(moment);
    return range.getLowerValue()
        ? asInterval(range)
        : null;
  }

  @Override
  public Optional<Interval> getIfContains(Moment moment) {
    return Optional.ofNullable(get(moment));
  }

  @Override
  public boolean contains(Moment moment) {
    return map.get(moment).getLowerValue();
  }

  @Override
  public boolean encloses(Interval other) {
    var range = map.get(other.getLower());
    return range.getLowerValue() && asInterval(range).getRelationTo(other).encloses();
  }

  @Override
  public FluentIterator<Interval> intervals() {
    return intervals(Intervals.always(), FORWARD);
  }

  @Override
  public FluentIterator<Interval> intervals(Interval window, QueryDirection direction) {
    return BoundariesToIntervalIterator.of(bounds(window, direction), direction);
  }

  @Override
  public FluentIterator<Interval> intervals(DirectedInterval directed) {
    return intervals(directed.getInterval(), directed.getDirection());
  }

  @Override
  public FluentIterator<Signal<Moment, Boolean>> bounds(Interval window, QueryDirection direction) {
    return map.signals(window.getLower(), window.getUpper(), direction, true);
  }

  @Override
  public Optional<Interval> find(Interval window, QueryDirection direction, Duration duration) {
    return IntervalCollectionFinder.find(this, window, direction, duration);
  }

  @Override
  public boolean isNever() {
    return intervals()
        .findFirst()
        .map(Interval::isNever)
        .orElse(true);
  }

  @Override
  public boolean isAlways() {
    return intervals()
        .findFirst()
        .map(Interval::isAlways)
        .orElse(false);
  }

  @Override
  public Optional<Interval> span() {
    return IntervalCollections.span(this);
  }

  @Override
  public Duration totalDuration(Interval window) {
    return Durations.total(intervals(window, FORWARD));
  }

  @Override
  public IntervalCollection copy() {
    return IntervalSets.disjoint(intervals());
  }

  @Override
  public Iterator<Interval> iterator() {
    return intervals();
  }

  @Override
  public int hashCode() {
    return map.hashCode();
  }

  @Override
  @SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
  public boolean equals(Object obj) {
    return IntervalCollections.equals(this, obj);
  }

  @Override
  public String toString() {
    return IntervalCollections.toString(this);
  }

}
