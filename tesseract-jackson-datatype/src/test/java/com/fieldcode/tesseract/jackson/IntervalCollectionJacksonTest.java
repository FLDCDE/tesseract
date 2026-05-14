package com.fieldcode.tesseract.jackson;

import java.time.Duration;

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

public class IntervalCollectionJacksonTest extends TimeTestSupport {

  public static final Duration DAY = Duration.ofDays(1);

  @Test
  void clone_intervalCollection_Collection() {
    var availabilities = IntervalSets.disjoint(
        interval(8, 19),                // normal working hours
        interval(8, 19).shift(DAY)      // normal working hours the next day
    );

    var absences = IntervalSets.disjoint(
        interval(12, 13),   // lunch
        interval(15, 24)    // early finish because of a personal appointment
    );

    var effectiveAvailabilities = IntervalCollections.exclude(availabilities, absences);

    var cloned = Jsons.clone(effectiveAvailabilities, IntervalCollection.class);

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
  void clone_SingleInterval_Interval() {
    var availabilities = IntervalSets.disjoint(
        interval(8, 18)
    );

    var cloned = Jsons.clone(availabilities, IntervalCollection.class);

    assertThat(cloned.intervals())
        .toIterable()
        .containsExactly(interval(8, 18));

    assertThat(cloned)
        .isInstanceOf(IntervalCollection.class);
  }

  @Test
  void clone_alwaysSet_Success() {
    var collection = IntervalCollections.always();
    var cloned = Jsons.clone(collection, IntervalCollection.class);

    assertThat(cloned)
        .isInstanceOf(IntervalCollection.class)
        .isEqualTo(collection);
  }

  @Test
  void clone_IntervalAsCollection_Success() {
    var interval = interval(0, 24);

    var cloned = Jsons.clone(interval, IntervalCollection.class);

    assertThat(cloned)
        .isInstanceOf(IntervalCollection.class)
        .isInstanceOf(DisjointIntervalSet.class)
        .isEqualTo(IntervalSets.disjoint(interval))
        .isEqualTo(interval);
  }
}
