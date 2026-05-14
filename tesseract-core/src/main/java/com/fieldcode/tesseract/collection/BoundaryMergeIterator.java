package com.fieldcode.tesseract.collection;

import java.util.Iterator;
import java.util.function.BiPredicate;

import com.fieldcode.tesseract.FluentIterator;
import com.fieldcode.tesseract.Moment;
import com.fieldcode.tesseract.QueryDirection;
import com.fieldcode.tesseract.Signal;
import com.fieldcode.tesseract.utils.iterator.Iterators;
import com.fieldcode.tesseract.utils.iterator.MultiEmmitStateIterator;
import com.google.common.collect.PeekingIterator;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.collect.Iterators.peekingIterator;

/**
 * An iterator that merges signals with the same key. The iterator will emit the last signal with the same key if the value of the signal changes.
 *
 * <h3>Examples</h3>
 *
 * <pre>
 *  Forward direction:
 *
 *  Given signals s = [{0,true}, {5,false}, {5,true}, {10,false}, {10,true}, {15,false}]  (this defines three connected intervals: [0..5), [5..10) and [10..15))
 *
 *  merge(s, FORWARD) => [{0,true},{15,false}]
 *
 *  Backward direction:
 *
 *  Given signals s = [{15,true}, {10,false}, {10,true}, {5,false}, {5,true}, {0,false}]  (this defines three connected intervals: [10..15), [5..10) and [0..5))
 *
 *  merge(s, BACKWARD) => [{15,true},{0,false}]
 *
 * </pre>
 *
 * This implementation assumes that the signals are ordered in a backward direction by keys. In case of wrong order an {@link IllegalArgumentException} will be thrown.
 */
public class BoundaryMergeIterator extends MultiEmmitStateIterator<Signal<Moment, Boolean>> {

  private final QueryDirection direction;
  private final BiPredicate<Moment, Moment> orderChecker;
  private final PeekingIterator<Signal<Moment, Boolean>> delegate;

  private BoundaryMergeIterator(Iterator<Signal<Moment, Boolean>> delegate, QueryDirection direction) {
    state(this::head);
    this.direction = direction;
    this.delegate = peekingIterator(delegate);
    this.orderChecker = direction.select(Moment::le, Moment::ge);
  }

  public static FluentIterator<Signal<Moment, Boolean>> fluent(Iterator<Signal<Moment, Boolean>> delegate, QueryDirection direction) {
    return Iterators.fluent(new BoundaryMergeIterator(delegate, direction));
  }

  public static Iterator<Signal<Moment, Boolean>> of(Iterator<Signal<Moment, Boolean>> delegate, QueryDirection direction) {
    return new BoundaryMergeIterator(delegate, direction);
  }

  private void head() {
    if (!delegate.hasNext()) {
      end();
      return;
    }

    var next = getLastWithSameKey();
    state(() -> tail(next));
  }


  private void tail(Signal<Moment, Boolean> known) {
    if (!delegate.hasNext()) {
      emit(known);
      end();
      return;
    }

    var current = getLastWithSameKey();

    checkArgument(
        orderChecker.test(known.getKey(), current.getKey()),
        "The signals are not in the correct order [direction=%s, known=%s, current=%s]", direction, known, current
    );

    if (!current.valueEqualsTo(known.getValue())) {
      emit(known);
      state(() -> tail(current));
    }

  }

  private Signal<Moment, Boolean> getLastWithSameKey() {
    var current = delegate.next();

    if (delegate.hasNext() && current.keyEqualsTo(delegate.peek().getKey())) {
      return getLastWithSameKey();
    }
    return current;
  }

}
