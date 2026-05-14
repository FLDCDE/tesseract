package com.fieldcode.tesseract.signal;

import java.util.function.BiFunction;

import com.fieldcode.tesseract.Signal;

public abstract class AbstractSignal<K extends Comparable<K>, V> implements Signal<K, V> {

  @Override
  public Signal<K, V> withKey(K key) {
    return Signals.signal(key, getValue());
  }

  @Override
  public Signal<K, V> withValue(V value) {
    return Signals.signal(getKey(), value);
  }

  @Override
  public Signal<K, V> mapIfBefore(K key, BiFunction<Signal<K, V>, K, Signal<K, V>> mapper) {
    return key.compareTo(getKey()) > 0
        ? mapper.apply(this, key)
        : this;
  }

  @Override
  public boolean equals(Object another) {
    if (another == this) {
      return true;
    }
    return another instanceof Signal<?, ?>
        && equalTo((Signal<?, ?>) another);
  }

  @Override
  public String toString() {
    return getKey() + "=" + getValue();
  }

  private boolean equalTo(Signal<?, ?> another) {
    return getKey().equals(another.getKey())
        && getValue().equals(another.getValue());
  }

}
