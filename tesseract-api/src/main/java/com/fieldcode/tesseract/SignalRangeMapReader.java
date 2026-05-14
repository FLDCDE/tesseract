package com.fieldcode.tesseract;

public interface SignalRangeMapReader<K extends Comparable<K>, V> {

  NavigableSignalRange<K, V> get(K key);

  NavigableSignalRange<K, V> getFirst();

  NavigableSignalRange<K, V> getLast();

  FluentIterator<SignalRange<K, V>> ranges();

  FluentIterator<SignalRange<K, V>> ranges(K from, QueryDirection direction);

  FluentIterator<SignalRange<K, V>> ranges(K lower, K upper, QueryDirection direction);

  FluentIterator<Signal<K, V>> signals(K lower, K upper, QueryDirection direction);

  FluentIterator<Signal<K, V>> bounds(K lower, K upper, QueryDirection direction);

  FluentIterator<Signal<K, V>> signals(K lower, K upper, QueryDirection direction, boolean reverseOnBackward);

}
