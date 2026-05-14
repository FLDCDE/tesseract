package com.fieldcode.tesseract.interval;

import com.fieldcode.tesseract.DirectedInterval;
import com.fieldcode.tesseract.FluentIterator;
import com.fieldcode.tesseract.Interval;
import com.fieldcode.tesseract.IntervalAlignment;
import com.fieldcode.tesseract.IntervalCollection;
import com.fieldcode.tesseract.IntervalsRelation;
import com.fieldcode.tesseract.Moment;
import com.fieldcode.tesseract.QueryDirection;
import com.fieldcode.tesseract.Signal;
import com.fieldcode.tesseract.collection.BoundariesToIntervalIterator;
import com.fieldcode.tesseract.collection.IntervalCollectionFinder;
import com.fieldcode.tesseract.collection.SignalExpanderIterator;
import com.fieldcode.tesseract.duration.Durations;
import com.fieldcode.tesseract.moment.Moments;
import com.fieldcode.tesseract.utils.iterator.Iterators;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.Optional;
import java.util.function.BiFunction;
import static com.fieldcode.tesseract.QueryDirection.BACKWARD;
import static com.fieldcode.tesseract.QueryDirection.FORWARD;
import static com.fieldcode.tesseract.signal.Signals.signal;

abstract class AbstractInterval implements Interval {

  @Override
  public FluentIterator<Moment> getBounds() {
    return Iterators.iterator(getLower(), getUpper());
  }

  @Override
  public boolean isEmpty() {
    return false;
  }

  @Override
  public boolean contains(OffsetDateTime at) {
    return contains(Moments.moment(at));
  }

  @Override
  public Interval ifContains(Moment at, BiFunction<Interval, Moment, Interval> mapper) {

    // TODO: 2023. 09. 18. Add test

    if (contains(at)) {
      return mapper.apply(this, at);
    }
    return this;
  }

  @Override
  public Interval mapRelativeTo(
      OffsetDateTime at,
      BiFunction<Interval, Moment, Interval> beforeAction,
      BiFunction<Interval, Moment, Interval> containsAction,
      BiFunction<Interval, Moment, Interval> afterAction
  ) {
    return mapRelativeTo(Moments.moment(at), beforeAction, containsAction, afterAction);
  }

  @Override
  public Interval mapRelativeTo(
      Moment at,
      BiFunction<Interval, Moment, Interval> beforeAction,
      BiFunction<Interval, Moment, Interval> containsAction,
      BiFunction<Interval, Moment, Interval> afterAction
  ) {

    // TODO: 2023. 09. 18.  Add test

    if (getLower().gt(at)) {
      return beforeAction.apply(this, at);
    }

    if (contains(at)) {
      return containsAction.apply(this, at);
    }
    return afterAction.apply(this, at);
  }

  @Override
  public IntervalsRelation getRelationTo(Interval interval) {
    return IntervalRelationCalculator.relation(this, interval);
  }

  @Override
  public boolean distinct(Interval other) {
    // TODO: 2023. 09. 18.  Add test
    return getRelationTo(other).distinct();
  }

  @Override
  public boolean intersects(Interval other) {
    // TODO: 2023. 09. 18.  Add test
    return getRelationTo(other).intersects();
  }

  @Override
  public boolean connects(Interval other) {
    // TODO: 2023. 09. 18.  Add test
    return getRelationTo(other).connects();
  }

  @Override
  public FluentIterator<Interval> union(Interval other) {
    var relationTo = getRelationTo(other);

    if (relationTo.isBefore()) {
      return Iterators.iterator(this, other);
    }

    if (relationTo.isAfter()) {
      return Iterators.iterator(other, this);
    }

    return Iterators.iterator(span(other));
  }

  @Override
  public FluentIterator<Interval> intersect(Interval other) {
    return intervals(other, FORWARD);
  }

  @Override
  public Interval shift(Duration shift) {
    return Intervals.interval(getLower().shift(shift), getUpper().shift(shift));
  }

  @Override
  public Interval withLower(OffsetDateTime lower) {
    return Intervals.interval(Moments.moment(lower), getUpper());
  }

  @Override
  public Interval withLower(Moment lower) {
    return Intervals.interval(lower, getUpper());
  }

  @Override
  public Interval withUpper(Moment to) {
    return Intervals.interval(getLower(), to);
  }

  @Override
  public Interval withUpper(OffsetDateTime end) {
    return Intervals.interval(getLower(), Moments.moment(end));
  }

  @Override
  public Interval withDuration(Duration duration, IntervalAlignment alignment) {
    return alignment.accept(IntervalDurationAligner.of(this, duration));
  }

  @Override
  public Interval withZoneIdSameInterval(ZoneId zoneId) {
    var lower = getLower().withZoneIdSameMoment(zoneId);
    var upper = getUpper().withZoneIdSameMoment(zoneId);
    return Intervals.interval(lower, upper);
  }

  @Override
  public Interval withZoneIdSameInterval(String zoneId) {
    return withZoneIdSameInterval(ZoneId.of(zoneId));
  }

  @Override
  public DirectedInterval forward() {
    return directed(FORWARD);
  }

  @Override
  public DirectedInterval backward() {
    return directed(BACKWARD);
  }

  @Override
  public DirectedInterval directed(QueryDirection direction) {
    return Intervals.directed(this, direction);
  }

  @Override
  public FluentIterator<Interval> intervals() {
    return Iterators.iterator(this);
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

    var lower = getLower();
    var upper = getUpper();

    var signals = direction.select(
        Iterators.iterator(signal(lower, true), signal(upper, false)),
        Iterators.iterator(signal(upper, true), signal(lower, false))
    );

    return SignalExpanderIterator.crop(signals, window, direction);
  }

  @Override
  public Optional<Interval> find(Interval window, QueryDirection direction, Duration duration) {
    return IntervalCollectionFinder.find(this, window, direction, duration);
  }

  @Override
  public Optional<Interval> span() {
    return Optional.of(this);
  }

  @Override
  public Duration totalDuration(Interval window) {
    return Durations.total(Iterators.iterator(this));
  }

  @Override
  public IntervalCollection copy() {
    return this;
  }

  protected <T> T unsupported() {
    throw new UnsupportedOperationException("Unsupported operation");
  }

}
