package com.fieldcode.tesseract;

import java.util.List;

/**
 * A high level data structure for querying (scheduling) {@link Interval}s. It consists of a window and a list of {@link IntervalDurationQuery}s. Using this query it is possible to
 * find one or more intervals in an {Link IntervalCollection} that match the query.
 */
public interface IntervalQuery {

  /**
   * The global window of the query.
   *
   * @return the window
   */
  Interval getWindow();

  /**
   * Duration definitions of the query.
   *
   * @return the definitions
   */
  List<IntervalDurationQuery> getIntervalDefinitions();

  /**
   * Creates a new instance of {@link IntervalQuery} with the given {@link Interval} as window.
   *
   * @param window the window of the query
   * @return the created {@link IntervalQuery}
   */
  IntervalQuery window(Interval window);

}
