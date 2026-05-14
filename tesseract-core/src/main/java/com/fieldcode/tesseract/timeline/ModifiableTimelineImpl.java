package com.fieldcode.tesseract.timeline;

import com.fieldcode.tesseract.DirectedInterval;
import com.fieldcode.tesseract.FluentIterator;
import com.fieldcode.tesseract.Interval;
import com.fieldcode.tesseract.IntervalCollection;
import com.fieldcode.tesseract.Movement;
import com.fieldcode.tesseract.Presence;
import com.fieldcode.tesseract.PresenceSet;
import com.fieldcode.tesseract.Taggable.TagHolder;
import com.google.common.base.Objects;

class ModifiableTimelineImpl implements ModifiableTimeline {

  private final InternalTimelineLayersManager layers;

  private ModifiableTimelineImpl(InternalTimelineLayersManager layers) {
    this.layers = layers;
  }

  static ModifiableTimelineImpl of(InternalTimelineLayersManager layers) {
    return new ModifiableTimelineImpl(layers);
  }

  @Override
  public InternalLayer getLayer(String layerId) {
    return layers.layer(layerId);
  }

  @Override
  public TimelineLayersManager layers() {
    return layers;
  }

  @Override
  public ModifiableTimeline put(String layerId, TimelineEvent event) {
    getLayer(layerId).put(event);
    return this;
  }

  @Override
  public ModifiableTimeline put(String layerId, Interval interval) {
    getLayer(layerId).put(interval);
    return this;
  }

  @Override
  public ModifiableTimeline put(String layerId, Presence presence) {
    getLayer(layerId).put(presence);
    return this;
  }

  @Override
  public ModifiableTimeline putAll(String layerId, IntervalCollection intervals) {
    getLayer(layerId).putAll(intervals);
    return this;
  }

  @Override
  public ModifiableTimeline putAll(String layerId, IntervalCollection intervals, TagHolder tags) {
    getLayer(layerId).putAll(intervals, tags);
    return this;
  }

  @Override
  public ModifiableTimeline putAll(String layerId, PresenceSet presences) {
    var layer = getLayer(layerId);
    presences.presences()
        .forEach(layer::put);
    return this;
  }

  @Override
  public ModifiableTimeline remove(String layerId, Interval interval) {
    getLayer(layerId).remove(interval);
    return this;
  }

  @Override
  public FluentIterator<TimelineEvent> selectEvents(String layerId, DirectedInterval directed) {
    return getLayer(layerId).selectEvents(directed);
  }

  @Override
  public FluentIterator<Presence> selectPresences(String layerId, DirectedInterval directed) {
    return getLayer(layerId).selectPresences(directed);
  }

  @Override
  public FluentIterator<Movement> selectMovements(String layerId, DirectedInterval directed) {
    return getLayer(layerId).selectMovements(directed);
  }

  @Override
  public FluentIterator<Interval> selectIntervals(String layerId, DirectedInterval directed) {
    return getLayer(layerId).selectIntervals(directed);
  }

  @Override
  public IntervalCollection asIntervalCollection(String layerId) {
    return getLayer(layerId).asIntervalCollection();
  }

  @Override
  public PresenceSet asPresenceSet(String layerId) {
    return getLayer(layerId).asPresenceSet();
  }

  @Override
  public ModifiableTimeline copy() {
    return new ModifiableTimelineImpl(layers.copy());
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(layers());
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {return true;}
    if (o == null || getClass() != o.getClass()) {return false;}
    ModifiableTimeline that = (ModifiableTimeline) o;

    return Objects.equal(this.layers(), that.layers());
  }

}
