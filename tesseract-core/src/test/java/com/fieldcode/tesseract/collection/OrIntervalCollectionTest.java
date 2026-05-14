package com.fieldcode.tesseract.collection;

import org.junit.jupiter.api.Test;

import com.fieldcode.tesseract.QueryDirection;
import com.fieldcode.tesseract.interval.IntervalSets;
import com.fieldcode.tesseract.interval.IntervalTestSupport;

import static org.assertj.core.api.Assertions.assertThat;

class OrIntervalCollectionTest extends IntervalTestSupport {


  @Test
  void intervalsCollectionOr_All_Success() {

    var set1 = IntervalSets.disjoint(
        interval(0, 1),
        interval(4, 5)
    );

    var set2 = IntervalSets.disjoint(
        interval(0, 2),
        interval(5, 6)
    );

    var collection = IntervalCollections.or(set1, set2);

    assertThat(collection.intervals())
        .toIterable()
        .containsExactly(
            interval(0, 2),
            interval(4, 6)
        );
  }

  @Test
  void intervalsCollectionOr_Window_Forward_Success() {

    var set1 = IntervalSets.disjoint(
        interval(0, 1),
        interval(4, 5)
    );
    var set2 = IntervalSets.disjoint(
        interval(0, 2),
        interval(5, 6)
    );

    var collection = IntervalCollections.or(set1, set2);

    assertThat(collection.intervals(interval(3, 6), QueryDirection.FORWARD))
        .toIterable()
        .containsExactly(
            interval(4, 6)
        );

  }

  @Test
  void intervalsCollectionOr_Window_Backward_Success() {

    var set1 = IntervalSets.disjoint(
        interval(0, 1),
        interval(4, 5)
    );
    var set2 = IntervalSets.disjoint(
        interval(0, 2),
        interval(5, 6)
    );

    var collection = IntervalCollections.or(set1, set2);

    assertThat(collection.intervals(interval(3, 6), QueryDirection.BACKWARD))
        .toIterable()
        .containsExactly(
            interval(4, 6)
        );

  }

  @Test
  void intervalsCollectionOr_withEmptySet_containsOthers_Success() {
    var set1 = IntervalSets.disjoint(
        interval(0, 5),
        interval(10, 15),
        interval(20, 25)
    );
    var set2 = IntervalSets.disjoint(
        interval(1, 4),
        interval(11, 14),
        interval(21, 24)
    );

    var set3 = IntervalSets.disjoint();

    var collection = IntervalCollections.or(set1, set2, set3);

    assertThat(collection.intervals())
        .toIterable()
        .containsExactly(
            interval(0, 5),
            interval(10, 15),
            interval(20, 25)
        );

  }

}
