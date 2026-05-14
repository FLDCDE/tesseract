package com.fieldcode.tesseract.map;

import com.fieldcode.tesseract.NavigableSignal;
import com.fieldcode.tesseract.Signal;

interface InternalNode<K extends Comparable<K>, V> extends NavigableSignal<K, V> {


  InternalNode<K, V> getNext();

  InternalNode<K, V> getPrevious();

  void setPrevious(InternalNode<K, V> next);

  void setNext(InternalNode<K, V> next);

  void invalidate();

  void setValue(V value);

  InternalNode<K, V> insertAfter(K key, V value);

  default boolean keyEqualsTo(K key) {
    return getKey().equals(key);
  }

  void remove();

  Signal<K, V> getBackwardSignal();

}
