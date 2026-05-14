package com.fieldcode.tesseract.track;


import com.fieldcode.tesseract.DirectionFeature;
import com.fieldcode.tesseract.Distance;
import com.fieldcode.tesseract.Location;
import com.fieldcode.tesseract.Moment;
import com.fieldcode.tesseract.Presence;
import com.fieldcode.tesseract.Track;
import com.fieldcode.tesseract.presence.Presences;

import java.time.Duration;
import java.time.OffsetDateTime;

/**
 * Utility class for creating {@link Track} instances through various factory methods.
 * <p>
 * This class provides several convenient ways to create tracks between locations with different combinations of parameters such as direction features, distances, durations, and
 * start times.
 * <p>
 * All factory methods follow a consistent pattern of creating origin and destination {@link Presence} objects and combining them with distance information to form complete
 * tracks.
 * <p>
 * Note that this class does not validate whether the provided parameters are physically appropriate or realistic. It is the caller's responsibility to ensure that distances,
 * durations, and other parameters are consistent with physical constraints.
 */
public final class Tracks {

  private Tracks() {
  }

  /**
   * Creates a track from a direction feature with the given start time.
   * <p>
   * The origin presence begins at the start time. The destination presence begins at the start time plus the duration from the direction feature.
   *
   * @param feature the direction feature containing origin, destination, distance and duration
   * @param start the start time of the track
   * @return a new {@link Track} instance
   */
  public static Track track(DirectionFeature feature, OffsetDateTime start) {
    var originLocation = feature.getOrigin();
    var destinationLocation = feature.getDestination();

    var originPresence = Presences.presence(start, originLocation);
    var destinationPresence = Presences.presence(start, destinationLocation).shift(feature.getDuration());
    return track(originPresence, destinationPresence, feature.getDistance());
  }

  /**
   * Creates a track from the given origin presence, destination presence, and distance.
   *
   * @param origin the origin presence
   * @param destination the destination presence
   * @param distance the distance between the origin and destination
   * @return a new {@link Track} instance
   */
  public static Track track(Presence origin, Presence destination, Distance distance) {
    return ImmutableTrack.of(origin, destination, distance);
  }

  /**
   * Creates a track between the given origin and destination locations using a direction feature with the specified start time.
   * <p>
   * The origin presence begins at the start time. The destination presence begins at the start time plus the duration from the direction feature.
   *
   * @param origin the origin location
   * @param destination the destination location
   * @param feature the direction feature containing distance and duration
   * @param start the start time of the track
   * @return a new {@link Track} instance
   */
  public static Track track(Location origin, Location destination, DirectionFeature feature, OffsetDateTime start) {
    var originPresence = Presences.presence(start, origin);
    var duration = feature.getDuration();
    var destinationPresence = Presences.presence(start, destination).shift(duration);
    return ImmutableTrack.of(originPresence, destinationPresence, feature.getDistance());
  }

  /**
   * Creates a track between the given origin and destination locations with the specified distance, duration, and moment-based start time.
   * <p>
   * The origin presence begins at the start time. The destination presence begins at the start time plus the given duration.
   *
   * @param origin the origin location
   * @param destination the destination location
   * @param distance the distance between the origin and destination
   * @param duration the travel duration between the origin and destination
   * @param start the start moment of the track
   * @return a new {@link Track} instance
   */
  public static Track track(Location origin, Location destination, Distance distance, Duration duration, Moment start) {
    return ImmutableTrack.of(
        Presences.presence(start, origin),
        Presences.presence(start, destination).shift(duration),
        distance
    );
  }

  /**
   * Creates a track between the given origin and destination locations with the specified distance, duration, and timestamp-based start time.
   * <p>
   * The origin presence begins at the start time. The destination presence begins at the start time plus the given duration.
   *
   * @param origin the origin location
   * @param destination the destination location
   * @param distance the distance between the origin and destination
   * @param duration the travel duration between the origin and destination
   * @param start the start timestamp of the track
   * @return a new {@link Track} instance
   */
  public static Track track(Location origin, Location destination, Distance distance, Duration duration, OffsetDateTime start) {
    return ImmutableTrack.of(
        Presences.presence(start, origin),
        Presences.presence(start, destination).shift(duration),
        distance
    );
  }

}

