package com.fieldcode.tesseract;

import java.util.stream.Stream;

public interface SignalMapReader<K extends Comparable<K>, V> {

  V getDefaultValue();

  NavigableSignal<K, V> get(K key);

  NavigableSignal<K, V> getFirst();

  NavigableSignal<K, V> getLast();

  FluentIterator<Signal<K, V>> signals();

  FluentIterator<Signal<K, V>> signals(K from, QueryDirection direction);

  FluentIterator<Signal<K, V>> signals(K lower, K upper, QueryDirection direction);

  FluentIterator<Signal<K, V>> signals(K lower, K upper, QueryDirection direction, boolean reverseOnBackward);

  Stream<Signal<K, V>> stream();

}
