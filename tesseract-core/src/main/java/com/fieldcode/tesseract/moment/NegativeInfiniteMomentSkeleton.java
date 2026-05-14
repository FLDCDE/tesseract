package com.fieldcode.tesseract.moment;

import org.immutables.value.Value.Immutable;

import com.fieldcode.tesseract.FiniteMoment;
import com.fieldcode.tesseract.InfiniteMoment;
import com.fieldcode.tesseract.Moment;
import com.fieldcode.tesseract.MomentMapper;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.function.Function;
import static com.fieldcode.tesseract.Constants.NEGATIVE_INFINITE_SIGN;
import static com.fieldcode.tesseract.Constants.POSITIVE_INFINITE_MOMENT_INSTANCE;
import static java.util.Objects.requireNonNull;

@Immutable(singleton = true)
abstract class NegativeInfiniteMomentSkeleton extends AbstractMoment implements InfiniteMoment {

  @Override
  public OffsetDateTime getAt() {
    throw new UnsupportedOperationException("Negative infinite moment has no at value");
  }

  @Override
  public boolean isFinite() {
    return false;
  }

  @Override
  public boolean isInfinite() {
    return true;
  }

  @Override
  public boolean isNegativeInfinite() {
    return true;
  }

  @Override
  public boolean isPositiveInfinite() {
    return false;
  }

  @Override
  public Moment min(Moment other) {
    return this;
  }

  @Override
  public Moment max(Moment other) {
    return other;
  }

  @Override
  public InfiniteMoment shift(Duration duration) {
    return this;
  }

  @Override
  public <T> T map(MomentMapper<T> mapper) {
    return mapper.map(this);
  }

  @Override
  public <T> T map(Function<FiniteMoment, T> finiteAction, Function<InfiniteMoment, T> infiniteAction) {
    return infiniteAction.apply(this);
  }

  @Override
  public Moment withZoneIdSameMoment(ZoneId zoneId) {
    return this;
  }

  @Override
  public int signum() {
    return -1;
  }

  @Override
  public InfiniteMoment abs() {
    return POSITIVE_INFINITE_MOMENT_INSTANCE;
  }

  @Override
  public InfiniteMoment negate() {
    return POSITIVE_INFINITE_MOMENT_INSTANCE;
  }

  @Override
  public boolean eq(Moment other) {
    return other == this;
  }

  @Override
  public boolean ne(Moment other) {
    return other != this;
  }

  @Override
  public int compareTo(Moment other) {
    return requireNonNull(other) == this ? 0 : -1;
  }

  @Override
  public String toString() {
    return NEGATIVE_INFINITE_SIGN;
  }

}
