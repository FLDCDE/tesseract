package com.fieldcode.tesseract.map;

import java.util.stream.Stream;

import com.fieldcode.tesseract.FluentIterator;
import com.fieldcode.tesseract.NavigableSignal;
import com.fieldcode.tesseract.NavigableSignalRange;
import com.fieldcode.tesseract.QueryDirection;
import com.fieldcode.tesseract.Signal;
import com.fieldcode.tesseract.SignalMap;
import com.fieldcode.tesseract.SignalRange;
import com.fieldcode.tesseract.SignalRangeMap;
import com.fieldcode.tesseract.signal.Signals;

import static com.fieldcode.tesseract.QueryDirection.FORWARD;
import static com.google.common.base.Preconditions.checkArgument;

abstract class AbstractSignalRangeMap<K extends Comparable<K>, V> implements SignalRangeMap<K, V> {

  protected final SignalMap<K, V> signals;

  AbstractSignalRangeMap(K min, K max, V defaultValue) {this.signals = SignalMaps.modifiable(min, max, defaultValue);}

  protected void checkRange(K lower, K upper) {
    checkArgument(lower.compareTo(upper) < 0, "Lower key must be less than upper [lower=%s, upper=%s]", lower, upper);
  }

  @Override
  public void put(SignalRange<K, V> signalRange) {
    var lowerKey = signalRange.getLowerKey();
    var upperKey = signalRange.getUpperKey();
    var value = signalRange.getLowerValue();
    put(lowerKey, upperKey, value);
  }

  @Override
  public void putAll(Iterable<SignalRange<K, V>> signalRanges) {
    signalRanges.forEach(this::put);
  }

  @Override
  public void putAll(Stream<SignalRange<K, V>> signalRanges) {
    signalRanges.forEach(this::put);
  }

  @Override
  public void remove(K lower, K upper) {
    put(lower, upper, signals.getDefaultValue());
  }

  @Override
  public NavigableSignalRange<K, V> get(K key) {
    var lower = signals.get(key);
    return asRange(lower);
  }

  @Override
  public NavigableSignalRange<K, V> getFirst() {
    return asRange(signals.getFirst());
  }

  @Override
  public NavigableSignalRange<K, V> getLast() {
    return asRange(signals.getLast().getPrevious());
  }

  @Override
  public FluentIterator<SignalRange<K, V>> ranges() {
    return signals
        .signals()
        .successiveMap(
            SignalRangeMapper.mapper(FORWARD)
        );
  }

  public FluentIterator<SignalRange<K, V>> ranges(K from, QueryDirection direction) {
    return signals
        .signals(from, direction)
        .successiveMap(
            SignalRangeMapper.mapper(direction)
        );
  }

  public FluentIterator<SignalRange<K, V>> ranges(K lower, K upper, QueryDirection direction) {
    return signals
        .signals(lower, upper, direction)
        .successiveMap(
            SignalRangeMapper.mapper(direction)
        );
  }

  @Override
  public FluentIterator<Signal<K, V>> signals(K lower, K upper, QueryDirection direction) {
    return signals.signals(lower, upper, direction);
  }

  @Override
  public FluentIterator<Signal<K, V>> signals(K lower, K upper, QueryDirection direction, boolean reverseOnBackward) {
    return signals.signals(lower, upper, direction, reverseOnBackward);
  }

  @Override
  public FluentIterator<Signal<K, V>> bounds(K lower, K upper, QueryDirection direction) {
    return signals.signals(lower, upper, direction);
  }

  public Stream<SignalRange<K, V>> stream() {
    return Stream
        .iterate(signals.getFirst(), s1 -> s1.getNext() != null, NavigableSignal::getNext)
        .map(this::asRange)
        .map(Signals::copy);
  }

  private NavigableSignalRange<K, V> asRange(NavigableSignal<K, V> s) {
    return ImmutableNavigableSignalRange.of(s);
  }

}
