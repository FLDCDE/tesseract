package com.fieldcode.tesseract;

import java.util.function.BiFunction;

public interface SignalRange<K extends Comparable<K>, V> {

  K getLowerKey();

  K getUpperKey();

  V getLowerValue();

  V getUpperValue();

  Signal<K, V> getLower();

  Signal<K, V> getUpper();

  boolean contains(K key);

  SignalRange<K, V> withLowerKey(K key);

  SignalRange<K, V> withLowerValue(V value);

  SignalRange<K, V> withUpperKey(K key);

  SignalRange<K, V> withUpperValue(V value);

  SignalRange<K, V> withLower(K key, V value);

  SignalRange<K, V> withUpper(K key, V value);

  SignalRange<K, V> mapByRelation(
      K key,
      BiFunction<SignalRange<K, V>, K, SignalRange<K, V>> ifRangeBeforeThan,
      BiFunction<SignalRange<K, V>, K, SignalRange<K, V>> ifRangeContainsThan,
      BiFunction<SignalRange<K, V>, K, SignalRange<K, V>> ifRangeAfterThan
  );

  SignalRange<K, V> mapIfContains(K key, BiFunction<SignalRange<K, V>, K, SignalRange<K, V>> ifRangeContains);

}
