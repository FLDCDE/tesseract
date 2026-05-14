package com.fieldcode.tesseract.jackson.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fieldcode.tesseract.Presence;
import com.fieldcode.tesseract.PresenceCollection;

import java.io.IOException;

public class PresenceCollectionSerializer extends JsonSerializer<PresenceCollection> {

  @Override
  public void serialize(PresenceCollection presenceSet, JsonGenerator generator, SerializerProvider serializerProvider) throws IOException {
    var ser = serializerProvider.findValueSerializer(Presence.class);

    generator.writeStartArray();

    presenceSet.presences().forEach(a -> {
      try {
        ser.serialize(a, generator, serializerProvider);
      } catch (IOException e) {
        throw new IllegalStateException(e);
      }
    });

    generator.writeEndArray();
  }

}
