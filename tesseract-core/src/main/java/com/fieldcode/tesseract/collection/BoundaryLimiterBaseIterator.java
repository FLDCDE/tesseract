package com.fieldcode.tesseract.collection;

import java.util.Iterator;

import com.fieldcode.tesseract.Interval;
import com.fieldcode.tesseract.Moment;
import com.fieldcode.tesseract.Signal;
import com.fieldcode.tesseract.utils.iterator.MultiEmmitStateIterator;
import com.google.common.collect.PeekingIterator;

import static com.google.common.collect.Iterators.peekingIterator;

public abstract class BoundaryLimiterBaseIterator extends MultiEmmitStateIterator<Signal<Moment, Boolean>> {

  protected final Moment lower;
  protected final Moment upper;

  private final PeekingIterator<Signal<Moment, Boolean>> input;

  private Signal<Moment, Boolean> known;

  public BoundaryLimiterBaseIterator(Iterator<Signal<Moment, Boolean>> input, Interval window, Signal<Moment, Boolean> known) {
    this.input = peekingIterator(input);
    this.lower = window.getLower();
    this.upper = window.getUpper();
    this.known = known;
  }

  /**
   * Returns the arrangement of the given moment in the interval.
   *
   * <pre>
   *
   *        |-------------|
   * -2    -1      0      1     2
   *
   * </pre>
   *
   * @param at the moment to be arranged
   * @return the arrangement of the given moment in the interval
   */
  protected int arrangement(Moment at) {

    if (at.lt(lower)) {
      return -2;
    }

    if (at.eq(lower)) {
      return -1;
    }

    if (at.gt(lower) && at.lt(upper)) {
      return 0;
    }

    if (at.eq(upper)) {
      return 1;
    }

    return 2;
  }

  protected void emitKnownAt(Moment moment) {
    emit(knownAt(moment));
  }

  protected void emitKnownAt(Moment moment1, Moment moment2) {
    emit(knownAt(moment1));
    emit(knownAt(moment2));
  }

  private Signal<Moment, Boolean> knownAt(Moment moment) {
    return known.withKey(moment);
  }

  protected void memorize(Signal<Moment, Boolean> known) {
    this.known = known;
  }

  protected boolean hasNoNextSignal() {
    return !input.hasNext();
  }

  protected Signal<Moment, Boolean> peekNextSignal() {
    return input.peek();
  }

  protected Signal<Moment, Boolean> popNextSignal() {
    return input.next();
  }

  protected void ignoreNextSignal() {
    input.next();
  }

}
