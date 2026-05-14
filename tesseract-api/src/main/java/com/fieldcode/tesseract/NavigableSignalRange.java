package com.fieldcode.tesseract;

import java.util.Iterator;
import java.util.function.Predicate;

public interface NavigableSignalRange<K extends Comparable<K>, V> extends SignalRange<K, V> {

  NavigableSignalRange<K, V> getNext();

  NavigableSignalRange<K, V> getPrevious();

  Iterator<SignalRange<K, V>> iterator(QueryDirection direction);

  Iterator<SignalRange<K, V>> iterator(QueryDirection direction, Predicate<SignalRange<K, V>> takeWhile);

}
