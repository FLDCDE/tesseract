package com.fieldcode.tesseract.jackson.deserializer;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fieldcode.tesseract.IntervalCollection;
import com.fieldcode.tesseract.Taggable.TagHolder;
import com.fieldcode.tesseract.TaggedIntervalCollection;
import com.fieldcode.tesseract.collection.ImmutableTaggedIntervalCollection;

public class TaggedIntervalCollectionDeserializer extends JsonDeserializer<TaggedIntervalCollection> {

  @Override
  public TaggedIntervalCollection deserialize(JsonParser p, DeserializationContext ignore) throws IOException {

    JsonNode node = p.readValueAsTree();

    var id = node.get("id").asText();

    var tags = p.getCodec().treeToValue(node.get("tags"), TagHolder.class);

    var collectionNode = node.get("collection");

    var collection = p.getCodec().treeToValue(collectionNode, IntervalCollection.class);

    return ImmutableTaggedIntervalCollection.of(id, collection, tags);

  }

}
