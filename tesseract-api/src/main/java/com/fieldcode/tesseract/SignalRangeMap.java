package com.fieldcode.tesseract;

import java.util.stream.Stream;

public interface SignalRangeMap<K extends Comparable<K>, V> extends SignalRangeMapReader<K, V> {

  void put(K key, V value);

  void put(K lowerKey, K upperKey, V value);

  /**
   * Adds a signal range to the map. The upper signal value is ignored.
   *
   * @param signalRange instance
   * @deprecated This method will be removed. Use {@link SignalRangeMap#put(K, K, V)} instead.
   */
  @Deprecated(forRemoval = true)
  void put(SignalRange<K, V> signalRange);

  void putAll(Iterable<SignalRange<K, V>> signalRanges);

  void putAll(Stream<SignalRange<K, V>> signalRanges);

  void remove(K lower, K upper);

  @Override
  NavigableSignalRange<K, V> get(K key);

  @Override
  NavigableSignalRange<K, V> getFirst();

  @Override
  NavigableSignalRange<K, V> getLast();

  @Override
  FluentIterator<SignalRange<K, V>> ranges();

  @Override
  FluentIterator<SignalRange<K, V>> ranges(K from, QueryDirection direction);

  @Override
  FluentIterator<SignalRange<K, V>> ranges(K lower, K upper, QueryDirection direction);

  @Override
  FluentIterator<Signal<K, V>> signals(K lower, K upper, QueryDirection direction);

  @Override
  FluentIterator<Signal<K, V>> signals(K lower, K upper, QueryDirection direction, boolean reverseOnBackward);

}
