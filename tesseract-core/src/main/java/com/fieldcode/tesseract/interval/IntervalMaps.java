package com.fieldcode.tesseract.interval;

import com.fieldcode.tesseract.Interval;
import com.fieldcode.tesseract.IntervalMap;
import com.fieldcode.tesseract.IntervalMap.IntervalEntry;

public class IntervalMaps {

  private IntervalMaps() {
  }

  public static <T> IntervalMap<T> disjoint(T defaultValue) {
    return IntervalTreeMap.of(defaultValue);
  }

  public static <T> IntervalEntry<T> entry(Interval interval, T value) {
    return ImmutableIntervalMapEntry.of(interval, value);
  }

}
