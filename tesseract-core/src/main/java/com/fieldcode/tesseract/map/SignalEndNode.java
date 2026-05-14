package com.fieldcode.tesseract.map;

import com.fieldcode.tesseract.Signal;

import static com.google.common.base.Preconditions.checkNotNull;

class SignalEndNode<K extends Comparable<K>, V> extends SignalNode<K, V> {

  private final V defaultValue;

  public SignalEndNode(K key, V value, NodeContext<K, V> ctx) {
    super(key, value, ctx);
    this.defaultValue = value;
  }

  public static <K extends Comparable<K>, V> SignalEndNode<K, V> of(K key, V defaultValue, NodeContext<K, V> ctx) {
    checkNotNull(key, "key");
    checkNotNull(defaultValue, "defaultValue");
    return new SignalEndNode<>(key, defaultValue, ctx);
  }

  @Override
  public boolean hasNext() {
    return false;
  }

  @Override
  public boolean hasPrevious() {
    return true;
  }

  @Override
  public void remove() {
    invalidate();
  }

  @Override
  public Signal<K, V> getBackwardSignal() {
    return getPrevious();
  }

  @Override
  public InternalNode<K, V> getNext() {
    return null;
  }

  @Override
  public void setNext(InternalNode<K, V> previous) {
    throw new UnsupportedOperationException("Operation not supported");
  }

  @Override
  public void invalidate() {
    this.value = defaultValue;
  }

  @Override
  public boolean isActive() {
    return true;
  }

}
