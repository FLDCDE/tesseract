package com.fieldcode.tesseract.jackson.test_utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

class JsonImpl implements Json {

  private final ObjectMapper mapper;

  private JsonImpl(ObjectMapper mapper) {
    this.mapper = mapper;
  }

  static JsonImpl of(ObjectMapper mapper) {
    return new JsonImpl(mapper);
  }

  @Override
  public ObjectMapper getMapper() {
    return mapper;
  }

  @Override
  public <T> T parse(String input, Class<T> type) {
    try {
      return mapper.readValue(input, type);
    } catch (JsonProcessingException e) {
      return parseError(e);
    }
  }

  @Override
  public <T> T parse(String input, TypeReference<T> type) {
    try {
      return mapper.readValue(input, type);
    } catch (JsonProcessingException e) {
      return parseError(e);
    }
  }

  @Override
  public <T> T parse(InputStream inputStream, Class<T> type) {
    try {
      return mapper.readValue(inputStream, type);
    } catch (IOException e) {
      return parseError(e);
    }
  }

  @Override
  public <T> T parse(InputStream inputStream, TypeReference<T> type) {
    try {
      return mapper.readValue(inputStream, type);
    } catch (IOException e) {
      return parseError(e);
    }
  }

  @Override
  public <T> Optional<T> tryParse(String input, Class<T> type) {
    try {
      return Optional.of(mapper.readValue(input, type));
    } catch (JsonProcessingException e) {
      return Optional.empty();
    }
  }

  @Override
  public <T> Optional<T> tryParse(String input, TypeReference<T> type) {
    try {
      return Optional.of(mapper.readValue(input, type));
    } catch (JsonProcessingException e) {
      return Optional.empty();
    }
  }

  @Override
  public <T> Optional<T> tryParse(InputStream inputStream, Class<T> type) {
    try {
      return Optional.of(mapper.readValue(inputStream, type));
    } catch (IOException e) {
      return Optional.empty();
    }
  }

  @Override
  public <T> Optional<T> tryParse(InputStream inputStream, TypeReference<T> type) {
    try {
      return Optional.of(mapper.readValue(inputStream, type));
    } catch (IOException e) {
      return Optional.empty();
    }
  }

  @Override
  public <T> String stringify(T entity) {
    return stringify(entity, false);
  }

  @Override
  public <T> String stringify(T entity, boolean pretty) {
    try {
      var writer = pretty
          ? mapper.writerWithDefaultPrettyPrinter()
          : mapper.writer();
      return writer.writeValueAsString(entity);
    } catch (JsonProcessingException e) {
      return stringifyError(e);
    }
  }

  @Override
  public <T> T clone(Object entity, Class<T> type) {
    return parse(stringify(entity), type);
  }

  @Override
  public <T> T clone(Object entity, TypeReference<T> type) {
    return parse(stringify(entity), type);
  }

  private <T> T parseError(Exception e) {
    throw new IllegalArgumentException("Unable to parse json!", e);
  }

  private <T> T stringifyError(Exception e) {
    throw new IllegalArgumentException("Unable to stringify entity!", e);
  }

}
