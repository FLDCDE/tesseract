package com.fieldcode.tesseract.tag;

import org.junit.jupiter.api.Test;

import static com.fieldcode.tesseract.tag.Tags.*;
import static com.fieldcode.tesseract.tag.Tags.tag;
import static org.assertj.core.api.Assertions.assertThat;

class TagsBuilderTest {


  @Test
  void empty_EmptyInstance() {
    var tags = TagsBuilder.of().build();
    assertThat(tags)
        .isInstanceOf(EmptyTagHolder.class)
        .isEmpty();
  }

  @Test
  void simpleTag_Success() {
    var tags = TagsBuilder.of()
        .tag("tag1")
        .build();

    assertThat(tags)
        .isInstanceOf(ImmutableTagHolder.class)
        .containsExactly(
            tag("tag1")
        );
  }

  @Test
  void simpleTagWithValues_Success() {
    var tags = TagsBuilder.of()
        .tag("tag1", "value1")
        .build();

    assertThat(tags)
        .isInstanceOf(ImmutableTagHolder.class)
        .containsExactly(
            tag("tag1", "value1")
        );
  }

  @Test
  void multipleTags_Success() {
    var tags = TagsBuilder.of()
        .tag("tag1")
        .tag("tag2", "value2")
        .tag("tag3", "value3")
        .build();

    assertThat(tags)
        .isInstanceOf(ImmutableTagHolder.class)
        .containsExactly(
            tag("tag1"),
            tag("tag2", "value2"),
            tag("tag3", "value3")
        );
  }

  @Test
  void multiTags_Success() {
    var tags = TagsBuilder.of()
        .tag("tag1", "value1")
        .tag("tag1", "value2")
        .tag("tag1", "value3")
        .build();

    assertThat(tags)
        .isInstanceOf(ImmutableTagHolder.class)
        .containsExactly(
            tag("tag1", "value1"),
            tag("tag1", "value2"),
            tag("tag1", "value3")
        );

    assertThat(tags.getValues("tag1"))
        .containsExactly("value1", "value2", "value3");
  }

  @Test
  void builderFrom_Success() {

    var original1 = holder(tag("tag1", "value1"));
    var original2 = holder(tag("tag2", "value2"));

    var tags = builderFrom(original1, original2)
        .tag("tag3", "value3")
        .build();

    assertThat(tags)
        .containsExactly(
            tag("tag1", "value1"),
            tag("tag2", "value2"),
            tag("tag3", "value3")
        );
  }

}
