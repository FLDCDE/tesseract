package com.fieldcode.tesseract.container.expression;

import com.fieldcode.tesseract.TaggedIntervalCollection;
import org.immutables.value.Value.Immutable;

import java.util.function.Predicate;

@Immutable
abstract class CollectionFilterByIdSkeleton implements CollectionFilterById, InternalCollectionFilter {

  @Override
  public Predicate<TaggedIntervalCollection> getPredicate() {
    return collection -> getIds().contains(collection.getId());
  }

}
