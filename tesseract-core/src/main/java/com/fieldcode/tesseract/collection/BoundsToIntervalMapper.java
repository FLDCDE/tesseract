package com.fieldcode.tesseract.collection;

import java.util.function.BiFunction;

import com.fieldcode.tesseract.Interval;
import com.fieldcode.tesseract.Moment;
import com.fieldcode.tesseract.QueryDirection;
import com.fieldcode.tesseract.interval.Intervals;

abstract class BoundsToIntervalMapper implements BiFunction<Moment, Moment, Interval> {

  private static final BoundsToIntervalMapper FORWARD_INSTANCE = new BoundsToIntervalMapper() {
    @Override
    public Interval apply(Moment a, Moment b) {
      return Intervals.interval(a, b);
    }
  };

  private static final BoundsToIntervalMapper BACKWARD_INSTANCE = new BoundsToIntervalMapper() {
    @Override
    public Interval apply(Moment a, Moment b) {
      return Intervals.interval(b, a);
    }
  };

  private BoundsToIntervalMapper() {}

  static BiFunction<Moment, Moment, Interval> of(QueryDirection direction) {
    return direction.select(FORWARD_INSTANCE, BACKWARD_INSTANCE);
  }

}
