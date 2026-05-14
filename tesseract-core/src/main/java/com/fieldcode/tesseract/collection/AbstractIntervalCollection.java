package com.fieldcode.tesseract.collection;

import java.time.Duration;
import java.util.Optional;

import com.fieldcode.tesseract.Interval;
import com.fieldcode.tesseract.IntervalCollection;
import com.fieldcode.tesseract.QueryDirection;
import com.fieldcode.tesseract.duration.Durations;
import com.fieldcode.tesseract.interval.IntervalSets;

import static com.fieldcode.tesseract.QueryDirection.FORWARD;

public abstract class AbstractIntervalCollection implements IntervalCollection {

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
        .map(IntervalCollection::isAlways)
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
  public int hashCode() {
    return IntervalCollections.hashCode(this);
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
