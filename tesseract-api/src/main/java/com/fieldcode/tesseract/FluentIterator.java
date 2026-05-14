package com.fieldcode.tesseract;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * A fluent iterator add functionality to the standard iterator similar to the stream API.
 *
 * @param <T> the type of the elements
 */
public interface FluentIterator<T> extends Iterator<T> {

  /**
   * Iterates over the elements of this iterator and applies the action to each element.
   *
   * @param action the action to apply
   */
  void forEach(Consumer<? super T> action);

  /**
   * Returns a new iterator that filters the elements of this iterator.
   *
   * @param filter the filter to apply
   * @return a new iterator
   */
  FluentIterator<T> filter(Predicate<T> filter);

  /**
   * Returns a new iterator that map the elements of this iterator.
   *
   * @param mapper the mapper to apply
   * @return a new iterator
   */
  <U> FluentIterator<U> map(Function<T, U> mapper);

  /**
   * Returns a new iterator that flatMap the elements of this iterator.
   *
   * @param mapper the mapper to apply
   * @return a new iterator
   */
  <R> FluentIterator<R> flatMap(Function<T, Iterator<R>> mapper);

  /**
   * Returns a new iterator that map the elements of this iterator based on a Bifunction. This mapper can be used for successive mapping.
   *
   * @param mapper the mapper to apply
   * @return a new iterator
   */
  <U> FluentIterator<U> successiveMap(BiFunction<T, T, U> mapper);

  /**
   * Returns a new iterator that limits the number of elements of this iterator. If the number of elements is less than the limit, all elements are returned. If the number of
   * elements is greater than the limit, only the first n elements are returned. If the number of elements is equal to the limit, all elements are returned.
   *
   * @param n the limit
   * @return a new iterator
   */
  FluentIterator<T> limit(int n);

  /**
   * Returns a new iterator that skips the first n elements of this iterator. If the number of elements is less than n, an empty iterator is returned.
   * @param n the number of elements to skip
   * @return a new iterator
   */
  FluentIterator<T> skip(int n) ;

  /**
   * Returns a new iterator that takes the elements of this iterator while the predicate is true. If the number of elements is less than 1, an empty iterator is returned.
   *
   * @param takeWhile the predicate to apply
   * @return a new iterator
   */
  FluentIterator<T> takeWhile(Predicate<T> takeWhile);

  /**
   * Returns a new iterator that drops the elements of this iterator while the predicate is true. If the number of elements is less than 1, an empty iterator is returned. not
   * implemented yet
   *
   * @param dropWhile the predicate to apply
   * @return a new iterator
   */
  FluentIterator<T> dropWhile(Predicate<T> dropWhile);

  /**
   * Returns a new iterator that skips the first element of this iterator. If the number of elements is less than 1, an empty iterator is returned.
   *
   * @return a new iterator
   */
  Optional<T> findFirst();

  /**
   * Converts this iterator to a stream.
   *
   * @return a stream
   */
  Stream<T> stream();

  /**
   * Converts this iterator to a list.
   *
   * @return a list
   */
  List<T> list();

  /**
   * Converts this iterator to a set.
   *
   * @return a set
   */
  Set<T> set();

  /**
   * If the iterator has a next element, it is passed to the consumer. If the iterator has no next element, the end runnable is called.
   *
   * @param onNext the consumer to call
   */
  void next(Consumer<T> onNext, Runnable onEnd);

  /**
   * If the iterator has a next element, it is passed to the consumer.
   *
   * @param onNext the consumer to call
   */
  void next(Consumer<T> onNext);

  /**
   * Returns true if the iterator has no elements.
   *
   * @return true if the iterator has no elements
   */
  boolean isEmpty();
}
