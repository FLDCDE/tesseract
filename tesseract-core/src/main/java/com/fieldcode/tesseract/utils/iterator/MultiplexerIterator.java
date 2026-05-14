package com.fieldcode.tesseract.utils.iterator;

import com.google.common.collect.AbstractIterator;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

public class MultiplexerIterator<T> extends AbstractIterator<List<T>> {

  private final int n;
  private final List<T> elements;
  private final Comparator<T> comparator;
  private final List<? extends Iterator<T>> sources;

  private T highWaterMark;

  private MultiplexerIterator(List<? extends Iterator<T>> sources, Comparator<T> comparator) {
    this.n = sources.size();
    this.sources = sources;
    this.comparator = (a, b) -> a == null || b == null ? 1 : comparator.compare(a, b);
    this.elements = new ArrayList<>(n);

    for (int index = 0; index < n; index++) {
      elements.add(null);
      fill(index);
    }
  }

  static <T> Iterator<List<T>> of(List<? extends Iterator<T>> sources, Comparator<T> comparator) {
    return new MultiplexerIterator<>(sources, comparator);
  }

  private void fill(int index) {
    var iterator = sources.get(index);
    if (iterator.hasNext()) {
      elements.set(index, iterator.next());
    } else {
      elements.set(index, null);
    }
  }

  @Override
  protected List<T> computeNext() {
    var nextGroup = getNextGroup();
    if (nextGroup == null) {
      endOfData();
      return null;
    }
    if (!nextGroup.isEmpty() && comparator.compare(nextGroup.get(0), highWaterMark) < 0) {
      throw new IllegalStateException("Iterators must be ordered!");
    }
    if (!nextGroup.isEmpty()) {
      highWaterMark = nextGroup.get(0);
    }

    return nextGroup;
  }

  private List<T> getNextGroup() {
    T next = null;
    // Find next by comparing element using comparator
    for (int i = 0; i < n; i++) {
      var current = elements.get(i);
      if (next == null || comparator.compare(current, next) < 0) {
        next = current;
      }
    }

    if (next == null) {
      return null;
    }

    var groups = new ArrayList<T>();

    // Find group members
    for (int i = 0; i < n; i++) {
      var current = elements.get(i);
      if (comparator.compare(current, next) == 0) {
        groups.add(current);
        fill(i);
      }
    }

    return groups;
  }

}
