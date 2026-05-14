package com.fieldcode.tesseract.timeline;

import org.immutables.value.Value.Immutable;
import org.immutables.value.Value.Parameter;

import com.fieldcode.tesseract.Taggable.TagHolder;

@Immutable
abstract class LayerDefinitionSkeleton implements LayerDefinition {

  @Override
  @Parameter
  public abstract String getId();

  @Override
  @Parameter
  public abstract LayerType getType();

  @Override
  @Parameter
  public abstract TagHolder getTags();

}
