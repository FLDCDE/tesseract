package com.fieldcode.tesseract.container.expression;

import java.util.function.Predicate;

import org.immutables.value.Value.Immutable;
import org.immutables.value.Value.Parameter;

import com.fieldcode.tesseract.Taggable.TagPredicate;
import com.fieldcode.tesseract.TaggedIntervalCollection;

@Immutable
abstract class CollectionFilterByTagSkeleton implements CollectionFilterByTag, InternalCollectionFilter {

  @Parameter
  protected abstract TagPredicate getTagQuery();

  @Override
  public Predicate<TaggedIntervalCollection> getPredicate() {
    return tagged -> getTagQuery().test(tagged);
  }

}
