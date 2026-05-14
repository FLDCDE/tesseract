package com.fieldcode.tesseract.map;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Stream;

import com.fieldcode.tesseract.Signal;
import com.fieldcode.tesseract.SignalMap;

public class SignalMaps {

  private SignalMaps() {}

  public static <K extends Comparable<K>, V> SignalMap<K, V> modifiable(K min, K max, V defaultValue) {
    return TreeSignalMap.of(min, max, defaultValue);
  }

  public static <K extends Comparable<K>, V> SignalMap<K, V> modifiable(K min, K max, V defaultValue, Map<K, V> signals) {
    var map = TreeSignalMap.of(min, max, defaultValue);
    signals.forEach(map::put);
    return map;
  }

  public static <K extends Comparable<K>, V> SignalMap<K, V> modifiable(K min, K max, V defaultValue, Collection<Signal<K, V>> signals) {
    var map = TreeSignalMap.of(min, max, defaultValue);
    signals.forEach(map::put);
    return map;
  }

  public static <K extends Comparable<K>, V> SignalMap<K, V> modifiable(K min, K max, V defaultValue, Stream<Signal<K, V>> signals) {
    var map = TreeSignalMap.of(min, max, defaultValue);
    signals.forEach(map::put);
    return map;
  }

}
