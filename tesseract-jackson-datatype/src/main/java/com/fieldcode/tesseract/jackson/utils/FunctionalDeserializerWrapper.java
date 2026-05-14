package com.fieldcode.tesseract.jackson.utils;

import java.util.function.BiFunction;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

public class FunctionalDeserializerWrapper<T> extends JsonDeserializer<T> {

  private final BiFunction<JsonParser, DeserializationContext, FunctionalDeserializer<T>> supplier;

  private FunctionalDeserializerWrapper(BiFunction<JsonParser, DeserializationContext, FunctionalDeserializer<T>> supplier) {
    this.supplier = supplier;
  }

  public static <T> FunctionalDeserializerWrapper<T> of(BiFunction<JsonParser, DeserializationContext, FunctionalDeserializer<T>> supplier) {
    return new FunctionalDeserializerWrapper<>(supplier);
  }

  @Override
  public T deserialize(JsonParser p, DeserializationContext ctxt) {
    return supplier.apply(p, ctxt).get();
  }

}
