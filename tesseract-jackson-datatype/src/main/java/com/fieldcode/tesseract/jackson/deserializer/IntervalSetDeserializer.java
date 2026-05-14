package com.fieldcode.tesseract.jackson.deserializer;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fieldcode.tesseract.IntervalSet;
import com.fieldcode.tesseract.interval.IntervalSets;
import com.fieldcode.tesseract.interval.Intervals;
import com.fieldcode.tesseract.utils.iterator.Iterators;

public class IntervalSetDeserializer extends JsonDeserializer<IntervalSet> {

  @Override
  public IntervalSet deserialize(JsonParser p, DeserializationContext ignore) throws IOException {
    JsonNode node = p.readValueAsTree();

    var intervals = Iterators.fluent(node.elements())
        .map(JsonNode::textValue)
        .map(Intervals::parse)
        .list();

    return IntervalSets.disjoint(intervals);
  }

}
