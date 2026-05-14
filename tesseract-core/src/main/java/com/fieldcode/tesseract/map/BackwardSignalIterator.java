package com.fieldcode.tesseract.map;

import java.util.NavigableMap;

import com.fieldcode.tesseract.FluentIterator;
import com.fieldcode.tesseract.Signal;
import com.fieldcode.tesseract.utils.iterator.Iterators;
import com.fieldcode.tesseract.utils.iterator.StateIterator;

public class BackwardSignalIterator<K extends Comparable<K>, V> extends StateIterator<Signal<K, V>> {

  private final K lower;
  private final K upper;
  private final TreeSignalMap<K, V> map;
  private final NavigableMap<K, InternalNode<K, V>> index;

  private BackwardSignalIterator(TreeSignalMap<K, V> map, K lower, K upper) {
    this.map = map;
    this.lower = lower;
    this.upper = upper;
    this.index = map.getIndex();
  }

  public static <K extends Comparable<K>, V> FluentIterator<Signal<K, V>> of(TreeSignalMap<K, V> map, K lower, K upper) {
    return Iterators.fluent(new BackwardSignalIterator<>(map, lower, upper));
  }

  @Override
  protected void start() {

    // Using direct index fetch to access IntervalNode
    var first = index
        .floorEntry(upper)
        .getValue();

    if (first.keyEqualsTo(upper)) {
      emit(first.getBackwardSignal().withKey(upper));
    } else {
      emit(first.withKey(upper));
    }

    var iterator = getIterator();
    state(() -> iterate(iterator));
  }

  private FluentIterator<InternalNode<K, V>> getIterator() {
    var iterator = index
        .subMap(lower, false, upper, false)
        .descendingMap()
        .values()
        .iterator();
    return Iterators.fluent(iterator);
  }

  private void iterate(FluentIterator<InternalNode<K, V>> iterator) {
    iterator.next(
        element -> emit(element.getBackwardSignal().withKey(element.getKey())),
        this::last
    );
  }

  private void last() {
    var last = index
        .floorEntry(lower)
        .getValue();

    if (last.keyEqualsTo(lower)) {
      emit(last.getBackwardSignal().withKey(lower));
    } else {
      emit(last.withKey(lower));
    }

    state(this::end);
  }

}
