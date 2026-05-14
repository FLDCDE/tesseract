package com.fieldcode.tesseract.tag;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.fieldcode.tesseract.Taggable.TagHolder;

import static org.assertj.core.api.Assertions.assertThat;

class TagHolderTest {

  private TagHolder tags;

  @BeforeEach
  void setUp() {
    // Same as previous
    tags = TagsBuilder.of()
        .tag("tag1")
        .tag("tag2", "value2.1")
        .tag("tag2", "value2.2")
        .tag("tag2", "value2.2")            // Same as previous
        .tag(Tags.tag("tag3", "value3"))
        .build();
  }

  @Test
  void hasTag_Success() {
    assertThat(tags.hasTag("tag1"))
        .isTrue();

    assertThat(tags.hasTag("tag2"))
        .isTrue();

    assertThat(tags.hasTag("tag3"))
        .isTrue();

    assertThat(tags.hasTag("tag4"))
        .isFalse();
  }

  @Test
  void getValue_Success() {
    assertThat(tags.getValue("tag1"))
        .isEmpty();

    assertThat(tags.getValue("tag2"))
        .isPresent()
        .get()
        .isIn("value2.1", "value2.2");

    assertThat(tags.getValue("tag3"))
        .contains("value3");

    assertThat(tags.getValue("tag4"))
        .isEmpty();

  }

  @Test
  void getValues_Success() {
    assertThat(tags.getValues("tag1"))
        .isEmpty();

    assertThat(tags.getValues("tag2"))
        .contains("value2.1", "value2.2");

    assertThat(tags.getValues("tag3"))
        .contains("value3");

    assertThat(tags.getValue("tag4"))
        .isEmpty();
  }

  @Test
  void isMultiple_Success() {
    assertThat(tags.isMultiple("tag1"))
        .isFalse();

    assertThat(tags.isMultiple("tag2"))
        .isTrue();

    assertThat(tags.isMultiple("tag3"))
        .isFalse();

    assertThat(tags.isMultiple("tag4"))
        .isFalse();
  }

  @Test
  void empty() {
    var holder = Tags.holder((List.of()));
    assertThat(holder.isEmpty()).isTrue();
    assertThat(holder).isInstanceOf(EmptyTagHolder.class);
  }

  @Test
  void getValue_Default_Success() {

    var tags = Tags.holder(Tags.tag("tag1", "1"));

    assertThat(tags.getValue("tag2", "n/a"))
        .contains("n/a");

  }

  @Test
  void getValue_Mapper_Success() {

    var tags = Tags.holder(Tags.tag("tag1", "1"));

    assertThat(tags.getValue("tag1", Integer::parseInt))
        .contains(1);

  }

  @Test
  void getValue_MapperDefault_Success() {
    var tags = Tags.holder(Tags.tag("tag1", "1"));

    assertThat(tags.getValue("tag2", Integer::parseInt, 2))
        .isEqualTo(2);
  }

  @Test
  void any_True_Success() {
    var any = tags.any("tag1", "tag2", "unknown");
    assertThat(any).isTrue();
  }

  @Test
  void any_False_Success() {
    var any = tags.any("unknown1", "unknown2", "unknown3");
    assertThat(any).isFalse();
  }

  @Test
  void any_TagTrue_Success() {
    var tag1 = Tags.tag("unknown");
    var tag2 = Tags.tag("tag2");
    var any = tags.any(tag1, tag2);
    assertThat(any).isTrue();
  }

  @Test
  void any_TagFalse_Success() {
    var tag1 = Tags.tag("unknown1");
    var tag2 = Tags.tag("unknown2");
    var any = tags.any(tag1, tag2);
    assertThat(any).isFalse();
  }

  @Test
  void all_True_Success() {
    var all = tags.all("tag1", "tag2", "tag3");
    assertThat(all).isTrue();
  }

  @Test
  void all_False_Success() {
    var all = tags.all("tag1", "tag2", "unknown");
    assertThat(all).isFalse();
  }

  @Test
  void all_TagTrue_Success() {
    var tag1 = Tags.tag("tag1");
    var tag2 = Tags.tag("tag2");
    var all = tags.all(tag1, tag2);
    assertThat(all).isTrue();
  }

  @Test
  void all_TagFalse_Success() {
    var tag1 = Tags.tag("tag1");
    var tag2 = Tags.tag("unknown");
    var all = tags.all(tag1, tag2);
    assertThat(all).isFalse();
  }

  @Test
  void none_True_Success() {
    var none = tags.none("unknown1", "unknown2", "unknown3");
    assertThat(none).isTrue();
  }

  @Test
  void none_False_Success() {
    var none = tags.none("tag1", "tag2", "unknown");
    assertThat(none).isFalse();
  }

  @Test
  void none_TagTrue_Success() {
    var tag1 = Tags.tag("unknown1");
    var tag2 = Tags.tag("unknown2");
    var none = tags.none(tag1, tag2);
    assertThat(none).isTrue();
  }

  @Test
  void none_TagFalse_Success() {
    var tag1 = Tags.tag("tag1");
    var tag2 = Tags.tag("unknown");
    var none = tags.none(tag1, tag2);
    assertThat(none).isFalse();
  }

}

