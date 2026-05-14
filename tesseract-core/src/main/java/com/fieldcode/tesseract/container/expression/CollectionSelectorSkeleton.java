package com.fieldcode.tesseract.container.expression;

import org.immutables.value.Value.Default;
import org.immutables.value.Value.Immutable;

import static com.fieldcode.tesseract.IntervalContainerExpression.FallbackStrategy.ALWAYS;
import static com.fieldcode.tesseract.IntervalContainerExpression.FallbackStrategy.EMPTY;
import static com.fieldcode.tesseract.IntervalContainerExpression.FallbackStrategy.ERROR;

@Immutable
abstract class CollectionSelectorSkeleton implements CollectionSelector, InternalExpressionNode {

  @Override
  public <T> T visit(NodeVisitor<T> visitor) {
    return visitor.visit(this);
  }

  @Override
  public CollectionSelector elseAlways() {
    return withFallbackStrategy(ALWAYS);
  }

  @Override
  public CollectionSelector elseEmpty() {
    return withFallbackStrategy(EMPTY);
  }

  @Override
  @Default
  public FallbackStrategy getFallbackStrategy() {
    return ERROR;
  }

  protected abstract CollectionSelector withFallbackStrategy(FallbackStrategy fallbackStrategy);

}
