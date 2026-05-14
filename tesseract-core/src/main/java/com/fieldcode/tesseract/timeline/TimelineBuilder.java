package com.fieldcode.tesseract.timeline;

import java.util.HashMap;
import java.util.Map;

import com.fieldcode.tesseract.Taggable.TagHolder;
import com.fieldcode.tesseract.tag.Tags;

import static com.fieldcode.tesseract.timeline.LayerType.EVENT_MERGE;
import static com.fieldcode.tesseract.timeline.LayerType.EVENT_OVERLAP;
import static com.fieldcode.tesseract.timeline.LayerType.EVENT_STRICT;
import static com.fieldcode.tesseract.timeline.LayerType.INTERVAL;
import static com.fieldcode.tesseract.timeline.LayerType.PRESENCE;
import static com.google.common.base.Preconditions.checkArgument;

public class TimelineBuilder {

  private static final TagHolder EMPTY_TAGS = Tags.holder();

  private final Map<String, LayerDefinition> eventLayerDefinitions = new HashMap<>();

  private TimelineBuilder() {}

  public static TimelineBuilder of() {
    return new TimelineBuilder();
  }

  private void checkLayerId(String layerId) {
    var layers = eventLayerDefinitions.keySet();
    checkArgument(!eventLayerDefinitions.containsKey(layerId), "InternalLayer already defined [layerId=%s, reserved=%s]", layerId, layers);
  }

  public TimelineBuilder layer(String layerId, LayerType type, TagHolder tags) {
    checkLayerId(layerId);
    var definition = ImmutableLayerDefinition.of(layerId, type, tags);
    eventLayerDefinitions.put(layerId, definition);
    return this;
  }

  public TimelineBuilder strict(String layerId) {
    return layer(layerId, EVENT_STRICT, EMPTY_TAGS);
  }

  public TimelineBuilder strict(String layerId, TagHolder tags) {
    return layer(layerId, EVENT_STRICT, tags);
  }

  public TimelineBuilder overlap(String layerId) {
    return layer(layerId, EVENT_OVERLAP, EMPTY_TAGS);
  }

  public TimelineBuilder overlap(String layerId, TagHolder tags) {
    return layer(layerId, EVENT_OVERLAP, tags);
  }

  public TimelineBuilder merge(String layerId) {
    return layer(layerId, EVENT_MERGE, EMPTY_TAGS);
  }

  public TimelineBuilder merge(String layerId, TagHolder tags) {
    return layer(layerId, EVENT_MERGE, tags);
  }

  public TimelineBuilder presence(String layerId, TagHolder tags) {
    return layer(layerId, PRESENCE, tags);
  }

  public TimelineBuilder presence(String layerId) {
    return layer(layerId, PRESENCE, EMPTY_TAGS);
  }

  public TimelineBuilder interval(String layerId, TagHolder tags) {
    return layer(layerId, INTERVAL, tags);
  }

  public TimelineBuilder interval(String layerId) {
    return layer(layerId, INTERVAL, EMPTY_TAGS);
  }

  public ModifiableTimeline build() {
    var manager = InternalLayerManagerFactory.manager(eventLayerDefinitions.values());
    return ModifiableTimelineImpl.of(manager);
  }

}
