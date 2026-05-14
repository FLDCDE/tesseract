package com.fieldcode.tesseract.utils.iterator;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class EmptyIterator<T> implements Iterator<T> {

  static final EmptyIterator<?> EMPTY_ITERATOR_INSTANCE = new EmptyIterator<>();

  private EmptyIterator() {
  }

  public static <T> Iterator<T> of() {
    //noinspection unchecked
    return (EmptyIterator<T>) EMPTY_ITERATOR_INSTANCE; // Can be cast because has no state
  }

  @Override
  public boolean hasNext() {
    return false;
  }

  @Override
  public T next() {
    throw new NoSuchElementException();
  }

}
