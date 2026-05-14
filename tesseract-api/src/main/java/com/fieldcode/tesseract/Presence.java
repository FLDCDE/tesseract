package com.fieldcode.tesseract;

import java.time.Duration;

public interface Presence extends Comparable<Presence>, Taggable {

  /**
   * Returns the moment of this presence.
   *
   * @return the moment of this presence
   */
  Moment getMoment();

  /**
   * Returns the location of this presence.
   *
   * @return the location of this presence
   */
  Location getLocation();

  /**
   * Returns a new presence with the same location but with the moment shifted by the given duration.
   *
   * @param duration the duration to shift the moment by
   * @return a new presence with the same location but with the moment shifted by the given duration
   */
  Presence shift(Duration duration);

  /**
   * Returns a new presence with the same location but with the moment moved to the given moment.
   *
   * @param moment the moment to move the presence to
   * @return a new presence with the same location but with the moment moved to the given moment
   */
  Presence move(Moment moment);

  /**
   * Returns a new presence instance with the given moment.
   *
   * @param moment the moment to set
   * @return a new presence with the given moment
   */
  Presence withMoment(Moment moment);

  /**
   * Returns a new presence instance with the given location.
   *
   * @param location the location to set
   * @return a new presence with the given location
   */
  Presence withLocation(Location location);

  Presence withTags(TagHolder tags);

  Presence withTags(Tag... tags);

  /**
   * Returns a new presence instance with the merged tags.
   *
   * @param tags the tags to set
   * @return a new presence with the given tags
   */
  Presence mergeTags(Tag... tags);

  /**
   * Default implementation of the {@link Comparable#compareTo(Object)} method. It compares the moment of this presence with the moment of the other presence.
   *
   * @param other the object to be compared.
   * @return a negative integer, zero, or a positive integer as this object is less than, equal to, or greater than the specified object.
   */
  @Override
  default int compareTo(Presence other) {
    return getMoment().compareTo(other.getMoment());
  }

}
