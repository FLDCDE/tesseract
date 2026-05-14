package com.fieldcode.tesseract.jackson.utils;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Supplier;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

public abstract class FunctionalDeserializer<T> implements Supplier<T> {

  private final TopLevelFieldDecoder decoder;

  public FunctionalDeserializer(JsonParser parser, DeserializationContext ctx) {
    this.decoder = TopLevelFieldDecoder.of(parser, ctx);
  }

  protected static <R> JsonDeserializer<R> wrap(BiFunction<JsonParser, DeserializationContext, FunctionalDeserializer<R>> supplier) {
    return FunctionalDeserializerWrapper.of(supplier);
  }

  protected <R> R field(String fieldName, Class<R> valueType) {
    return decoder.decodeField(fieldName, valueType);
  }

  protected <R> List<R> array(String fieldName, Class<R> elementType) {
    return decoder.decodeArray(fieldName, elementType);
  }

}
