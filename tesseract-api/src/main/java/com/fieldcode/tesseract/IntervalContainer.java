package com.fieldcode.tesseract;

import java.util.Collection;

import com.fieldcode.tesseract.Taggable.TagPredicate;

public interface IntervalContainer {

  /**
   * Returns the {@link IntervalCollection} with the given id. If no such collection exists, an empty collection is returned.
   *
   * @param id the id of the collection
   * @return the collection with the given id or an empty collection
   */
  IntervalCollection get(String id);

  /**
   * Returns the {@link IntervalCollection} with the given id. If no such collection exists, an empty collection is returned.
   * Equivalent to calling {@link #get(String)} with the name of the enum.
   *
   * @param id the id of the collection
   * @return the collection with the given id or an empty collection
   */
  IntervalCollection get(Enum<?> id);

  /**
   * Checks if a collection with the given id exists.
   *
   * @param id the id to check
   * @return true if a collection with the given id exists, false otherwise
   */
  boolean hasId(String id);

  /**
   * Checks if a collection with the given id exists.
   * Equivalent to calling {@link #hasId(String)} with the name of the enum.
   *
   * @param id the id to check
   * @return true if a collection with the given id exists, false otherwise
   */
  boolean hasId(Enum<?> id);

  /**
   * Produces a new {@link IntervalCollection} representing the intersection of chosen collections. Collections are chosen based on the provided filter.
   *
   * @param filter The filter used for selecting collections.
   * @return The intersected {@link IntervalCollection}.
   */
  IntervalCollection and(TagPredicate filter);

  /**
   * Produces a new {@link IntervalCollection} representing the union of chosen collections. Collections are chosen based on the provided filter.
   *
   * @param filter The filter used for selecting collections.
   * @return The intersected {@link IntervalCollection}.
   */
  IntervalCollection or(TagPredicate filter);

  /**
   * Produces a new {@link IntervalCollection} representing the complement of the union of chosen collections. Collections are chosen based on the provided filter.
   *
   * @param filter The filter used for selecting collections.
   * @return The intersected {@link IntervalCollection}.
   */
  IntervalCollection not(TagPredicate filter);

  /**
   * Returns all collections.
   *
   * @return all collections
   */
  Collection<TaggedIntervalCollection> getAll();

  IntervalCollection combine(IntervalContainerExpression expression);

}
