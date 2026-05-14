package com.fieldcode.tesseract.collection;

import java.time.Duration;
import java.util.Optional;

import com.fieldcode.tesseract.Interval;
import com.fieldcode.tesseract.IntervalCollection;
import com.fieldcode.tesseract.QueryDirection;
import com.fieldcode.tesseract.interval.Intervals;

import static com.fieldcode.tesseract.IntervalAlignment.ALIGN_ENDS;
import static com.fieldcode.tesseract.IntervalAlignment.ALIGN_STARTS;

public class IntervalCollectionFinder {

  private IntervalCollectionFinder() {
  }

  /**
   * Searches for the first interval within the provided collection that completely contains the specified duration. The identified interval is aligned either to the beginning or
   * the end of the interval, based on the given search direction. The discovered interval's duration is assured to be at least as long as the specified duration. If duration is
   * zero, the empty interval is returned.
   *
   * @param collection the collection of intervals to search through
   * @param window the range within which to look for the interval
   * @param direction the search direction, determining the alignment of the interval (start or end)
   * @param duration the minimum duration that the found interval should enclose
   * @return an Optional containing the first interval that encloses the specified duration, or empty if none found
   */
  public static Optional<Interval> find(IntervalCollection collection, Interval window, QueryDirection direction, Duration duration) {
    if (duration.isZero()) {
      return Optional.of(Intervals.empty());
    }

    var alignment = direction.select(ALIGN_STARTS, ALIGN_ENDS);

    return collection
        .intervals(window, direction)
        .filter(interval -> interval.encloses(duration))
        .map(interval -> interval.tryWithDuration(duration, alignment))
        .filter(Optional::isPresent)
        .map(Optional::get)
        .findFirst();
  }

}
