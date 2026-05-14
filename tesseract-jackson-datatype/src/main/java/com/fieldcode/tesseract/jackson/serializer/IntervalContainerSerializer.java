package com.fieldcode.tesseract.jackson.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fieldcode.tesseract.IntervalContainer;
import com.fieldcode.tesseract.TaggedIntervalCollection;

import java.io.IOException;

public class IntervalContainerSerializer extends JsonSerializer<IntervalContainer> {

  @Override
  public void serialize(IntervalContainer container, JsonGenerator generator, SerializerProvider serializerProvider) throws IOException {

    var ser = serializerProvider.findValueSerializer(TaggedIntervalCollection.class);

    generator.writeStartArray();

    container.getAll().forEach(a -> {
      try {
        ser.serialize(a, generator, serializerProvider);
      } catch (IOException e) {
        throw new IllegalStateException(e);
      }
    });

    generator.writeEndArray();

  }

}
