package com.fieldcode.tesseract.tag;

import java.util.Optional;

import org.immutables.value.Value.Immutable;
import org.immutables.value.Value.Parameter;

import com.fieldcode.tesseract.Taggable.KeyValueTag;
import com.fieldcode.tesseract.Taggable.NamedTag;
import com.fieldcode.tesseract.Taggable.Tag;

@Immutable
abstract class NamedTagSkeleton implements NamedTag {

  @Override
  @Parameter
  public abstract String getName();

  @Override
  public boolean matches(Tag other) {
    if (other instanceof KeyValueTag) {
      return false;
    }

    if (other instanceof NamedTag) {
      return this.equals(other);
    }
    throw new IllegalStateException("Unknown tag type: " + other.getClass());
  }

  @Override
  public boolean matchesName(String name) {
    return getName().equals(name);
  }

  @Override
  public Optional<String> getValue() {
    return Optional.empty();
  }

  @Override
  public String toString() {
    return TagDecoder.quote(getName());
  }

}
