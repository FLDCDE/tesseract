package com.fieldcode.tesseract.container.expression;

import org.immutables.value.Value.Default;
import org.immutables.value.Value.Immutable;

import static com.fieldcode.tesseract.IntervalContainerExpression.FallbackStrategy.*;

@Immutable
abstract class LogicalOperatorSkeleton implements LogicalOperator {

  @Override
  public <T> T visit(NodeVisitor<T> visitor) {
    return visitor.visit(this);
  }

  @Override
  public LogicalOperator elseAlways() {
    return withFallbackStrategy(ALWAYS);
  }

  @Override
  public LogicalOperator elseEmpty() {
    return withFallbackStrategy(EMPTY);
  }

  @Override
  @Default
  public FallbackStrategy getFallbackStrategy() {
    return ERROR;
  }

  protected abstract LogicalOperator withFallbackStrategy(FallbackStrategy fallbackStrategy);

}
