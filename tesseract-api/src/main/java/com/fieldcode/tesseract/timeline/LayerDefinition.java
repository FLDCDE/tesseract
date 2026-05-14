package com.fieldcode.tesseract.timeline;

import com.fieldcode.tesseract.Taggable.TagHolder;

public interface LayerDefinition {

  String getId();

  LayerType getType();

  TagHolder getTags();

}
