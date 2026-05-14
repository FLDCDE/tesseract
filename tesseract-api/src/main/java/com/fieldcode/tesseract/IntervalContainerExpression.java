package com.fieldcode.tesseract;

public interface IntervalContainerExpression {

  IntervalContainerExpression elseAlways();

  IntervalContainerExpression elseEmpty();

  FallbackStrategy getFallbackStrategy();

  enum FallbackStrategy {

    ERROR, ALWAYS, EMPTY

  }

}
