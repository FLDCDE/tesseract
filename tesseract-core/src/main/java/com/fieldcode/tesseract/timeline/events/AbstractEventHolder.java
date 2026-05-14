package com.fieldcode.tesseract.timeline.events;

import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;

import com.fieldcode.tesseract.DirectedInterval;
import com.fieldcode.tesseract.FluentIterator;
import com.fieldcode.tesseract.Interval;
import com.fieldcode.tesseract.IntervalCollection;
import com.fieldcode.tesseract.IntervalMap;
import com.fieldcode.tesseract.IntervalMap.IntervalEntry;
import com.fieldcode.tesseract.Moment;
import com.fieldcode.tesseract.Taggable.TagHolder;
import com.fieldcode.tesseract.collection.IntervalCollections;
import com.fieldcode.tesseract.interval.IntervalMaps;
import com.fieldcode.tesseract.timeline.ImmutableTimelineEvent;
import com.fieldcode.tesseract.timeline.TimelineEvent;
import com.fieldcode.tesseract.utils.iterator.Iterators;

import static com.fieldcode.tesseract.interval.Intervals.always;
import static java.util.stream.Collectors.toList;

abstract class AbstractEventHolder implements EventHolder {

  static final InternalTimelineEvent UNDEFINED_VALUE = ImmutableUndefinedIntervalEvent.of();
  static final Predicate<IntervalEntry<InternalTimelineEvent>> BY_NOT_UNDEFINED = entry -> !entry.getValue().equals(UNDEFINED_VALUE);
  static final Comparator<IntervalEntry<InternalTimelineEvent>> BY_INTERVAL_LOWER_ASC = Comparator.comparing(e -> e.getInterval().getLower());
  static final Comparator<IntervalEntry<InternalTimelineEvent>> BY_INTERVAL_UPPER_DESC = Comparator.<IntervalEntry<InternalTimelineEvent>, Moment>comparing(
      e -> e.getInterval().getUpper()).reversed();


  protected AbstractEventHolder() {
  }

  protected static IntervalMap<InternalTimelineEvent> copy(IntervalMap<InternalTimelineEvent> layer) {
    // TODO: 2024. 04. 08. Add factory copy method to IntervalMaps.
    var copied = IntervalMaps.disjoint(UNDEFINED_VALUE);
    layer.entries().forEach(entry -> copied.put(entry.getInterval(), entry.getValue()));
    return copied;
  }

  protected static List<IntervalMap<InternalTimelineEvent>> copy(List<IntervalMap<InternalTimelineEvent>> layers) {
    return layers.stream()
        .map(AbstractEventHolder::copy)
        .collect(toList());
  }

  FluentIterator<IntervalEntry<InternalTimelineEvent>> layerDefinedEvents(IntervalMap<InternalTimelineEvent> layer, DirectedInterval window) {
    return layer
        .entries(window.getInterval(), window.getDirection())
        .filter(BY_NOT_UNDEFINED);
  }

  boolean isLayerEmpty(IntervalMap<InternalTimelineEvent> layer) {
    return isLayerEmptyBetween(layer, always());
  }

  boolean isLayerEmptyBetween(IntervalMap<InternalTimelineEvent> layer, Interval window) {
    return !layerDefinedEvents(layer, window.forward()).hasNext();
  }

  FluentIterator<IntervalEntry<InternalTimelineEvent>> internalEvents(DirectedInterval window) {
    var comparator = window.getDirection().select(BY_INTERVAL_LOWER_ASC, BY_INTERVAL_UPPER_DESC);
    var sorted = getLayers()
        .flatMap(layer -> layerDefinedEvents(layer, window))
        .stream()
        .sorted(comparator) // We should add sorted method to FluentIterator;
        .iterator();
    return Iterators.fluent(sorted);
  }

  TimelineEvent toPublicEvent(IntervalEntry<InternalTimelineEvent> event) {
    var interval = event.getInterval();
    var tagHolder = event.getValue().getTags();
    return ImmutableTimelineEvent.of(interval, tagHolder);
  }

  InternalTimelineEvent event(TagHolder tags) {
    return ImmutableInternalTimelineEvent.of(tags);
  }

  @Override
  public String toString() {
    return EventHolderToString.toString(getLayers());
  }

  protected abstract FluentIterator<IntervalMap<InternalTimelineEvent>> getLayers();

  @Override
  public EventHolder put(TimelineEvent event) {
    return put(event.getInterval(), event.getTags());
  }

  @Override
  public FluentIterator<TimelineEvent> events(DirectedInterval window) {
    return Iterators.fluent(internalEvents(window))
        .map(this::toPublicEvent);
  }

  @Override
  public IntervalCollection intervals() {
    var intervalCollections = getLayers()
        .map(IntervalMap::intervals)
        .list();
    return IntervalCollections.or(intervalCollections);
  }

}
