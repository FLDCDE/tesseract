package com.fieldcode.tesseract.jackson.deserializer;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fieldcode.tesseract.IntervalCollection;
import com.fieldcode.tesseract.collection.IntervalCollections;
import com.fieldcode.tesseract.interval.IntervalSets;
import com.fieldcode.tesseract.interval.Intervals;
import com.fieldcode.tesseract.utils.iterator.Iterators;

public class IntervalCollectionDeserializer extends JsonDeserializer<IntervalCollection> {

  @Override
  public IntervalCollection deserialize(JsonParser p, DeserializationContext ignore) throws IOException {
    JsonNode node = p.readValueAsTree();

    if (node.isValueNode()) {
      return IntervalSets.disjoint(Intervals.parse(node.textValue()));
    }

    var intervals = Iterators.fluent(node.elements())
        .map(JsonNode::textValue)
        .map(Intervals::parse)
        .list();

    // If there are no intervals, return an empty collection
    return intervals.isEmpty()
        ? IntervalCollections.empty()
        : IntervalSets.disjoint(intervals);
  }

}
