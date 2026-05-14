package com.fieldcode.tesseract.jackson;

import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fieldcode.tesseract.Taggable.Tag;
import com.fieldcode.tesseract.jackson.test_utils.Jsons;
import com.fieldcode.tesseract.tag.Tags;

import static org.assertj.core.api.Assertions.assertThat;

class TaggableJacksonTest {

  @Test
  void clone_Tag_Success() {
    var tag1 = Tags.tag("tag1");
    var tag2 = Tags.tag("tag2", "value2");

    var tags = Set.of(tag1, tag2);

    var cloned = Jsons.clone(tags, new TypeReference<List<Tag>>() {});
    assertThat(cloned).containsAll(tags);
  }

}
