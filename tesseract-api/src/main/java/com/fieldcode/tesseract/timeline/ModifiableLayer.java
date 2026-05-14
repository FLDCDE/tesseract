package com.fieldcode.tesseract.timeline;

import com.fieldcode.tesseract.Interval;
import com.fieldcode.tesseract.IntervalCollection;
import com.fieldcode.tesseract.Moment;
import com.fieldcode.tesseract.Presence;
import com.fieldcode.tesseract.PresenceSet;
import com.fieldcode.tesseract.Taggable.TagHolder;

public interface ModifiableLayer extends Layer {

  /**
   * Add an interval to the layer. Works only for INTERVAL type layers. In case of other types, exception will be thrown.
   * @param interval interval of the event
   * @return this
   */
  ModifiableLayer put(Interval interval);

  /**
   * Add an event to the layer. Works only for EVENT* type layers. In case of other types, exception will be thrown.
   * @param event event to add
   * @return this
   */
  ModifiableLayer put(TimelineEvent event);

  /**
   * Adds a presence to the layer. Works only for PRESENCE type layers. In case of other types, exception will be thrown.
   * @param presence presence of the event
   * @return this
   */
  ModifiableLayer put(Presence presence);

  /**
   * Adds all intervals to the layer. Works only for INTERVAL type layers. In case of other types, exception will be thrown.
   *
   * @param intervals intervals to add
   * @return this
   */
  ModifiableLayer putAll(IntervalCollection intervals);

  /**
   * Adds all events based on the given intervals and tags to the layer. Works only for EVENT* type layers. In case of other types, exception will be thrown.
   * @param intervals intervals to add
   * @param tags tags to add
   * @return this
   */
  ModifiableLayer putAll(IntervalCollection intervals, TagHolder tags);

  /**
   * Adds all presences to the layer. Works only for PRESENCE type layers. In case of other types, exception will be thrown.
   * @param presences presences to add
   * @return this
   */
  ModifiableLayer putAll(PresenceSet presences);

  /**
   * Removes a presence. Works only for PRESENCE type layers. In case of other types, exception will be thrown.
   * @param at moment of the presence
   * @return this
   */
  ModifiableLayer remove(Moment at);

  /**
   * Removes an interval from the layer. Works for all types of layers.
   * @param interval interval to remove
   * @return this
   */
  ModifiableLayer remove(Interval interval);

}
