package com.fieldcode.tesseract.jackson.serializer;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fieldcode.tesseract.Interval;
import com.fieldcode.tesseract.IntervalSet;

public class IntervalSetSerializer extends JsonSerializer<IntervalSet> {


  @Override
  public void serialize(IntervalSet intervalSet, JsonGenerator generator, SerializerProvider serializerProvider) throws IOException {

    var ser = serializerProvider.findValueSerializer(Interval.class);

    generator.writeStartArray();

    intervalSet.intervals().forEach(a -> {
      try {
        ser.serialize(a, generator, serializerProvider);
      } catch (IOException e) {
        throw new IllegalStateException(e);
      }
    });

    generator.writeEndArray();
  }

}
