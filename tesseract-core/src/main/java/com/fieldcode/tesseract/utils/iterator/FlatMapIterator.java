package com.fieldcode.tesseract.utils.iterator;

import java.util.Iterator;
import java.util.function.Function;

public class FlatMapIterator<T, R> extends StateIterator<R> {

  private final Iterator<T> delegate;
  private final Function<T, Iterator<R>> mapper;
  private Iterator<R> subIterator;

  private FlatMapIterator(Iterator<T> delegate, Function<T, Iterator<R>> mapper) {
    this.delegate = delegate;
    this.mapper = mapper;
  }

  public static <T, R> Iterator<R> of(Iterator<T> delegate, Function<T, Iterator<R>> mapper) {
    return new FlatMapIterator<>(delegate, mapper);
  }

  @Override
  protected void start() {
    if (delegate.hasNext()) {
      subIterator = mapper.apply(delegate.next());
      state(this::iterateSub);
      iterateSub();
    } else {
      end();
    }
  }

  private void iterateSub() {
    if (subIterator.hasNext()) {
      var next = subIterator.next();
      emit(next);
    } else {
      start();
    }
  }
}
