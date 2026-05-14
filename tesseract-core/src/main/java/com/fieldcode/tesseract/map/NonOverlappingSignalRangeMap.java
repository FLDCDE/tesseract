package com.fieldcode.tesseract.map;

public class NonOverlappingSignalRangeMap<K extends Comparable<K>, V> extends AbstractSignalRangeMap<K, V> {

  private NonOverlappingSignalRangeMap(K min, K max, V defaultValue) {
    super(min, max, defaultValue);
  }

  public static <K extends Comparable<K>, V> NonOverlappingSignalRangeMap<K, V> of(K min, K max, V defaultValue) {
    return new NonOverlappingSignalRangeMap<>(min, max, defaultValue);
  }

  @Override
  public void put(K key, V value) {
    signals.put(key, value);
  }

  @Override
  public void put(K lowerKey, K upperKey, V value) {
    checkRange(lowerKey, upperKey);

    // Stash current value at upper bound
    var currentUpperValue = signals.get(upperKey).getValue();

    // Remove all intermediate signals
    signals.remove(lowerKey, true, upperKey, true);

    signals.put(lowerKey, value);

    signals.put(upperKey, currentUpperValue);

  }

}
