package com.fieldcode.tesseract.jackson;

import java.time.OffsetDateTime;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;

import com.fieldcode.tesseract.DistanceUnit;
import com.fieldcode.tesseract.FiniteMoment;
import com.fieldcode.tesseract.InfiniteMoment;
import com.fieldcode.tesseract.IntervalSet;
import com.fieldcode.tesseract.Track;
import com.fieldcode.tesseract.distance.Distances;
import com.fieldcode.tesseract.interval.IntervalSets;
import com.fieldcode.tesseract.jackson.test_utils.Jsons;
import com.fieldcode.tesseract.jackson.test_utils.TimeTestSupport;
import com.fieldcode.tesseract.location.Locations;
import com.fieldcode.tesseract.moment.Moments;
import com.fieldcode.tesseract.presence.Presences;
import com.fieldcode.tesseract.track.Tracks;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;
import static org.assertj.core.api.Assertions.assertThat;

class MomentJacksonModuleTest extends TimeTestSupport {

  @Test
  void moment() {

    var now = OffsetDateTime.now();

    var all = AllMoments.builder()
        .inf(Moments.inf())
        .ninf(Moments.ninf())
        .moment(Moments.moment(now))
        .build();

    var cloned = Jsons.clone(all, AllMoments.class);

    assertThat(cloned).isEqualTo(all);

  }

  @Test
  void track_SERDES_success() {
    var track = Tracks.track(
        Presences.presence(Moments.now(), Locations.location(10, 100)),
        Presences.presence(Moments.now(), Locations.location(200, 100)),
        Distances.distance(100, DistanceUnit.KILOMETER)
    );
    var json = Jsons.stringify(track, true);

    var parsed = Jsons.parse(json, Track.class);

    assertThat(parsed).isEqualTo(track);

  }

  @Test
  void intervalSet_SERDES_success() {
    var set = IntervalSets.disjoint(
        Stream.of(
            interval(at(0), at(2)),
            interval(at(5), at(10)),
            interval(at(11), at(15))
        )
    );

    var json = Jsons.stringify(set, true);

    var parsed = Jsons.parse(json, IntervalSet.class);

    assertThat(parsed.intervals().stream()).containsExactly(
        interval(at(0), at(2)),
        interval(at(5), at(10)),
        interval(at(11), at(15))
    );

  }

  @Data
  @Builder
  @Jacksonized
  private static class AllMoments {

    private FiniteMoment moment;
    private InfiniteMoment inf;
    private InfiniteMoment ninf;

  }

}
