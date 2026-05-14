package com.fieldcode.tesseract.tag;

import java.util.function.Predicate;

import com.fieldcode.tesseract.Taggable;
import com.fieldcode.tesseract.Taggable.Tag;

import static com.google.common.base.Preconditions.checkNotNull;

public class TagPredicates {

  private TagPredicates() {}

  public static Taggable.TagPredicate match(Enum<?> key) {
    return match(key.name());
  }

  public static Taggable.TagPredicate match(String key) {
    var tag = Tags.tag(key);
    Predicate<Taggable> setPredicate = taggable -> anyTagMatch(taggable, tag);
    return ImmutableTagPredicate.of(setPredicate);
  }

  public static Taggable.TagPredicate match(Enum<?> key, String value) {
    return match(key.name(), value);
  }

  public static Taggable.TagPredicate match(Enum<?> key, Enum<?> value) {
    return match(key.name(), value.name());
  }

  public static Taggable.TagPredicate match(String key, String value) {
    var tag = Tags.tag(key, value);
    Predicate<Taggable> setPredicate = taggable -> anyTagMatch(taggable, tag);
    return ImmutableTagPredicate.of(setPredicate);
  }

  private static boolean anyTagMatch(Taggable taggable, Tag tag) {
    return taggable.getTags().anyTagMatch(tag);
  }

  public static Taggable.TagPredicate any(Taggable.TagPredicate first, Taggable.TagPredicate... others) {
    check(first);
    Predicate<Taggable> anyPredicate = first;
    for (Taggable.TagPredicate query : others) {
      anyPredicate = anyPredicate.or(query);

    }
    return ImmutableTagPredicate.of(anyPredicate);
  }

  public static Taggable.TagPredicate all(Taggable.TagPredicate first, Taggable.TagPredicate... others) {
    check(first);
    Predicate<Taggable> anyPredicate = first;
    for (Taggable.TagPredicate query : others) {
      anyPredicate = anyPredicate.and(query);

    }
    return ImmutableTagPredicate.of(anyPredicate);
  }

  public static Taggable.TagPredicate none(Taggable.TagPredicate first, Taggable.TagPredicate... others) {
    check(first);
    Predicate<Taggable> anyPredicate = first.negate();
    for (Taggable.TagPredicate query : others) {
      anyPredicate = anyPredicate.and(query.negate());

    }
    return ImmutableTagPredicate.of(anyPredicate);
  }

  private static void check(Taggable.TagPredicate first) {
    checkNotNull(first, "At least one query must be provided");
  }

}

