package com.fieldcode.tesseract;

import java.time.Duration;
import java.util.stream.Stream;

public interface Track {

  Presence getOrigin();

  Presence getDestination();

  Moment getStart();

  Moment getEnd();

  Distance getDistance();

  Duration getDuration();

  Stream<Presence> getLocationBounds();

  Interval getInterval();

  boolean isConnected(Track other);

}
