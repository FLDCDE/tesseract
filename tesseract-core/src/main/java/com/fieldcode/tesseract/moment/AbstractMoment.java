package com.fieldcode.tesseract.moment;

import com.fieldcode.tesseract.Moment;

import java.time.ZoneId;
import static com.google.common.base.Preconditions.checkArgument;

abstract class AbstractMoment implements Moment {

  @Override
  public boolean eq(Moment other) {
    return compareTo(other) == 0;
  }

  @Override
  public boolean ne(Moment other) {
    return compareTo(other) != 0;
  }

  @Override
  public boolean lt(Moment other) {
    return compareTo(other) < 0;
  }

  @Override
  public boolean le(Moment other) {
    return compareTo(other) <= 0;
  }

  @Override
  public boolean gt(Moment other) {
    return compareTo(other) > 0;
  }

  @Override
  public boolean ge(Moment other) {
    return compareTo(other) >= 0;
  }

  @Override
  public Moment pull(Moment lower, Moment upper) {
    checkArgument(lower.lt(upper), "Lower must be less than upper. [lower=%s, upper=%s]", lower, upper);

    if (lt(lower)) {
      return lower;
    }

    if (ge(upper)) {
      return upper;
    }

    return this;
  }

  @Override
  public Moment withZoneIdSameMoment(String zoneId) {
    return withZoneIdSameMoment(ZoneId.of(zoneId));
  }

}
