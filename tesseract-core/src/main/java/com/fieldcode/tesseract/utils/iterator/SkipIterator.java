package com.fieldcode.tesseract.utils.iterator;

import java.util.Iterator;

import static com.google.common.base.Preconditions.checkArgument;

public class SkipIterator<T> extends MultiEmmitStateIterator<T> {

  private SkipIterator(Iterator<T> delegate, int n) {
    state(() -> start(delegate, n));
  }

  public static <T> Iterator<T> of(Iterator<T> delegate, int n) {
    checkArgument(n >= 0, "n must be >= 0");
    if (n == 0) {return delegate;}
    return new SkipIterator<>(delegate, n);
  }

  protected void start(Iterator<T> delegate, int n) {
    for (int i = 0; i < n; i++) {
      if (!delegate.hasNext()) {
        end();
        return;
      }
      delegate.next();
    }
    emitRemainingThan(delegate, this::end);
  }

}
