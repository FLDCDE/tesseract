package com.fieldcode.tesseract.map;

import com.fieldcode.tesseract.Signal;

import static com.google.common.base.Preconditions.checkNotNull;

class SignalStartNode<K extends Comparable<K>, V> extends SignalNode<K, V> {

  private final V defaultValue;

  public SignalStartNode(K key, V value, NodeContext<K, V> ctx) {
    super(key, value, ctx);
    this.defaultValue = value;
  }

  public static <K extends Comparable<K>, V> SignalStartNode<K, V> of(K key, V defaultValue, NodeContext<K, V> ctx) {
    checkNotNull(key, "key");
    checkNotNull(defaultValue, "defaultValue");
    return new SignalStartNode<>(key, defaultValue, ctx);
  }

  @Override
  public boolean hasPrevious() {
    return false;
  }

  @Override
  public void remove() {
    invalidate();
  }

  @Override
  public Signal<K, V> getBackwardSignal() {
    return this;
  }

  @Override
  public InternalNode<K, V> getPrevious() {
    return null;
  }

  @Override
  public void setPrevious(InternalNode<K, V> previous) {
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
