package com.fieldcode.tesseract.tag;

import com.fieldcode.tesseract.Taggable.Tag;
import com.fieldcode.tesseract.Taggable.TagHolder;
import com.fieldcode.tesseract.utils.iterator.Iterators;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import static com.fieldcode.tesseract.tag.TagHolderSkeleton.EMPTY_TAG_NAME_ERROR_MESSAGE;
import static com.google.common.base.Preconditions.checkArgument;

public class EmptyTagHolder implements TagHolder {

  private static final List<Tag> EMPTY_TAGS = List.of();
  private static final TagHolder INSTANCE = new EmptyTagHolder();

  private EmptyTagHolder() {}

  static TagHolder of() {
    return INSTANCE;
  }

  @Override
  public List<Tag> getTags() {
    return EMPTY_TAGS;
  }

  @Override
  public boolean hasTag(Tag tag) {
    return false;
  }

  @Override
  public boolean hasTag(String tag) {
    return false;
  }

  @Override
  public boolean anyTagMatch(Tag tag) {
    return false;
  }

  @Override
  public boolean any(String... tagNames) {
    checkArgument(tagNames.length > 0, EMPTY_TAG_NAME_ERROR_MESSAGE);
    return false;
  }

  @Override
  public boolean any(Enum<?>... tagNames) {
    checkArgument(tagNames.length > 0, EMPTY_TAG_NAME_ERROR_MESSAGE);
    return false;
  }

  @Override
  public boolean any(Tag... tags) {
    checkArgument(tags.length > 0, EMPTY_TAG_NAME_ERROR_MESSAGE);
    return false;
  }

  @Override
  public boolean all(String... tagNames) {
    checkArgument(tagNames.length > 0, EMPTY_TAG_NAME_ERROR_MESSAGE);
    return false;
  }

  @Override
  public boolean all(Enum<?>... tagNames) {
    checkArgument(tagNames.length > 0, EMPTY_TAG_NAME_ERROR_MESSAGE);
    return false;
  }

  @Override
  public boolean all(Tag... tags) {
    checkArgument(tags.length > 0, EMPTY_TAG_NAME_ERROR_MESSAGE);
    return false;
  }

  @Override
  public boolean none(String... tagNames) {
    checkArgument(tagNames.length > 0, EMPTY_TAG_NAME_ERROR_MESSAGE);
    return true;
  }

  @Override
  public boolean none(Enum<?>... tagNames) {
    checkArgument(tagNames.length > 0, EMPTY_TAG_NAME_ERROR_MESSAGE);
    return true;
  }

  @Override
  public boolean none(Tag... tags) {
    checkArgument(tags.length > 0, EMPTY_TAG_NAME_ERROR_MESSAGE);
    return true;
  }

  @Override
  public boolean isEmpty() {
    return true;
  }

  @Override
  public boolean isMultiple(String name) {
    return false;
  }

  @Override
  public Optional<String> getValue(String name) {
    return Optional.empty();
  }

  @Override
  public Optional<String> getValue(Enum<?> name) {
    return Optional.empty();
  }

  @Override
  public String getValue(String name, String defaultValue) {
    return defaultValue;
  }

  @Override
  public String getValue(Enum<?> name, String defaultValue) {
    return defaultValue;
  }

  @Override
  public <T> Optional<T> getValue(String name, Function<String, T> mapper) {
    return Optional.empty();
  }

  @Override
  public <T> Optional<T> getValue(Enum<?> name, Function<String, T> mapper) {
    return Optional.empty();
  }

  @Override
  public <T> T getValue(String name, Function<String, T> mapper, T defaultValue) {
    return defaultValue;
  }

  @Override
  public <T> T getValue(Enum<?> name, Function<String, T> mapper, T defaultValue) {
    return defaultValue;
  }

  @Override
  public List<String> getValues(String name) {
    return List.of();
  }

  @Override
  public String toString() {
    return "";
  }

  @Override
  public Iterator<Tag> iterator() {
    return Iterators.empty();
  }

}
