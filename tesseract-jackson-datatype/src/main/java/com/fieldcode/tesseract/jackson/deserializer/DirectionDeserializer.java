package com.fieldcode.tesseract.jackson.deserializer;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fieldcode.tesseract.Direction;
import com.fieldcode.tesseract.Location;
import com.fieldcode.tesseract.direction.Directions;

public class DirectionDeserializer extends JsonDeserializer<Direction> {

  @Override
  public Direction deserialize(JsonParser jp, DeserializationContext deserializationContext) throws IOException {
    var tree = jp.readValueAsTree();
    var origin = jp.getCodec().treeToValue(tree.get("origin"), Location.class);
    var destination = jp.getCodec().treeToValue(tree.get("destination"), Location.class);
    return Directions.direction(origin, destination);
  }

}
