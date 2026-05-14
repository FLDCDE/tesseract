package com.fieldcode.tesseract.map;

interface NodeContext<K extends Comparable<K>, V> {

  void internalRemove(K key);

  void internalInsert(K key, InternalNode<K, V> value);

}
