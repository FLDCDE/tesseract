package com.fieldcode.tesseract.utils.iterator;

import java.util.Iterator;
import java.util.function.BiFunction;

import com.fieldcode.tesseract.FluentIterator;

class SuccessiveMapperIterator<T, R> extends StateIterator<R> {

  private final FluentIterator<T> source;
  private final BiFunction<T, T, R> mapper;

  private SuccessiveMapperIterator(Iterator<T> source, BiFunction<T, T, R> mapper) {
    this.mapper = mapper;
    this.source = Iterators.fluent(source);
  }

  public static <T, R> Iterator<R> of(Iterator<T> source, BiFunction<T, T, R> mapper) {
    return new SuccessiveMapperIterator<>(source, mapper);
  }

  @Override
  protected void start() {
    source.next(this::selectPair, this::end);
  }

  private void selectPair(T current) {
    source.next(next -> emit(current, next), this::end);
  }

  private void emit(T first, T second) {
    var mapped = mapper.apply(first, second);
    emit(mapped);
    state(() -> selectPair(second));
  }

}
