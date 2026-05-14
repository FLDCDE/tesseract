package com.fieldcode.tesseract.timeline;

import com.fieldcode.tesseract.DirectedInterval;
import com.fieldcode.tesseract.FluentIterator;
import com.fieldcode.tesseract.Interval;
import com.fieldcode.tesseract.IntervalCollection;
import com.fieldcode.tesseract.Movement;
import com.fieldcode.tesseract.Presence;
import com.fieldcode.tesseract.PresenceSet;

public interface Timeline {

  Layer getLayer(String layerId);

  TimelineLayers layers();

  FluentIterator<TimelineEvent> selectEvents(String layerId, DirectedInterval directed);

  FluentIterator<Presence> selectPresences(String layerId, DirectedInterval directed);

  FluentIterator<Movement> selectMovements(String layerId, DirectedInterval directed);

  FluentIterator<Interval> selectIntervals(String layerId, DirectedInterval directed);

  IntervalCollection asIntervalCollection(String layerId);

  PresenceSet asPresenceSet(String layerId);

  ModifiableTimeline copy();

}
