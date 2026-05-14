package com.fieldcode.tesseract.jackson.serializer;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fieldcode.tesseract.SignalMap;

@SuppressWarnings("rawtypes")
public class SignalMapSerializer extends JsonSerializer<SignalMap> {


  @Override
  public void serialize(SignalMap signalMap, JsonGenerator generator, SerializerProvider serializerProvider) throws IOException {
    generator.writeStartObject();
    generator.writeObjectField("default", signalMap.getDefaultValue());
    generator.writeArrayFieldStart("signals");

    // TODO: 2023. 01. 12. Whe should use map style serialization in case of the key can be serialize as string.
    // For example:
    //
    // {
    //   "default": -360000.000000000,
    //   "signals": {
    //     "-∞": -360000.000000000,
    //     "2023-01-11T14:00+01:00": -36000.000000000,
    //     "2023-01-12T00:00+01:00": 0.0,
    //     "2023-01-12T10:00+01:00": 36000.000000000,
    //     "∞": 360000.000000000
    //   }
    // }

    //noinspection unchecked
    signalMap.stream().forEach(s -> {
      try {
        serializerProvider.defaultSerializeValue(s, generator);
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    });

    generator.writeEndArray();
    generator.writeEndObject();
  }

}
