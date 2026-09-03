package com.fieldcode.tesseract.jackson;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.fieldcode.tesseract.TaggedIntervalCollection;
import com.fieldcode.tesseract.collection.ImmutableTaggedIntervalCollection;
import com.fieldcode.tesseract.interval.IntervalSets;
import com.fieldcode.tesseract.jackson.test_utils.Jsons;
import com.fieldcode.tesseract.jackson.test_utils.TimeTestSupport;
import com.fieldcode.tesseract.tag.Tags;

import static com.fieldcode.tesseract.interval.Intervals.interval;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("TaggedIntervalCollection serialization and deserialization")
class TaggedIntervalCollectionJacksonTest extends TimeTestSupport {

  @Test
  @DisplayName("should round-trip a tagged interval collection with its id, tags, and intervals")
  void clone_TaggedIntervalSet_Success() {

    // Arrange: a disjoint interval collection with two tags, wrapped with an id
    var collection = IntervalSets.disjoint(
        interval(at(0), at(2)),
        interval(at(5), at(10)),
        interval(at(11), at(15))
    );

    var tag1 = Tags.tag("tag1");
    var tag2 = Tags.tag("tag2");
    var tags = Tags.builder().add(tag1).add(tag2).build();
    var tagged = ImmutableTaggedIntervalCollection.of("id", collection, tags);

    // Act: serialize and deserialize it back
    var cloned = Jsons.clone(tagged, TaggedIntervalCollection.class);

    // Assert: the round-tripped collection retains the same id, tags, and intervals
    assertThat(cloned.getId())
        .isEqualTo("id");
    assertThat(cloned.getTags().getTags())
        .containsOnly(tag1, tag2);
    assertThat(cloned.intervals())
        .toIterable()
        .containsExactly(
            interval(at(0), at(2)),
            interval(at(5), at(10)),
            interval(at(11), at(15))
        );
  }

}
