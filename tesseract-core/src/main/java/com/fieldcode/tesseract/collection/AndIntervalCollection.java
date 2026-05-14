package com.fieldcode.tesseract.collection;

import com.fieldcode.tesseract.FluentIterator;
import com.fieldcode.tesseract.Interval;
import com.fieldcode.tesseract.IntervalCollection;
import com.fieldcode.tesseract.Moment;
import com.fieldcode.tesseract.QueryDirection;
import com.fieldcode.tesseract.Signal;

public class AndIntervalCollection extends TemporalLogicIntervalCollection {

  private AndIntervalCollection(IntervalCollection... collections) {
    super(collections);
  }

  public static AndIntervalCollection of(IntervalCollection... collections) {
    return new AndIntervalCollection(collections);
  }

  @Override
  public FluentIterator<Signal<Moment, Boolean>> bounds(Interval window, QueryDirection direction) {
    return TemporalLogicEventIterator.and(collections, window, direction);
  }

}

