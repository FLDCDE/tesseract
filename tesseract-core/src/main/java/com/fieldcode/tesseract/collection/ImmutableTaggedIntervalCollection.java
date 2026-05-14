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
import com.fieldcode.tesseract.TaggedIntervalCollection;
import com.fieldcode.tesseract.interval.IntervalSets;
import com.google.common.base.Objects;

public class ImmutableTaggedIntervalCollection extends AbstractIntervalCollection implements TaggedIntervalCollection {

  private final String id;
  private final TagHolder tags;
  private final IntervalCollection collection;

  private ImmutableTaggedIntervalCollection(String id, IntervalCollection collection, TagHolder tags) {
    this.id = id;
    this.tags = tags;
    this.collection = IntervalSets.disjoint(collection);
  }

  public static ImmutableTaggedIntervalCollection of(String id, IntervalCollection collection, TagHolder tags) {
    return new ImmutableTaggedIntervalCollection(id, collection, tags);
  }

  @Override
  public FluentIterator<Interval> intervals() {return collection.intervals();}

  @Override
  public FluentIterator<Interval> intervals(Interval window, QueryDirection direction) {return collection.intervals(window, direction);}

  @Override
  public FluentIterator<Interval> intervals(DirectedInterval directed) {return collection.intervals(directed);}

  @Override
  public FluentIterator<Signal<Moment, Boolean>> bounds(Interval window, QueryDirection direction) {return collection.bounds(window, direction);}

  @Override
  public Optional<Interval> find(Interval window, QueryDirection direction, Duration duration) {return collection.find(window, direction, duration);}

  @Override
  public boolean isNever() {return collection.isNever();}

  @Override
  public boolean isAlways() {return collection.isAlways();}

  @Override
  public IntervalCollection copy() {
    return of(id, collection.copy(), tags);
  }

  /**
   * Returns the id of this collection.
   *
   * @return the id of this collection
   */
  @Override
  public String getId() {
    return id;
  }

  @Override
  public TagHolder getTags() {
    return tags;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {return true;}
    if (o == null || getClass() != o.getClass()) {return false;}
    if (!super.equals(o)) {return false;}
    ImmutableTaggedIntervalCollection that = (ImmutableTaggedIntervalCollection) o;
    return Objects.equal(id, that.id) && Objects.equal(tags, that.tags) && Objects.equal(collection,
        that.collection);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(id, tags, collection);
  }
  
}
