package com.fieldcode.tesseract.tag;

import java.util.Set;

import com.fieldcode.tesseract.Taggable.Tag;
import com.fieldcode.tesseract.Taggable.TagHolder;
import com.google.common.collect.Iterables;

public class Tags {

  private Tags() {}

  /**
   * Creates a named tag with the given parameter.
   *
   * @param name The name of the tag
   * @return The tag
   */
  public static Tag tag(String name) {
    return ImmutableNamedTag.of(name);
  }

  /**
   * Creates a named tag with the given parameter. The name of the tag is the name of the enum.
   *
   * @param name The name of the tag
   * @return The tag
   */
  public static Tag tag(Enum<?> name) {
    return ImmutableNamedTag.of(name.name());
  }

  /**
   * Creates a key-value tag with the given parameters. The value of the tag is the string representation of the value. If the value is an enum, the name of the enum is used.
   *
   * @param key The key of the tag
   * @param value The value of the tag
   * @return The tag
   */
  public static Tag tag(String key, Object value) {
    var valueAsString = value instanceof Enum
        ? ((Enum<?>) value).name()
        : String.valueOf(value);
    return ImmutableKeyValueTag.builder()
        .name(key)
        .value(valueAsString)
        .build();
  }

  /**
   * Creates a key-value tag with the given parameters. The key of the tag is the name of the enum.
   * The value of the tag is the string representation of the value. If the value is an enum, the name of the enum is used.
   *
   * @param key The key of the tag
   * @param value The value of the tag
   * @return The tag
   */
  public static Tag tag(Enum<?> key, Object value) {
    return tag(key.name(), value);
  }

  /**
   * Returns a new instance of a builder for creating a TagHolder.
   *
   * @return The builder
   */
  public static TagsBuilder builder() {
    return TagsBuilder.of();
  }

  /**
   * Returns a new instance of a builder for creating a TagHolder with copying the given holders' tags.
   * @param tagHolders The tag holders to copy
   * @return The builder
   */
  public static TagsBuilder builderFrom(TagHolder... tagHolders) {
    var tagsBuilder = TagsBuilder.of();
    for (TagHolder tagHolder : tagHolders) {
      tagsBuilder.tags(tagHolder);
    }
    return tagsBuilder;
  }

  /**
   * Returns a TagHolder with the given tags.
   *
   * @param tags The tags
   * @return The TagHolder
   */
  public static TagHolder holder(Iterable<Tag> tags) {
    return Iterables.isEmpty(tags)
        ? holder()
        : ImmutableTagHolder.of(tags);
  }

  /**
   * Returns a TagHolder with the given tags.
   *
   * @param tags The tags
   * @return The TagHolder
   */
  public static TagHolder holder(Tag... tags) {
    return tags.length == 0
        ? holder()
        : ImmutableTagHolder.builder().addTags(tags).build();
  }

  /**
   * Returns a TagHolder with the given tag.
   *
   * @param tag The tag
   * @return The TagHolder
   */
  public static TagHolder holder(Tag tag) {
    return ImmutableTagHolder.of(Set.of(tag));
  }

  /**
   * Returns an empty TagHolder.
   *
   * @return The empty TagHolder
   */
  public static TagHolder holder() {
    return EmptyTagHolder.of();
  }

  public static Tag parseTag(String value) {
    return TagDecoder.parseTag(value);
  }

  public static TagHolder parseHolder(String value) {
    return TagDecoder.parseHolder(value);
  }

}
