package com.fieldcode.tesseract.duration;

import java.time.Duration;

import com.fieldcode.tesseract.FluentIterator;
import com.fieldcode.tesseract.Interval;
import com.fieldcode.tesseract.IntervalCollection;

import static com.fieldcode.tesseract.QueryDirection.FORWARD;
import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkState;

public class Durations {

  private Durations() {}

  /**
   * Returns the total duration of all bounded intervals in the given iterator. Unbounded intervals are not allowed.
   *
   * @param intervals the intervals to sum
   * @return the total duration of all bounded intervals
   * @throws IllegalStateException if an unbounded interval is encountered
   */
  public static Duration total(FluentIterator<Interval> intervals) {
    var total = Duration.ZERO;
    while (intervals.hasNext()) {
      var interval = intervals.next();
      checkState(interval.isBounded(), "Unbounded interval: %s", interval);
      total = total.plus(interval.getDuration().orElseThrow());
    }
    return total;
  }

  /**
   * Returns the ratio of the collection durations in the given window. The ratio is calculated as the total duration of collection A divided by the total duration of collection B.
   * The ratio is zero if the total duration of collection A is zero. Throw an exception if the total duration of collection B is zero.
   *
   * @param a the first collection
   * @param b the second collection
   * @param window the window to query
   * @return the ratio of the collection durations
   */
  public static double ratio(IntervalCollection a, IntervalCollection b, Interval window) {
    var totalA = total(a.intervals(window, FORWARD));

    if (totalA.isZero()) {
      return 0;
    }

    var totalB = total(b.intervals(window, FORWARD));

    checkArgument(!totalB.isZero(), "Total duration of collection B is zero");
    return totalA.toMillis() / (double) totalB.toMillis();
  }

}
