package com.fieldcode.tesseract.utils.iterator;

import java.util.Iterator;
import java.util.function.Predicate;

public class DropWhileIterator<T> extends StateIterator<T> {

  private final Iterator<T> delegate;
  private final Predicate<T> dropWhile;

  private DropWhileIterator(Iterator<T> delegate, Predicate<T> dropWhile) {
    this.delegate = delegate;
    this.dropWhile = dropWhile;
  }

  public static <T> DropWhileIterator<T> of(Iterator<T> delegate, Predicate<T> dropWhile) {
    return new DropWhileIterator<>(delegate, dropWhile);
  }

  @Override
  protected void start() {
    while (delegate.hasNext()) {
      var next = delegate.next();
      if (!dropWhile.test(next)) {
        emit(next);
        state(this::tail);
        return;
      }
    }
    end();
  }

  private void tail() {
    if (delegate.hasNext()) {
      emit(delegate.next());
    } else {
      end();
    }
  }
}
