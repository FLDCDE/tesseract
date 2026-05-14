package com.fieldcode.tesseract.signal;

import java.util.function.BiFunction;

import com.fieldcode.tesseract.SignalRange;

public abstract class AbstractSignalRange<K extends Comparable<K>, V> implements SignalRange<K, V> {

  @Override
  public boolean contains(K key) {
    return key.compareTo(getLowerKey()) >= 0
        && key.compareTo(getUpperKey()) < 0;
  }

  @Override
  public SignalRange<K, V> withLowerKey(K key) {
    return key.equals(getLowerKey())
        ? this
        : SignalRanges.range(
            getLower().withKey(key),
            getUpper()
        );
  }

  @Override
  public SignalRange<K, V> withLowerValue(V value) {
    return getLower().valueEqualsTo(value)
        ? this
        : SignalRanges.range(
            getLower().withValue(value),
            getUpper()
        );
  }

  @Override
  public SignalRange<K, V> withUpperKey(K key) {
    return key.equals(getUpperKey())
        ? this
        : SignalRanges.range(
            getLower(),
            getUpper().withKey(key)
        );
  }

  @Override
  public SignalRange<K, V> withUpperValue(V value) {
    return getUpper().valueEqualsTo(value)
        ? this
        : SignalRanges.range(getLower(), getUpper().withValue(value));
  }

  @Override
  public SignalRange<K, V> withLower(K key, V value) {
    var lower = Signals.signal(key, value);
    return SignalRanges.range(lower, getUpper());
  }

  @Override
  public SignalRange<K, V> withUpper(K key, V value) {
    var upper = Signals.signal(key, value);
    return SignalRanges.range(getLower(), upper);
  }

  @Override
  public SignalRange<K, V> mapByRelation(
      K key,
      BiFunction<SignalRange<K, V>, K, SignalRange<K, V>> ifRangeBeforeThan,
      BiFunction<SignalRange<K, V>, K, SignalRange<K, V>> ifRangeContainsThan,
      BiFunction<SignalRange<K, V>, K, SignalRange<K, V>> ifRangeAfterThan
  ) {

    if (key.compareTo(getLowerKey()) >= 0 && key.compareTo(getUpperKey()) < 0) {
      return ifRangeContainsThan.apply(this, key);
    }

    if (key.compareTo(getUpperKey()) >= 0) {
      return ifRangeBeforeThan.apply(this, key);
    }

    return ifRangeAfterThan.apply(this, key);
  }

  @Override
  public SignalRange<K, V> mapIfContains(K key, BiFunction<SignalRange<K, V>, K, SignalRange<K, V>> mapper) {
    return contains(key)
        ? mapper.apply(this, key)
        : this;
  }

  @Override
  public String toString() {
    return String.format("[%s..%s) : %s (..%s)", getLowerKey(), getUpperKey(), getLowerValue(), getUpperValue());
  }

}
