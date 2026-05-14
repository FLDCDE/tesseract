package com.fieldcode.tesseract.jackson.utils;

import java.io.IOException;
import java.util.function.Function;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

public class SimpleStringDeserializer<T> extends JsonDeserializer<T> {

  private final Function<String, T> mapper;

  private SimpleStringDeserializer(Function<String, T> mapper) {
    this.mapper = mapper;
  }

  public static <T> SimpleStringDeserializer<T> of(Function<String, T> mapper) {
    return new SimpleStringDeserializer<>(mapper);
  }

  @Override
  public T deserialize(JsonParser p, DeserializationContext ignore) throws IOException {
    return mapper.apply(p.getText());
  }

}
