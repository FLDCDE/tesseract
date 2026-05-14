package com.fieldcode.tesseract.utils.iterator;

import java.util.Iterator;
import java.util.function.Function;
import java.util.function.Predicate;

import com.fieldcode.tesseract.FluentIterator;

import static java.util.function.Function.identity;

public class ExtendedIteratorFactoryBuilder<T, R> {

  private final Function<Iterator<T>, Iterator<R>> factory;

  private ExtendedIteratorFactoryBuilder(Function<Iterator<T>, Iterator<R>> factory) {
    this.factory = factory;
  }

  static <T> ExtendedIteratorFactoryBuilder<T, T> of() {
    return new ExtendedIteratorFactoryBuilder<>(identity());
  }

  static <T> ExtendedIteratorFactoryBuilder<T, T> of(Class<T> inputType) {
    return new ExtendedIteratorFactoryBuilder<>(identity());
  }

  public ExtendedIteratorFactoryBuilder<T, R> filter(Predicate<R> predicate) {
    return new ExtendedIteratorFactoryBuilder<>(
        iterator -> com.google.common.collect.Iterators.filter(factory.apply(iterator), predicate::test)
    );
  }

  public ExtendedIteratorFactoryBuilder<T, R> takeWhile(Predicate<R> takeWhile) {
    return new ExtendedIteratorFactoryBuilder<>(
        iterator -> TakeWhileIterator.of(factory.apply(iterator), takeWhile)
    );
  }

  public <U> ExtendedIteratorFactoryBuilder<T, U> map(Function<R, U> mapper) {
    return new ExtendedIteratorFactoryBuilder<>(
        iterator -> com.google.common.collect.Iterators.transform(factory.apply(iterator), mapper::apply)
    );
  }

  public ExtendedIteratorFactoryBuilder<T, R> limit(int limit) {
    return new ExtendedIteratorFactoryBuilder<>(
        iterator -> com.google.common.collect.Iterators.limit(factory.apply(iterator), limit)
    );
  }

  public Function<Iterator<T>, FluentIterator<R>> factory() {
    return iterator -> Iterators.instanceOfEmpty(iterator)
        ? Iterators.empty()
        : Iterators.fluent(factory.apply(iterator));
  }

}
