package com.fieldcode.tesseract.map;

import com.fieldcode.tesseract.FluentIterator;
import com.fieldcode.tesseract.Signal;
import com.fieldcode.tesseract.utils.iterator.Iterators;
import com.fieldcode.tesseract.utils.iterator.StateIterator;

public class BackwardBoundsIterator<K extends Comparable<K>, V> extends StateIterator<Signal<K, V>> {

  private final FluentIterator<Signal<K, V>> iterator;

  private Signal<K, V> last;

  private BackwardBoundsIterator(FluentIterator<Signal<K, V>> source) {
    this.iterator = source.successiveMap(this::shiftImpact);
  }

  public static <K extends Comparable<K>, V> FluentIterator<Signal<K, V>> of(FluentIterator<Signal<K, V>> source) {
    return Iterators.fluent(new BackwardBoundsIterator<>(source));
  }

  private Signal<K, V> shiftImpact(Signal<K, V> first, Signal<K, V> second) {
    last = second;
    return first.withValue(second.getValue());
  }

  @Override
  protected void start() {
    if (iterator.hasNext()) {
      state(this::tail);
    } else {
      state(this::end);
    }
  }

  private void tail() {

    // TODO: 2023. 03. 07. Refactor to use IteratorCallback

    var value = applyNextOrEnd(
        iterator,
        a -> a,
        null
    );

    emit(value);

    if (!iterator.hasNext()) {
      state(this::last);
    }
  }

  private void last() {
    emit(last);
    state(this::end);
  }

}
