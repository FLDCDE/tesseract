package com.fieldcode.tesseract.interval;

import org.immutables.value.Value.Immutable;
import org.immutables.value.Value.Parameter;

import com.fieldcode.tesseract.Interval;
import com.fieldcode.tesseract.IntervalsRelation;

import static com.fieldcode.tesseract.interval.IntervalsRelationConstants.INTERVAL_CONTAINS;
import static com.fieldcode.tesseract.interval.IntervalsRelationConstants.INTERVAL_DURING;
import static com.fieldcode.tesseract.interval.IntervalsRelationConstants.INTERVAL_ENCLOSES;
import static com.fieldcode.tesseract.interval.IntervalsRelationConstants.INTERVAL_EQUALS_TO;
import static com.fieldcode.tesseract.interval.IntervalsRelationConstants.INTERVAL_FINISHES;
import static com.fieldcode.tesseract.interval.IntervalsRelationConstants.INTERVAL_IS_AFTER;
import static com.fieldcode.tesseract.interval.IntervalsRelationConstants.INTERVAL_IS_BEFORE;
import static com.fieldcode.tesseract.interval.IntervalsRelationConstants.INTERVAL_IS_FINISHED_BY;
import static com.fieldcode.tesseract.interval.IntervalsRelationConstants.INTERVAL_IS_MET_BY;
import static com.fieldcode.tesseract.interval.IntervalsRelationConstants.INTERVAL_IS_OVERLAPPED_BY;
import static com.fieldcode.tesseract.interval.IntervalsRelationConstants.INTERVAL_IS_STARTED_BY;
import static com.fieldcode.tesseract.interval.IntervalsRelationConstants.INTERVAL_MEETS;
import static com.fieldcode.tesseract.interval.IntervalsRelationConstants.INTERVAL_OVERLAPS;
import static com.fieldcode.tesseract.interval.IntervalsRelationConstants.INTERVAL_STARTS;


@Immutable
abstract class IntervalsRelationSkeleton implements IntervalsRelation {

  @Override
  @Parameter
  public abstract Interval getA();

  @Override
  @Parameter
  public abstract Interval getB();

  @Override
  @Parameter
  public abstract int getFlags();

  @Override
  public boolean isBefore() {
    return getFlags() == INTERVAL_IS_BEFORE;
  }

  @Override
  public boolean meets() {
    return getFlags() == INTERVAL_MEETS;
  }

  @Override
  public boolean overlaps() {
    return getFlags() == INTERVAL_OVERLAPS;
  }

  @Override
  public boolean starts() {
    return getFlags() == INTERVAL_STARTS;
  }

  @Override
  public boolean during() {
    return getFlags() == INTERVAL_DURING;
  }

  @Override
  public boolean finishes() {
    return getFlags() == INTERVAL_FINISHES;
  }

  @Override
  public boolean equalsTo() {
    return getFlags() == INTERVAL_EQUALS_TO;
  }

  @Override
  public boolean isFinishedBy() {
    return getFlags() == INTERVAL_IS_FINISHED_BY;
  }

  @Override
  public boolean contains() {
    return getFlags() == INTERVAL_CONTAINS;
  }

  @Override
  public boolean isStartedBy() {
    return getFlags() == INTERVAL_IS_STARTED_BY;
  }

  @Override
  public boolean isOverlappedBy() {
    return getFlags() == INTERVAL_IS_OVERLAPPED_BY;
  }

  @Override
  public boolean isMetBy() {
    return getFlags() == INTERVAL_IS_MET_BY;
  }

  @Override
  public boolean isAfter() {
    return getFlags() == INTERVAL_IS_AFTER;
  }

  @Override
  public boolean intersects() {
    return !distinct();
  }

  @Override
  public boolean distinct() {
    // TODO: 2023. 09. 18.  Use flags
    return isBefore() || isAfter() || adjacent();
  }

  @Override
  public boolean adjacent() {
    // TODO: 2023. 09. 18.  Use flags
    return meets() || isMetBy();
  }

  @Override
  public boolean connects() {
    // TODO: 2023. 09. 18.  Use flags
    return intersects() || adjacent();
  }

  @Override
  public boolean encloses() {
    return (getFlags() & INTERVAL_ENCLOSES) > 0;
  }

  @Override
  public String toString() {
    return IntervalRelationToString.generateToString(this);
  }

}
