package com.fieldcode.tesseract.utils.iterator;

import com.google.common.collect.AbstractIterator;

import java.util.Iterator;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import static com.google.common.base.Preconditions.checkState;

public abstract class StateIterator<T> extends AbstractIterator<T> {

  private static final int MAX_STATE_ITERATIONS = 100_000;
  private T next;
  private boolean end;
  private Runnable state = this::start;

  protected StateIterator() {
  }

  @Override
  protected final T computeNext() {

    int stateIteration = MAX_STATE_ITERATIONS;
    while (true) {
      end = false;
      next = null;

      state.run();

      if (end) {
        endOfData();
        return null;
      }

      if (next != null) {
        return next;
      }

      stateIteration -= 1;

      checkState(stateIteration >= 0, "State iterations exceeded the limit. [limit=%d]", MAX_STATE_ITERATIONS);
    }
  }

  protected void start() {
    throw new IllegalStateException("No start state defined. Override start() method or define state in constructor.");
  }

  protected final void emit(T element) {
    checkState(next == null, "Multi emitting is not allowed");
    next = element;
  }

  protected final void state(Runnable state) {
    this.state = state;
  }

  protected final void end() {
    end = true;
    next = null;
    state = null;
  }

  protected <I> void onNextOrEnd(Iterator<I> iterator, Consumer<I> hasNext) {
    if (iterator.hasNext()) {
      hasNext.accept(iterator.next());
    } else {
      end();
    }
  }

  protected <I> void onNext(Iterator<I> iterator, Consumer<I> hasNext, Runnable hasNoNext) {
    if (iterator.hasNext()) {
      hasNext.accept(iterator.next());
    } else {
      hasNoNext.run();
    }
  }

  protected <I, R> R applyNextOrEnd(Iterator<I> iterator, Function<I, R> mapper, Supplier<R> defaultValue) {
    if (iterator.hasNext()) {
      return mapper.apply(iterator.next());
    } else {
      end();
      return defaultValue.get();
    }
  }

}
