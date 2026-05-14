package com.fieldcode.tesseract.timeline;

import com.fieldcode.tesseract.DirectedInterval;
import com.fieldcode.tesseract.FluentIterator;
import com.fieldcode.tesseract.Interval;
import com.fieldcode.tesseract.IntervalCollection;
import com.fieldcode.tesseract.Movement;
import com.fieldcode.tesseract.Presence;
import com.fieldcode.tesseract.PresenceSet;

public interface Layer extends LayerDefinition {

  /**
   * Selects events from the layer. The events are filtered by the given directed interval.
   * Only EVENT* and INTERVAL layers are supported.
   * @param directed interval to filter events
   * @return iterator of events
   */
  FluentIterator<TimelineEvent> selectEvents(DirectedInterval directed);

  /**
   * Selects presences from the layer. The presences are filtered by the given directed interval.
   * Only PRESENCE layers are supported.
   * @param directed interval to filter presences
   * @return iterator of presences
   */
  FluentIterator<Presence> selectPresences(DirectedInterval directed);

  /**
   * Selects movements from the layer. The movements are filtered by the given directed interval.
   * Only PRESENCE layers are supported.
   * @param directed interval to filter movements
   * @return iterator of movements
   */
  FluentIterator<Movement> selectMovements(DirectedInterval directed);

  /**
   * Selects intervals from the layer. The intervals are filtered by the given directed interval.
   * Only EVENT* and INTERVAL layers are supported.
   * @param directed interval to filter intervals
   * @return iterator of intervals
   */
  FluentIterator<Interval> selectIntervals(DirectedInterval directed);

  /**
   * Returns the layer as an interval collection. Only EVENT* and INTERVAL layers are supported.
   * The returned collection is a copy of the layer data.
   * @return interval collection
   */
  IntervalCollection asIntervalCollection();

  /**
   * Returns the layer as a presence set. Only PRESENCE layers are supported.
   * The returned set is a copy of the layer data.
   * @return presence set
   */
  PresenceSet asPresenceSet();

}
