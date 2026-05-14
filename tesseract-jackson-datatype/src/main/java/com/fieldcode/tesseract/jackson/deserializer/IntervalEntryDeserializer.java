package com.fieldcode.tesseract.jackson.deserializer;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fieldcode.tesseract.Interval;
import com.fieldcode.tesseract.IntervalMap.IntervalEntry;
import com.fieldcode.tesseract.interval.IntervalMaps;
import com.fieldcode.tesseract.jackson.utils.GenericRawClassExtractor;
import com.fieldcode.tesseract.jackson.utils.TopLevelFieldDecoder;

public class IntervalEntryDeserializer<T> extends JsonDeserializer<IntervalEntry<T>> implements ContextualDeserializer {

  private final Class<T> valueType;

  public IntervalEntryDeserializer() {
    this(null);
  }

  public IntervalEntryDeserializer(Class<T> valueType) {
    this.valueType = valueType;
  }

  @Override
  public JsonDeserializer<?> createContextual(DeserializationContext ctx, BeanProperty property) {
    var rawClass = GenericRawClassExtractor.getGenericRawClass(ctx, property, 0);
    return new IntervalEntryDeserializer<>(rawClass);
  }

  @Override
  public IntervalEntry<T> deserialize(JsonParser parser, DeserializationContext ctx) throws IOException {
    var decoder = TopLevelFieldDecoder.of(parser, ctx);

    var interval = decoder.decodeField("interval", Interval.class);
    var value = decoder.decodeField("value", valueType);

    return IntervalMaps.entry(interval, value);
  }

}
