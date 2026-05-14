package com.fieldcode.tesseract;

import java.util.List;
import java.util.stream.Stream;

public interface IntervalQueryResults {

  /**
   * Returns the results of the query.
   *
   * @return the results of the query
   */
  List<IntervalQueryResult> getResults();

  /**
   * Returns true if the query was successful. A query is successful if all of its intervals were found.
   *
   * @return true if the query was successful
   */
  boolean isSuccess();

  /**
   * Returns true if the query failed. A query fails if at least one of its intervals was not found.
   *
   * @return true if the query failed
   */
  boolean isFailed();

  /**
   * Returns the number of intervals in the query.
   *
   * @return the number of intervals in the query
   */
  Stream<Interval> getIntervals();

  /**
   * Returns a specific interval from the query.
   *
   * @param index the index of the interval to return
   * @return the interval at the given index
   */
  Interval getInterval(int index);

}
