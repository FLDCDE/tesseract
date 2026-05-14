package com.fieldcode.tesseract.track;

import java.time.Duration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.fieldcode.tesseract.Distance;
import com.fieldcode.tesseract.Location;
import com.fieldcode.tesseract.Moment;
import com.fieldcode.tesseract.Track;
import com.fieldcode.tesseract.distance.Distances;
import com.fieldcode.tesseract.interval.Intervals;
import com.fieldcode.tesseract.location.Locations;
import com.fieldcode.tesseract.moment.Moments;
import com.fieldcode.tesseract.presence.Presences;

import static org.assertj.core.api.Assertions.assertThat;


public class TrackTest {


  private static final Duration ONE_MINUTE = Duration.ofMinutes(1);
  private final Distance distance = Distances.oneKilometer();
  private final Location origin = Locations.location(10, 20);
  private final Location destination = Locations.location(50, 60);
  private final Moment trackTime = Moments.now();
  private Track track;

  @BeforeEach
  public void setUp() {
    track = Tracks.track(origin, destination, distance, Duration.ofSeconds(60), trackTime);
  }


  @Test
  void track_getOrigin_Success() {
    var expected = Presences.presence(trackTime, Locations.location(10, 20));

    assertThat(track.getOrigin())
        .isEqualTo(expected);
  }

  @Test
  void track_getDestination_Success() {
    var expected = Presences.presence(trackTime.shift(ONE_MINUTE), Locations.location(50.0, 60.0));

    assertThat(track.getDestination())
        .isEqualTo(expected);
  }


  @Test
  void track_getStart_Success() {
    assertThat(track.getStart()).isEqualTo(
        trackTime
    );
  }

  @Test
  void track_getEnd_Success() {
    assertThat(track.getEnd()).isEqualTo(
        trackTime.shift(ONE_MINUTE)
    );
  }

  @Test
  void track_getDistance_Success() {
    assertThat(track.getDistance()).isEqualTo(
        Distances.oneKilometer()
    );
  }


  @Test
  void track_getDuration_Success() {
    assertThat(track.getDuration()).isEqualTo(
        Duration.ofSeconds(60)
    );
  }


  @Test
  void track_getInterval_Success() {
    assertThat(track.getInterval()).isEqualTo(
        Intervals.interval(trackTime, trackTime.shift(ONE_MINUTE))
    );
  }


  @Test
  void track_getLocationBounds_Success() {
    assertThat(track.getLocationBounds()).containsExactly(
        Presences.presence(trackTime, Locations.location(10.0, 20.0)),
        Presences.presence(trackTime.shift(ONE_MINUTE), Locations.location(50.0, 60.0))
    );
  }

  @Test
  void track_isConnected_True_Success() {
    var trackOther = Tracks.track(
        Locations.location(100, 110),
        Locations.location(200, 220),
        Distances.ofKilometers(10),
        Duration.ofHours(2),
        Moments.now()
    );
    assertThat(track.isConnected(trackOther)).isTrue();
  }

  @Test
  void track_isConnected_False_Success() {
    var trackOther = Tracks.track(
        Locations.location(100, 110),
        Locations.location(200, 220),
        Distances.ofKilometers(10),
        Duration.ofHours(2),
        Moments.now().shift(Duration.ofHours(1))
    );
    assertThat(track.isConnected(trackOther)).isFalse();
  }

}
