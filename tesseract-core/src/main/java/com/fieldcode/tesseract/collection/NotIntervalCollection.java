package com.fieldcode.tesseract.collection;

import com.fieldcode.tesseract.FluentIterator;
import com.fieldcode.tesseract.Interval;
import com.fieldcode.tesseract.IntervalCollection;
import com.fieldcode.tesseract.Moment;
import com.fieldcode.tesseract.QueryDirection;
import com.fieldcode.tesseract.Signal;

public class NotIntervalCollection extends TemporalLogicIntervalCollection {

  private NotIntervalCollection(IntervalCollection... collections) {
    super(collections);
  }

  public static NotIntervalCollection of(IntervalCollection... collections) {
    return new NotIntervalCollection(collections);
  }

  @Override
  public FluentIterator<Signal<Moment, Boolean>> bounds(Interval window, QueryDirection direction) {
    return TemporalLogicEventIterator.not(collections, window, direction);
  }

}
