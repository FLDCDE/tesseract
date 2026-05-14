package com.fieldcode.tesseract.utils.iterator;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

import com.fieldcode.tesseract.FluentIterator;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Streams;

public class ExtendedIterator<T> implements FluentIterator<T> {

  static final FluentIterator<?> EMPTY_EXTENDED_ITERATOR_INSTANCE = Iterators.fluent(EmptyIterator.of());

  private final Iterator<T> delegate;

  ExtendedIterator(Iterator<T> delegate) {
    this.delegate = unwrap(delegate);
  }

  private static <T> Iterator<T> unwrap(Iterator<T> delegate) {
    // Unwraps ExtendedIterator for not storing wrappers
    return delegate instanceof ExtendedIterator
        ? ((ExtendedIterator<T>) delegate).delegate
        : delegate;
  }

  @Override
  public boolean hasNext() {
    return delegate.hasNext();
  }

  @Override
  public T next() {
    return delegate.next();
  }

  @Override
  public void forEach(Consumer<? super T> action) {
    stream().forEach(action);
  }

  @Override
  public FluentIterator<T> filter(Predicate<T> filter) {
    return Iterators.fluent(com.google.common.collect.Iterators.filter(delegate, filter::test));
  }

  @Override
  public <U> FluentIterator<U> map(Function<T, U> mapper) {
    return Iterators.fluent(com.google.common.collect.Iterators.transform(delegate, mapper::apply));
  }

  @Override
  public <R> FluentIterator<R> flatMap(Function<T, Iterator<R>> mapper) {
    return Iterators.fluent(FlatMapIterator.of(delegate, mapper));
  }

  @Override
  public <U> FluentIterator<U> successiveMap(BiFunction<T, T, U> mapper) {
    return Iterators.fluent(SuccessiveMapperIterator.of(delegate, mapper));
  }

  @Override
  public FluentIterator<T> limit(int n) {
    return Iterators.fluent(com.google.common.collect.Iterators.limit(delegate, n));
  }

  @Override
  public FluentIterator<T> skip(int n) {
    return Iterators.fluent(SkipIterator.of(delegate, n));
  }

  @Override
  public FluentIterator<T> takeWhile(Predicate<T> takeWhile) {
    return Iterators.fluent(TakeWhileIterator.of(delegate, takeWhile));
  }

  @Override
  public FluentIterator<T> dropWhile(Predicate<T> dropWhile) {
    return Iterators.fluent(DropWhileIterator.of(delegate, dropWhile));
  }

  @Override
  public Optional<T> findFirst() {
    return delegate.hasNext()
        ? Optional.of(delegate.next())
        : Optional.empty();
  }

  @Override
  public Stream<T> stream() {
    //noinspection UnstableApiUsage
    return Streams.stream(delegate);
  }

  @Override
  public List<T> list() {
    return stream()
        .collect(ImmutableList.toImmutableList());
  }

  @Override
  public Set<T> set() {
    return stream()
        .collect(ImmutableSet.toImmutableSet());
  }

  @Override
  public void next(Consumer<T> onNext, Runnable onEnd) {
    if (delegate.hasNext()) {
      onNext.accept(delegate.next());
    } else {
      onEnd.run();
    }
  }

  @Override
  public void next(Consumer<T> onNext) {
    if (delegate.hasNext()) {
      onNext.accept(delegate.next());
    }
  }

  @Override
  public boolean isEmpty() {
    return !delegate.hasNext();
  }

}
