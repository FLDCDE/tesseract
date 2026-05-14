package com.fieldcode.tesseract;

import java.time.Duration;

public interface IntervalDurationQuery {

  /**
   * Retrieves the duration of the interval being queried.
   *
   * @return the duration of the interval
   */
  Duration getDuration();

  /**
   * Specifies the intervals (availabilities) within which the interval is scheduled.
   *
   * @return the intervals where the interval is scheduled
   */
  IntervalCollection getIntervals();

  /**
   * Specifies the window within which the interval is scheduled. Default value is Intervals.always().
   *
   * @return the window where the interval is scheduled
   */
  Interval getWindow();

  /**
   * Determines if the queried interval is locked. If locked, the query executor attempts to schedule the interval within the window. If the locked flag is true, the provided
   * window duration must equal the query duration; otherwise, an IllegalArgumentException is thrown. Default value is false.
   *
   * @return true if the interval is locked
   */
  boolean isLocked();

  /**
   * Indicates whether the result interval of this duration query must be a center interval. All other intervals are aligned to the left or right. Default value is false.
   *
   * @return true if the interval is a center interval
   */
  boolean isCenter();

  /**
   * Creates a new instance with the center flag set to true using the copy method.
   *
   * @return a new instance with center flag set to true
   */
  IntervalDurationQuery center();

  /**
   * Creates a new instance with the center flag set to the given value using the copy method.
   *
   * @param center the value of the center flag
   * @return a new instance with center flag set to the provided value
   */
  IntervalDurationQuery center(boolean center);

  /**
   * Creates a new instance with the specified window using the copy method.
   *
   * @param window the window to query
   * @return a new instance with the provided window
   */
  IntervalDurationQuery window(Interval window);

  /**
   * Creates a new instance with the locked flag set to true using the copy method.
   *
   * @return a new instance with the locked flag set to true
   */
  IntervalDurationQuery locked();

  /**
   * Creates a new instance with the locked flag set to the given value using the copy method.
   *
   * @param locked the value of the locked flag
   * @return a new instance with the locked flag set to the provided value
   */
  IntervalDurationQuery locked(boolean locked);

}
