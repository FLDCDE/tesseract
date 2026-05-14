package com.fieldcode.tesseract.container.expression;

interface NodeVisitor<T> {

  T visit(CollectionSelector collectionSelector);

  T visit(LogicalOperator logicalOperator);

}
