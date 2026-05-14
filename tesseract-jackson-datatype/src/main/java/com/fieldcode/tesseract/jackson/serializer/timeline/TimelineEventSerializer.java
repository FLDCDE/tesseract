package com.fieldcode.tesseract.jackson.serializer.timeline;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fieldcode.tesseract.jackson.utils.FunctionalSerializer;
import com.fieldcode.tesseract.timeline.TimelineEvent;

public class TimelineEventSerializer extends FunctionalSerializer<TimelineEvent> {

  public TimelineEventSerializer(JsonGenerator generator, SerializerProvider serializerProvider) {
    super(generator, serializerProvider);
  }

  public static JsonSerializer<TimelineEvent> serializer() {
    return wrap(TimelineEventSerializer::new);
  }

  @Override
  public void accept(TimelineEvent value) {
    object(
        () -> field("interval", value.getInterval()),
        () -> field("tags", value.getTags())
    );
  }

}
