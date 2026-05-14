package com.fieldcode.tesseract.interval;

import org.immutables.value.Value.Immutable;
import org.immutables.value.Value.Parameter;

import com.fieldcode.tesseract.Interval;
import com.fieldcode.tesseract.IntervalMap.IntervalEntry;

@Immutable
abstract class IntervalMapEntrySkeleton<T> implements IntervalEntry<T> {

  @Override
  @Parameter
  public abstract Interval getInterval();

  @Override
  @Parameter
  public abstract T getValue();

  @Override
  public String toString() {
    return getValue() + "@" + getInterval();
  }

}
