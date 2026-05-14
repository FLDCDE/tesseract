package com.fieldcode.tesseract.collection;

import java.util.List;

import com.fieldcode.tesseract.DirectedInterval;
import com.fieldcode.tesseract.FluentIterator;
import com.fieldcode.tesseract.Interval;
import com.fieldcode.tesseract.IntervalCollection;
import com.fieldcode.tesseract.QueryDirection;
import com.fieldcode.tesseract.interval.Intervals;

import static com.fieldcode.tesseract.QueryDirection.FORWARD;

public abstract class TemporalLogicIntervalCollection extends AbstractIntervalCollection {

  protected final List<IntervalCollection> collections;

  protected TemporalLogicIntervalCollection(IntervalCollection... collections) {
    this.collections = List.of(collections);
  }

  @Override
  public FluentIterator<Interval> intervals() {
    return intervals(Intervals.always(), FORWARD);
  }

  @Override
  public FluentIterator<Interval> intervals(Interval window, QueryDirection direction) {
    return BoundariesToIntervalIterator.of(
        bounds(window, direction),
        direction
    );
  }

  @Override
  public FluentIterator<Interval> intervals(DirectedInterval directed) {
    return intervals(directed.getInterval(), directed.getDirection());
  }

}
