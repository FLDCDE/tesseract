package com.fieldcode.tesseract.tag;

import org.immutables.value.Value.Immutable;
import org.immutables.value.Value.Parameter;

import com.fieldcode.tesseract.Taggable.KeyValueTag;
import com.fieldcode.tesseract.Taggable.Tag;
import com.fieldcode.tesseract.Taggable.TagHolder;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Stream;
import static com.google.common.base.Preconditions.checkArgument;
import static java.util.stream.Collectors.toUnmodifiableList;

@Immutable
public abstract class TagHolderSkeleton implements TagHolder {

  static final String EMPTY_TAG_ERROR_MESSAGE = "At least one tag must be provided";
  static final String EMPTY_TAG_NAME_ERROR_MESSAGE = "At least one tag name must be provided";

  @Override
  @Parameter
  public abstract List<Tag> getTags();

  @Override
  public boolean hasTag(Tag tag) {
    return getTags().contains(tag);
  }

  @Override
  public boolean hasTag(String tag) {
    return anyTagMatch(Tags.tag(tag));
  }

  @Override
  public boolean anyTagMatch(Tag tag) {
    for (var tag1 : getTags()) {
      if (tag1.matches(tag)) {
        return true;
      }
    }
    return false;
  }

  @Override
  public boolean any(String... tagNames) {
    checkArgument(tagNames.length > 0, EMPTY_TAG_NAME_ERROR_MESSAGE);
    var set = Set.of(tagNames);
    for (Tag tag : getTags()) {
      if (set.contains(tag.getName())) {
        return true;
      }
    }
    return false;
  }

  @Override
  public boolean any(Enum<?>... tagNames) {
    return any(asStringArray(tagNames));
  }

  @Override
  public boolean any(Tag... tags) {
    checkArgument(tags.length > 0, EMPTY_TAG_ERROR_MESSAGE);
    for (Tag tag : tags) {
      if (anyTagMatch(tag)) {
        return true;
      }
    }
    return false;
  }

  @Override
  public boolean all(String... tagNames) {
    checkArgument(tagNames.length > 0, EMPTY_TAG_NAME_ERROR_MESSAGE);
    var set = Set.of(tagNames);
    for (Tag tag : getTags()) {
      if (!set.contains(tag.getName())) {
        return false;
      }
    }
    return true;
  }

  @Override
  public boolean all(Enum<?>... tagNames) {
    return all(asStringArray(tagNames));
  }

  @Override
  public boolean all(Tag... tags) {
    checkArgument(tags.length > 0, EMPTY_TAG_ERROR_MESSAGE);
    for (Tag tag : tags) {
      if (!anyTagMatch(tag)) {
        return false;
      }
    }
    return true;
  }

  @Override
  public boolean none(String... tagNames) {
    checkArgument(tagNames.length > 0, EMPTY_TAG_NAME_ERROR_MESSAGE);
    var set = Set.of(tagNames);
    for (Tag tag : getTags()) {
      if (set.contains(tag.getName())) {
        return false;
      }
    }
    return true;
  }

  @Override
  public boolean none(Enum<?>... tagNames) {
    return none(asStringArray(tagNames));
  }

  @Override
  public boolean none(Tag... tags) {
    checkArgument(tags.length > 0, EMPTY_TAG_ERROR_MESSAGE);
    for (Tag tag : tags) {
      if (anyTagMatch(tag)) {
        return false;
      }
    }
    return true;
  }

  @Override
  public boolean isEmpty() {
    return getTags().isEmpty();
  }

  @Override
  public boolean isMultiple(String name) {
    var count = getTags()
        .stream()
        .filter(tag -> tag.matchesName(name))
        .count();
    return count > 1;
  }

  @Override
  public Optional<String> getValue(String name) {
    return getTagsByName(name)
        .map(KeyValueTag::getValue)
        .flatMap(Optional::stream)
        .findFirst();
  }

  @Override
  public Optional<String> getValue(Enum<?> name) {
    return getValue(name.name());
  }

  @Override
  public String getValue(String name, String defaultValue) {
    return getValue(name).orElse(defaultValue);
  }

  @Override
  public String getValue(Enum<?> name, String defaultValue) {
    return getValue(name).orElse(defaultValue);
  }

  @Override
  public <T> Optional<T> getValue(String name, Function<String, T> mapper) {
    return getValue(name).map(mapper);
  }

  @Override
  public <T> Optional<T> getValue(Enum<?> name, Function<String, T> mapper) {
    return getValue(name).map(mapper);
  }

  @Override
  public <T> T getValue(String name, Function<String, T> mapper, T defaultValue) {
    return getValue(name, mapper).orElse(defaultValue);
  }

  @Override
  public <T> T getValue(Enum<?> name, Function<String, T> mapper, T defaultValue) {
    return getValue(name, mapper).orElse(defaultValue);
  }

  @Override
  public List<String> getValues(String name) {
    return getTagsByName(name)
        .map(KeyValueTag::getValue)
        .flatMap(Optional::stream)
        .collect(toUnmodifiableList());
  }

  private Stream<KeyValueTag> getTagsByName(String name) {
    return getTags()
        .stream()
        .filter(KeyValueTag.class::isInstance)
        .map(KeyValueTag.class::cast)
        .filter(tag -> tag.matchesName(name));
  }

  @Override
  public String toString() {
    return getTags().toString();
  }

  private String[] asStringArray(Enum<?>... tagNames) {
    String[] strings = new String[tagNames.length];
    for (int i = 0; i < tagNames.length; i++) {
      strings[i] = tagNames[i].name();
    }
    return strings;
  }

  @Override
  public Iterator<Tag> iterator() {
    return getTags().iterator();
  }

}
