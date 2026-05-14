package com.fieldcode.tesseract;

import java.util.function.Predicate;

public interface NavigableSignal<K extends Comparable<K>, V> extends Signal<K, V> {

  NavigableSignal<K, V> getNext();

  NavigableSignal<K, V> getPrevious();

  boolean hasNext();

  boolean hasPrevious();

  boolean isActive();

  FluentIterator<Signal<K, V>> iterator(QueryDirection direction);

  FluentIterator<Signal<K, V>> iterator(QueryDirection direction, Predicate<Signal<K, V>> takeWhile);

  Signal<K, V> toImmutable();

}
