package com.fieldcode.tesseract.utils.iterator;

import java.util.Iterator;
import java.util.function.Function;
import java.util.function.UnaryOperator;

public class ChainIterator<T, R> extends StateIterator<R> {

  private final Function<T, R> mapper;
  private final UnaryOperator<T> next;

  private T current;

  private ChainIterator(T seed, UnaryOperator<T> next, Function<T, R> mapper) {
    this.next = next;
    this.current = seed;
    this.mapper = mapper;
  }

  public static <T, R> Iterator<R> of(T seed, UnaryOperator<T> next, Function<T, R> mapper) {
    return new ChainIterator<>(seed, next, mapper);
  }

  public static <T> Iterator<T> of(T seed, UnaryOperator<T> next) {
    return new ChainIterator<>(seed, next, Function.identity());
  }

  @Override
  protected void start() {
    if (current != null) {
      emit(mapper.apply(current));
      current = next.apply(current);
    } else {
      end();
    }
  }

}
