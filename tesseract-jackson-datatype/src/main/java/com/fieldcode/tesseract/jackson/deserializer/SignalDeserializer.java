package com.fieldcode.tesseract.jackson.deserializer;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fieldcode.tesseract.Signal;
import com.fieldcode.tesseract.signal.Signals;

@SuppressWarnings("rawtypes")
public class SignalDeserializer extends JsonDeserializer<Signal> implements ContextualDeserializer {

  private final Class<?> keyType;

  private final Class<?> valueType;

  public SignalDeserializer(Class<?> keyType, Class<?> valueType) {
    this.keyType = keyType;
    this.valueType = valueType;
  }

  public static SignalDeserializer factory() {
    return new SignalDeserializer(null, null);
  }

  public static SignalDeserializer of(Class<?> keyType, Class<?> valueType) {
    return new SignalDeserializer(keyType, valueType);
  }


  private static Class<?> getRawClass(DeserializationContext ctx, int index) {
    return ctx.getContextualType()
        .containedType(index)
        .getRawClass();
  }

  @Override
  public JsonDeserializer<?> createContextual(DeserializationContext ctx, BeanProperty property) {
    var keyType = getRawClass(ctx, 0);
    var valueType = getRawClass(ctx, 1);
    return of(keyType, valueType);
  }

  @Override
  public Signal deserialize(JsonParser parser, DeserializationContext deserializationContext) throws IOException {

    // TODO: 2023. 01. 12. This can cause concurrency issues. Must be rewritten like SignalMapDeserializer
    var codec = parser.getCodec();
    var tree = parser.readValueAsTree();

    var key = codec.treeToValue(tree.get("key"), keyType);
    var value = codec.treeToValue(tree.get("value"), valueType);
    //noinspection unchecked
    return Signals.signal((Comparable) key, value);
  }

}
