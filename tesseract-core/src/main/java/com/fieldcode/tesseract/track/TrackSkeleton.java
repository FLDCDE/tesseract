package com.fieldcode.tesseract.track;

import java.time.Duration;
import java.util.stream.Stream;

import org.immutables.value.Value.Immutable;
import org.immutables.value.Value.Parameter;

import com.fieldcode.tesseract.Distance;
import com.fieldcode.tesseract.Interval;
import com.fieldcode.tesseract.Moment;
import com.fieldcode.tesseract.Presence;
import com.fieldcode.tesseract.Track;
import com.fieldcode.tesseract.interval.Intervals;


@Immutable
abstract class TrackSkeleton implements Track {

  @Override
  @Parameter
  public abstract Presence getOrigin();

  @Override
  @Parameter
  public abstract Presence getDestination();

  @Override
  public Moment getStart() {
    return getOrigin().getMoment();
  }

  @Override
  public Moment getEnd() {
    return getDestination().getMoment();
  }

  @Override
  @Parameter
  public abstract Distance getDistance();

  @Override
  public Duration getDuration() {
    return Duration.between(getOrigin().getMoment().getAt(), getDestination().getMoment().getAt());
  }

  @Override
  public Stream<Presence> getLocationBounds() {
    return Stream.of(getOrigin(), getDestination());
  }

  @Override
  public Interval getInterval() {
    return Intervals.interval(getStart(), getEnd());
  }

  @Override
  public boolean isConnected(Track other) {
    return getInterval().getRelationTo(other.getInterval()).connects();
  }

}
