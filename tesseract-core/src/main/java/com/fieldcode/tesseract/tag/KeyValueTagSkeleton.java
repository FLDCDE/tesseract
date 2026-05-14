package com.fieldcode.tesseract.tag;

import java.util.Optional;

import org.immutables.value.Value.Immutable;
import org.immutables.value.Value.Parameter;

import com.fieldcode.tesseract.Taggable.KeyValueTag;
import com.fieldcode.tesseract.Taggable.NamedTag;
import com.fieldcode.tesseract.Taggable.Tag;

@Immutable
abstract class KeyValueTagSkeleton implements KeyValueTag {

  @Override
  @Parameter
  public abstract String getName();

  @Override
  public boolean matches(Tag other) {
    if (other instanceof KeyValueTag) {
      return this.equals(other);
    }

    if (other instanceof NamedTag) {
      var keyTag = (NamedTag) other;
      return this.getName().equals(keyTag.getName());
    }

    throw new IllegalStateException("Unknown tag type: " + other.getClass());
  }

  @Override
  public boolean matchesName(String name) {
    return getName().equals(name);
  }

  @Override
  @Parameter
  public abstract Optional<String> getValue();

  @Override
  public String toString() {
    var name = getName();
    var value = getValue().orElseThrow();
    return TagDecoder.quote(name, value);
  }

}
