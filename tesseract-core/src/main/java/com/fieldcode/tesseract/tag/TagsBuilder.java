package com.fieldcode.tesseract.tag;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;

import com.fieldcode.tesseract.Taggable;
import com.fieldcode.tesseract.Taggable.Tag;
import com.fieldcode.tesseract.Taggable.TagHolder;
import com.fieldcode.tesseract.utils.iterator.Iterators;

public class TagsBuilder implements Consumer<Tag> {

  private final List<Tag> tags = new ArrayList<>();

  private TagsBuilder() {}

  /**
   * Creates a new {@link TagsBuilder} instance.
   * @return a new {@link TagsBuilder} instance
   */
  static TagsBuilder of() {
    return new TagsBuilder();
  }

  private void addTag(Tag tag) {
    if (tags.contains(tag)) {
      return;
    }
    tags.add(tag);
  }

  /**
   * Adds a tag to the builder.
   * @param tag the tag to add
   * @return this builder
   * @deprecated use {@link #tag(Tag)} instead
   */
  @Deprecated(forRemoval = true)
  public TagsBuilder add(Tag tag) {
    addTag(tag);
    return this;
  }

  /**
   * Adds a tag to the builder.
   * If the taf is already present, it will be ignored.
   * @param tag the tag to add
   * @return this builder
   */
  public TagsBuilder tag(Tag tag) {
    addTag(tag);
    return this;
  }

  /**
   * Adds a tag name tag to the builder.
   * If the tag is already present, it will be ignored.
   * @param value the tag name
   * @return this builder
   */
  public TagsBuilder tag(String value) {
    addTag(Tags.tag(value));
    return this;
  }

  /**
   * Adds a tag name tag to the builder. The tag name is the key.name().
   * If the tag is already present, it will be ignored.
   * @param key the tag name
   * @return this builder
   */
  public TagsBuilder tag(Enum<?> key) {
    addTag(Tags.tag(key));
    return this;
  }

  /**
   * Adds a key value tag to the builder. The value is converted to a string. If the value is an enum, the name of the enum is used.
   * If the tag is already present, it will be ignored.
   * @param key the tag key
   * @param value the tag value
   * @return this builder
   */
  public TagsBuilder tag(String key, Object value) {
    addTag(Tags.tag(key, value));
    return this;
  }

  /**
   * Adds a key value tag to the builder. The value is converted to a string. If the value is an enum, the name of the enum is used. The key is the key.name().
   * If the tag is already present, it will be ignored.
   * @param key the tag key
   * @param value the tag value
   * @return this builder
   */
  public TagsBuilder tag(Enum<?> key, Object value) {
    addTag(Tags.tag(key, value));
    return this;
  }

  /**
   * Adds all tags to the builder.
   * If a tag is already present, it will be ignored.
   * @param tags the tags to add
   * @return this builder
   */
  public TagsBuilder tags(Stream<Tag> tags) {
    tags.forEach(this);
    return this;
  }

  /**
   * Adds all tags to the builder.
   * If a tag is already present, it will be ignored.
   * @param tags the tags to add
   * @return this builder
   */
  public TagsBuilder tags(Tag... tags) {
    return tags(Stream.of(tags));
  }

  /**
   * Adds all tags to the builder.
   * If a tag is already present, it will be ignored.
   * @param tags the tags to add
   * @return this builder
   */
  public TagsBuilder tags(Iterable<Tag> tags) {
    tags.forEach(this);
    return this;
  }

  /**
   * Adds all tags from the taggable to the builder.
   * If a tag is already present, it will be ignored.
   * @param taggable the taggable
   * @return this builder
   */
  public TagsBuilder tags(Taggable taggable) {
    return tags(taggable.getTags().getTags());
  }

  /**
   * Adds all tags from the tag holder to the builder.
   * If a tag is already present, it will be ignored.
   * @param tagHolder the tag holder
   * @return this builder
   */
  public TagsBuilder tags(TagHolder tagHolder) {
    return tags(tagHolder.getTags());
  }

  /**
   * Adds all tags from the input iterator to the builder using the mapper.
   * If a tag is already present, it will be ignored.
   * @param input the input iterator
   * @param mapper the mapper
   * @return this builder
   * @param <T> the input type
   */
  public <T> TagsBuilder tags(Iterable<T> input, Function<T, Tag> mapper) {
    Iterators.iterator(input).map(mapper).forEach(this);
    return this;
  }

  /**
   * Adds tag to the builder.
   * If the tag is already present, it will be ignored.
   * @param tag the input argument
   */
  @Override
  public void accept(Tag tag) {
    tag(tag);
  }

  /**
   * Builds the tag holder.
   * @return the tag holder
   */
  public TagHolder build() {
    return tags.isEmpty()
        ? Tags.holder()
        : Tags.holder(tags);
  }

}
