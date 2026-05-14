package com.fieldcode.tesseract.tag;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.fieldcode.tesseract.Taggable;

import lombok.Value;
import static com.fieldcode.tesseract.tag.TagPredicates.all;
import static com.fieldcode.tesseract.tag.TagPredicates.any;
import static com.fieldcode.tesseract.tag.TagPredicates.match;
import static com.fieldcode.tesseract.tag.TagPredicates.none;
import static org.assertj.core.api.Assertions.assertThat;

class TagPredicatesTest {

  private TaggableObject taggable;

  @BeforeEach
  void setUp() {

    var tags = Tags.builder()
        .tag("tag1")
        .tag("tag2", "value2")
        .tag("tag3", "value3")
        .tag("tag3", "value3-2")
        .build();

    taggable = TaggableObject.of(tags);

  }

  @Test
  void match_Key_Success() {

    var predicate = match("tag1");

    assertThat(predicate.test(taggable))
        .isTrue();

  }

  @Test
  void match_KeyValue_Success() {

    var predicate = match("tag2", "value2");

    assertThat(predicate.test(taggable))
        .isTrue();

  }

  @Test
  void match_Any_Success() {

    var predicate = any(
        match("tag1"),
        match("tag1", "value1")
    );

    assertThat(predicate.test(taggable))
        .isTrue();

  }

  @Test
  void match_All_Success() {

    var predicate = any(
        match("tag1"),
        match("tag2", "value2")
    );

    assertThat(predicate.test(taggable))
        .isTrue();

  }

  @Test
  void match_None_Success() {

    var predicate = none(
        match("tag4"),
        match("tag2", "value1")
    );

    assertThat(predicate.test(taggable))
        .isTrue();

  }

  @Test
  void match_Nested_Success() {

    var predicate = any(
        match("tag4"),
        none(
            match("tag2", "value2"),
            match("tag3", "value3")
        ),
        all(
            match("tag1"),
            match("tag2", "value2")
        )
    );

    assertThat(predicate.test(taggable))
        .isTrue();

  }

  @Test
  void match_Stream_Success() {

    var tag1 = Tags.tag("tag1");
    var tag2 = Tags.tag("tag2", "value2");
    var tag3 = Tags.tag("tag3", "value3");

    var tags1 = Tags.builder()
        .add(tag1)
        .add(tag2)
        .add(tag3)
        .build();

    var tags2 = Tags.builder()
        .add(tag2)
        .build();

    var tags3 = Tags.builder()
        .add(tag3)
        .build();

    var taggable1 = TaggableObject.of(tags1);
    var taggable2 = TaggableObject.of(tags2);
    var taggable3 = TaggableObject.of(tags3);

    var entities = List.of(taggable1, taggable2, taggable3);

    var predicate = any(
        match("tag1"),
        match("tag2", "value2"),
        none(
            match("tag3")
        )
    );

    var filtered = entities.stream()
        .filter(predicate);

    assertThat(filtered)
        .containsExactly(taggable1, taggable2);

  }

  @Value(staticConstructor = "of")
  private static class TaggableObject implements Taggable {

    TagHolder tags;

  }

}
