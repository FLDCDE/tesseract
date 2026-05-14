package com.fieldcode.tesseract.container.expression;

import java.util.function.Predicate;

import com.fieldcode.tesseract.TaggedIntervalCollection;

public interface CollectionFilterByTag extends CollectionFilter {

  Predicate<TaggedIntervalCollection> getPredicate();

}
