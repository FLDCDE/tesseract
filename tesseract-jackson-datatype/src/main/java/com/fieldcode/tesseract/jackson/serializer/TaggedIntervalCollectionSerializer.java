package com.fieldcode.tesseract.jackson.serializer;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fieldcode.tesseract.Interval;
import com.fieldcode.tesseract.TaggedIntervalCollection;

public class TaggedIntervalCollectionSerializer extends JsonSerializer<TaggedIntervalCollection> {

  @Override
  public void serialize(TaggedIntervalCollection intervalCollection, JsonGenerator generator, SerializerProvider serializerProvider) throws IOException {

    var ser = serializerProvider.findValueSerializer(Interval.class);

    generator.writeStartObject();
    serializerProvider.defaultSerializeField("id", intervalCollection.getId(), generator);
    serializerProvider.defaultSerializeField("tags", intervalCollection.getTags(), generator);

    generator.writeFieldName("collection");

    generator.writeStartArray();

    intervalCollection.intervals().forEach(a -> {
      try {
        ser.serialize(a, generator, serializerProvider);
      } catch (IOException e) {
        throw new IllegalStateException(e);
      }
    });

    generator.writeEndArray();

    generator.writeEndObject();

  }

}
