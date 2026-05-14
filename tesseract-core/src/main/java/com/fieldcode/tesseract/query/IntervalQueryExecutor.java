package com.fieldcode.tesseract.query;


import java.util.ArrayList;
import java.util.List;

import com.fieldcode.tesseract.Interval;
import com.fieldcode.tesseract.IntervalDurationQuery;
import com.fieldcode.tesseract.IntervalQuery;
import com.fieldcode.tesseract.IntervalQueryResult;
import com.fieldcode.tesseract.IntervalQueryResults;
import com.fieldcode.tesseract.Moment;
import com.fieldcode.tesseract.collection.IntervalCollections;
import com.fieldcode.tesseract.interval.Intervals;

import static com.fieldcode.tesseract.QueryDirection.BACKWARD;
import static com.fieldcode.tesseract.QueryDirection.FORWARD;

public class IntervalQueryExecutor {

  private final Moment start;
  private final IntervalQuery query;
  private final List<IntervalQueryResult> results = new ArrayList<>();
  private final Interval globalWindow;
  private boolean failed;
  private boolean noCenter = true;

  private IntervalQueryExecutor(IntervalQuery query) {
    this.query = query;
    this.start = query.getWindow().getLower();
    this.globalWindow = query.getWindow();
  }

  public static IntervalQueryResults execute(IntervalQuery query) {
    return ImmutableIntervalQueryResults.of(new IntervalQueryExecutor(query).execute());
  }

  public List<IntervalQueryResult> execute() {
    executeAll();
    arrangeToCenter();
    return results;
  }

  private void executeAll() {
    for (IntervalDurationQuery intervalDurationQuery : query.getIntervalDefinitions()) {
      if (intervalDurationQuery.isCenter()) {
        noCenter = false;
      }
      execute(intervalDurationQuery);
    }
  }

  private void execute(IntervalDurationQuery query) {

    if (failed) {
      failed(query);
      return;
    }

    var lockBreach = query.isLocked() && !getRemaining().encloses(query.getWindow());

    if (lockBreach) {
      failed(query);
      return;
    }

    if (query.isLocked()) {
      locked(query);
    } else {
      normal(query);
    }

  }

  private Interval getRemaining() {
    var pointer = results.isEmpty()
        ? start
        : getLastPointer();

    return pointer.isFinite()
        ? Intervals.intervalFrom(pointer)
        : Intervals.always();
  }

  private Moment getLastPointer() {
    return results
        .stream()
        .map(IntervalQueryResult::getInterval)
        .filter(Interval::isBounded)
        .reduce((a, b) -> b).map(Interval::getUpper)
        .orElse(start);
  }

  private void locked(IntervalDurationQuery query) {
    success(query.getWindow(), query);
  }

  private void normal(IntervalDurationQuery query) {
    var remaining = getRemaining();
    var duration = query.getDuration();

    var searchInterval = IntervalCollections.and(
        globalWindow,
        remaining,
        query.getWindow(),
        query.getIntervals()
    );

    searchInterval.find(Intervals.always(), FORWARD, duration)
        .ifPresentOrElse(
            interval -> success(interval, query),
            () -> failed(query)
        );
  }

  private void arrangeToCenter() {

    if (failed || noCenter) {
      return;
    }

    Moment upperLimit = null;

    for (int i = results.size() - 1; i >= 0; i--) {

      var result = results.get(i);

      if (result.getInterval().isEmpty()) {
        continue;
      }

      var durationQuery = result.getQuery();

      if (durationQuery.isCenter()) {
        upperLimit = result.getInterval().getLower();
        continue;
      }

      if (upperLimit != null) {
        var arranged = arrangeToCenter(result, upperLimit);
        upperLimit = arranged.getInterval().getLower();
        results.set(i, arranged);
      }
    }

  }

  private IntervalQueryResult arrangeToCenter(IntervalQueryResult result, Moment upperLimit) {
    var durationQuery = result.getQuery();

    var window = durationQuery.getWindow();
    var duration = durationQuery.getDuration();
    var collection = durationQuery.getIntervals();

    if (duration.isZero()) {
      return result;
    }

    var arrangementWindow = calculateArrangeWidow(window, upperLimit);

    var arranged = collection.find(
        arrangementWindow,
        BACKWARD,
        duration
    ).orElseThrow();

    if (arranged.equals(result.getInterval())) {
      return result;
    }

    return ImmutableSuccessResult.builder()
        .from(result)
        .interval(arranged)
        .build();
  }

  private Interval calculateArrangeWidow(Interval window, Moment upper) {
    if (upper.ge(window.getUpper())) {
      return window;
    }

    if (window.contains(upper)) {
      return window.withUpper(upper);
    }

    var message = String.format("Arrange window cannot be calculated. [window=%s, upperLimit=%s]", window, upper);
    throw new IllegalStateException(message);
  }

  private void success(Interval interval, IntervalDurationQuery query) {
    results.add(ImmutableSuccessResult.of(query, interval));
  }

  private void failed(IntervalDurationQuery query) {
    failed = true;
    results.add(ImmutableFailedResult.of(query));
  }

}
