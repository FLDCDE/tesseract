package com.fieldcode.tesseract.signal;

import com.fieldcode.tesseract.Signal;
import com.fieldcode.tesseract.SignalRange;

public class SignalRanges {

  private SignalRanges() {}

  public static <K extends Comparable<K>, V> SignalRange<K, V> range(Signal<K, V> lower, Signal<K, V> upper) {
    return ImmutableSignalRange.of(lower, upper);
  }

  public static <K extends Comparable<K>, V> SignalRange<K, V> copy(SignalRange<K, V> signalRange) {
    return ImmutableSignalRange.of(
        Signals.copy(signalRange.getLower()),
        Signals.copy(signalRange.getUpper())
    );
  }

}
