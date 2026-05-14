package com.fieldcode.tesseract.container.expression;

import com.fieldcode.tesseract.IntervalContainerExpression;

interface InternalExpressionNode extends IntervalContainerExpression {

  <T> T visit(NodeVisitor<T> visitor);

}
