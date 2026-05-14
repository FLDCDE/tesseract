package com.fieldcode.tesseract.timeline;

import com.fieldcode.tesseract.DirectedInterval;
import com.fieldcode.tesseract.FluentIterator;
import com.fieldcode.tesseract.Interval;
import com.fieldcode.tesseract.IntervalCollection;
import com.fieldcode.tesseract.Moment;
import com.fieldcode.tesseract.Movement;
import com.fieldcode.tesseract.Presence;
import com.fieldcode.tesseract.PresenceSet;
import com.fieldcode.tesseract.Taggable.Tag;
import com.fieldcode.tesseract.Taggable.TagHolder;

public abstract class InternalLayer implements ModifiableLayer {

  private final LayerDefinition definition;

  protected InternalLayer(LayerDefinition definition) {
    this.definition = definition;
  }

  public abstract InternalLayer copy();

  public abstract InternalLayer duplicate(String newId);

  protected LayerDefinition getDefinition() {
    return definition;
  }

  protected LayerDefinition copyDefinition(String newId) {
    return ImmutableLayerDefinition.builder()
        .from(definition)
        .id(newId)
        .build();
  }

  public boolean hasAllTags(Tag... tags) {
    for (Tag tag : tags) {
      if (!getDefinition().getTags().hasTag(tag)) {
        return false;
      }
    }
    return true;
  }

  @Override
  public String getId() {
    return definition.getId();
  }

  @Override
  public LayerType getType() {
    return definition.getType();
  }

  @Override
  public TagHolder getTags() {
    return definition.getTags();
  }

  @Override
  public FluentIterator<TimelineEvent> selectEvents(DirectedInterval directed) {
    return unsupported("selectEvents(DirectedInterval directed)");
  }

  @Override
  public FluentIterator<Presence> selectPresences(DirectedInterval directed) {
    return unsupported("selectPresences(DirectedInterval directed)");
  }

  @Override
  public FluentIterator<Movement> selectMovements(DirectedInterval directed) {
    return unsupported("selectMovements(DirectedInterval directed)");
  }

  @Override
  public FluentIterator<Interval> selectIntervals(DirectedInterval directed) {
    return unsupported("selectIntervals(DirectedInterval directed)");
  }

  @Override
  public IntervalCollection asIntervalCollection() {
    return unsupported("asIntervalCollection()");
  }

  @Override
  public PresenceSet asPresenceSet() {
    return unsupported("asPresenceSet()");
  }

  @Override
  public ModifiableLayer put(Interval interval) {
    return unsupported("put(Interval interval)");
  }

  @Override
  public ModifiableLayer put(TimelineEvent event) {
    return unsupported("put(TimelineEvent event)");
  }

  @Override
  public ModifiableLayer put(Presence presence) {
    return unsupported("putPresence(Moment at, Presence presence)");
  }

  @Override
  public ModifiableLayer putAll(IntervalCollection intervals) {
    return unsupported("putAll(IntervalCollection intervals)");
  }

  @Override
  public ModifiableLayer putAll(IntervalCollection intervals, TagHolder tags) {
    return unsupported("putAll(IntervalCollection intervals, TagHolder tags)");
  }

  @Override
  public ModifiableLayer putAll(PresenceSet presences) {
    return unsupported("putAll(PresenceSet presences)");
  }

  @Override
  public ModifiableLayer remove(Moment at) {
    return unsupported("removePresence(Moment at)");
  }

  @Override
  public ModifiableLayer remove(Interval interval) {
    return unsupported("remove(Interval interval)");
  }

  protected <T> T unsupported(String method) {
    throw new UnsupportedOperationException("Method " + method + " is not supported for " + getClass().getSimpleName());
  }


}
