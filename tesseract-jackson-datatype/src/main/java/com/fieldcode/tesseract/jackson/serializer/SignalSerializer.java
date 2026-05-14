package com.fieldcode.tesseract.jackson.serializer;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fieldcode.tesseract.Signal;

@SuppressWarnings("rawtypes")
public class SignalSerializer extends JsonSerializer<Signal> {


  @Override
  public void serialize(Signal signal, JsonGenerator generator, SerializerProvider serializerProvider) throws IOException {
    generator.writeStartObject();
    serializerProvider.defaultSerializeField("key", signal.getKey(), generator);
    serializerProvider.defaultSerializeField("value", signal.getValue(), generator);
    generator.writeEndObject();
  }
}
