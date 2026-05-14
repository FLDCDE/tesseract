package com.fieldcode.tesseract.collection;

import java.util.Iterator;
import java.util.function.BiFunction;

import com.fieldcode.tesseract.FluentIterator;
import com.fieldcode.tesseract.Interval;
import com.fieldcode.tesseract.Moment;
import com.fieldcode.tesseract.QueryDirection;
import com.fieldcode.tesseract.Signal;
import com.fieldcode.tesseract.utils.iterator.Iterators;
import com.fieldcode.tesseract.utils.iterator.StateIterator;

public class BoundariesToIntervalIterator extends StateIterator<Interval> {

  private final FluentIterator<Signal<Moment, Boolean>> source;
  private final BiFunction<Moment, Moment, Interval> mapper;

  private BoundariesToIntervalIterator(Iterator<Signal<Moment, Boolean>> source, QueryDirection direction) {
    this.source = Iterators.fluent(source);
    this.mapper = BoundsToIntervalMapper.of(direction);
  }

  public static FluentIterator<Interval> of(Iterator<Signal<Moment, Boolean>> source, QueryDirection direction) {
    return Iterators.fluent(new BoundariesToIntervalIterator(source, direction));
  }

  @Override
  protected void start() {
    source.next(this::start, this::end);
  }

  private void start(Signal<Moment, Boolean> next) {
    if (next.getValue()) {
      state(() -> findStop(next.getKey()));
    }
  }

  private void findStop(Moment start) {
    source.next(next -> stop(start, next), this::end);
  }

  private void stop(Moment start, Signal<Moment, Boolean> next) {
    if (source.isEmpty() || !next.getValue()) {
      var interval = mapper.apply(start, next.getKey());
      emit(interval);
      state(this::start);
    }
  }

}
