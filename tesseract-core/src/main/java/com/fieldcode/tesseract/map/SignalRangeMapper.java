package com.fieldcode.tesseract.map;

import java.util.function.BiFunction;

import com.fieldcode.tesseract.QueryDirection;
import com.fieldcode.tesseract.Signal;
import com.fieldcode.tesseract.SignalRange;
import com.fieldcode.tesseract.signal.Signals;

class SignalRangeMapper {

  private SignalRangeMapper() {
  }

  static <K extends Comparable<K>, V> BiFunction<Signal<K, V>, Signal<K, V>, SignalRange<K, V>> mapper(QueryDirection direction) {
    return direction.select(SignalRangeMapper::forwardRange, SignalRangeMapper::backwardRange);
  }

  private static <K extends Comparable<K>, V> SignalRange<K, V> forwardRange(Signal<K, V> a, Signal<K, V> b) {
    return Signals.range(a, b);
  }

  private static <K extends Comparable<K>, V> SignalRange<K, V> backwardRange(Signal<K, V> a, Signal<K, V> b) {
    var lowerKey = b.getKey();
    var upperKey = a.getKey();

    return Signals.range(b, a);
  }

}
