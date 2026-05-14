package com.fieldcode.tesseract.jackson.deserializer;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.node.TextNode;
import com.fieldcode.tesseract.Distance;
import com.fieldcode.tesseract.DistanceUnit;
import com.fieldcode.tesseract.distance.Distances;

public class DistanceDeserializer extends JsonDeserializer<Distance> {


  @Override
  public Distance deserialize(JsonParser parser, DeserializationContext deserializationContext) throws IOException {
    var tree = parser.readValueAsTree();
    var meters = ((TextNode) tree).textValue();
    if (meters != null) {
      if (meters.contains("m")) {
        String[] values = meters.split("m");
        var distance = values[0];
        return Distances.distance(Long.parseLong(distance), DistanceUnit.METER);
      }
    }

    // TODO: 2023. 01. 12. Maybe we should raise an error
    return null;
  }
}
