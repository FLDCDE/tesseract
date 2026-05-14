package com.fieldcode.tesseract.collection;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.fieldcode.tesseract.IntervalCollection;
import com.fieldcode.tesseract.interval.IntervalSets;
import com.fieldcode.tesseract.interval.IntervalTestSupport;

import static com.fieldcode.tesseract.QueryDirection.BACKWARD;
import static com.fieldcode.tesseract.QueryDirection.FORWARD;
import static org.assertj.core.api.Assertions.assertThat;

class AndIntervalCollectionTest extends IntervalTestSupport {

  private IntervalCollection collection;

  @BeforeEach
  void setUp() {
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

    var set3 = IntervalSets.disjoint(
        interval(2, 3),
        interval(12, 13),
        interval(22, 23)
    );

    collection = IntervalCollections.and(set1, set2, set3);
  }

  @Test
  void intervals_All_Success() {
    assertThat(collection.intervals())
        .toIterable()
        .containsExactly(
            interval(2, 3),
            interval(12, 13),
            interval(22, 23)
        );
  }

  @Test
  void intervals_EmbeddedCollectionAndInterval_Success() {

    var embedded = IntervalCollections.and(collection, interval(2, 22));

    assertThat(embedded.intervals())
        .toIterable()
        .containsExactly(
            interval(2, 3),
            interval(12, 13)
        );
  }

  @Test
  void intervals_WindowForward_Success() {
    assertThat(collection.intervals(interval(2, 22), FORWARD))
        .toIterable()
        .containsExactly(
            interval(2, 3),
            interval(12, 13)
        );
  }

  @Test
  void intervals_WindowBackward_Success() {

    assertThat(collection.intervals(interval(2, 22), BACKWARD))
        .toIterable()
        .containsExactly(
            interval(12, 13),
            interval(2, 3)
        );

  }

  @Test
  void intervals_EmptySet_containsEmpty_Success() {
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

    var empty = IntervalSets.disjoint();

    collection = IntervalCollections.and(set1, set2, empty);
    assertThat(collection.intervals())
        .toIterable()
        .containsExactly();
  }

}
