package com.fieldcode.tesseract.map;

import java.util.ArrayList;
import java.util.NavigableMap;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.fieldcode.tesseract.FluentIterator;
import com.fieldcode.tesseract.NavigableSignal;
import com.fieldcode.tesseract.QueryDirection;
import com.fieldcode.tesseract.Signal;
import com.fieldcode.tesseract.SignalMap;
import com.fieldcode.tesseract.signal.Signals;
import com.fieldcode.tesseract.utils.iterator.Iterators;

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.function.Function.identity;

public class TreeSignalMap<K extends Comparable<K>, V> implements SignalMap<K, V>, NodeContext<K, V> {

  private final K min;
  private final K max;
  private final V defaultValue;
  private final SignalStartNode<K, V> chainStartNode;
  private final SignalEndNode<K, V> chainEndNode;
  private final NavigableMap<K, InternalNode<K, V>> index = new TreeMap<>();

  private TreeSignalMap(K min, K max, V defaultValue) {
    this.min = min;
    this.max = max;
    this.defaultValue = defaultValue;
    this.chainStartNode = SignalStartNode.of(min, defaultValue, this);
    this.chainEndNode = SignalEndNode.of(max, defaultValue, this);
    this.index.put(min, chainStartNode);
    this.index.put(max, chainEndNode);
    chainStartNode.setNext(chainEndNode);
    chainEndNode.setPrevious(chainStartNode);
  }

  public static <K extends Comparable<K>, V> SignalMap<K, V> of(K min, K max, V defaultValue) {
    checkArgument(min.compareTo(max) < 0, "Min must be less than max [min=%s, max=%s]", min, max);
    return new TreeSignalMap<>(min, max, defaultValue);
  }

  private void checkBounds(K key) {
    checkArgument(key.compareTo(min) >= 0 && key.compareTo(max) <= 0, "Key must be an element of [%s..%s]. [Key=%s]", min, max, key);
  }

  private InternalNode<K, V> floor(K key) {
    checkBounds(key);
    return index.floorEntry(key).getValue();
  }

  @Override
  public V getDefaultValue() {
    return defaultValue;
  }

  @Override
  public NavigableSignal<K, V> get(K key) {
    return floor(key);
  }

  @Override
  public NavigableSignal<K, V> getFirst() {
    return chainStartNode;
  }

  @Override
  public NavigableSignal<K, V> getLast() {
    return chainEndNode;
  }

  @Override
  public FluentIterator<Signal<K, V>> signals() {
    return Iterators.chain(
        (NavigableSignal<K, V>) chainStartNode,
        NavigableSignal::getNext,
        Signals::copy
    );
  }

  @Override
  public FluentIterator<Signal<K, V>> signals(K from, QueryDirection direction) {
    return direction.get(
        () -> this.signals(from, max, direction),
        () -> this.signals(min, from, direction)
    );
  }

  @Override
  public FluentIterator<Signal<K, V>> signals(K lower, K upper, QueryDirection direction) {
    return direction.get(
        () -> RangedSignalForwardIterator.of(index, lower, upper),
        () -> RangedSignalBackwardIterator.of(index, lower, upper)
    );
  }

  @Override
  public FluentIterator<Signal<K, V>> signals(K lower, K upper, QueryDirection direction, boolean reverseOnBackward) {
    if (reverseOnBackward) {
      return direction.get(
          () -> ForwardSignalIterator.of(this, lower, upper),
          () -> BackwardSignalIterator.of(this, lower, upper)
      );
    }
    return signals(lower, upper, direction);
  }

  @Override
  public Stream<Signal<K, V>> stream() {
    return index.values()
        .stream()
        .map(identity());
  }

  @Override
  public NavigableSignal<K, V> put(K key, V value) {
    return floor(key).insertAfter(key, value);
  }

  @Override
  public NavigableSignal<K, V> put(Signal<K, V> signal) {
    return put(signal.getKey(), signal.getValue());
  }

  @Override
  public void putAll(Iterable<Signal<K, V>> signals) {
    signals.forEach(this::put);
  }

  @Override
  public void putAll(Stream<Signal<K, V>> signals) {
    signals.forEach(this::put);
  }

  @Override
  public void remove(K key) {
    var node = floor(key);
    if (node.keyEqualsTo(key)) {
      node.remove();
    }
  }

  @Override
  public void remove(K lower, boolean lowerInclusive, K upper, boolean upperInclusive) {
    var keys = new ArrayList<>(index.subMap(lower, lowerInclusive, upper, upperInclusive).keySet());
    for (K key : keys) {
      index.get(key).remove();
    }
  }

  @Override
  public void internalRemove(K key) {
    index.remove(key);
  }

  @Override
  public void internalInsert(K key, InternalNode<K, V> value) {
    index.put(key, value);
  }

  @Override
  public String toString() {
    return index
        .values()
        .stream()
        .map(Object::toString)
        .collect(Collectors.joining(", ", "[", "]"));
  }

  NavigableMap<K, InternalNode<K, V>> getIndex() {
    return index;
  }

}
