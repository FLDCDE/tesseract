package com.fieldcode.tesseract.map;

import com.fieldcode.tesseract.Signal;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

class SignalIntermediateNode<K extends Comparable<K>, V> extends SignalNode<K, V> {

  private boolean active = true;

  public SignalIntermediateNode(K key, V value, NodeContext<K, V> ctx) {
    super(key, value, ctx);
  }

  public static <K extends Comparable<K>, V> SignalIntermediateNode<K, V> of(K key, V value, NodeContext<K, V> ctx) {
    checkNotNull(key, "key");
    checkNotNull(value, "value");
    return new SignalIntermediateNode<>(key, value, ctx);
  }

  private void check() {
    checkState(active, "Node has been deactivated.");
  }

  @Override
  public boolean hasNext() {
    check();
    return super.hasNext();
  }

  @Override
  public boolean hasPrevious() {
    check();
    return previous != null;
  }

  @Override
  public InternalNode<K, V> getNext() {
    check();
    return super.getNext();
  }

  @Override
  public InternalNode<K, V> getPrevious() {
    check();
    return previous;
  }

  @Override
  public void setPrevious(InternalNode<K, V> previous) {
    this.previous = previous;
  }

  @Override
  public void invalidate() {
    active = false;
    next = null;
    previous = null;
  }

  @Override
  public Signal<K, V> getBackwardSignal() {
    return getPrevious();
  }

  @Override
  public boolean isActive() {
    return active;
  }

}
