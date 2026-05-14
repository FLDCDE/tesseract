package com.fieldcode.tesseract.interval;

import java.time.Duration;
import java.util.Optional;
import java.util.function.Function;

import com.fieldcode.tesseract.DirectedInterval;
import com.fieldcode.tesseract.FluentIterator;
import com.fieldcode.tesseract.Interval;
import com.fieldcode.tesseract.IntervalCollection;
import com.fieldcode.tesseract.Moment;
import com.fieldcode.tesseract.QueryDirection;
import com.fieldcode.tesseract.Signal;
import com.fieldcode.tesseract.collection.BoundariesToIntervalIterator;
import com.fieldcode.tesseract.collection.IntervalCollectionFinder;
import com.fieldcode.tesseract.collection.IntervalCollections;
import com.fieldcode.tesseract.duration.Durations;

import static com.fieldcode.tesseract.QueryDirection.FORWARD;

public class IntervalMapCollection implements IntervalCollection {

  private final Function<DirectedInterval, FluentIterator<Signal<Moment, Boolean>>> query;

  private IntervalMapCollection(Function<DirectedInterval, FluentIterator<Signal<Moment, Boolean>>> query) {
    this.query = query;
  }

  public static IntervalMapCollection of(Function<DirectedInterval, FluentIterator<Signal<Moment, Boolean>>> query) {
    return new IntervalMapCollection(query);
  }

  @Override
  public FluentIterator<Interval> intervals() {
    var bounds = bounds(Intervals.always(), FORWARD);
    return BoundariesToIntervalIterator.of(bounds, FORWARD);
  }

  @Override
  public FluentIterator<Interval> intervals(Interval window, QueryDirection direction) {
    var bounds = bounds(window, direction);
    return BoundariesToIntervalIterator.of(bounds, direction);
  }

  @Override
  public FluentIterator<Interval> intervals(DirectedInterval directed) {
    return intervals(directed.getInterval(), directed.getDirection());
  }

  @Override
  public FluentIterator<Signal<Moment, Boolean>> bounds(Interval window, QueryDirection direction) {
    return query.apply(window.directed(direction));
  }

  @Override
  public Optional<Interval> find(Interval window, QueryDirection direction, Duration duration) {
    return IntervalCollectionFinder.find(this, window, direction, duration);
  }

  @Override
  public boolean isNever() {
    return intervals().isEmpty();
  }

  @Override
  public boolean isAlways() {
    return intervals()
        .findFirst()
        .filter(Interval::isAlways)
        .isPresent();
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
  public IntervalMapCollection copy() {
    throw new UnsupportedOperationException("Not implemented, yet");
  }

}
