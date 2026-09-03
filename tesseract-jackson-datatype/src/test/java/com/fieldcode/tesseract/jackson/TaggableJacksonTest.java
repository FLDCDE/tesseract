package com.fieldcode.tesseract.jackson;

import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fieldcode.tesseract.Taggable.Tag;
import com.fieldcode.tesseract.jackson.test_utils.Jsons;
import com.fieldcode.tesseract.tag.Tags;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Taggable serialization and deserialization")
class TaggableJacksonTest {

  @Test
  @DisplayName("should round-trip a set of tags through serialization as a list")
  void clone_Tag_Success() {
    // Arrange: a set of two tags, one plain and one with a value
    var tag1 = Tags.tag("tag1");
    var tag2 = Tags.tag("tag2", "value2");

    var tags = Set.of(tag1, tag2);

    // Act: serialize and deserialize the tags as a list
    var cloned = Jsons.clone(tags, new TypeReference<List<Tag>>() {});

    // Assert: the round-tripped list contains all original tags
    assertThat(cloned)
        .containsAll(tags);
  }

}
