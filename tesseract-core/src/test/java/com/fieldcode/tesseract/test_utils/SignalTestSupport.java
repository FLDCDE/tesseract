package com.fieldcode.tesseract.test_utils;


import java.util.stream.Stream;

import com.fieldcode.tesseract.NavigableSignal;
import com.fieldcode.tesseract.Signal;
import com.fieldcode.tesseract.SignalMap;
import com.fieldcode.tesseract.SignalRange;
import com.fieldcode.tesseract.SignalRangeMap;
import com.fieldcode.tesseract.map.SignalMaps;
import com.fieldcode.tesseract.map.SignalRangeMaps;
import com.fieldcode.tesseract.signal.Signals;

import static org.assertj.core.api.Assertions.assertThat;

public class SignalTestSupport extends TimeTestSupport {

  protected static final int MIN = -100;
  protected static final int MAX = 100;
  protected static final String DEFAULT_VALUE = "default";

  protected static final String RANDOM_VALUE = "random_value";


  protected static Signal<Integer, String> signal(int key, String value) {
    return Signals.signal(key, value);
  }

  protected static Signal<Integer, String> signal(int key) {
    return Signals.signal(key, String.valueOf(key));
  }

  protected static SignalRange<Integer, String> range(Signal<Integer, String> lower, Signal<Integer, String> upper) {
    return Signals.range(lower, upper);
  }

  protected static SignalRange<Integer, String> range(int lowerKey, String lowerValue, int upperKey, String upperValue) {
    return Signals.range(
        signal(lowerKey, lowerValue),
        signal(upperKey, upperValue)
    );
  }

  protected SignalMap<Integer, String> createSignalMap(Stream<Signal<Integer, String>> signals) {
    return SignalMaps.modifiable(MIN, MAX, DEFAULT_VALUE, signals);
  }

  protected SignalRangeMap<Integer, String> createDisjointMap(Stream<SignalRange<Integer, String>> ranges) {
    return SignalRangeMaps.disjoint(MIN, MAX, DEFAULT_VALUE, ranges);
  }

  protected SignalRangeMap<Integer, String> createNonOverlappingMap(Stream<SignalRange<Integer, String>> ranges) {
    return SignalRangeMaps.nonOverlapping(MIN, MAX, DEFAULT_VALUE, ranges);
  }

  @SafeVarargs
  protected final SignalMap<Integer, String> createSignalMap(Signal<Integer, String>... signals) {
    return createSignalMap(Stream.of(signals));
  }

  @SafeVarargs
  protected final SignalRangeMap<Integer, String> createDisjointMap(SignalRange<Integer, String>... signalRanges) {
    return createDisjointMap(Stream.of(signalRanges));
  }

  @SafeVarargs
  protected final SignalRangeMap<Integer, String> createNonOverlappingMap(SignalRange<Integer, String>... signalRanges) {
    return createNonOverlappingMap(Stream.of(signalRanges));
  }

  public SignalMap<Integer, String> createSampleMap() {
    return createSignalMap(
        signal(-50),
        signal(-10),
        signal(0),
        signal(10),
        signal(50)
    );
  }

  public SignalRangeMap<Integer, String> createSampleDisjointMap() {
    return createDisjointMap(
        range(signal(-50), signal(-10)),
        range(signal(-10), signal(0)),
        range(signal(0), signal(10)),
        range(signal(10), signal(50))
    );
  }

  protected void assertNext(NavigableSignal<Integer, String> signal, Signal<Integer, String> expected) {
    assertThat(signal.getNext()).isEqualTo(expected);
  }

  protected void assertPrevious(NavigableSignal<Integer, String> signal, Signal<Integer, String> expected) {
    assertThat(signal.getPrevious()).isEqualTo(expected);
  }

}
