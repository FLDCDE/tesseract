package com.fieldcode.tesseract.jackson;


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

class TrackJacksonModuleTest extends TimeTestSupport {

  @Test
  void track_Jackson() {
    var track = Tracks.track(
        Presences.presence(Moments.now(), Locations.location(10, 100)),
        Presences.presence(Moments.now(), Locations.location(200, 100)),
        Distances.distance(100, DistanceUnit.KILOMETER)
    );

    var parsed = Jsons.clone(track, Track.class);

    assertThat(parsed)
        .isEqualTo(track)
        .isNotSameAs(track);

  }

}
