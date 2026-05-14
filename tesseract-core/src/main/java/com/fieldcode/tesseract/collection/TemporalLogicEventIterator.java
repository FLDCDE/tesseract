package com.fieldcode.tesseract.collection;

import com.fieldcode.tesseract.FluentIterator;
import com.fieldcode.tesseract.Interval;
import com.fieldcode.tesseract.IntervalCollection;
import com.fieldcode.tesseract.Moment;
import com.fieldcode.tesseract.QueryDirection;
import com.fieldcode.tesseract.Signal;
import com.fieldcode.tesseract.signal.Signals;
import com.fieldcode.tesseract.utils.iterator.Iterators;
import com.fieldcode.tesseract.utils.iterator.StateIterator;

import java.util.List;
import java.util.function.BiPredicate;

class TemporalLogicEventIterator extends StateIterator<Signal<Moment, Boolean>> {

  private static final BiPredicate<Integer, Integer> OR_START = (n, c) -> c != 0;
  private static final BiPredicate<Integer, Integer> OR_STOP = (n, c) -> c == 0;
  private static final BiPredicate<Integer, Integer> AND_START = Integer::equals;
  private static final BiPredicate<Integer, Integer> AND_STOP = (n, c) -> !n.equals(c);
  private static final BiPredicate<Integer, Integer> NOT_START = (n, c) -> c == 0;
  private static final BiPredicate<Integer, Integer> NOT_STOP = (n, c) -> c != 0;
  private static final BiPredicate<Integer, Integer> EXCLUSIVE_START = (n, c) -> c == 1;
  private static final BiPredicate<Integer, Integer> EXCLUSIVE_STOP = (n, c) -> c != 1;

  private final int n;
  private final BiPredicate<Integer, Integer> isStart;
  private final BiPredicate<Integer, Integer> isStop;
  private final FluentIterator<Signal<Moment, Integer>> counted;

  protected TemporalLogicEventIterator(
      List<? extends IntervalCollection> collections,
      Interval window,
      QueryDirection direction,
      BiPredicate<Integer, Integer> isStart,
      BiPredicate<Integer, Integer> isStop
  ) {
    this.n = collections.size();
    this.isStart = isStart;
    this.isStop = isStop;
    this.counted = EventCounterIterator.of(collections, window, direction);
  }

  private static FluentIterator<Signal<Moment, Boolean>> create(
      List<? extends IntervalCollection> collections,
      Interval window,
      QueryDirection direction,
      BiPredicate<Integer, Integer> isStart,
      BiPredicate<Integer, Integer> isStop
  ) {
    return Iterators.fluent(new TemporalLogicEventIterator(collections, window, direction, isStart, isStop));
  }

  static FluentIterator<Signal<Moment, Boolean>> or(
      List<? extends IntervalCollection> collections,
      Interval window,
      QueryDirection direction
  ) {
    return create(collections, window, direction, OR_START, OR_STOP);
  }

  static FluentIterator<Signal<Moment, Boolean>> and(
      List<? extends IntervalCollection> collections,
      Interval window,
      QueryDirection direction
  ) {
    return create(collections, window, direction, AND_START, AND_STOP);
  }

  static FluentIterator<Signal<Moment, Boolean>> not(
      List<? extends IntervalCollection> collections,
      Interval window,
      QueryDirection direction
  ) {
    return create(collections, window, direction, NOT_START, NOT_STOP);
  }

  static FluentIterator<Signal<Moment, Boolean>> exclusive(
      List<? extends IntervalCollection> collections,
      Interval window,
      QueryDirection direction
  ) {
    return create(collections, window, direction, EXCLUSIVE_START, EXCLUSIVE_STOP);
  }

  @Override
  protected void start() {
    onNextOrEnd(counted, this::firstStart);
  }

  private void firstStart(Signal<Moment, Integer> next) {
    var at = next.getKey();
    if (isStart(next)) {
      emit(at, true);
      state(this::findStop);
    } else {
      // For preserving the boundaries
      emit(at, false);
      state(this::findStart);
    }
  }

  private void findStart() {
    onNextOrEnd(counted, this::start);
  }

  private void start(Signal<Moment, Integer> next) {
    var at = next.getKey();
    if (isStart(next)) {
      emit(at, true);
      state(this::findStop);
    } else if (hasNoMore()) {
      // For preserving the boundaries
      emit(at, false);
    }
  }

  private void findStop() {
    onNextOrEnd(counted, this::stop);
  }

  private void stop(Signal<Moment, Integer> next) {
    if (isStop(next) || hasNoMore()) {
      emit(next.getKey(), false);
      state(this::findStart);
    }
  }

  private boolean isStart(Signal<Moment, Integer> next) {
    return isStart.test(n, next.getValue());
  }

  private boolean isStop(Signal<Moment, Integer> stop) {
    return isStop.test(n, stop.getValue());
  }

  private boolean hasNoMore() {
    return !counted.hasNext();
  }

  private void emit(Moment at, boolean start) {
    var signal = Signals.signal(at, start);
    emit(signal);
  }
}


