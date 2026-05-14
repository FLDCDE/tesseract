package com.fieldcode.tesseract.timeline;

import com.fieldcode.tesseract.Interval;
import com.fieldcode.tesseract.IntervalCollection;
import com.fieldcode.tesseract.Presence;
import com.fieldcode.tesseract.PresenceSet;
import com.fieldcode.tesseract.Taggable.TagHolder;

public interface ModifiableTimeline extends Timeline {

  /**
   * Returns a modifiable layer with the given id. If the layer does not exist, exception is thrown.
   * @param layerId layer id
   * @return modifiable layer
   */
  @Override
  ModifiableLayer getLayer(String layerId);

  /**
   * Returns a timeline layers manager for layers management.
   * @return timeline layers manager
   */
  @Override
  TimelineLayersManager layers();

  /**
   * Add an event to the layer. Layer must be EVENT* type. See {@link ModifiableLayer#put(TimelineEvent)}.
   * @param layerId event layer id
   * @param event event to add
   * @return this
   */
  ModifiableTimeline put(String layerId, TimelineEvent event);

  /**
   * Add an interval to the layer. Layer must be INTERVAL type. See {@link ModifiableLayer#put(Interval)}.
   * @param layerId event layer id
   * @param interval interval to add
   * @return this
   */
  ModifiableTimeline put(String layerId, Interval interval);

  /**
   * Add a presence to the layer. Layer must be PRESENCE type. See {@link ModifiableLayer#put(Presence)}.
   * @param layerId event layer id
   * @param presence presence to add
   * @return this
   */
  ModifiableTimeline put(String layerId, Presence presence);

  /**
   * Add all intervals based on the given intervals to the specified layer. Layer must be INTERVAL type. See {@link ModifiableLayer#put(Interval)}.
   * @param layerId event layer id
   * @param intervals intervals to add
   * @return this
   */
  ModifiableTimeline putAll(String layerId, IntervalCollection intervals);

  /**
   * Add all events based on the given intervals and tags to the specified layer. Layer must be EVENT* type. See {@link ModifiableLayer#put(TimelineEvent)}.
   * @param layerId event layer id
   * @param intervals intervals to add
   * @param tags tags to add
   * @return this
   */
  ModifiableTimeline putAll(String layerId, IntervalCollection intervals, TagHolder tags);

  ModifiableTimeline putAll(String layerId, PresenceSet presences);

  /**
   * Clear all events or presences in the given layer within the given interval.
   * @param layerId event or presence layer id
   * @param interval interval to clear
   * @return this
   */
  ModifiableTimeline remove(String layerId, Interval interval);

}
