package com.fieldcode.tesseract.jackson.utils;

import java.io.IOException;
import java.util.function.BiFunction;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class FunctionalSerializerWrapper<T> extends JsonSerializer<T> {

  private final BiFunction<JsonGenerator, SerializerProvider, FunctionalSerializer<T>> supplier;

  private FunctionalSerializerWrapper(BiFunction<JsonGenerator, SerializerProvider, FunctionalSerializer<T>> supplier) {
    this.supplier = supplier;
  }

  public static <T> FunctionalSerializerWrapper<T> of(BiFunction<JsonGenerator, SerializerProvider, FunctionalSerializer<T>> supplier) {
    return new FunctionalSerializerWrapper<>(supplier);
  }

  @Override
  public void serialize(T value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
    supplier.apply(gen, serializers).accept(value);
  }

}
