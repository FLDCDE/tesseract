package com.fieldcode.tesseract.query;

import java.time.Duration;

import com.fieldcode.tesseract.Interval;
import com.fieldcode.tesseract.IntervalCollection;
import com.fieldcode.tesseract.IntervalDurationQuery;
import com.fieldcode.tesseract.IntervalQuery;

public class IntervalQueries {

  private IntervalQueries() {
  }

  /**
   * Creates a new instance of {@link IntervalQuery} with the given {@link Interval} as window and the given {@link IntervalDurationQuery}s as definitions.
   *
   * @param window the window of the query
   * @param definitions the definitions of the query
   * @return the created {@link IntervalQuery}
   */
  public static IntervalQuery query(Interval window, IntervalDurationQuery... definitions) {
    return query(definitions).window(window);
  }

  /**
   * Creates a new instance of {@link IntervalQuery} with the given {@link IntervalDurationQuery}s as definitions.
   *
   * @param definitions the definitions of the query
   * @return the created {@link IntervalQuery}
   */
  public static IntervalQuery query(IntervalDurationQuery... definitions) {
    return ImmutableIntervalQuery
        .builder()
        .addIntervalDefinitions(definitions)
        .build();
  }

  /**
   * Creates a new instance of {@link IntervalDurationQuery} with the given {@link Duration} as duration.
   *
   * @param duration the duration of the query
   * @return the created {@link IntervalDurationQuery}
   */
  public static IntervalDurationQuery schedule(Duration duration, IntervalCollection intervals) {
    return ImmutableIntervalDurationQuery
        .builder()
        .duration(duration)
        .intervals(intervals)
        .build();
  }

  /**
   * Creates a new instance of {@link IntervalQuery} with the given {@link Interval} as window.
   *
   * @param intervalQuery the query to copy
   * @param window the window of the query
   * @return the created {@link IntervalQuery}
   */
  public static IntervalQuery window(IntervalQuery intervalQuery, Interval window) {
    return ImmutableIntervalQuery.builder()
        .from(intervalQuery)
        .window(window)
        .build();
  }

}
