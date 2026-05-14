package com.fieldcode.tesseract;

import java.util.function.BiFunction;

public interface Signal<K extends Comparable<K>, V> extends Comparable<Signal<K, V>> {

  K getKey();

  V getValue();

  default boolean keyEqualsTo(K other) {
    return other.equals(getKey());
  }

  default boolean valueEqualsTo(V other) {
    return other.equals(getValue());
  }

  Signal<K, V> withKey(K key);

  Signal<K, V> withValue(V value);

  Signal<K, V> mapIfBefore(K key, BiFunction<Signal<K, V>, K, Signal<K, V>> mapper);

  @Override
  default int compareTo(Signal<K, V> o) {
    return getKey().compareTo(o.getKey());
  }

}
