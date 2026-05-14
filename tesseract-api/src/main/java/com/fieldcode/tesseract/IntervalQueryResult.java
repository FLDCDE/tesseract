package com.fieldcode.tesseract;

public interface IntervalQueryResult {

  /**
   * The query that was used to generate this result.
   *
   * @return the query
   */
  IntervalDurationQuery getQuery();

  /**
   * The interval that was found.
   *
   * @return the interval
   * @throws IllegalStateException if the query was not successful
   */
  Interval getInterval();

  /**
   * Whether the query was successful.
   *
   * @return true if the query was successful, false otherwise
   */
  boolean isSuccess();

}
