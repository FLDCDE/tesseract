package com.fieldcode.tesseract.utils.iterator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

import com.fieldcode.tesseract.FluentIterator;

import static com.fieldcode.tesseract.utils.iterator.EmptyIterator.EMPTY_ITERATOR_INSTANCE;

public class Iterators {

  private Iterators() {}

  public static <T> ExtendedIteratorFactoryBuilder<T, T> factoryBuilder() {
    return ExtendedIteratorFactoryBuilder.of();
  }

  public static <T> ExtendedIteratorFactoryBuilder<T, T> factoryBuilder(Class<T> type) {
    return ExtendedIteratorFactoryBuilder.of();
  }


  public static <T> FluentIterator<T> fluent(Iterator<T> delegate) {
    return new ExtendedIterator<>(delegate);
  }

  public static <T> FluentIterator<T> iterator(T single) {
    return fluent(com.google.common.collect.Iterators.singletonIterator(single));
  }

  @SafeVarargs
  public static <T> FluentIterator<T> iterator(T first, T... elements) {
    var all = new ArrayList<T>();
    all.add(first);
    all.addAll(Arrays.asList(elements));
    return iterator(all);
  }

  public static <T> FluentIterator<T> iterator(Iterable<T> iterable) {
    return fluent(iterable.iterator());
  }

  public static <T> FluentIterator<T> chain(T seed, UnaryOperator<T> next) {
    return fluent(ChainIterator.of(seed, next));
  }

  public static <T, R> FluentIterator<R> chain(T seed, UnaryOperator<T> next, Function<T, R> mapper) {
    return fluent(ChainIterator.of(seed, next, mapper));
  }

  public static <T, R> FluentIterator<R> mapSuccessive(Iterator<T> source, BiFunction<T, T, R> mapper) {
    return Iterators.fluent(SuccessiveMapperIterator.of(source, mapper));
  }

  @SafeVarargs
  public static <T> FluentIterator<T> concat(Iterator<T>... iterators) {
    return fluent(com.google.common.collect.Iterators.concat(iterators));
  }

  public static <T> FluentIterator<List<T>> multiplex(List<? extends Iterator<T>> sources, Comparator<T> comparator) {
    return fluent(MultiplexerIterator.of(sources, comparator));
  }

  public static <T> FluentIterator<T> empty() {
    //noinspection unchecked
    return (FluentIterator<T>) ExtendedIterator.EMPTY_EXTENDED_ITERATOR_INSTANCE;
  }

  public static <T> FluentIterator<T> lazy(List<Supplier<Iterator<T>>> suppliers) {
    return fluent(CompositeLazyIterator.of(suppliers));
  }

  public static <T> FluentIterator<T> lazy(Supplier<Iterator<T>> supplier) {
    return fluent(CompositeLazyIterator.of(supplier));
  }

  public static <T> boolean instanceOfEmpty(Iterator<T> iterator) {
    return iterator == ExtendedIterator.EMPTY_EXTENDED_ITERATOR_INSTANCE || iterator == EMPTY_ITERATOR_INSTANCE;
  }

}
