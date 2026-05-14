package com.fieldcode.tesseract.jackson.deserializer;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fieldcode.tesseract.Signal;
import com.fieldcode.tesseract.map.TreeSignalMap;
import com.fieldcode.tesseract.signal.Signals;

@SuppressWarnings("rawtypes")
public class SignalMapDeserializer extends JsonDeserializer<TreeSignalMap> implements ContextualDeserializer {

  private final Class<?> keyType;
  private final Class<?> valueType;

  public SignalMapDeserializer(Class<?> keyType, Class<?> valueType) {
    this.keyType = keyType;
    this.valueType = valueType;
  }

  public static SignalMapDeserializer factory() {
    return new SignalMapDeserializer(null, null);
  }

  public static SignalMapDeserializer of(Class<?> keyType, Class<?> valueType) {
    return new SignalMapDeserializer(keyType, valueType);
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

  @SuppressWarnings("rawtypes")
  @Override
  public TreeSignalMap deserialize(JsonParser parser, DeserializationContext ctx) throws IOException {
    var codec = parser.getCodec();

    var tree = parser.readValueAsTree();
    var signals = tree.get("signals");

    var defaultValue = decode(tree, "default", valueType, codec);

    var startNode = signals.get(0);
    var chainStartNode = asSignal(startNode, codec);

    var endNode = signals.get(signals.size() - 1);
    var chainEndNode = asSignal(endNode, codec);

    //noinspection unchecked
    var treeSignalMap = TreeSignalMap.of(chainStartNode.getKey(), chainEndNode.getKey(), defaultValue);

    for (int i = 0; i < signals.size(); i++) {
      var node = signals.get(i);
      var nodeSignal = asSignal(node, codec);
      //noinspection unchecked
      treeSignalMap.put(nodeSignal.getKey(), nodeSignal.getValue());
    }

    //noinspection rawtypes
    return (TreeSignalMap) treeSignalMap;
  }

  private Signal asSignal(TreeNode node, ObjectCodec codec) throws JsonProcessingException {

    // noinspection rawtypes
    var nodeKey = (Comparable) decode(node, "key", keyType, codec);
    var nodeValue = decode(node, "value", valueType, codec);

    //noinspection unchecked
    return Signals.signal(nodeKey, nodeValue);
  }

  private Object decode(TreeNode node, String property, Class<?> targetClass, ObjectCodec codec) throws JsonProcessingException {
    return codec.treeToValue(node.get(property), targetClass);
  }

}
