package com.fieldcode.tesseract.tag;

import java.util.function.Predicate;

import org.immutables.value.Value.Immutable;
import org.immutables.value.Value.Parameter;

import com.fieldcode.tesseract.Taggable;

@Immutable
abstract class TagPredicateSkeleton implements Taggable.TagPredicate {

  @Parameter
  public abstract Predicate<Taggable> getPredicate();

  @Override
  public boolean test(Taggable taggable) {
    return getPredicate().test(taggable);
  }

}
