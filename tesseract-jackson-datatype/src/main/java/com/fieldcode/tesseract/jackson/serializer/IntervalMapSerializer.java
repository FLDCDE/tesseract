package com.fieldcode.tesseract.jackson.serializer;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fieldcode.tesseract.IntervalMap;
import com.fieldcode.tesseract.IntervalMap.IntervalEntry;

@SuppressWarnings("rawtypes")
public class IntervalMapSerializer extends JsonSerializer<IntervalMap> {


  @Override
  public void serialize(IntervalMap map, JsonGenerator generator, SerializerProvider serializer) throws IOException {
    generator.writeStartObject();
    serializer.defaultSerializeField("default", map.getDefaultValue(), generator);

    generator.writeArrayFieldStart("entries");

    var ser = serializer.findValueSerializer(IntervalEntry.class);

    map.entries().forEach(a -> {
      try {
        ser.serialize(a, generator, serializer);
      } catch (IOException e) {
        throw new IllegalStateException(e);
      }
    });

    generator.writeEndArray();

    generator.writeEndObject();
  }
}
