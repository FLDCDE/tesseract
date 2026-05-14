package com.fieldcode.tesseract.jackson.serializer.timeline;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fieldcode.tesseract.jackson.utils.FunctionalSerializer;
import com.fieldcode.tesseract.timeline.Timeline;

public class TimelineSerializer extends FunctionalSerializer<Timeline> {

  public TimelineSerializer(JsonGenerator generator, SerializerProvider sp) {
    super(generator, sp);
  }

  public static JsonSerializer<Timeline> serializer() {
    return wrap(TimelineSerializer::new);
  }

  @Override
  public void accept(Timeline timeline) {
    var layers = timeline.layers().all();
    object(
        () -> array("layers", layers, this::object)
    );
  }

}
