package com.fieldcode.tesseract.distance;

import org.immutables.value.Value.Check;
import org.immutables.value.Value.Immutable;
import org.immutables.value.Value.Parameter;

import com.fieldcode.tesseract.Distance;

import static com.google.common.base.Preconditions.checkArgument;

@Immutable
abstract class DistanceSkeleton implements Distance {

  @Override
  @Parameter
  public abstract long toMeters();

  @Override
  public Distance plus(Distance other) {
    var sum = toMeters() + other.toMeters();
    return Distances.ofMeters(sum);
  }

  @Override
  public boolean lt(Distance other) {
    return toMeters() < other.toMeters();
  }

  @Override
  public boolean le(Distance other) {
    return toMeters() <= other.toMeters();
  }

  @Override
  public boolean gt(Distance other) {
    return toMeters() > other.toMeters();
  }

  @Override
  public boolean ge(Distance other) {
    return toMeters() >= other.toMeters();
  }

  @Override
  public boolean eq(Distance other) {
    return toMeters() == other.toMeters();
  }

  @Override
  public boolean ne(Distance other) {
    return toMeters() != other.toMeters();
  }

  @Override
  public String toString() {
    return toMeters() + "m";
  }

  @Override
  public int compareTo(Distance o) {
    return Long.compare(toMeters(), o.toMeters());
  }

  @Check
  protected void check() {
    checkArgument(toMeters() >= 0, "Distance must be non-negative [%s]", toMeters());
  }

}
