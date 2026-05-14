package com.fieldcode.tesseract.collection;

import java.util.Iterator;

import com.fieldcode.tesseract.FluentIterator;
import com.fieldcode.tesseract.Interval;
import com.fieldcode.tesseract.Moment;
import com.fieldcode.tesseract.QueryDirection;
import com.fieldcode.tesseract.Signal;
import com.fieldcode.tesseract.signal.Signals;
import com.fieldcode.tesseract.utils.iterator.Iterators;
import com.fieldcode.tesseract.utils.iterator.MultiEmmitStateIterator;

public class IntervalToBoundaryIterator extends MultiEmmitStateIterator<Signal<Moment, Boolean>> {

  private IntervalToBoundaryIterator(Iterator<Interval> delegate, QueryDirection direction) {

    callRemainingThenEnd(
        delegate,
        direction.select(this::forward, this::backward)
    );

  }

  public static IntervalToBoundaryIterator of(Iterator<Interval> delegate, QueryDirection direction) {
    return new IntervalToBoundaryIterator(delegate, direction);
  }

  public static FluentIterator<Signal<Moment, Boolean>> fluent(Iterator<Interval> delegate, QueryDirection direction) {
    return Iterators.fluent(of(delegate, direction));
  }

  private void forward(Interval interval) {

    var lower = interval.getLower();
    var upper = interval.getUpper();

    emit(eventStart(lower));
    emit(eventEnd(upper));

  }

  private void backward(Interval interval) {
    var lower = interval.getLower();
    var upper = interval.getUpper();

    emit(eventStart(upper));
    emit(eventEnd(lower));
  }

  private Signal<Moment, Boolean> eventStart(Moment moment) {
    return Signals.signal(moment, true);
  }

  private Signal<Moment, Boolean> eventEnd(Moment moment) {
    return Signals.signal(moment, false);
  }

}
