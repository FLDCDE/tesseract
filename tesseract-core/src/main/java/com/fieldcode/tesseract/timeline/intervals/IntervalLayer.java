package com.fieldcode.tesseract.timeline.intervals;

import com.fieldcode.tesseract.DirectedInterval;
import com.fieldcode.tesseract.FluentIterator;
import com.fieldcode.tesseract.Interval;
import com.fieldcode.tesseract.IntervalCollection;
import com.fieldcode.tesseract.IntervalSet;
import com.fieldcode.tesseract.interval.IntervalSets;
import com.fieldcode.tesseract.timeline.InternalLayer;
import com.fieldcode.tesseract.timeline.LayerDefinition;
import com.fieldcode.tesseract.timeline.ModifiableLayer;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;

import static com.fieldcode.tesseract.timeline.LayerType.INTERVAL;

public class IntervalLayer extends InternalLayer {

  private final IntervalSet intervals;

  private IntervalLayer(LayerDefinition definition, IntervalSet intervals) {
    super(definition);
    Preconditions.checkState(definition.getType() == INTERVAL, "Layer type must be %s", INTERVAL);
    this.intervals = intervals;
  }

  public static IntervalLayer of(LayerDefinition definition) {
    return new IntervalLayer(definition, IntervalSets.disjoint());
  }

  @Override
  public InternalLayer copy() {
    var copied = IntervalSets.disjoint(intervals);
    return new IntervalLayer(getDefinition(), copied);
  }

  @Override
  public InternalLayer duplicate(String newId) {
    var copied = IntervalSets.disjoint(intervals);
    return new IntervalLayer(copyDefinition(newId), copied);
  }

  @Override
  public FluentIterator<Interval> selectIntervals(DirectedInterval directed) {
    return intervals.intervals(directed);
  }

  @Override
  public IntervalCollection asIntervalCollection() {
    return intervals.snapshot();
  }

  @Override
  public ModifiableLayer put(Interval interval) {
    intervals.include(interval);
    return this;
  }

  @Override
  public ModifiableLayer remove(Interval interval) {
    intervals.exclude(interval);
    return this;
  }

  @Override
  public ModifiableLayer putAll(IntervalCollection intervals) {
    intervals.intervals().forEach(this::put);
    return this;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {return true;}
    if (o == null || getClass() != o.getClass()) {return false;}
    IntervalLayer that = (IntervalLayer) o;
    return Objects.equal(intervals, that.intervals);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(intervals);
  }

}
