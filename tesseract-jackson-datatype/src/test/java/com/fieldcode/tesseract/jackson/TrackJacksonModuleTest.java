package com.fieldcode.tesseract.jackson;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.fieldcode.tesseract.DistanceUnit;
import com.fieldcode.tesseract.Track;
import com.fieldcode.tesseract.distance.Distances;
import com.fieldcode.tesseract.jackson.test_utils.Jsons;
import com.fieldcode.tesseract.jackson.test_utils.TimeTestSupport;
import com.fieldcode.tesseract.location.Locations;
import com.fieldcode.tesseract.moment.Moments;
import com.fieldcode.tesseract.presence.Presences;
import com.fieldcode.tesseract.track.Tracks;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Track serialization and deserialization")
class TrackJacksonModuleTest extends TimeTestSupport {

  @Test
  @DisplayName("should round-trip a track with two presences and a distance")
  void track_Jackson() {
    // Arrange: a track made of two presences and a distance
    var track = Tracks.track(
        Presences.presence(Moments.now(), Locations.location(10, 100)),
        Presences.presence(Moments.now(), Locations.location(200, 100)),
        Distances.distance(100, DistanceUnit.KILOMETER)
    );

    // Act: serialize and deserialize it back
    var parsed = Jsons.clone(track, Track.class);

    // Assert: the round-tripped track equals but is not the same instance as the original
    assertThat(parsed)
        .isEqualTo(track)
        .isNotSameAs(track);

  }

}
