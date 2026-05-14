package com.fieldcode.tesseract.signal;

import org.immutables.value.Value.Immutable;
import org.immutables.value.Value.Parameter;

@Immutable
abstract class SignalSkeleton<K extends Comparable<K>, V> extends AbstractSignal<K, V> {

  @Override
  @Parameter
  public abstract K getKey();

  @Override
  @Parameter
  public abstract V getValue();

}

