package com.fieldcode.tesseract.jackson.utils;

import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;

public class GenericRawClassExtractor {

  private GenericRawClassExtractor() {
  }

  public static Class<?> getGenericRawClass(DeserializationContext ctxt, BeanProperty property, int index) {
    var type = ctxt.getContextualType();
    if (type == null && property != null) {
      type = property.getType();
    }
    if (type == null) {
      throw new IllegalStateException("Cannot find type definition");
    }

    var valueType = type.containedTypeOrUnknown(index);  // This should get the generic type
    return valueType.getRawClass();
  }
}
