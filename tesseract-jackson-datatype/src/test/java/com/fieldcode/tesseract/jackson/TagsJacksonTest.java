package com.fieldcode.tesseract.jackson;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fieldcode.tesseract.Taggable.Tag;
import com.fieldcode.tesseract.Taggable.TagHolder;
import com.fieldcode.tesseract.jackson.test_utils.Jsons;
import com.fieldcode.tesseract.tag.Tags;

import java.util.Set;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Tag serialization and deserialization")
class TagsJacksonTest {

  @Test
  @DisplayName("Should correctly serialize and deserialize a set of tags")
  void clone_Tag_Success() {
    var tag1 = Tags.tag("tag1");
    var tag2 = Tags.tag("tag2", "value2");
    var tag3 = Tags.tag("tag3", "value3");

    var tags = Set.of(tag1, tag2, tag3);

    var cloned = Jsons.clone(tags, new TypeReference<Set<Tag>>() {});

    assertThat(cloned)
        .isEqualTo(tags);
  }

  @Test
  @DisplayName("Should correctly serialize and deserialize a TagHolder with multiple tags")
  void clone_TagHolder_Success() {

    var tags = Tags.builder()
        .tag("tag1")
        .tag("tag2", "value2")
        .tag("tag3", "value3")
        .build();

    var cloned = Jsons.clone(tags, TagHolder.class);

    assertThat(cloned)
        .isEqualTo(tags);
  }

}
