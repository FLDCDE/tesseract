package com.fieldcode.tesseract.jackson.deserializer;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fieldcode.tesseract.Taggable.Tag;
import com.fieldcode.tesseract.Taggable.TagHolder;
import com.fieldcode.tesseract.tag.Tags;
import com.fieldcode.tesseract.utils.iterator.Iterators;

public class TagHolderDeserializer extends JsonDeserializer<TagHolder> {


  @Override
  public TagHolder deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
    var tree = jsonParser.readValueAsTree();

    if (tree.isArray()) {
      return deserialize(jsonParser, (ArrayNode) tree);
    }

    throw new IllegalStateException("Unexpected tag type:" + tree);
  }

  private TagHolder deserialize(JsonParser jsonParser, ArrayNode tree) {

    if (tree.isEmpty()) {
      return Tags.holder();
    }

    var tags = Iterators.fluent(tree.elements())
        .map(e -> getTag(jsonParser, e))
        .set();

    return Tags.holder(tags);
  }

  private Tag getTag(JsonParser jsonParser, JsonNode e) {
    try {
      return jsonParser.getCodec().treeToValue(e, Tag.class);
    } catch (JsonProcessingException ex) {
      throw new IllegalStateException("Unexpected tag type:" + e);
    }
  }

}
