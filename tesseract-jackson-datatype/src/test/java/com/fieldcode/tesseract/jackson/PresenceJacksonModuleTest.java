package com.fieldcode.tesseract.jackson;

import org.junit.jupiter.api.Test;

import com.fieldcode.tesseract.Presence;
import com.fieldcode.tesseract.jackson.test_utils.Jsons;
import com.fieldcode.tesseract.jackson.test_utils.TimeTestSupport;
import com.fieldcode.tesseract.tag.Tags;

import static com.fieldcode.tesseract.location.Locations.location;
import static org.assertj.core.api.Assertions.assertThat;

class PresenceJacksonModuleTest extends TimeTestSupport {

  @Test
  public void clone_NonTagged_Success() {
    var presence = presence(
        10,
        location(-15, 15)
    );

    var cloned = Jsons.clone(presence, Presence.class);

    assertThat(cloned)
        .isEqualTo(
            presence(10, location(-15, 15))
        );
  }

  @Test
  void clone_Tagged_Success() {
    var tags = Tags.builder()
        .tag("tag1")
        .tag("tag2", "value2")
        .tag("tag3", "value3")
        .build();

    var presence = presence(10, location(-15, 15), tags);

    var cloned = Jsons.clone(presence, Presence.class);

    assertThat(cloned)
        .isEqualTo(presence);
  }

}
