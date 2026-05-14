package com.fieldcode.tesseract.signal;

import com.fieldcode.tesseract.Signal;
import com.fieldcode.tesseract.SignalRange;

public class Signals {

  private Signals() {}

  public static <K extends Comparable<K>, V> Signal<K, V> signal(K key, V value) {
    return ImmutableSignal.of(key, value);
  }

  public static <K extends Comparable<K>, V> Signal<K, V> copy(Signal<K, V> original) {
    return ImmutableSignal.of(original.getKey(), original.getValue());
  }

  public static <K extends Comparable<K>, V> SignalRange<K, V> range(Signal<K, V> lower, Signal<K, V> upper) {
    return ImmutableSignalRange.of(lower, upper);
  }

  public static <K extends Comparable<K>, V> SignalRange<K, V> copy(SignalRange<K, V> range) {
    return ImmutableSignalRange.<K, V>builder()
        .lower(range.getLower())
        .upper(range.getUpper())
        .build();
  }

}
