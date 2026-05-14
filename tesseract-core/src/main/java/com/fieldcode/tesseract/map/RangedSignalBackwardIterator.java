package com.fieldcode.tesseract.map;

import java.util.Iterator;
import java.util.NavigableMap;

import com.fieldcode.tesseract.FluentIterator;
import com.fieldcode.tesseract.Signal;
import com.fieldcode.tesseract.utils.iterator.Iterators;

public class RangedSignalBackwardIterator<K extends Comparable<K>, V> extends RangedSignalIterator<K, V> {

  private RangedSignalBackwardIterator(NavigableMap<K, InternalNode<K, V>> map, K lower, K upper) {
    super(map, lower, upper);
  }

  public static <K extends Comparable<K>, V> FluentIterator<Signal<K, V>> of(NavigableMap<K, InternalNode<K, V>> map, K lower, K upper) {
    return Iterators.fluent(new RangedSignalBackwardIterator<>(map, lower, upper));
  }

  @Override
  protected void first() {
    emit(resolve(upper));
    var intermediateIterator = intermediateIterator();
    state(() -> intermediate(intermediateIterator));
  }

  @Override
  protected void last() {
    emit(resolve(lower));
    state(this::end);
  }

  protected Iterator<InternalNode<K, V>> intermediateIterator() {
    return range()
        .descendingMap()
        .values()
        .iterator();
  }
}
