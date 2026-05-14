package com.fieldcode.tesseract.interval;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.fieldcode.tesseract.collection.IntervalCollections;
import com.fieldcode.tesseract.test_utils.TimeTestSupport;

import static org.assertj.core.api.Assertions.assertThat;

class IntervalSetsTest extends TimeTestSupport {

  @Test
  void disjoint_OffsetDateTimeMapper_Success() {
    var input = List.of(0, 1, 2);
    var set = IntervalSets.disjoint(input, i -> at(i * 10), i -> at(i * 10 + 2));

    assertThat(set.intervals().stream())
        .containsExactly(
            interval(0, 2),
            interval(10, 12),
            interval(20, 22)
        );
  }

  @Test
  void disjoint_IntervalCollection_Success() {
    var set1 = IntervalSets.disjoint(
        interval(0, 2),
        interval(10, 12),
        interval(20, 22)
    );

    var set2 = IntervalSets.disjoint(
        interval(11, 12),
        interval(21, 22)
    );

    var collection = IntervalCollections.and(set1, set2);

    var set = IntervalSets.disjoint(collection);

    assertThat(set.intervals().stream())
        .containsExactly(
            interval(11, 12),
            interval(21, 22)
        );
  }
}
