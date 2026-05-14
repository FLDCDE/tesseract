package com.fieldcode.tesseract.collection;

import java.util.Comparator;

import com.fieldcode.tesseract.Moment;
import com.fieldcode.tesseract.QueryDirection;
import com.fieldcode.tesseract.Signal;

abstract class IntervalBoundComparator implements Comparator<Signal<Moment, Boolean>> {

  private static final IntervalBoundComparator FORWARD = new IntervalBoundComparator() {
    @Override
    public int compare(Signal<Moment, Boolean> a, Signal<Moment, Boolean> b) {
      return a.compareTo(b);
    }
  };

  private static final IntervalBoundComparator BACKWARD = new IntervalBoundComparator() {
    @Override
    public int compare(Signal<Moment, Boolean> a, Signal<Moment, Boolean> b) {
      return b.compareTo(a);
    }
  };

  private IntervalBoundComparator() {}

  public static IntervalBoundComparator of(QueryDirection direction) {
    return direction.select(FORWARD, BACKWARD);
  }

}
