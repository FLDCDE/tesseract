package com.fieldcode.tesseract;

/**
 * A movement is a pair of presences, one origin and one destination. It represents the movement from the origin to the destination within a given time interval.
 */
public interface Movement {

  /**
   * The origin of the movement.
   *
   * @return the origin of the movement
   */
  Presence getOrigin();

  /**
   * The destination of the movement.
   *
   * @return the destination of the movement
   */
  Presence getDestination();

  /**
   * The time interval during which the movement occurred.
   *
   * @return the time interval during which the movement occurred
   */
  Interval getInterval();

  /**
   * Returns a new movement with the given lower moment for origin presence.
   *
   * @param lower the lower moment for origin presence
   * @return a new movement with the given lower bound
   */
  Movement withLower(Moment lower);

  /**
   * Returns a new movement with the given upper moment for destination presence.
   *
   * @param upper the upper moment for destination presence
   * @return a new movement with the given upper bound
   */
  Movement withUpper(Moment upper);

}
