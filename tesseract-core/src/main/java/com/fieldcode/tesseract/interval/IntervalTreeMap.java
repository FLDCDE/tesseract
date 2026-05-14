package com.fieldcode.tesseract.interval;

import java.util.Map.Entry;
import java.util.function.BiPredicate;

import com.fieldcode.tesseract.FluentIterator;
import com.fieldcode.tesseract.Interval;
import com.fieldcode.tesseract.IntervalCollection;
import com.fieldcode.tesseract.IntervalMap;
import com.fieldcode.tesseract.Moment;
import com.fieldcode.tesseract.QueryDirection;
import com.fieldcode.tesseract.utils.iterator.Iterators;
import com.google.common.collect.Range;
import com.google.common.collect.RangeMap;
import com.google.common.collect.TreeRangeMap;

import static com.google.common.base.Preconditions.checkState;

@SuppressWarnings("UnstableApiUsage")
public class IntervalTreeMap<T> implements IntervalMap<T> {

  private final T defaultValue;
  private final RangeMap<Moment, T> map = TreeRangeMap.create();

  private IntervalTreeMap(T defaultValue) {
    this.defaultValue = defaultValue;
    put(Intervals.always(), defaultValue);
  }

  public static <T> IntervalTreeMap<T> of(T defaultValue) {
    return new IntervalTreeMap<>(defaultValue);
  }

  private Range<Moment> asRange(Interval interval) {
    return Range.closedOpen(interval.getLower(), interval.getUpper());
  }

  private IntervalEntry<T> asIntervalEntry(Entry<Range<Moment>, T> entry) {
    return entry.getValue().equals(defaultValue)
        ? IntervalMaps.entry(asInterval(entry.getKey()), defaultValue)
        : IntervalMaps.entry(asInterval(entry.getKey()), entry.getValue());
  }

  private Interval asInterval(Range<Moment> range) {
    return Intervals.interval(range.lowerEndpoint(), range.upperEndpoint());
  }

  @Override
  public IntervalMap<T> put(Interval interval, T value) {
    map.putCoalescing(asRange(interval), value);
    return this;
  }

  @Override
  public IntervalEntry<T> get(Moment moment) {
    var entry = map.getEntry(moment);
    // This should never happen, because we always have a default value
    checkState(entry != null, "No entry found [moment=%s]", moment);
    return asIntervalEntry(entry);
  }

  @Override
  public IntervalMap<T> remove(Interval interval) {
    return put(interval, defaultValue);
  }

  @Override
  public T getDefaultValue() {
    return defaultValue;
  }

  @Override
  public FluentIterator<IntervalEntry<T>> entries() {
    return Iterators
        .iterator(map.asMapOfRanges().entrySet())
        .map(this::asIntervalEntry);
  }

  @Override
  public FluentIterator<IntervalEntry<T>> entries(Interval window, QueryDirection direction) {
    return direction.get(
        () -> entriesForward(window),
        () -> entriesBackward(window)
    );
  }

  @Override
  public IntervalCollection intervals() {
    return intervals((i, v) -> !v.equals(defaultValue));
  }

  @Override
  public IntervalCollection intervals(BiPredicate<Interval, T> intervalPredicate) {
    return IntervalMapToIntervalCollectionFactory.create(intervalPredicate, this);
  }

  private FluentIterator<IntervalEntry<T>> entriesForward(Interval window) {
    var range = asRange(window);
    var entries = map.subRangeMap(range).asMapOfRanges().entrySet();
    return Iterators
        .iterator(entries)
        .map(this::asIntervalEntry);
  }

  private FluentIterator<IntervalEntry<T>> entriesBackward(Interval window) {
    var range = asRange(window);
    var entries = map.subRangeMap(range).asDescendingMapOfRanges().entrySet();
    return Iterators
        .iterator(entries)
        .map(this::asIntervalEntry);
  }

  @Override
  public int hashCode() {
    return map.hashCode();
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }

    if (obj == null) {
      return false;
    }

    if (getClass() != obj.getClass()) {
      return false;
    }

    var other = (IntervalTreeMap<?>) obj;

    return entries().list().equals(other.entries().list());

  }

  @Override
  public String toString() {
    return entries().list().toString();
  }

}
