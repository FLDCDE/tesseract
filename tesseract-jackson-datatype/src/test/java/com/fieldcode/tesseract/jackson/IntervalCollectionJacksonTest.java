package com.fieldcode.tesseract.jackson;

import java.time.Duration;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.fieldcode.tesseract.Interval;
import com.fieldcode.tesseract.IntervalCollection;
import com.fieldcode.tesseract.collection.IntervalCollections;
import com.fieldcode.tesseract.interval.DisjointIntervalSet;
import com.fieldcode.tesseract.interval.IntervalSets;
import com.fieldcode.tesseract.interval.Intervals;
import com.fieldcode.tesseract.jackson.test_utils.Jsons;
import com.fieldcode.tesseract.jackson.test_utils.TimeTestSupport;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("IntervalCollectionJackson")
public class IntervalCollectionJacksonTest extends TimeTestSupport {

  public static final Duration DAY = Duration.ofDays(1);

  @Test
  @DisplayName("should round-trip an interval collection with absences excluded from availabilities")
  void clone_intervalCollection_Collection() {
    // Arrange: two days of working-hour availabilities with a lunch break and an early finish excluded
    var availabilities = IntervalSets.disjoint(
        interval(8, 19),                // normal working hours
        interval(8, 19).shift(DAY)      // normal working hours the next day
    );

    var absences = IntervalSets.disjoint(
        interval(12, 13),   // lunch
        interval(15, 24)    // early finish because of a personal appointment
    );

    var effectiveAvailabilities = IntervalCollections.exclude(availabilities, absences);

    // Act: serialize and deserialize the resulting collection back into an IntervalCollection
    var cloned = Jsons.clone(effectiveAvailabilities, IntervalCollection.class);

    // Assert: the round-tripped collection preserves the gaps left by the excluded absences
    assertThat(cloned.intervals())
        .toIterable()
        .containsExactly(
            interval(8, 12),
            interval(13, 15),
            interval(8, 19).shift(DAY)
        );

    assertThat(cloned)
        .isInstanceOf(IntervalCollection.class);

  }

  @Test
  @DisplayName("should round-trip an interval collection containing a single interval")
  void clone_SingleInterval_Interval() {
    // Arrange: an interval collection with a single interval
    var availabilities = IntervalSets.disjoint(
        interval(8, 18)
    );

    // Act: serialize and deserialize it back into an IntervalCollection
    var cloned = Jsons.clone(availabilities, IntervalCollection.class);

    // Assert: the round-tripped collection still contains the single interval
    assertThat(cloned.intervals())
        .toIterable()
        .containsExactly(interval(8, 18));

    assertThat(cloned)
        .isInstanceOf(IntervalCollection.class);
  }

  @Test
  @DisplayName("should round-trip the always-available interval collection")
  void clone_alwaysSet_Success() {
    // Arrange: the always-available interval collection
    var collection = IntervalCollections.always();

    // Act: serialize and deserialize it back into an IntervalCollection
    var cloned = Jsons.clone(collection, IntervalCollection.class);

    // Assert: the round-tripped collection is an IntervalCollection equal to the original
    assertThat(cloned)
        .isInstanceOf(IntervalCollection.class)
        .isEqualTo(collection);
  }

  @Test
  @DisplayName("should round-trip a single interval deserialized as an interval collection")
  void clone_IntervalAsCollection_Success() {
    // Arrange: a single interval
    var interval = interval(0, 24);

    // Act: serialize it and deserialize it back as an IntervalCollection
    var cloned = Jsons.clone(interval, IntervalCollection.class);

    // Assert: the round-tripped value is a disjoint interval set equal to both the wrapped and original interval
    assertThat(cloned)
        .isInstanceOf(IntervalCollection.class)
        .isInstanceOf(DisjointIntervalSet.class)
        .isEqualTo(IntervalSets.disjoint(interval))
        .isEqualTo(interval);
  }
}
