package com.fieldcode.tesseract.map;

import java.util.Iterator;
import java.util.NavigableMap;

import com.fieldcode.tesseract.Signal;
import com.fieldcode.tesseract.utils.iterator.StateIterator;

import static com.google.common.base.Preconditions.checkArgument;

public abstract class RangedSignalIterator<K extends Comparable<K>, V> extends StateIterator<Signal<K, V>> {

  protected final NavigableMap<K, InternalNode<K, V>> map;
  protected final K lower;
  protected final K upper;

  public RangedSignalIterator(NavigableMap<K, InternalNode<K, V>> map, K lower, K upper) {
    checkArgument(lower.compareTo(upper) < 0, "Lower must be less than upper [lower=%s, upper=%s]", lower, upper);
    this.map = map;
    this.lower = lower;
    this.upper = upper;
    state(this::first);
  }

  protected Signal<K, V> resolve(K key) {
    return map.floorEntry(key)
        .getValue()
        .withKey(key);
  }

  @Override
  protected void start() {
    first();
  }

  protected NavigableMap<K, InternalNode<K, V>> range() {
    return map.subMap(lower, false, upper, false);
  }

  protected void intermediate(Iterator<InternalNode<K, V>> iterator) {

    if (iterator.hasNext()) {
      emit(iterator.next().toImmutable());
    } else {
      state(this::last);
    }

  }

  protected abstract void first();

  protected abstract void last();
}
