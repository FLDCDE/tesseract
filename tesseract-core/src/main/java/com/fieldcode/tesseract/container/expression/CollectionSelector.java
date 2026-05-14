package com.fieldcode.tesseract.container.expression;

import java.util.Set;

import com.fieldcode.tesseract.IntervalContainerExpression;

public interface CollectionSelector extends IntervalContainerExpression {

  Set<CollectionFilter> getFilters();

}
