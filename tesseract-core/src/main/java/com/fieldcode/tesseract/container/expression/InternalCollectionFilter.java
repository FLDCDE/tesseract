package com.fieldcode.tesseract.container.expression;

import java.util.function.Predicate;

import com.fieldcode.tesseract.TaggedIntervalCollection;

interface InternalCollectionFilter {

  Predicate<TaggedIntervalCollection> getPredicate();

}
