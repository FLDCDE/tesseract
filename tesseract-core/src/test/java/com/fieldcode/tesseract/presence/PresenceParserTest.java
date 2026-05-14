package com.fieldcode.tesseract.presence;

import java.time.OffsetDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.fieldcode.tesseract.Presence;
import com.fieldcode.tesseract.tag.Tags;

import static com.fieldcode.tesseract.location.Locations.location;
import static com.fieldcode.tesseract.moment.Moments.moment;
import static org.assertj.core.api.Assertions.assertThat;

public class PresenceParserTest {

  private Presence presence;
  private OffsetDateTime locationTime;

  @BeforeEach
  public void setUp() {
    locationTime = OffsetDateTime.now();
    var location = location(50, 100);
    presence = Presences.presence(locationTime, location);
  }

  @Test
  public void presenceParser_Parse_Success() {
    var parsed = PresenceParser.parse("50,100@" + locationTime);
    assertThat(parsed).isEqualTo(presence);
  }

  @Test
  void parse_WithTags_Success() {
    var tags = Tags.builder()
        .tag("tag1")
        .tag("tag2", "value2")
        .tag("tag3", "value3.1")
        .tag("tag3", "value3.2")
        .build();

    var presence = Presences.presence(moment(locationTime), location(50, 100), tags);
    var parsed = PresenceParser.parse(presence.toString());

    assertThat(parsed)
        .isEqualTo(presence);
  }

}
