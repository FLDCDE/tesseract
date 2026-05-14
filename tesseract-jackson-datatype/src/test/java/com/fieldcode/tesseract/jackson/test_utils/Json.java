package com.fieldcode.tesseract.jackson.test_utils;

import java.io.InputStream;
import java.util.Optional;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * A JSON processing interface using the Jackson library. This interface provides methods for parsing and stringify JSON data, as well as obtaining the ObjectMapper instance.
 */
public interface Json {

  /**
   * Returns the {@link ObjectMapper} instance used for JSON processing.
   *
   * @return the ObjectMapper instance
   */
  ObjectMapper getMapper();

  /**
   * Parses the given JSON string into an object of the specified type.
   *
   * @param <T> the type of the object to be parsed
   * @param input the JSON string to parse
   * @param type the class of the object to be parsed
   * @return the parsed object
   * @throws IllegalArgumentException if the input cannot be parsed
   */
  <T> T parse(String input, Class<T> type);

  /**
   * Parses the given JSON string into an object of the specified {@link TypeReference}.
   *
   * @param <T> the type of the object to be parsed
   * @param input the JSON string to parse
   * @param type the TypeReference representing the object to be parsed
   * @return the parsed object
   */
  <T> T parse(String input, TypeReference<T> type);

  /**
   * Parses the given JSON {@link InputStream} into an object of the specified type.
   *
   * @param <T> the type of the object to be parsed
   * @param inputStream the JSON InputStream to parse
   * @param type the class of the object to be parsed
   * @return the parsed object
   * @throws IllegalArgumentException if the input cannot be parsed
   */
  <T> T parse(InputStream inputStream, Class<T> type);


  /**
   * Parses the given JSON {@link InputStream} into an object of the specified {@link TypeReference}.
   *
   * @param <T> the type of the object to be parsed
   * @param inputStream the JSON InputStream to parse
   * @param type the TypeReference representing the object to be parsed
   * @return the parsed object
   * @throws IllegalArgumentException if the input cannot be parsed
   */
  <T> T parse(InputStream inputStream, TypeReference<T> type);

  /**
   * Tries to parse the given JSON string into an object of the specified type.
   *
   * @param <T> the type of the object to be parsed
   * @param input the JSON string to parse
   * @param type the class of the object to be parsed
   * @return an Optional containing the parsed object, or an empty Optional if the parsing failed
   * @throws IllegalArgumentException if the input cannot be parsed
   */
  <T> Optional<T> tryParse(String input, Class<T> type);

  /**
   * Tries to parse the given JSON string into an object of the specified {@link TypeReference}.
   *
   * @param <T> the type of the object to be parsed
   * @param input the JSON string to parse
   * @param type the TypeReference representing the object to be parsed
   * @return an Optional containing the parsed object, or an empty Optional if the parsing failed
   */
  <T> Optional<T> tryParse(String input, TypeReference<T> type);

  /**
   * Tries to parse the given JSON {@link InputStream} into an object of the specified type.
   *
   * @param <T> the type of the object to be parsed
   * @param inputStream the JSON InputStream to parse
   * @param type the class of the object to be parsed
   * @return an Optional containing the parsed object, or an empty Optional if the parsing failed
   */
  <T> Optional<T> tryParse(InputStream inputStream, Class<T> type);

  /**
   * Tries to parse the given JSON {@link InputStream} into an object of the specified {@link TypeReference}.
   *
   * @param <T> the type of the object to be parsed
   * @param inputStream the JSON InputStream to parse
   * @param type the TypeReference representing the object to be parsed
   * @return an Optional containing the parsed object, or an empty Optional if the parsing failed
   */
  <T> Optional<T> tryParse(InputStream inputStream, TypeReference<T> type);

  /**
   * Stringifies the given object into a JSON string.
   *
   * @param entity the object to stringify
   * @return the JSON string
   */
  <T> String stringify(T entity);

  /**
   * Stringifies the given object into a JSON string.
   *
   * @param entity the object to stringify
   * @param pretty whether to pretty-print the JSON string
   * @return the JSON string
   */
  <T> String stringify(T entity, boolean pretty);

  /**
   * Clones the given object into a new object of the specified type.
   *
   * @param <T> the type of the object to be cloned
   * @param entity the object to clone
   * @param type the class of the object to be cloned
   * @return the cloned object
   * @throws IllegalArgumentException if the input cannot be parsed or the object cannot be cloned
   */
  <T> T clone(Object entity, Class<T> type);

  /**
   * Clones the given object into a new object of the specified {@link TypeReference}.
   *
   * @param <T> the type of the object to be cloned
   * @param entity the object to clone
   * @param type the TypeReference representing the object to be cloned
   * @return the cloned object
   * @throws IllegalArgumentException if the input cannot be parsed or the object cannot be cloned
   */
  <T> T clone(Object entity, TypeReference<T> type);

}
