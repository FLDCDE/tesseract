package com.fieldcode.tesseract.timeline.events;

import java.util.Map;
import java.util.function.Function;

import com.fieldcode.tesseract.timeline.InternalLayer;
import com.fieldcode.tesseract.timeline.LayerDefinition;
import com.fieldcode.tesseract.timeline.LayerType;

import static com.fieldcode.tesseract.timeline.LayerType.EVENT_MERGE;
import static com.fieldcode.tesseract.timeline.LayerType.EVENT_OVERLAP;
import static com.fieldcode.tesseract.timeline.LayerType.EVENT_STRICT;
import static com.google.common.base.Preconditions.checkNotNull;

public class EventLayerFactory {

  private static final Map<LayerType, Function<LayerDefinition, InternalLayer>> CREATOR = Map.of(
      EVENT_MERGE, definition -> EventLayer.of(definition, MergeEventHolder.of()),
      EVENT_STRICT, definition -> EventLayer.of(definition, StrictEventHolder.of()),
      EVENT_OVERLAP, definition -> EventLayer.of(definition, OverlapEventHolder.of())
  );

  private EventLayerFactory() {}

  public static InternalLayer event(LayerDefinition definition) {
    var type = definition.getType();
    var creator = checkNotNull(CREATOR.get(type), "Layer type not supported: %s", type);
    return creator.apply(definition);
  }

}
