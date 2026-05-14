package com.fieldcode.tesseract.container.expression;

import java.util.List;

import com.fieldcode.tesseract.IntervalContainerExpression;

public interface LogicalOperator extends InternalExpressionNode {

  LogicalOperatorType getType();

  List<IntervalContainerExpression> getOperands();

  enum LogicalOperatorType {
    AND, OR, NOT

  }

}
