package com.fieldcode.tesseract.map;

public class DisjointSignalRangeMap<K extends Comparable<K>, V> extends AbstractSignalRangeMap<K, V> {


  private DisjointSignalRangeMap(K min, K max, V defaultValue) {
    super(min, max, defaultValue);
  }

  public static <K extends Comparable<K>, V> DisjointSignalRangeMap<K, V> of(K min, K max, V defaultValue) {
    return new DisjointSignalRangeMap<>(min, max, defaultValue);
  }

  @Override
  public void put(K key, V value) {

    var signal = signals.get(key);

    if (signal.valueEqualsTo(value)) {return;}

    signals.remove(key);

    if (signals.get(key).valueEqualsTo(value)) {return;}

    if (signal.hasNext() && signal.getNext().valueEqualsTo(value)) {
      signals.remove(signal.getNext().getKey());
    }
    signals.put(key, value);
  }


  @Override
  public void put(K lowerKey, K upperKey, V value) {
    checkRange(lowerKey, upperKey);

    // Stash current value at upper bound
    var currentUpperValue = signals.get(upperKey).getValue();

    // Remove all intermediate signals
    signals.remove(lowerKey, true, upperKey, true);

    // Put lower signal if required or ignore (merging)
    if (!signals.get(lowerKey).valueEqualsTo(value)) {
      signals.put(lowerKey, value);
    }

    // Put upper signal if required or ignore (merging)
    if (!currentUpperValue.equals(value)) {
      signals.put(upperKey, currentUpperValue);
    }

  }

}
