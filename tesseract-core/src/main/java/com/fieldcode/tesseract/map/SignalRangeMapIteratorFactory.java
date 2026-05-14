package com.fieldcode.tesseract.map;

import com.fieldcode.tesseract.NavigableSignalRange;
import com.fieldcode.tesseract.QueryDirection;
import com.fieldcode.tesseract.SignalRange;
import com.fieldcode.tesseract.signal.SignalRanges;
import com.fieldcode.tesseract.utils.iterator.Iterators;

import java.util.Iterator;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import static com.fieldcode.tesseract.QueryDirection.FORWARD;
import static com.google.common.base.Preconditions.checkNotNull;

class SignalRangeMapIteratorFactory {

  private SignalRangeMapIteratorFactory() {
  }

  static <K extends Comparable<K>, V> Iterator<SignalRange<K, V>> chainIterator(NavigableSignalRange<K, V> seed, QueryDirection direction) {
    checkNotNull(direction, "Direction must be non null");
    return direction == FORWARD
        ? chainIterator(seed, NavigableSignalRange::getNext)
        : chainIterator(seed, NavigableSignalRange::getPrevious);
  }

  static <K extends Comparable<K>, V> Iterator<SignalRange<K, V>> chainIterator(NavigableSignalRange<K, V> seed, QueryDirection direction, Predicate<SignalRange<K, V>> takeWhile) {
    checkNotNull(direction, "Direction must be non null");
    checkNotNull(takeWhile, "Take while predicate must be non null");
    var iterator = chainIterator(seed, direction);
    return Iterators.fluent(iterator)
        .takeWhile(takeWhile);
  }

  public static <K extends Comparable<K>, V> Iterator<SignalRange<K, V>> chainIterator(NavigableSignalRange<K, V> seed, UnaryOperator<NavigableSignalRange<K, V>> nextMapper) {
    return Iterators.chain(seed, nextMapper, SignalRanges::copy);
  }

}
