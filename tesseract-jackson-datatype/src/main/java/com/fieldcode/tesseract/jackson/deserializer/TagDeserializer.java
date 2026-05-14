package com.fieldcode.tesseract.jackson.deserializer;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fieldcode.tesseract.Taggable.Tag;
import com.fieldcode.tesseract.tag.Tags;

public class TagDeserializer extends JsonDeserializer<Tag> {


  @Override
  public Tag deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
    var tree = jsonParser.readValueAsTree();

    if (tree.isArray()) {
      return deserialize((ArrayNode) tree);
    }

    throw new IllegalStateException("Unexpected tag type:" + tree);

  }

  private Tag deserialize(ArrayNode tree) {

    if (tree.isEmpty() && tree.size() > 2) {
      throw new IllegalStateException("Unexpected tag size:" + tree.size());
    }

    if (tree.size() == 1) {
      return Tags.tag(tree.get(0).asText());
    }

    return Tags.tag(tree.get(0).asText(), tree.get(1).asText());
  }

}
