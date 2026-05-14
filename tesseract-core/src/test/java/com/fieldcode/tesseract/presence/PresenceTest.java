package com.fieldcode.tesseract.presence;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.fieldcode.tesseract.Location;
import com.fieldcode.tesseract.Moment;
import com.fieldcode.tesseract.Presence;
import com.fieldcode.tesseract.location.Locations;
import com.fieldcode.tesseract.moment.Moments;
import com.fieldcode.tesseract.tag.Tags;

import static org.assertj.core.api.Assertions.assertThat;

public class PresenceTest {


  private Location location;
  private Presence presence;
  private Moment locationTime;

  @BeforeEach
  public void setUp() {
    locationTime = Moments.now();
    location = Locations.location(50, 100);
    presence = Presences.presence(locationTime, location);
  }

  @Test
  void presence_builder_Success() {
    assertThat(Presences.presence(locationTime, location.getCode()))
        .isEqualTo(
            ImmutablePresence.of(locationTime, Locations.location(50, 100), Tags.holder())
        );
  }

  @Test
  void presence_getTime_Success() {
    assertThat(presence.getMoment())
        .isEqualTo(locationTime);
  }

  @Test
  void presence_compareTo_Success() {
    var locationMomentOther = Presences.presence(
        Moments.now(),
        Locations.location(150, 100)
    );

    assertThat(presence.compareTo(locationMomentOther)).isNegative();
  }

}
