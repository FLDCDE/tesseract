package com.fieldcode.tesseract.map;

import java.util.Iterator;
import java.util.function.Predicate;

import org.immutables.value.Value.Immutable;
import org.immutables.value.Value.Parameter;

import com.fieldcode.tesseract.NavigableSignal;
import com.fieldcode.tesseract.NavigableSignalRange;
import com.fieldcode.tesseract.QueryDirection;
import com.fieldcode.tesseract.SignalRange;
import com.fieldcode.tesseract.signal.AbstractSignalRange;


@Immutable
abstract class NavigableSignalRangeSkeleton<K extends Comparable<K>, V> extends AbstractSignalRange<K, V> implements NavigableSignalRange<K, V> {

  @Override
  public NavigableSignalRange<K, V> getNext() {
    return getLower().getNext().hasNext()
        ? ImmutableNavigableSignalRange.of(getLower().getNext())
        : null;
  }

  @Override
  public NavigableSignalRange<K, V> getPrevious() {
    return getLower().hasPrevious()
        ? ImmutableNavigableSignalRange.of(getLower().getPrevious())
        : null;
  }

  @Override
  public Iterator<SignalRange<K, V>> iterator(QueryDirection direction) {
    return SignalRangeMapIteratorFactory.chainIterator(this, direction);
  }

  @Override
  public Iterator<SignalRange<K, V>> iterator(QueryDirection direction, Predicate<SignalRange<K, V>> takeWhile) {
    return SignalRangeMapIteratorFactory.chainIterator(this, direction, takeWhile);
  }

  @Override
  public K getLowerKey() {
    return getLower().getKey();
  }

  @Override
  public K getUpperKey() {
    return getLower().getNext().getKey();
  }

  @Override
  public V getLowerValue() {
    return getLower().getValue();
  }

  @Override
  public V getUpperValue() {
    return getLower().getNext().getValue();
  }

  @Override
  @Parameter
  public abstract NavigableSignal<K, V> getLower();

  @Override
  public NavigableSignal<K, V> getUpper() {
    return getLower().getNext();
  }

  @Override
  public String toString() {
    return super.toString();
  }

}
