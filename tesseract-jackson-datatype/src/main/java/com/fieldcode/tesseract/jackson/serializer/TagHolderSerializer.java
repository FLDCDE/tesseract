package com.fieldcode.tesseract.jackson.serializer;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fieldcode.tesseract.Taggable.TagHolder;

public class TagHolderSerializer extends JsonSerializer<TagHolder> {

  @Override
  public void serialize(TagHolder holder, JsonGenerator generator, SerializerProvider serializerProvider) throws IOException {
    generator.writeStartArray();

    holder.getTags().forEach(tag -> {
      try {
        serializerProvider.defaultSerializeValue(tag, generator);
      } catch (IOException e) {
        throw new IllegalStateException(e);
      }
    });
    generator.writeEndArray();
  }
}
