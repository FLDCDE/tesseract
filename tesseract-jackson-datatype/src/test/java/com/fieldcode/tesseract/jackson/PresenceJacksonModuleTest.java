package com.fieldcode.tesseract.jackson;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.fieldcode.tesseract.Presence;
import com.fieldcode.tesseract.jackson.test_utils.Jsons;
import com.fieldcode.tesseract.jackson.test_utils.TimeTestSupport;
import com.fieldcode.tesseract.tag.Tags;

import static com.fieldcode.tesseract.location.Locations.location;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("PresenceJacksonModule")
class PresenceJacksonModuleTest extends TimeTestSupport {

  @Test
  @DisplayName("should round-trip a presence without tags")
  public void clone_NonTagged_Success() {
    // Arrange: a presence with no tags
    var presence = presence(
        10,
        location(-15, 15)
    );

    // Act: serialize and deserialize it back
    var cloned = Jsons.clone(presence, Presence.class);

    // Assert: the round-tripped presence equals the original
    assertThat(cloned)
        .isEqualTo(
            presence(10, location(-15, 15))
        );
  }

  @Test
  @DisplayName("should round-trip a presence carrying tags")
  void clone_Tagged_Success() {
    // Arrange: a presence carrying a mix of valueless and valued tags
    var tags = Tags.builder()
        .tag("tag1")
        .tag("tag2", "value2")
        .tag("tag3", "value3")
        .build();

    var presence = presence(10, location(-15, 15), tags);

    // Act: serialize and deserialize it back
    var cloned = Jsons.clone(presence, Presence.class);

    // Assert: the round-tripped presence equals the original
    assertThat(cloned)
        .isEqualTo(presence);
  }

}
