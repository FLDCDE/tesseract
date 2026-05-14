package com.fieldcode.tesseract.jackson.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fieldcode.tesseract.Interval;
import com.fieldcode.tesseract.IntervalCollection;

import java.io.IOException;

public class IntervalCollectionSerializer extends JsonSerializer<IntervalCollection> {

  @Override
  public void serialize(IntervalCollection intervalCollection, JsonGenerator generator, SerializerProvider serializerProvider) throws IOException {

    var ser = serializerProvider.findValueSerializer(Interval.class);

    generator.writeStartArray();

    intervalCollection.intervals().forEach(a -> {
      try {
        ser.serialize(a, generator, serializerProvider);
      } catch (IOException e) {
        throw new IllegalStateException(e);
      }
    });

    generator.writeEndArray();
  }

}
