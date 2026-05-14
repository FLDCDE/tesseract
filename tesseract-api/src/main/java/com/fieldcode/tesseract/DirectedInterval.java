package com.fieldcode.tesseract;

/**
 * A directed interval represents an interval with a direction in time. Mostly used for querying intervals in a specific direction.
 */
public interface DirectedInterval {

  /**
   * Returns the interval of this directed interval.
   *
   * @return the interval of this directed interval
   */
  Interval getInterval();

  /**
   * Returns the direction of this directed interval.
   *
   * @return the direction of this directed interval
   */
  QueryDirection getDirection();

}
