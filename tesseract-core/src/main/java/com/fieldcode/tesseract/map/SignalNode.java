package com.fieldcode.tesseract.map;

import java.util.function.Predicate;

import com.fieldcode.tesseract.FluentIterator;
import com.fieldcode.tesseract.NavigableSignal;
import com.fieldcode.tesseract.QueryDirection;
import com.fieldcode.tesseract.Signal;
import com.fieldcode.tesseract.signal.AbstractSignal;
import com.fieldcode.tesseract.signal.Signals;
import com.fieldcode.tesseract.utils.iterator.Iterators;

import static com.google.common.base.Preconditions.checkNotNull;

abstract class SignalNode<K extends Comparable<K>, V> extends AbstractSignal<K, V> implements InternalNode<K, V> {

  private final K key;
  private final NodeContext<K, V> ctx;

  protected V value;
  protected InternalNode<K, V> next;
  protected InternalNode<K, V> previous;

  public SignalNode(K key, V value, NodeContext<K, V> ctx) {
    this.key = key;
    this.value = value;
    this.ctx = ctx;
  }

  @Override
  public boolean hasNext() {
    return next != null;
  }

  public boolean hasPrevious() {
    return previous != null;
  }

  @Override
  public FluentIterator<Signal<K, V>> iterator(QueryDirection direction) {
    checkNotNull(direction, "Direction must be non null");
    return direction.get(
        () -> Iterators.chain((NavigableSignal<K, V>) this, NavigableSignal::getNext, Signals::copy),
        () -> Iterators.chain((NavigableSignal<K, V>) this, NavigableSignal::getPrevious, Signals::copy)
    );
  }

  @Override
  public FluentIterator<Signal<K, V>> iterator(QueryDirection direction, Predicate<Signal<K, V>> takeWhile) {
    return iterator(direction).takeWhile(takeWhile);
  }

  @Override
  public Signal<K, V> toImmutable() {
    return Signals.copy(this);
  }

  @Override
  public K getKey() {
    return key;
  }

  @Override
  public V getValue() {
    return value;
  }

  @Override
  public void setValue(V value) {
    checkNotNull(value, "value");
    this.value = value;
  }

  @Override
  public InternalNode<K, V> insertAfter(K key, V value) {
    if (this.key.equals(key)) {
      setValue(value);
      return this;
    } else {
      var node = SignalIntermediateNode.of(key, value, ctx);
      linkAfter(node);
      ctx.internalInsert(key, node);
      return node;
    }
  }

  @Override
  public void remove() {
    getPrevious().setNext(getNext());
    getNext().setPrevious(getPrevious());
    invalidate();
    ctx.internalRemove(key);
  }

  @Override
  public InternalNode<K, V> getNext() {
    return next;
  }

  @Override
  public void setNext(InternalNode<K, V> next) {
    this.next = next;
  }

  @Override
  public InternalNode<K, V> getPrevious() {
    return previous;
  }

  @Override
  public void setPrevious(InternalNode<K, V> previous) {
    this.previous = previous;
  }

  protected void linkAfter(InternalNode<K, V> node) {
    getNext().setPrevious(node);
    node.setNext(getNext());
    node.setPrevious(this);
    setNext(node);
  }

}
