package com.fieldcode.tesseract.jackson;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.fieldcode.tesseract.PresenceCollection;
import com.fieldcode.tesseract.jackson.test_utils.Jsons;
import com.fieldcode.tesseract.jackson.test_utils.TimeTestSupport;
import com.fieldcode.tesseract.presence.PresenceSets;
import com.fieldcode.tesseract.tag.Tags;

import static com.fieldcode.tesseract.location.Locations.location;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("PresenceCollectionJacksonModule")
class PresenceCollectionJacksonModuleTest extends TimeTestSupport {

  @Test
  @DisplayName("should round-trip a presence collection with mixed locations and tags")
  void clone_Mixed_Success() {
    // Arrange: a presence collection with two presences, one carrying tags
    var tags = Tags.builder()
        .tag("tag1")
        .tag("tag2", "value2")
        .tag("tag3", "value3")
        .build();

    var presenceSet = PresenceSets.immutable(
        presence(10, location(-15, 15), tags),
        presence(15, -15, 15)
    );

    // Act: serialize and deserialize it back
    var cloned = Jsons.clone(presenceSet, PresenceCollection.class);

    // Assert: the round-tripped collection equals the original
    assertThat(cloned)
        .isEqualTo(presenceSet);
  }

  @Test
  @DisplayName("should deserialize an empty array to the singleton empty presence collection")
  void clone_Empty_ReturnsSingletonEmptyInstance() {
    // Act: deserialize an empty JSON array
    var deserialized = Jsons.parse("[]", PresenceCollection.class);

    // Assert: it's the same singleton instance returned by PresenceSets.empty(), not a freshly allocated one
    assertThat(deserialized)
        .isSameAs(PresenceSets.empty());
  }

}
