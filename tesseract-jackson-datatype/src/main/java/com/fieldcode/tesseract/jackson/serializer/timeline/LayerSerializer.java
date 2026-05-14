package com.fieldcode.tesseract.jackson.serializer.timeline;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fieldcode.tesseract.jackson.utils.FunctionalSerializer;
import com.fieldcode.tesseract.timeline.Layer;

public class LayerSerializer extends FunctionalSerializer<Layer> {

  public LayerSerializer(JsonGenerator generator, SerializerProvider sp) {
    super(generator, sp);
  }

  public static JsonSerializer<Layer> serializer() {
    return wrap(LayerSerializer::new);
  }

  @Override
  public void accept(Layer layer) {
    switch (layer.getType()) {
      case EVENT_STRICT:
      case EVENT_OVERLAP:
      case EVENT_MERGE:
        object(EventLayerSerializer::new, layer);
        break;
      case INTERVAL:
        object(IntervalLayerSerializer::new, layer);
        break;
      case PRESENCE:
        object(PresenceLayerSerializer::new, layer);
        break;
      default:
        throw new IllegalArgumentException("Unsupported layer type: " + layer.getType());
    }
  }


}
