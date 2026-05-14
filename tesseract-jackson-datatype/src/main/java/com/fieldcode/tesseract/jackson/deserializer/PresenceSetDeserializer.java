package com.fieldcode.tesseract.jackson.deserializer;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fieldcode.tesseract.Presence;
import com.fieldcode.tesseract.PresenceSet;
import com.fieldcode.tesseract.presence.PresenceSets;
import com.google.common.collect.Streams;

public class PresenceSetDeserializer extends JsonDeserializer<PresenceSet> {

  @Override
  public PresenceSet deserialize(JsonParser parser, DeserializationContext ignore) throws IOException {
    JsonNode node = parser.readValueAsTree();

    var iterator = node.elements();

    //noinspection UnstableApiUsage
    var presences = Streams
        .stream(iterator)
        .map(n -> presence(parser, n));

    return PresenceSets.presences(presences);
  }

  private Presence presence(JsonParser parser, JsonNode node) {
    try {
      return parser.getCodec().treeToValue(node, Presence.class);
    } catch (JsonProcessingException e) {
      throw new IllegalStateException("Unexpected presence type:" + node);
    }
  }

}
