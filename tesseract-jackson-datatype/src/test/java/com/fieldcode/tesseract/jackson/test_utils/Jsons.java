package com.fieldcode.tesseract.jackson.test_utils;

import java.io.InputStream;
import java.util.Optional;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;


/**
 * JSON handling helper class based on Jackson library. Provides a simplified API for JSON datastructures handling.
 * <p>
 * Static methods are provided for the most common use cases.
 * </p>
 * <p>
 * These methods use a default Jackson {@link ObjectMapper} instance which enables all Jackson modules found in the classpath. This instance is lazily initialized and JVM wide
 * singleton.
 * </p>
 * <p>
 * If special mapper configuration is needed, use the {@link #instance(ObjectMapper)} method to create a custom {@link Json} instance.
 * </p>
 */
public class Jsons {

  private static final ObjectMapper MAPPER = createDefaultMapper();

  private Jsons() {}

  /**
   * Creates the default Jackson object mapper. Disables the default serialization of dates as timestamps. Enables all Jackson modules found in the classpath.
   */
  private static ObjectMapper createDefaultMapper() {
    return new ObjectMapper()
        .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
        .findAndRegisterModules();
  }

  /**
   * Creates the default simplified json handler using the default mapper.
   */
  private static Json createDefaultJson() {
    return instance(MAPPER);
  }

  /**
   * Returns the default Jackson object mapper. Lazy initialized, JVM wide singleton.
   *
   * @return the default mapper
   */
  public static ObjectMapper mapper() {
    return MAPPER;
  }

  /**
   * Returns the default simplified json handler. Lazy initialized, JVM wide singleton.
   *
   * @return the default json
   */
  public static Json instance() {
    return instance(MAPPER);
  }

  /**
   * Returns a simplified json handler utility instance using the given mapper.
   *
   * @param mapper the mapper to use
   * @return the json
   */
  public static Json instance(ObjectMapper mapper) {
    return JsonImpl.of(mapper);
  }

  /**
   * Parses the given input using the default mapper.
   *
   * @param input the input to parse
   * @param type the type to parse
   * @return the parsed object
   * @throws IllegalArgumentException if the input cannot be parsed
   */
  public static <T> T parse(String input, Class<T> type) {
    return instance().parse(input, type);
  }

  /**
   * Parses the given input using the default mapper.
   *
   * @param input the input to parse
   * @param type the type to parse
   * @return the parsed object
   * @throws IllegalArgumentException if the input cannot be parsed
   */
  public static <T> T parse(String input, TypeReference<T> type) {
    return instance().parse(input, type);
  }

  /**
   * Parses the given input using the default mapper.
   *
   * @param inputStream the input to parse
   * @param type the type to parse
   * @return the parsed object
   * @throws IllegalArgumentException if the input cannot be parsed
   */
  public static <T> T parse(InputStream inputStream, Class<T> type) {
    return instance().parse(inputStream, type);
  }

  /**
   * Parses the given input using the default mapper.
   *
   * @param inputStream the input to parse
   * @param type the type to parse
   * @return the parsed object
   * @throws IllegalArgumentException if the input cannot be parsed
   */
  public static <T> T parse(InputStream inputStream, TypeReference<T> type) {
    return instance().parse(inputStream, type);
  }

  /**
   * Tries to parse the given input using the default mapper.
   *
   * @param input the input to parse
   * @param type the type to parse
   * @return the parsed object
   */
  public static <T> Optional<T> tryParse(String input, Class<T> type) {
    return instance().tryParse(input, type);
  }

  /**
   * Tries to parse the given input using the default mapper.
   *
   * @param input the input to parse
   * @param type the type to parse
   * @return the parsed object
   */
  public static <T> Optional<T> tryParse(String input, TypeReference<T> type) {
    return instance().tryParse(input, type);
  }

  /**
   * Tries to parse the given input using the default mapper.
   *
   * @param inputStream the input to parse
   * @param type the type to parse
   * @return the parsed object
   */
  public static <T> Optional<T> tryParse(InputStream inputStream, Class<T> type) {
    return instance().tryParse(inputStream, type);
  }

  /**
   * Tries to parse the given input using the default mapper.
   *
   * @param inputStream the input to parse
   * @param type the type to parse
   * @return the parsed object
   */
  public static <T> Optional<T> tryParse(InputStream inputStream, TypeReference<T> type) {
    return instance().tryParse(inputStream, type);
  }

  /**
   * Parses the given input using the default mapper.
   *
   * @param entity the input to parse
   * @return the parsed object
   * @throws IllegalArgumentException if the input cannot be parsed
   */
  public static <T> String stringify(T entity) {
    return instance().stringify(entity);
  }

  /**
   * Parses the given input using the default mapper.
   *
   * @param entity the input to parse
   * @param pretty whether to pretty print the output
   * @return the parsed object
   * @throws IllegalArgumentException if the input cannot be parsed
   */
  public static <T> String stringify(T entity, boolean pretty) {
    return instance().stringify(entity, pretty);
  }

  /**
   * Parses the given input using the default mapper.
   *
   * @param entity the input to parse
   * @return the parsed object
   * @throws IllegalArgumentException if the input cannot be parsed or the object cannot be cloned
   */
  public static <T> T clone(Object entity, Class<T> type) {
    return instance().clone(entity, type);
  }

  /**
   * Parses the given input using the default mapper.
   *
   * @param entity the input to parse
   * @return the parsed object
   * @throws IllegalArgumentException if the input cannot be parsed or the object cannot be cloned
   */
  public static <T> T clone(Object entity, TypeReference<T> type) {
    return instance().clone(entity, type);
  }

}
