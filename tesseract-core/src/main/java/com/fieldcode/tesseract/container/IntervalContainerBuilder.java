package com.fieldcode.tesseract.container;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.fieldcode.tesseract.IntervalCollection;
import com.fieldcode.tesseract.IntervalContainer;
import com.fieldcode.tesseract.Taggable.Tag;
import com.fieldcode.tesseract.Taggable.TagHolder;
import com.fieldcode.tesseract.TaggedIntervalCollection;
import com.fieldcode.tesseract.collection.ImmutableTaggedIntervalCollection;
import com.fieldcode.tesseract.tag.Tags;

import static com.google.common.base.Preconditions.checkArgument;

public class IntervalContainerBuilder {

  private final Map<String, TaggedIntervalCollection> collections = new HashMap<>();

  private IntervalContainerBuilder() {
  }

  static IntervalContainerBuilder create() {
    return new IntervalContainerBuilder();
  }

  private void checkDuplications(String id) {
    checkArgument(!collections.containsKey(id), "Duplicate id: %s", id);
  }

  /**
   * Adds a new collection to the builder. Duplicate ids are not allowed.
   *
   * @param id id of the collection
   * @param intervals intervals of the collection
   * @param tags tags of the collection
   * @return this builder
   */
  public IntervalContainerBuilder add(String id, IntervalCollection intervals, TagHolder tags) {
    checkDuplications(id);
    collections.put(id, ImmutableTaggedIntervalCollection.of(id, intervals, tags));
    return this;
  }

  public IntervalContainerBuilder add(Enum<?> id, IntervalCollection intervals, TagHolder tags) {
    return add(id.name(), intervals, tags);
  }

  public IntervalContainerBuilder add(String id, IntervalCollection intervals) {
    checkDuplications(id);
    collections.put(id, ImmutableTaggedIntervalCollection.of(id, intervals, Tags.holder()));
    return this;
  }

  public IntervalContainerBuilder add(Enum<?> id, IntervalCollection intervals) {
    return add(id.name(), intervals);
  }

  public IntervalContainerBuilder add(String id, IntervalCollection intervals, Tag tag) {
    checkDuplications(id);
    collections.put(id, ImmutableTaggedIntervalCollection.of(id, intervals, Tags.holder(tag)));
    return this;
  }

  public IntervalContainerBuilder add(Enum<?> id, IntervalCollection intervals, Tag tag) {
    return add(id.name(), intervals, tag);
  }

  public IntervalContainerBuilder add(String id, IntervalCollection intervals, Set<String> tags) {
    checkDuplications(id);

    var tagSet = tags.stream()
        .map(Tags::tag)
        .collect(Collectors.toSet());

    collections.put(id, ImmutableTaggedIntervalCollection.of(id, intervals, Tags.holder(tagSet)));
    return this;
  }

  /**
   * Adds all {@link TaggedIntervalCollection} of the given collections to the builder. Duplicate ids are not allowed.
   *
   * @param collections collections to add
   * @return this builder
   */
  public IntervalContainerBuilder addAll(Collection<TaggedIntervalCollection> collections) {
    collections.forEach(tagged -> add(tagged.getId(), tagged, tagged.getTags()));
    return this;
  }

  /**
   * Adds all {@link TaggedIntervalCollection} of the given collections to the builder. Duplicate ids are not allowed.
   *
   * @param collections collections to add
   * @return this builder
   */
  public IntervalContainerBuilder addAll(TaggedIntervalCollection... collections) {
    Arrays.stream(collections).forEach(tagged -> add(tagged.getId(), tagged, tagged.getTags()));
    return this;
  }

  /**
   * Adds a new collection to the builder. Duplicate ids are not allowed.
   *
   * @return new IntervalContainer
   */
  public IntervalContainer build() {
    return ImmutableIntervalContainer.of(collections.values());
  }

}
