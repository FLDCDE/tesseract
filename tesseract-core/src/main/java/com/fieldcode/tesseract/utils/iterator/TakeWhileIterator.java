package com.fieldcode.tesseract.utils.iterator;

import java.util.Iterator;
import java.util.function.Predicate;

public class TakeWhileIterator<T> extends StateIterator<T> {

  private final Iterator<T> delegate;
  private final Predicate<T> takeWhile;

  private TakeWhileIterator(Iterator<T> delegate, Predicate<T> takeWhile) {
    this.delegate = delegate;
    this.takeWhile = takeWhile;
  }

  public static <T> Iterator<T> of(Iterator<T> delegate, Predicate<T> takeWhile) {
    return new TakeWhileIterator<>(delegate, takeWhile);
  }

  @Override
  protected void start() {
    if (!delegate.hasNext()) {
      end();
      return;
    }

    var next = delegate.next();

    if (takeWhile.test(next)) {
      emit(next);
    } else {
      end();
    }

  }
}
