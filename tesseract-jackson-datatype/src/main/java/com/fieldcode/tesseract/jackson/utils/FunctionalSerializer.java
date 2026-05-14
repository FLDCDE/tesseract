package com.fieldcode.tesseract.jackson.utils;

import java.util.Collection;
import java.util.function.BiFunction;
import java.util.function.Consumer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import lombok.SneakyThrows;

public abstract class FunctionalSerializer<T> implements Consumer<T> {

  private final JsonGenerator generator;
  private final SerializerProvider serializerProvider;

  public FunctionalSerializer(JsonGenerator generator, SerializerProvider serializerProvider) {
    this.generator = generator;
    this.serializerProvider = serializerProvider;
  }

  protected static <T> JsonSerializer<T> wrap(BiFunction<JsonGenerator, SerializerProvider, FunctionalSerializer<T>> supplier) {
    return FunctionalSerializerWrapper.of(supplier);
  }

  /**
   * Write a JSON object with the given fields.
   *
   * @param nested the fields to write
   */
  @SneakyThrows
  protected void object(Runnable... nested) {
    generator.writeStartObject();
    for (Runnable r : nested) {
      r.run();
    }
    generator.writeEndObject();
  }

  @SneakyThrows
  protected void object(Object value) {
    generator.writeObject(value);
  }

  @SneakyThrows
  protected void field(String name, Object value) {
    serializerProvider.defaultSerializeField(name, value, generator);
  }

  @SneakyThrows
  protected <T> void array(String name, Collection<T> values, Consumer<T> writer) {
    generator.writeArrayFieldStart(name);
    for (T value : values) {
      writer.accept(value);
    }
    generator.writeEndArray();
  }

  protected <S extends FunctionalSerializer<T>> S serializer(BiFunction<JsonGenerator, SerializerProvider, S> supplier) {
    return supplier.apply(generator, serializerProvider);
  }

  protected <S extends FunctionalSerializer<T>> void object(BiFunction<JsonGenerator, SerializerProvider, S> supplier, T value) {
    supplier.apply(generator, serializerProvider).accept(value);
  }

}

