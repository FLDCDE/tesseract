package com.fieldcode.tesseract.jackson.serializer;

import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fieldcode.tesseract.Taggable.NamedTag;
import com.fieldcode.tesseract.Taggable.Tag;

public class TagSerializer extends JsonSerializer<Tag> {

  @Override
  public void serialize(Tag tag, JsonGenerator generator, SerializerProvider serializerProvider) throws IOException {

    var value = tag.getValue();

    if (value.isPresent()) {
      serialize(generator, List.of(tag.getName(), value.get()));
      return;
    }

    if (tag instanceof NamedTag) {
      var keyTag = (NamedTag) tag;
      serialize(generator, List.of(keyTag.getName()));
    }

  }

  private void serialize(JsonGenerator generator, List<String> values) throws IOException {
    generator.writeStartArray();
    for (String value : values) {
      generator.writeString(value);
    }
    generator.writeEndArray();
  }

}
