package com.fieldcode.tesseract.signal;

import java.util.function.Predicate;

import com.fieldcode.tesseract.NavigableSignal;
import com.fieldcode.tesseract.Signal;

public class SignalPredicates {

  private SignalPredicates() {
  }

  public static <K extends Comparable<K>, V> Predicate<Signal<K, V>> keyLessThan(K key) {
    return s -> s.getKey().compareTo(key) < 0;
  }

  public static <K extends Comparable<K>, V> Predicate<Signal<K, V>> keyLessThanOrEquals(K key) {
    return s -> s.getKey().compareTo(key) <= 0;
  }

  public static <K extends Comparable<K>, V> Predicate<Signal<K, V>> keyGreaterThan(K key) {
    return s -> s.getKey().compareTo(key) > 0;
  }

  public static <K extends Comparable<K>, V> Predicate<Signal<K, V>> keyGreaterThanOrEquals(K key) {
    return s -> s.getKey().compareTo(key) >= 0;
  }

  public static <K extends Comparable<K>, V> Predicate<Signal<K, V>> keyEqualsTo(K key) {
    return s -> s.getKey().compareTo(key) == 0;
  }

  public static <K extends Comparable<K>, V> Predicate<Signal<K, V>> keyBetween(K lower, boolean lowerInclusive, K upper, boolean upperInclusive) {
    Predicate<Signal<K, V>> lowerPredicate = lowerInclusive
        ? keyGreaterThanOrEquals(lower)
        : keyGreaterThan(lower);

    Predicate<Signal<K, V>> upperPredicate = upperInclusive
        ? keyLessThanOrEquals(upper)
        : keyLessThan(upper);

    return lowerPredicate.and(upperPredicate);
  }

  public static <K extends Comparable<K>, V> Predicate<Signal<K, V>> hasNext() {
    return s -> s instanceof NavigableSignal && ((NavigableSignal<K, V>) s).hasNext();
  }

  public static <K extends Comparable<K>, V> Predicate<Signal<K, V>> hasPrevious() {
    return s -> s instanceof NavigableSignal && ((NavigableSignal<K, V>) s).hasPrevious();
  }

  public static <K extends Comparable<K>, V> Predicate<NavigableSignal<K, V>> nextIs(Predicate<Signal<K, V>> predicate) {
    return s -> s.hasNext() && predicate.test(s.getNext());
  }

  public static <K extends Comparable<K>, V> Predicate<NavigableSignal<K, V>> previousIs(Predicate<Signal<K, V>> predicate) {
    return s -> s.hasPrevious() && predicate.test(s.getPrevious());
  }

}
