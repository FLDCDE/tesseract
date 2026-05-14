package com.fieldcode.tesseract.jackson;

import org.junit.jupiter.api.Test;

import com.fieldcode.tesseract.PresenceSet;
import com.fieldcode.tesseract.jackson.test_utils.Jsons;
import com.fieldcode.tesseract.jackson.test_utils.TimeTestSupport;
import com.fieldcode.tesseract.presence.PresenceSets;
import com.fieldcode.tesseract.tag.Tags;

import static com.fieldcode.tesseract.location.Locations.location;
import static org.assertj.core.api.Assertions.assertThat;

class PresenceSetJacksonModuleTest extends TimeTestSupport {

  @Test
  void clone_Mixed_Success() {
    var tags = Tags.builder()
        .tag("tag1")
        .tag("tag2", "value2")
        .tag("tag3", "value3")
        .build();

    var presenceSet = PresenceSets.presences(
        presence(10, location(-15, 15), tags),
        presence(15, -15, 15)
    );

    var cloned = Jsons.clone(presenceSet, PresenceSet.class);

    assertThat(cloned)
        .isEqualTo(presenceSet);
  }

}
