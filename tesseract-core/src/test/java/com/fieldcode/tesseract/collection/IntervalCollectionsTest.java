package com.fieldcode.tesseract.collection;

import org.junit.jupiter.api.Test;

import com.fieldcode.tesseract.interval.IntervalSets;
import com.fieldcode.tesseract.interval.IntervalTestSupport;
import com.fieldcode.tesseract.interval.Intervals;

import java.time.Duration;
import static org.assertj.core.api.Assertions.assertThat;

class IntervalCollectionsTest extends IntervalTestSupport {

  public static final Duration DAY = Duration.ofDays(1);

  @Test
  void exclude_IntervalCollection_Success() {
    var set1 = IntervalSets.disjoint(
        interval(8, 19),                // normal working hours
        interval(8, 19).shift(DAY)      // normal working hours the next day
    );

    var set2 = IntervalSets.disjoint(
        interval(12, 13),   // lunch
        interval(15, 24)    // early finish because of a personal appointment
    );

    var collection = IntervalCollections.exclude(set1, set2);

    assertThat(collection.intervals())
        .toIterable()
        .containsExactly(
            interval(8, 12),
            interval(13, 15),
            interval(8, 19).shift(DAY)
        );
  }

  @Test
  void span_EmptyCollection_ReturnsEmpty() {
    var emptyCollection = IntervalSets.disjoint();

    var span = IntervalCollections.span(emptyCollection);

    assertThat(span).isEmpty();
  }

  @Test
  void span_SingleInterval_ReturnsSameInterval() {
    var collection = IntervalSets.disjoint(interval(8, 17));

    var span = IntervalCollections.span(collection);

    assertThat(span)
        .isPresent()
        .contains(interval(8, 17));
  }

  @Test
  void span_MultipleAdjacentIntervals_ReturnsSpanningInterval() {
    // Adjacent intervals [8..12) and [12..17) should span [8..17)
    var collection = IntervalSets.disjoint(
        interval(8, 12),
        interval(12, 17)
    );

    var span = IntervalCollections.span(collection);

    // Adjacent intervals get merged by IntervalSet, so the span is the merged interval
    assertThat(span)
        .isPresent()
        .contains(interval(8, 17));
  }

  @Test
  void span_MultipleDisjointIntervals_ReturnsSpanningInterval() {
    // Disjoint intervals [8..12), [14..17), [20..22) should span [8..22)
    var collection = IntervalSets.disjoint(
        interval(8, 12),
        interval(14, 17),
        interval(20, 22)
    );

    var span = IntervalCollections.span(collection);

    assertThat(span)
        .isPresent()
        .contains(interval(8, 22));
  }

  @Test
  void span_OverlappingIntervals_ReturnsSpanningInterval() {
    // Overlapping intervals [8..14) and [12..17) should span [8..17)
    var collection = IntervalSets.disjoint(
        interval(8, 14),
        interval(12, 17)
    );

    var span = IntervalCollections.span(collection);

    // Overlapping intervals get merged by IntervalSet
    assertThat(span)
        .isPresent()
        .contains(interval(8, 17));
  }

  @Test
  void span_UnboundedIntervalFrom_ReturnsIntervalFrom() {
    var collection = IntervalSets.disjoint(
        interval(8, 12),
        Intervals.intervalFrom(at(20))
    );

    var span = IntervalCollections.span(collection);

    assertThat(span)
        .isPresent()
        .get()
        .satisfies(s -> {
          assertThat(s.getLower()).isEqualTo(moment(8));
          assertThat(s.getUpper()).isEqualTo(inf());
        });
  }

  @Test
  void span_UnboundedIntervalTo_ReturnsIntervalTo() {
    var collection = IntervalSets.disjoint(
        Intervals.intervalTo(at(10)),
        interval(14, 17)
    );

    var span = IntervalCollections.span(collection);

    assertThat(span)
        .isPresent()
        .get()
        .satisfies(s -> {
          assertThat(s.getLower()).isEqualTo(ninf());
          assertThat(s.getUpper()).isEqualTo(moment(17));
        });
  }

  @Test
  void span_AlwaysInterval_ReturnsAlways() {
    var collection = IntervalSets.disjoint(Intervals.always());

    var span = IntervalCollections.span(collection);

    assertThat(span)
        .isPresent()
        .get()
        .satisfies(s -> {
          assertThat(s.getLower()).isEqualTo(ninf());
          assertThat(s.getUpper()).isEqualTo(inf());
          assertThat(s.isAlways()).isTrue();
        });
  }

  @Test
  void span_MixedBoundedAndUnbounded_ReturnsCorrectSpan() {
    var collection = IntervalSets.disjoint(
        Intervals.intervalTo(at(5)),
        interval(10, 15),
        Intervals.intervalFrom(at(20))
    );

    var span = IntervalCollections.span(collection);

    assertThat(span)
        .isPresent()
        .get()
        .satisfies(s -> {
          assertThat(s.getLower()).isEqualTo(ninf());
          assertThat(s.getUpper()).isEqualTo(inf());
          assertThat(s.isAlways()).isTrue();
        });
  }

