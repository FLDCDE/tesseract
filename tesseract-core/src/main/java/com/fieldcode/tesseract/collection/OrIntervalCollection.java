package com.fieldcode.tesseract.collection;

import com.fieldcode.tesseract.FluentIterator;
import com.fieldcode.tesseract.Interval;
import com.fieldcode.tesseract.IntervalCollection;
import com.fieldcode.tesseract.Moment;
import com.fieldcode.tesseract.QueryDirection;
import com.fieldcode.tesseract.Signal;

public class OrIntervalCollection extends TemporalLogicIntervalCollection {

  private OrIntervalCollection(IntervalCollection... collections) {
    super(collections);
  }

  public static TemporalLogicIntervalCollection of(IntervalCollection... collections) {
    return new OrIntervalCollection(collections);
  }

  @Override
  public FluentIterator<Signal<Moment, Boolean>> bounds(Interval window, QueryDirection direction) {
    return TemporalLogicEventIterator.or(collections, window, direction);
  }

}
