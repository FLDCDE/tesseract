package com.fieldcode.tesseract.moment;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.function.Function;

import org.immutables.value.Value.Immutable;
import org.immutables.value.Value.Parameter;

import com.fieldcode.tesseract.FiniteMoment;
import com.fieldcode.tesseract.InfiniteMoment;
import com.fieldcode.tesseract.Moment;
import com.fieldcode.tesseract.MomentMapper;
import com.google.common.base.Objects;

/**
 * A skeleton implementation of {@link FiniteMoment}. This class is immutable and thread-safe.  All methods are final.
 * <p>
 * This class is intended to be used as a base class for implementing {@link FiniteMoment} interfaces.  It is not intended to be used directly. To create a new {@link FiniteMoment}
 * implementation, extend this class and annotate it with {@link Immutable}.
 * </p>
 *
 * @see Immutable
 * @see FiniteMoment
 * @see Moment
 * @see MomentMapper
 * @since 1.0.0
 */
@Immutable
abstract class FiniteMomentSkeleton extends AbstractMoment implements FiniteMoment {

  @Override
  @Parameter
  public abstract OffsetDateTime getAt();

  @Override
  public boolean isFinite() {
    return true;
  }

  @Override
  public boolean isInfinite() {
    return false;
  }

  @Override
  public boolean isNegativeInfinite() {
    return false;
  }

  @Override
  public boolean isPositiveInfinite() {
    return false;
  }

  @Override
  public Moment min(Moment other) {
    return le(other) ? this : other;
  }

  @Override
  public Moment max(Moment other) {
    return ge(other) ? this : other;
  }

  @Override
  public FiniteMoment shift(Duration duration) {
    return withAt(getAt().plus(duration));
  }

  @Override
  public <T> T map(MomentMapper<T> mapper) {
    return mapper.map(this);
  }

  @Override
  public <T> T map(Function<FiniteMoment, T> finiteAction, Function<InfiniteMoment, T> infiniteAction) {
    return finiteAction.apply(this);
  }

  @Override
  public Moment withZoneIdSameMoment(ZoneId zoneId) {
    var convertedDateTime = getAt()
        .toZonedDateTime()
        .withZoneSameInstant(zoneId)
        .toOffsetDateTime();
    return Moments.moment(convertedDateTime);
  }

  @Override
  public int compareTo(Moment other) {
    if (other.isFinite()) {
      return getAt().toInstant().compareTo(other.getAt().toInstant());
    }
    return other.isPositiveInfinite() ? -1 : 1;
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(getAt());
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {return true;}
    if (obj instanceof Moment) {return eq((Moment) obj);}
    return false;
  }

  @Override
  public String toString() {
    return getAt().toString();
  }

  protected abstract FiniteMoment withAt(OffsetDateTime at);

}
