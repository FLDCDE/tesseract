package com.fieldcode.tesseract.interval;

import java.util.function.BiPredicate;
import java.util.function.Function;

import com.fieldcode.tesseract.DirectedInterval;
import com.fieldcode.tesseract.FluentIterator;
import com.fieldcode.tesseract.Interval;
import com.fieldcode.tesseract.IntervalMap;
import com.fieldcode.tesseract.IntervalMap.IntervalEntry;
import com.fieldcode.tesseract.Moment;
import com.fieldcode.tesseract.Signal;
import com.fieldcode.tesseract.collection.BoundaryLimiterIterator;
import com.fieldcode.tesseract.collection.BoundaryMergeIterator;
import com.fieldcode.tesseract.collection.IntervalToBoundaryIterator;

class IntervalMapToIntervalCollectionFactory {

  private IntervalMapToIntervalCollectionFactory() {}

  static <T> IntervalMapCollection create(BiPredicate<Interval, T> intervalPredicate, IntervalMap<T> map) {
    var mapper = getMapper(intervalPredicate, map);
    return IntervalMapCollection.of(mapper);
  }

  private static <T> Function<DirectedInterval, FluentIterator<Signal<Moment, Boolean>>> getMapper(BiPredicate<Interval, T> predicate, IntervalMap<T> map) {
    return directed -> {
      var interval = directed.getInterval();
      var direction = directed.getDirection();

      var filteredIntervals = map.entries(interval, direction)
          .filter(entry -> predicate.test(entry.getInterval(), entry.getValue()))
          .map(IntervalEntry::getInterval);

      var boundaries = IntervalToBoundaryIterator.of(filteredIntervals, direction);
      var merged = BoundaryMergeIterator.of(boundaries, direction);

      return BoundaryLimiterIterator.fluent(merged, directed);
    };
  }
}
