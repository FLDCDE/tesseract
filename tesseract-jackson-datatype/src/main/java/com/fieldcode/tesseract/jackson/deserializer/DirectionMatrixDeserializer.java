package com.fieldcode.tesseract.jackson.deserializer;

import java.io.IOException;
import java.util.Arrays;
import java.util.TreeSet;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fieldcode.tesseract.DirectionMatrix;
import com.fieldcode.tesseract.Location;
import com.fieldcode.tesseract.matrix.DirectionMatrices;

public class DirectionMatrixDeserializer extends JsonDeserializer<DirectionMatrix> {


  @Override
  public DirectionMatrix deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
    var tree = jsonParser.readValueAsTree();

    var distancesInMeter = jsonParser.getCodec().treeToValue(tree.get("distancesInMeter"), long[][].class);
    var durationsInSeconds = jsonParser.getCodec().treeToValue(tree.get("durationsInSeconds"), long[][].class);
    var locations = jsonParser.getCodec().treeToValue(tree.get("locations"), Location[].class);
    var locationSet = Arrays.stream(locations).collect(Collectors.toCollection(TreeSet::new));

    return DirectionMatrices.matrix(locationSet, distancesInMeter, durationsInSeconds);
  }
}
