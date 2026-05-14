package com.fieldcode.tesseract.utils.iterator;

import com.google.common.collect.AbstractIterator;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.function.Consumer;
import java.util.function.Function;

public abstract class MultiEmmitStateIterator<T> extends AbstractIterator<T> {

  private static final int MAX_STATE_ITERATIONS = 100_000;
  private final Queue<T> queue = new LinkedList<>();

  private boolean end;
  private Runnable state;

  @Override
  protected final T computeNext() {

    if (!queue.isEmpty()) {
      return queue.poll();
    }
    int stateIteration = MAX_STATE_ITERATIONS;
    while (true) {

      end = false;

      state.run();

      if (end && queue.isEmpty()) {
        endOfData();
        return null;
      }

      if (!queue.isEmpty()) {
        return queue.poll();
      }

      stateIteration -= 1;
      if (stateIteration <= 0) {
        throw new IllegalStateException("State interactions exceeded the limit.");
      }

    }
  }

  protected final void emit(T element) {
    queue.add(element);
  }

  protected final void state(Runnable state) {
    this.state = state;
  }

  protected final void end() {
    end = true;
    state = this::end;
  }

  protected void emitRemainingThan(Iterator<T> iterator, Runnable than) {
    state(() ->
        {
          if (iterator.hasNext()) {
            emit(iterator.next());
          } else {
            than.run();
          }
        }
    );

  }

  protected <R> void emitRemainingThan(Iterator<R> iterator, Function<R, T> mapper, Runnable than) {
    state(() ->
        {
          if (iterator.hasNext()) {
            emit(mapper.apply(iterator.next()));
          } else {
            than.run();
          }
        }
    );
  }

  protected <R> void callRemainingThenEnd(Iterator<R> iterator, Consumer<R> action) {
    state(() ->
        {
          if (iterator.hasNext()) {
            action.accept(iterator.next());
          } else {
            state(this::end);
          }
        }
    );
  }


}
