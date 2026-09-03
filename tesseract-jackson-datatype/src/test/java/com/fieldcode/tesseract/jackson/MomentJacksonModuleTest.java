package com.fieldcode.tesseract.jackson;

import java.time.OffsetDateTime;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
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

@DisplayName("MomentJacksonModule")
class MomentJacksonModuleTest extends TimeTestSupport {

  @Test
  @DisplayName("should round-trip a container holding a finite moment and both infinite moments")
  void moment() {

    // Arrange: a container with a finite moment and the positive/negative infinite moments
    var now = OffsetDateTime.now();

    var all = AllMoments.builder()
        .inf(Moments.inf())
        .ninf(Moments.ninf())
        .moment(Moments.moment(now))
        .build();

    // Act: serialize and deserialize it back
    var cloned = Jsons.clone(all, AllMoments.class);

    // Assert: the round-tripped container equals the original
    assertThat(cloned)
        .isEqualTo(all);

  }

  @Test
  @DisplayName("should round-trip a track built from presences and a distance")
  void track_Clone_success() {
    // Arrange: a track made of two presences and a distance
    var track = Tracks.track(
        Presences.presence(Moments.now(), Locations.location(10, 100)),
        Presences.presence(Moments.now(), Locations.location(200, 100)),
        Distances.distance(100, DistanceUnit.KILOMETER)
    );

    // Act: serialize the track to JSON and parse it back
    var json = Jsons.stringify(track, true);

    var parsed = Jsons.parse(json, Track.class);

    // Assert: the round-tripped track equals the original
    assertThat(parsed)
        .isEqualTo(track);

  }

  @Test
  @DisplayName("should round-trip a disjoint interval set, preserving its intervals")
  void intervalSet_Clone_success() {
    // Arrange: a disjoint set of three intervals
    var set = IntervalSets.disjoint(
        Stream.of(
            interval(at(0), at(2)),
            interval(at(5), at(10)),
            interval(at(11), at(15))
        )
    );

    // Act: serialize the interval set to JSON and parse it back
    var json = Jsons.stringify(set, true);

    var parsed = Jsons.parse(json, IntervalSet.class);

    // Assert: the round-tripped set contains the same intervals
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
