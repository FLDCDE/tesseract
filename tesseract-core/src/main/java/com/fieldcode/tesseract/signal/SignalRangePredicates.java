package com.fieldcode.tesseract.signal;

import java.util.function.Predicate;

import com.fieldcode.tesseract.SignalRange;

public class SignalRangePredicates {

  private SignalRangePredicates() {
  }

  public static <K extends Comparable<K>, V> Predicate<SignalRange<K, V>> lowerKeyLessThan(K key) {
    return s -> s.getLower().getKey().compareTo(key) < 0;
  }

  public static <K extends Comparable<K>, V> Predicate<SignalRange<K, V>> lowerKeyLessThanOrEquals(K key) {
    return s -> s.getLower().getKey().compareTo(key) <= 0;
  }

  public static <K extends Comparable<K>, V> Predicate<SignalRange<K, V>> lowerKeyGreaterThanOrEquals(K key) {
    return s -> s.getLower().getKey().compareTo(key) >= 0;
  }

  public static <K extends Comparable<K>, V> Predicate<SignalRange<K, V>> upperKeyGreaterThanOrEquals(K key) {
    return s -> s.getUpper().getKey().compareTo(key) >= 0;
  }

  public static <K extends Comparable<K>, V> Predicate<SignalRange<K, V>> upperKeyGreaterThan(K key) {
    return s -> s.getUpper().getKey().compareTo(key) > 0;
  }

  public static <K extends Comparable<K>, V> Predicate<SignalRange<K, V>> upperKeyLessThanOrEquals(K key) {
    return range -> range.getUpperKey().compareTo(key) <= 0;
  }

}
