package com.fieldcode.tesseract;

import java.util.stream.Stream;

public interface SignalMap<K extends Comparable<K>, V> extends SignalMapReader<K, V> {

  @Override
  V getDefaultValue();

  NavigableSignal<K, V> put(K key, V value);

  NavigableSignal<K, V> put(Signal<K, V> signal);

  void putAll(Iterable<Signal<K, V>> signals);

  void putAll(Stream<Signal<K, V>> signals);

  void remove(K key);

  void remove(K lower, boolean lowerInclusive, K upper, boolean upperInclusive);

}
