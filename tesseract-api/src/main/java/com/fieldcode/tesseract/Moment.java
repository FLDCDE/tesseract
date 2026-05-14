package com.fieldcode.tesseract;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.function.Function;

/**
 * Moment is an extension of {@link OffsetDateTime} adding infinite features. Also adds some convenience methods for comparing moments and shifting them.
 */
public interface Moment extends Comparable<Moment> {

  /**
   * Returns the underlying {@link OffsetDateTime} of this moment. In infinite moments, this method will throw an {@link UnsupportedOperationException}.
   *
   * @return the underlying {@link OffsetDateTime} of this moment
   */
  OffsetDateTime getAt();

  /**
   * Returns true if this moment is finite, false if it is infinite.
   *
   * @return true if this moment is finite, false if it is infinite
   */
  boolean isFinite();

  /**
   * Returns true if this moment is infinite, false if it is finite.
   *
   * @return true if this moment is infinite, false if it is finite
   */
  boolean isInfinite();

  /**
   * Returns true if this moment is negative infinite, false if it is not.
   *
   * @return true if this moment is negative infinite, false if it is not
   */
  boolean isNegativeInfinite();

  /**
   * Returns true if this moment is positive infinite, false if it is not.
   *
   * @return true if this moment is positive infinite, false if it is not
   */
  boolean isPositiveInfinite();

  /**
   * Returns true if this moment is equal to the other moment, false if it is not.
   *
   * @param other moment to compare to
   * @return true if this moment is equal to the other moment, false if it is not
   */
  boolean eq(Moment other);

  /**
   * Returns true if this moment is not equal to the other moment, false if it is.
   *
   * @param other moment to compare to
   * @return true if this moment is not equal to the other moment, false if it is
   */
  boolean ne(Moment other);

  /**
   * Returns true if this moment is less than the other moment, false if it is not.
   *
   * @param other moment to compare to
   * @return true if this moment is less than the other moment, false if it is not
   */
  boolean lt(Moment other);

  /**
   * Returns true if this moment is less than or equal to the other moment, false if it is not.
   *
   * @param other moment to compare to
   * @return true if this moment is less than or equal to the other moment, false if it is not
   */
  boolean le(Moment other);

  /**
   * Returns true if this moment is greater than the other moment, false if it is not.
   *
   * @param other moment to compare to
   * @return true if this moment is greater than the other moment, false if it is not
   */
  boolean gt(Moment other);

  /**
   * Returns true if this moment is greater than or equal to the other moment, false if it is not.
   *
   * @param other moment to compare to
   * @return true if this moment is greater than or equal to the other moment, false if it is not
   */
  boolean ge(Moment other);

  /**
   * Returns the minimum of this moment and the other moment.
   *
   * @param other moment to compare to
   * @return the minimum of this moment and the other moment
   */
  Moment min(Moment other);

  /**
   * Returns the maximum of this moment and the other moment.
   *
   * @param other moment to compare to
   * @return the maximum of this moment and the other moment
   */
  Moment max(Moment other);

  /**
   * Returns a new instance of this moment shifted by the given duration. If this moment is finite, the returned moment will be finite. Infinite moments will be returned as is.
   *
   * @param duration duration to shift by
   * @return shifted moment
   */
  Moment shift(Duration duration);

  /**
   * Returns a new instance of this moment pulled to the given range. If this moment is less than the lower bound, the lower bound will be returned. If this moment is greater than
   * or equal to the upper bound, the upper bound will be returned. Otherwise, this moment will be returned.
   *
   * @param lower lower bound of the range
   * @param upper upper bound of the range
   * @return pulled moment
   */
  Moment pull(Moment lower, Moment upper);

  /**
   * Maps this moment to a different type.
   *
   * @param mapper mapper to use
   * @param <T> type to map to
   */
  <T> T map(MomentMapper<T> mapper);

  /**
   * Maps this moment to a different type.
   *
   * @param finiteAction action to perform on finite moments
   * @param infiniteAction action to perform on infinite moments
   * @param <T> type to map to
   */
  <T> T map(Function<FiniteMoment, T> finiteAction, Function<InfiniteMoment, T> infiniteAction);

  /**
   * Returns a new instance of this moment with the same instant but a different time zone. In the case of infinite moments, the same instance will be returned.
   *
   * @param zoneId time zone to use
   * @return moment with the same instant but a different time zone
   */
  Moment withZoneIdSameMoment(ZoneId zoneId);

  /**
   * Returns a new instance of this moment with the same instant but a different time zone. In the case of infinite moments, the same instance will be returned.
   * @param zoneId time zone to use
   * @return moment with the same instant but a different time zone
   */
  Moment withZoneIdSameMoment(String zoneId);

}
