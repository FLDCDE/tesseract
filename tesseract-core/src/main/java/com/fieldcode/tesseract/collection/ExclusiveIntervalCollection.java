package com.fieldcode.tesseract.collection;

import com.fieldcode.tesseract.FluentIterator;
import com.fieldcode.tesseract.Interval;
import com.fieldcode.tesseract.IntervalCollection;
import com.fieldcode.tesseract.Moment;
import com.fieldcode.tesseract.QueryDirection;
import com.fieldcode.tesseract.Signal;

public class ExclusiveIntervalCollection extends TemporalLogicIntervalCollection {

  private ExclusiveIntervalCollection(IntervalCollection... collections) {
    super(collections);
  }

  public static ExclusiveIntervalCollection of(IntervalCollection... collections) {
    return new ExclusiveIntervalCollection(collections);
  }

  @Override
  public FluentIterator<Signal<Moment, Boolean>> bounds(Interval window, QueryDirection direction) {
    return TemporalLogicEventIterator.exclusive(collections, window, direction);
  }

}
