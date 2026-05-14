package com.fieldcode.tesseract.jackson.deserializer;

import java.io.IOException;
import java.time.Duration;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fieldcode.tesseract.DirectionFeature;
import com.fieldcode.tesseract.Distance;
import com.fieldcode.tesseract.Location;
import com.fieldcode.tesseract.direction.Directions;

public class TrackFeatureDeserializer extends JsonDeserializer<DirectionFeature> {


  @Override
  public DirectionFeature deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
    var tree = jsonParser.readValueAsTree();

    var origin = jsonParser.getCodec().treeToValue(tree.get("origin"), Location.class);
    var destination = jsonParser.getCodec().treeToValue(tree.get("destination"), Location.class);
    var distance = jsonParser.getCodec().treeToValue(tree.get("distance"), Distance.class);
    var duration = jsonParser.getCodec().treeToValue(tree.get("duration"), Duration.class);

    return Directions.feature(origin, destination, distance, duration);
  }
}
