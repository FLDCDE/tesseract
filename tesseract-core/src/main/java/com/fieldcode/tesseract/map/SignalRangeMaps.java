package com.fieldcode.tesseract.map;

import java.util.Collection;
import java.util.stream.Stream;

import com.fieldcode.tesseract.SignalRange;

public class SignalRangeMaps {

  private SignalRangeMaps() {}

  public static <K extends Comparable<K>, V> DisjointSignalRangeMap<K, V> disjoint(K min, K max, V defaultValue) {
    return DisjointSignalRangeMap.of(min, max, defaultValue);
  }

  public static <K extends Comparable<K>, V> DisjointSignalRangeMap<K, V> disjoint(K min, K max, V defaultValue, Collection<SignalRange<K, V>> ranges) {
    var rangeMap = DisjointSignalRangeMap.of(min, max, defaultValue);
    for (SignalRange<K, V> range : ranges) {
      rangeMap.put(range);
    }
    return rangeMap;
  }

  public static <K extends Comparable<K>, V> DisjointSignalRangeMap<K, V> disjoint(K min, K max, V defaultValue, Stream<SignalRange<K, V>> ranges) {
    var rangeMap = DisjointSignalRangeMap.of(min, max, defaultValue);
    ranges.forEach(rangeMap::put);
    return rangeMap;
  }

  public static <K extends Comparable<K>, V> NonOverlappingSignalRangeMap<K, V> nonOverlapping(K min, K max, V defaultValue) {
    return NonOverlappingSignalRangeMap.of(min, max, defaultValue);
  }

  public static <K extends Comparable<K>, V> NonOverlappingSignalRangeMap<K, V> nonOverlapping(K min, K max, V defaultValue, Collection<SignalRange<K, V>> ranges) {
    var rangeMap = NonOverlappingSignalRangeMap.of(min, max, defaultValue);
    for (SignalRange<K, V> range : ranges) {
      rangeMap.put(range);
    }
    return rangeMap;
  }

  public static <K extends Comparable<K>, V> NonOverlappingSignalRangeMap<K, V> nonOverlapping(K min, K max, V defaultValue, Stream<SignalRange<K, V>> ranges) {
    var rangeMap = NonOverlappingSignalRangeMap.of(min, max, defaultValue);
    ranges.forEach(rangeMap::put);
    return rangeMap;
  }

}
