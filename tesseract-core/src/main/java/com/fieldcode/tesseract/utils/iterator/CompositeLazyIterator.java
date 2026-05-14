package com.fieldcode.tesseract.utils.iterator;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.function.Supplier;

import static java.util.Objects.requireNonNull;

public class CompositeLazyIterator<T> implements Iterator<T> {

  private final Queue<Supplier<Iterator<T>>> queue;

  public Iterator<T> current;

  private CompositeLazyIterator(List<Supplier<Iterator<T>>> suppliers) {
    this.queue = new LinkedList<>(suppliers);
    this.queue.add(Iterators::empty);
    this.current = queue.poll().get();  // Queue always has at least one element
  }

  static <T> Iterator<T> of(Supplier<Iterator<T>> supplier) {
    return of(List.of(supplier));
  }

  static <T> Iterator<T> of(List<Supplier<Iterator<T>>> suppliers) {
    if (suppliers.isEmpty()) {
      return Iterators.empty();
    }

    return new CompositeLazyIterator<>(suppliers);
  }

  @Override
  public boolean hasNext() {
    var hasNext = current.hasNext();
    if (hasNext || queue.isEmpty()) {
      return hasNext;
    }
    current = requireNonNull(queue.poll()).get();
    return hasNext();
  }

  @Override
  public T next() {
    return current.next();
  }

}
