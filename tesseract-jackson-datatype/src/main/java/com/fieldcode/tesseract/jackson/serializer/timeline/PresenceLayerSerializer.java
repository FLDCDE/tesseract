package com.fieldcode.tesseract.jackson.serializer.timeline;

import java.util.Set;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fieldcode.tesseract.interval.Intervals;
import com.fieldcode.tesseract.jackson.utils.FunctionalSerializer;
import com.fieldcode.tesseract.timeline.Layer;
import com.fieldcode.tesseract.timeline.LayerType;

import static com.fieldcode.tesseract.timeline.LayerType.PRESENCE;
import static com.google.common.base.Preconditions.checkArgument;

class PresenceLayerSerializer extends FunctionalSerializer<Layer> {

  private static final Set<LayerType> SUPPORTED = Set.of(PRESENCE);

  PresenceLayerSerializer(JsonGenerator generator, SerializerProvider sp) {
    super(generator, sp);
  }

  @Override
  public void accept(Layer layer) {
    checkArgument(SUPPORTED.contains(layer.getType()), "Layer type must be %s", SUPPORTED);
    var presences = layer.selectPresences(Intervals.always().forward()).list();
    object(
        () -> field("type", layer.getType()),
        () -> field("id", layer.getId()),
        () -> field("tags", layer.getTags()),
        () -> array("presences", presences, this::object)
    );
  }

}
