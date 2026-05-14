package com.fieldcode.tesseract.timeline.events;

import com.fieldcode.tesseract.DirectedInterval;
import com.fieldcode.tesseract.FluentIterator;
import com.fieldcode.tesseract.Interval;
import com.fieldcode.tesseract.IntervalMap;
import com.fieldcode.tesseract.Taggable.TagHolder;
import com.fieldcode.tesseract.interval.IntervalMaps;
import com.fieldcode.tesseract.timeline.LayerType;
import com.fieldcode.tesseract.timeline.TimelineEvent;
import com.fieldcode.tesseract.utils.iterator.Iterators;
import com.google.common.base.Objects;

import static com.fieldcode.tesseract.timeline.LayerType.EVENT_MERGE;

class MergeEventHolder extends AbstractEventHolder implements EventHolder {

  private final IntervalMap<InternalTimelineEvent> events;

  protected MergeEventHolder(IntervalMap<InternalTimelineEvent> events) {
    super();
    this.events = events;
  }

  public static EventHolder of() {
    return new MergeEventHolder(IntervalMaps.disjoint(UNDEFINED_VALUE));
  }

  @Override
  public FluentIterator<TimelineEvent> events(DirectedInterval window) {
    return internalEvents(window)
        .filter(BY_NOT_UNDEFINED)
        .map(this::toPublicEvent);
  }

  @Override
  protected FluentIterator<IntervalMap<InternalTimelineEvent>> getLayers() {
    return Iterators.iterator(events);
  }

  @Override
  public EventHolder put(Interval window, TagHolder tags) {
    events.put(window, event(tags));
    return this;
  }


  @Override
  public EventHolder remove(Interval window) {
    events.remove(window);
    return this;
  }

  @Override
  public boolean isEmpty(Interval window) {
    return isLayerEmptyBetween(events, window);
  }

  @Override
  public LayerType getType() {
    return EVENT_MERGE;
  }

  @Override
  public EventHolder copy() {
    return new MergeEventHolder(copy(events));
  }

  protected IntervalMap<InternalTimelineEvent> getEventsMap() {
    return events;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {return true;}
    if (o == null || getClass() != o.getClass()) {return false;}
    MergeEventHolder that = (MergeEventHolder) o;
    return Objects.equal(events, that.events);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(events);
  }

}
