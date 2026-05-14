package com.fieldcode.tesseract;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

public interface Taggable {

  /**
   * Returns the tag holder of this object.
   *
   * @return the tag holder of this object
   */
  TagHolder getTags();

  /**
   * A tag interface.
   */
  interface Tag {

    /**
     * Returns true if this tag matches the given tag.
     *
     * @param other the tag to match
     * @return true if this tag matches the given tag
     */
    boolean matches(Tag other);

    /**
     * Returns true if this tag matches the given name.
     * @param name the name to match
     * @return true if this tag matches the given name
     */
    boolean matchesName(String name);

    /**
     * Returns the name of this tag.
     *
     * @return the name of this tag
     */
    String getName();

    Optional<String> getValue();

  }


  /**
   * A tag with a key and a value pair.
   */
  interface KeyValueTag extends NamedTag, Tag {

    /**
     * Returns true if this tag matches the given tag. If the other tag implements {@link NamedTag} then only the key is matched, otherwise both key and value are matched.
     *
     * @param other the tag to match
     * @return true if this tag matches the given tag
     */
    @Override
    boolean matches(Tag other);

    /**
     * Returns the name of this tag.
     *
     * @return the name of this tag
     */
    @Override
    String getName();

  }

  /**
   * A tag with only a name.
   */
  interface NamedTag extends Tag {

    /**
     * Returns true if this tag matches the given tag. Only the name is matched.
     *
     * @param other the tag to match
     * @return true if this tag matches the given tag
     */
    @Override
    boolean matches(Tag other);

    String getName();
  }

  interface TagHolder extends Iterable<Tag> {

    /**
     * Returns the tags.
     *
     * @return the tags
     */
    List<Tag> getTags();

    /**
     * Returns true if this object has the given tag.
     *
     * @param tag the tag to check
     * @return true if this object has the given tag
     */
    boolean hasTag(Tag tag);

    /**
     * Returns true if this object has the given tag.
     * @param tag the tag to check
     * @return true if this object has the given tag
     */
    boolean hasTag(String tag);

    /**
     * Returns true if this object has any tag that matches the given tag.
     *
     * @param tag the tag to check
     * @return true if this object has any tag that matches the given tag
     */
    boolean anyTagMatch(Tag tag);

    /**
     * Returns true if this object has at least one tag with any of the given tag names.
     * @param tagNames the tag names to check
     * @return true if this object has any of the given tag names
     */
    boolean any(String... tagNames);

    /**
     * Returns true if this object has at least one tag with any of the given tag names.
     * @param tagNames the tag names to check
     * @return true if this object has any of the given tag names
     */
    boolean any(Enum<?>... tagNames);

    /**
     * Returns true if this object has at least one tag with any of the given tag names.
     *
     * @param tags the tag names to check
     * @return true if this object has any of the given tag names
     */
    boolean any(Tag... tags);

    /**
     * Returns true if this object has all tags with the given tag names.
     * @param tagNames the tag names to check
     * @return true if this object has all of the given tag names
     */
    boolean all(String... tagNames);

    /**
     * Returns true if this object has all tags with the given tag names.
     * @param tagNames the tag names to check
     * @return true if this object has all of the given tag names
     */
    boolean all(Enum<?>... tagNames);

    /**
     * Returns true if this object has all tags with the given tags.
     * @param tags  the tags to check
     * @return true if this object has all of the given tags
     */
    boolean all(Tag... tags);

    /**
     * Returns true if this object has none of the given tag names.
     * @param tagNames the tag names to check
     * @return true if this object has none of the given tag names
     */
    boolean none(String... tagNames);

    /**
     * Returns true if this object has none of the given tag names.
     * @param tagNames the tag names to check
     * @return true if this object has none of the given tag names
     */
    boolean none(Enum<?>... tagNames);

    /**
     * Returns true if this object has none of the given tags.
     * @param tags the tags to check
     * @return true if this object has none of the given tags
     */
    boolean none(Tag... tags);

    /**
     * Returns true if this object has no tags.
     * @return true if this object has no tags
     */
    boolean isEmpty();

    /**
     * Returns true if the tag with the given name has multiple values.
     * @param name the name of the tag
     * @return true if the tag has multiple values
     */
    boolean isMultiple(String name);

    /**
     * Returns the value of the tag with the given name or an empty optional if the tag does not exist or has no value.
     * If a tag has multiple values, the first value is returned.
     * @param name the name of the tag
     * @return the value of the tag or an empty optional
     */
    Optional<String> getValue(String name);

    /**
     * Returns the value of the tag with the given enum.name() or an empty optional if the tag does not exist or has no value.
     * If a tag has multiple values, the first value is returned.
     * @param name the name of the tag
     * @return the value of the tag or an empty optional
     */
    Optional<String> getValue(Enum<?> name);

    /**
     * Returns the value of the tag with the given name or the default value if the tag does not exist or has no value.
     * If a tag has multiple values, the first value is returned.
     * @param name the name of the tag
     * @param defaultValue the default value
     * @return tag or default value
     */
    String getValue(String name, String defaultValue);

    /**
     * Returns the value of the tag with the given enum.name() or the default value if the tag does not exist or has no value.
     * If a tag has multiple values, the first value is returned.
     * @param name the name of the tag
     * @param defaultValue the default value
     * @return tag or default value
     */
    String getValue(Enum<?> name, String defaultValue);

    /**
     * Returns the value of the tag with the given name or an empty optional if the tag does not exist or has no value. The value is converted to the given type.
     * If a tag has multiple values, the first value is returned.
     * @param name the name of the tag
     * @param mapper the function to convert the value
     * @return the value of the tag or an empty optional
     * @param <T> the type of the value
     */
    <T> Optional<T> getValue(String name, Function<String, T> mapper);

    /**
     * Returns the value of the tag with the given enum.name() or an empty optional if the tag does not exist or has no value. The value is converted to the given type.
     * If a tag has multiple values, the first value is returned.
     * Mapper exception is propagated.
     * @param name the name of the tag
     * @param mapper the function to convert the value
     * @return the value of the tag or an empty optional
     * @param <T> the type of the value
     */
    <T> Optional<T> getValue(Enum<?> name, Function<String, T> mapper);

    /**
     * Returns the value of the tag with the given name or the default value if the tag does not exist or has no value. The value is converted to the given type.
     * If a tag has multiple values, the first value is returned.
     * Mapper exception is propagated.
     * @param name the name of the tag
     * @param mapper the function to convert the value
     * @param defaultValue the default value
     * @return the value of the tag or the default value
     * @param <T> the type of the value
     */
    <T> T getValue(String name, Function<String, T> mapper, T defaultValue);

    /**
     * Returns the value of the tag with the given enum.name() or the default value if the tag does not exist or has no value. The value is converted to the given type.
     * If a tag has multiple values, the first value is returned.
     * Mapper exception is propagated.
     * @param name the name of the tag
     * @param mapper the function to convert the value
     * @param defaultValue the default value
     * @return the value of the tag or the default value
     * @param <T> the type of the value
     */
    <T> T getValue(Enum<?> name, Function<String, T> mapper, T defaultValue);

    /**
     * Returns all values of the tag with the given name or an empty list if the tag does not exist or has no value.
     * @param name the name of the tag
     * @return the values of the tag or an empty list
     */
    List<String> getValues(String name);

  }

  @FunctionalInterface
  interface TagPredicate extends Predicate<Taggable> {

  }

}



