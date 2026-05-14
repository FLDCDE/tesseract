package com.fieldcode.tesseract.collection;

import java.util.Iterator;

import com.fieldcode.tesseract.Moment;
import com.fieldcode.tesseract.Signal;
import com.fieldcode.tesseract.utils.iterator.StateIterator;

public class ComplementIntervalSignalIterator extends StateIterator<Signal<Moment, Boolean>> {

  private final Iterator<Signal<Moment, Boolean>> source;

  private ComplementIntervalSignalIterator(Iterator<Signal<Moment, Boolean>> source) {
    this.source = source;
  }

  public static ComplementIntervalSignalIterator of(Iterator<Signal<Moment, Boolean>> source) {
    return new ComplementIntervalSignalIterator(source);
  }

  @Override
  protected void start() {
    onNextOrEnd(source, next -> emit(next.withValue(!next.getValue())));
  }

}
