package com.fieldcode.tesseract.jackson.deserializer;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fieldcode.tesseract.IntervalContainer;
import com.fieldcode.tesseract.TaggedIntervalCollection;
import com.fieldcode.tesseract.container.IntervalContainers;
import com.fieldcode.tesseract.utils.iterator.Iterators;

public class IntervalContainerDeserializer extends JsonDeserializer<IntervalContainer> {

  @Override
  public IntervalContainer deserialize(JsonParser p, DeserializationContext ignore) throws IOException {

    JsonNode node = p.readValueAsTree();

    var taggedContainers = Iterators.fluent(node.elements())
        .map(n -> getTaggedIntervalCollection(p, n))
        .list();

    return IntervalContainers.container(taggedContainers);

  }

  private TaggedIntervalCollection getTaggedIntervalCollection(JsonParser p, JsonNode n) {
    try {
      return p.getCodec().treeToValue(n, TaggedIntervalCollection.class);
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }

}
