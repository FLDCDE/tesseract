package com.fieldcode.tesseract.container;

import java.util.Collection;
import java.util.Map;

import com.fieldcode.tesseract.IntervalCollection;
import com.fieldcode.tesseract.IntervalContainer;
import com.fieldcode.tesseract.IntervalContainerExpression;
import com.fieldcode.tesseract.Taggable.TagPredicate;
import com.fieldcode.tesseract.TaggedIntervalCollection;
import com.fieldcode.tesseract.collection.IntervalCollections;
import com.fieldcode.tesseract.container.expression.ExpressionVisitorListener;
import com.fieldcode.tesseract.interval.IntervalSets;
import com.google.common.base.Objects;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toUnmodifiableMap;

class ImmutableIntervalContainer implements IntervalContainer {

  private final Map<String, TaggedIntervalCollection> collections;

  private ImmutableIntervalContainer(Collection<TaggedIntervalCollection> collections) {
    this.collections = collections
        .stream()
        .collect(toUnmodifiableMap(TaggedIntervalCollection::getId, identity()));
  }

  static ImmutableIntervalContainer of(Collection<TaggedIntervalCollection> collections) {
    return new ImmutableIntervalContainer(collections);
  }

  @Override
  public IntervalCollection get(String id) {
    var collection = collections.get(id);
    return collection == null
        ? IntervalSets.disjoint()
        : collection;
  }

  @Override
  public IntervalCollection get(Enum<?> id) {
    return get(id.name());
  }

  @Override
  public boolean hasId(String id) {
    return collections.containsKey(id);
  }

  @Override
  public boolean hasId(Enum<?> id) {
    return hasId(id.name());
  }

  @Override
  public IntervalCollection and(TagPredicate filter) {
    return IntervalCollections.and(find(filter));
  }

  @Override
  public IntervalCollection or(TagPredicate filter) {
    return IntervalCollections.or(find(filter));
  }

  @Override
  public IntervalCollection not(TagPredicate filter) {
    return IntervalCollections.not(find(filter));
  }

  @Override
  public Collection<TaggedIntervalCollection> getAll() {
    return collections.values();
  }

  @Override
  public IntervalCollection combine(IntervalContainerExpression expression) {
    return ExpressionVisitorListener.evaluate(this, expression);
  }

  private Collection<IntervalCollection> find(TagPredicate tagFilter) {
    return collections
        .values()
        .stream()
        .filter(tagFilter)
        .collect(toList());
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(collections);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {return true;}
    if (o == null || getClass() != o.getClass()) {return false;}
    ImmutableIntervalContainer that = (ImmutableIntervalContainer) o;
    return Objects.equal(collections, that.collections);
  }
}
