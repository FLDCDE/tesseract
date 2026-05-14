package com.fieldcode.tesseract.signal;

import com.fieldcode.tesseract.Signal;
import org.immutables.value.Value.Check;
import org.immutables.value.Value.Immutable;
import org.immutables.value.Value.Parameter;

import static com.google.common.base.Preconditions.checkArgument;

@Immutable
abstract class SignalRangeSkeleton<K extends Comparable<K>, V> extends AbstractSignalRange<K, V> {

  @Parameter
  public abstract Signal<K, V> getLower();

  @Parameter
  public abstract Signal<K, V> getUpper();

  @Override
  public K getLowerKey() {
    return getLower().getKey();
  }

  @Override
  public K getUpperKey() {
    return getUpper().getKey();
  }

  @Override
  public V getLowerValue() {
    return getLower().getValue();
  }

  @Override
  public V getUpperValue() {
    return getUpper().getValue();
  }

  @Check
  protected void check() {
    checkArgument(getLowerKey().compareTo(getUpperKey()) < 0, "Lower key must be less than upper key. [lowerKey=%s, upperKey=%s]", getLowerKey(), getUpperKey());
  }

}
