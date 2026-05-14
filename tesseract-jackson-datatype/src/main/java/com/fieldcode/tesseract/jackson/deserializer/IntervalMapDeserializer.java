package com.fieldcode.tesseract.jackson.deserializer;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fieldcode.tesseract.Interval;
import com.fieldcode.tesseract.IntervalMap;
import com.fieldcode.tesseract.interval.IntervalMaps;
import com.fieldcode.tesseract.jackson.utils.GenericRawClassExtractor;
import com.fieldcode.tesseract.jackson.utils.TopLevelFieldDecoder;

public class IntervalMapDeserializer<T> extends JsonDeserializer<IntervalMap<T>> implements ContextualDeserializer {

  private final Class<T> valueType;

  public IntervalMapDeserializer() {
    this(null);
  }

  public IntervalMapDeserializer(Class<T> valueType) {
    this.valueType = valueType;
  }

  @Override
  public JsonDeserializer<?> createContextual(DeserializationContext ctx, BeanProperty property) {
    var rawClass = GenericRawClassExtractor.getGenericRawClass(ctx, property, 0);
    return new IntervalMapDeserializer<>(rawClass);
  }

  @Override
  public IntervalMap<T> deserialize(JsonParser parser, DeserializationContext ctx) throws IOException {
    var decoder = TopLevelFieldDecoder.of(parser, ctx);

    var defaultValue = decoder.decodeField("default", valueType);

    var map = IntervalMaps.disjoint(defaultValue);

    var arrayNode = decoder.getArrayNode("entries");

    for (var node : arrayNode) {
      var interval = parser.getCodec().treeToValue(node.get("interval"), Interval.class);
      var value = parser.getCodec().treeToValue(node.get("value"), valueType);
      map = map.put(interval, value);
    }

    return map;
  }

}
