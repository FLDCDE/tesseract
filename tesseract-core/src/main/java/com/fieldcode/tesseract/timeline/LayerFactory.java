package com.fieldcode.tesseract.timeline;

import java.util.Map;
import java.util.function.Function;

import com.fieldcode.tesseract.Taggable.TagHolder;
import com.fieldcode.tesseract.timeline.events.EventLayerFactory;
import com.fieldcode.tesseract.timeline.intervals.IntervalLayer;
import com.fieldcode.tesseract.timeline.presences.PresenceLayer;

import static com.fieldcode.tesseract.timeline.LayerType.EVENT_MERGE;
import static com.fieldcode.tesseract.timeline.LayerType.EVENT_OVERLAP;
import static com.fieldcode.tesseract.timeline.LayerType.EVENT_STRICT;
import static com.fieldcode.tesseract.timeline.LayerType.INTERVAL;
import static com.fieldcode.tesseract.timeline.LayerType.PRESENCE;
import static com.google.common.base.Preconditions.checkNotNull;

class LayerFactory {

  private static final Map<LayerType, Function<LayerDefinition, InternalLayer>> CREATORS = Map.of(
      PRESENCE, LayerFactory::presence,
      INTERVAL, LayerFactory::interval,
      EVENT_MERGE, LayerFactory::event,
      EVENT_STRICT, LayerFactory::event,
      EVENT_OVERLAP, LayerFactory::event
  );

  private LayerFactory() {}

  static InternalLayer layer(String id, LayerType type, TagHolder tags) {
    var definition = ImmutableLayerDefinition.of(id, type, tags);
    return layer(definition);
  }

  static InternalLayer layer(LayerDefinition definition) {
    var type = definition.getType();
    var creator = checkNotNull(CREATORS.get(type), "Layer type not supported: %s", type);
    return creator.apply(definition);
  }

  private static InternalLayer event(LayerDefinition definition) {
    return EventLayerFactory.event(definition);
  }

  private static InternalLayer interval(LayerDefinition definition) {
    return IntervalLayer.of(definition);
  }

  private static InternalLayer presence(LayerDefinition definition) {
    return PresenceLayer.of(definition);
  }

}
