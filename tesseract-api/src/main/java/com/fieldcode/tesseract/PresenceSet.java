package com.fieldcode.tesseract;

import java.time.OffsetDateTime;

public interface PresenceSet extends PresenceCollection {

  /**
   * Puts the given moment and location into this presence set. If the moment already exists in this presence set, the location will be overwritten.
   *
   * @param moment the moment
   * @param location the location
   * @return this presence set
   */
  PresenceSet put(Moment moment, Location location);

  /**
   * Puts the given moment and location into this presence set. If the moment already exists in this presence set, the location will be overwritten.
   *
   * @param moment the moment
   * @param location the location
   * @return this presence set
   */
  PresenceSet put(OffsetDateTime moment, Location location);

  /**
   * Puts the given presence into this presence set. If the moment already exists in this presence set, the location will be overwritten.
   *
   * @param presence the presence
   * @return this presence set
   */
  PresenceSet put(Presence presence);

  /**
   * Removes the presence with the given moment from this presence set.
   *
   * @param moment the moment
   * @return this presence set
   */
  PresenceSet remove(Moment moment);

  /**
   * Removes all presences with moments between the given lower and upper bounds from this presence set.
   *
   * @param lower the lower bound
   * @param lowerInclusive whether the lower bound is inclusive
   * @param upper the upper bound
   * @param upperInclusive whether the upper bound is inclusive
   * @return this presence set
   */
  PresenceSet remove(Moment lower, boolean lowerInclusive, Moment upper, boolean upperInclusive);

}