  @Test
  void encloses_EmptyCollection_ReturnsFalse() {
    var emptyCollection = IntervalSets.disjoint();
    var queryInterval = interval(8, 17);

    var result = IntervalCollections.encloses(emptyCollection, queryInterval);

    assertThat(result).isFalse();
  }

  @Test
  void encloses_ExactMatch_ReturnsTrue() {
    // Collection contains interval [8..17), query for [8..17)
    var collection = IntervalSets.disjoint(interval(8, 17));
    var queryInterval = interval(8, 17);

    var result = IntervalCollections.encloses(collection, queryInterval);

    assertThat(result).isTrue();
  }

  @Test
  void encloses_ExactMatchInMultipleIntervals_ReturnsTrue() {
    // Collection contains [8..12), [14..17), query for [14..17)
    var collection = IntervalSets.disjoint(
        interval(8, 12),
        interval(14, 17),
        interval(20, 22)
    );
    var queryInterval = interval(14, 17);

    var result = IntervalCollections.encloses(collection, queryInterval);

    assertThat(result).isTrue();
  }

  @Test
  void encloses_SubintervalContained_ReturnsTrue() {
    // Collection contains [8..17), query for [10..15) (subinterval)
    // The query window [10..15) is fully contained, so cropped result equals query
    var collection = IntervalSets.disjoint(interval(8, 17));
    var queryInterval = interval(10, 15);

    var result = IntervalCollections.encloses(collection, queryInterval);

    // Collection fully contains/encloses the query interval
    assertThat(result).isTrue();
  }

  @Test
  void encloses_SuperintervalContains_ReturnsFalse() {
    // Collection contains [10..15), query for [8..17) (larger interval)
    // The collection doesn't fully cover [8..17), so it returns false
    var collection = IntervalSets.disjoint(interval(10, 15));
    var queryInterval = interval(8, 17);

    var result = IntervalCollections.encloses(collection, queryInterval);

    assertThat(result).isFalse();
  }

  @Test
  void encloses_PartialOverlap_ReturnsFalse() {
    // Collection contains [8..14), query for [12..17) (overlaps but not equal)
    var collection = IntervalSets.disjoint(interval(8, 14));
    var queryInterval = interval(12, 17);

    var result = IntervalCollections.encloses(collection, queryInterval);

    assertThat(result).isFalse();
  }

  @Test
  void encloses_DisjointIntervals_ReturnsFalse() {
    // Collection contains [8..12), query for [14..17) (no overlap)
    var collection = IntervalSets.disjoint(interval(8, 12));
    var queryInterval = interval(14, 17);

    var result = IntervalCollections.encloses(collection, queryInterval);

    assertThat(result).isFalse();
  }

  @Test
  void encloses_AdjacentIntervalsMerged_ReturnsTrue() {
    // Collection contains [8..12) and [12..17) which merge to [8..17), query for [8..17)
    var collection = IntervalSets.disjoint(
        interval(8, 12),
        interval(12, 17)
    );
    var queryInterval = interval(8, 17);

    var result = IntervalCollections.encloses(collection, queryInterval);

    // Adjacent intervals get merged by IntervalSet
    assertThat(result).isTrue();
  }

  @Test
  void encloses_UnboundedIntervalFrom_ExactMatch_ReturnsTrue() {
    var collection = IntervalSets.disjoint(Intervals.intervalFrom(at(10)));
    var queryInterval = Intervals.intervalFrom(at(10));

    var result = IntervalCollections.encloses(collection, queryInterval);

    assertThat(result).isTrue();
  }

  @Test
  void encloses_UnboundedIntervalTo_ExactMatch_ReturnsTrue() {
    var collection = IntervalSets.disjoint(Intervals.intervalTo(at(10)));
    var queryInterval = Intervals.intervalTo(at(10));

    var result = IntervalCollections.encloses(collection, queryInterval);

    assertThat(result).isTrue();
  }

  @Test
  void encloses_AlwaysInterval_ExactMatch_ReturnsTrue() {
    var collection = IntervalSets.disjoint(Intervals.always());
    var queryInterval = Intervals.always();

    var result = IntervalCollections.encloses(collection, queryInterval);

    assertThat(result).isTrue();
  }

  @Test
  void encloses_BoundedQueryInUnboundedCollection_ReturnsTrue() {
    // Collection is unbounded [10..∞), query is bounded [10..20)
    // The unbounded interval fully covers the bounded query
    var collection = IntervalSets.disjoint(Intervals.intervalFrom(at(10)));
    var queryInterval = interval(10, 20);

    var result = IntervalCollections.encloses(collection, queryInterval);

    // Unbounded interval [10..∞) fully contains [10..20)
    assertThat(result).isTrue();
  }

}
