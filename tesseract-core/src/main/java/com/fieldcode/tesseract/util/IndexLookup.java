package com.fieldcode.tesseract.util;

import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicInteger;

public class IndexLookup<T extends Comparable<T>> {

  private final Map<T, Integer> indexMap = new TreeMap<>();

  private IndexLookup(Collection<T> input) {
    var index = new AtomicInteger();
    input.forEach(t -> indexMap.put(t, index.getAndIncrement()));
  }

  public static <T extends Comparable<T>> IndexLookup<T> of(Collection<T> input) {
    return new IndexLookup<>(input);
  }

  public int indexOf(T element) {
    var index = indexMap.get(element);
    if (index == null) {
      throw new IllegalArgumentException("Element not found: " + element);
    }
    return index;
  }

}
