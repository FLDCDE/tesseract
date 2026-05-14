package com.fieldcode.tesseract.jackson.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.node.ArrayNode;

import lombok.SneakyThrows;

public class TopLevelFieldDecoder {

  private final TreeNode tree;
  private final JsonParser jsonParser;
  private final DeserializationContext ctx;

  @SneakyThrows
  private TopLevelFieldDecoder(JsonParser jsonParser, DeserializationContext ctx) {
    this.ctx = ctx;
    this.jsonParser = jsonParser;
    this.tree = jsonParser.readValueAsTree();
  }

  public static TopLevelFieldDecoder of(JsonParser jsonParser, DeserializationContext ctx) {
    return new TopLevelFieldDecoder(jsonParser, ctx);
  }

  @SneakyThrows
  public <T> T decodeField(String fieldName, Class<T> type) {
    return jsonParser.getCodec().treeToValue(tree.get(fieldName), type);
  }

  public <T> List<T> decodeArray(String fieldName, TypeReference<T> type) {
    var deserializer = deserializer(type);
    return decodeArray(fieldName, deserializer);
  }

  public <T> List<T> decodeArray(String fieldName, Class<T> type) {
    var deserializer = deserializer(type);
    return decodeArray(fieldName, deserializer);
  }

  @SneakyThrows
  private <T> List<T> decodeArray(String fieldName, JsonDeserializer<T> deserializer) {
    var arrayNode = getArrayNode(fieldName);
    var array = new ArrayList<T>();

    for (var node : arrayNode) {
      var traverse = node.traverse(jsonParser.getCodec());
      traverse.nextToken();
      var element = deserializer.deserialize(traverse, ctx);
      array.add(element);
    }

    return Collections.unmodifiableList(array);
  }

  public ArrayNode getArrayNode(String fieldName) {
    var node = tree.get(fieldName);
    if (node.isArray()) {
      return (ArrayNode) node;
    }
    throw new IllegalStateException("Expected array node but got " + node);
  }

  private <T> JsonDeserializer<T> deserializer(Class<T> type) {
    return deserializer(ctx.constructType(type));
  }

  private <T> JsonDeserializer<T> deserializer(TypeReference<T> type) {
    return deserializer(ctx.getTypeFactory().constructType(type));
  }

  @SneakyThrows
  private <T> JsonDeserializer<T> deserializer(JavaType type) {
    //noinspection unchecked
    return (JsonDeserializer<T>) ctx.findContextualValueDeserializer(type, null);
  }

}
