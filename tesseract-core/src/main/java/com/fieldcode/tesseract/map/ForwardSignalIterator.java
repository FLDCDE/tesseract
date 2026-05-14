package com.fieldcode.tesseract.map;

import java.util.NavigableMap;

import com.fieldcode.tesseract.FluentIterator;
import com.fieldcode.tesseract.Signal;
import com.fieldcode.tesseract.utils.iterator.Iterators;
import com.fieldcode.tesseract.utils.iterator.StateIterator;

public class ForwardSignalIterator<K extends Comparable<K>, V> extends StateIterator<Signal<K, V>> {

  private final K lower;
  private final K upper;
  private final TreeSignalMap<K, V> map;
  private final NavigableMap<K, InternalNode<K, V>> index;

  private ForwardSignalIterator(TreeSignalMap<K, V> map, K lower, K upper) {
    this.map = map;
    this.lower = lower;
    this.upper = upper;
    this.index = map.getIndex();
  }

  public static <K extends Comparable<K>, V> FluentIterator<Signal<K, V>> of(TreeSignalMap<K, V> map, K lower, K upper) {
    return Iterators.fluent(new ForwardSignalIterator<>(map, lower, upper));
  }

  @Override
  protected void start() {
    var first = map.get(lower).withKey(lower);
    emit(first);
    var iterator = getIterator();
    state(() -> iterate(iterator));
  }

  private FluentIterator<InternalNode<K, V>> getIterator() {
    var iterator = index
        .subMap(lower, false, upper, false)
        .values()
        .iterator();
    return Iterators.fluent(iterator);
  }

  private void iterate(FluentIterator<InternalNode<K, V>> iterator) {
    iterator.next(this::emit, this::last);
  }

  private void last() {
    var last = map.get(upper).withKey(upper);
    emit(last);
    state(this::end);
  }

}
