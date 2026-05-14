package com.fieldcode.tesseract.jackson.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fieldcode.tesseract.Presence;
import com.fieldcode.tesseract.PresenceCollection;
import com.fieldcode.tesseract.presence.PresenceSets;
import com.google.common.collect.Streams;

import java.io.IOException;

public class PresenceCollectionDeserializer extends JsonDeserializer<PresenceCollection> {

  @Override
  public PresenceCollection deserialize(JsonParser parser, DeserializationContext ignore) throws IOException {
    JsonNode node = parser.readValueAsTree();

    var iterator = node.elements();

    //noinspection UnstableApiUsage
    var presences = Streams
        .stream(iterator)
        .map(n -> presence(parser, n));

    return PresenceSets.immutable(presences);
  }

  private Presence presence(JsonParser parser, JsonNode node) {
    try {
      return parser.getCodec().treeToValue(node, Presence.class);
    } catch (JsonProcessingException e) {
      throw new IllegalStateException("Unexpected presence type:" + node);
    }
  }

}
