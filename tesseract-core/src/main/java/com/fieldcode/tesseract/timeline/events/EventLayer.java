package com.fieldcode.tesseract.timeline.events;

import com.fieldcode.tesseract.DirectedInterval;
import com.fieldcode.tesseract.FluentIterator;
import com.fieldcode.tesseract.Interval;
import com.fieldcode.tesseract.IntervalCollection;
import com.fieldcode.tesseract.Taggable.TagHolder;
import com.fieldcode.tesseract.timeline.InternalLayer;
import com.fieldcode.tesseract.timeline.LayerDefinition;
import com.fieldcode.tesseract.timeline.ModifiableLayer;
import com.fieldcode.tesseract.timeline.TimelineEvent;
import com.fieldcode.tesseract.timeline.TimelineEvents;
import com.google.common.base.Objects;

import static com.google.common.base.Preconditions.checkState;

public class EventLayer extends InternalLayer {

  private final EventHolder events;

  public EventLayer(LayerDefinition definition, EventHolder events) {
    super(definition);
    checkState(definition.getType() == events.getType(), "Layer type must be %s", events.getType());
    this.events = events;
  }

  public static EventLayer of(LayerDefinition definition, EventHolder events) {
    return new EventLayer(definition, events);
  }

  @Override
  public InternalLayer copy() {
    return new EventLayer(getDefinition(), events.copy());
  }

  @Override
  public InternalLayer duplicate(String newId) {
    return new EventLayer(copyDefinition(newId), events.copy());
  }

  @Override
  public FluentIterator<TimelineEvent> selectEvents(DirectedInterval directed) {
    return events.events(directed);
  }

  @Override
  public FluentIterator<Interval> selectIntervals(DirectedInterval directed) {
    return events.intervals().intervals(directed);
  }

  @Override
  public IntervalCollection asIntervalCollection() {
    return events.intervals();
  }

  @Override
  public ModifiableLayer put(TimelineEvent event) {
    events.put(event);
    return this;
  }

  @Override
  public ModifiableLayer putAll(IntervalCollection intervals, TagHolder tags) {
    intervals.intervals()
        .forEach(interval -> events.put(TimelineEvents.event(interval, tags)));
    return this;
  }

  @Override
  public ModifiableLayer remove(Interval interval) {
    events.remove(interval);
    return this;
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(events);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {return true;}
    if (o == null || getClass() != o.getClass()) {return false;}

    var that = (EventLayer) o;

    return Objects.equal(events, that.events);
  }
}
