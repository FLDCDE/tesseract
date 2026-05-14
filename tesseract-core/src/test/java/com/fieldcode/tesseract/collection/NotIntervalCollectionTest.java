package com.fieldcode.tesseract.collection;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.fieldcode.tesseract.IntervalCollection;
import com.fieldcode.tesseract.interval.IntervalSets;
import com.fieldcode.tesseract.interval.IntervalTestSupport;
import com.fieldcode.tesseract.interval.Intervals;

import static com.fieldcode.tesseract.QueryDirection.BACKWARD;
import static com.fieldcode.tesseract.QueryDirection.FORWARD;
import static org.assertj.core.api.Assertions.assertThat;

class NotIntervalCollectionTest extends IntervalTestSupport {

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

    collection = IntervalCollections.not(set1, set2, set3);
  }


  @Test
  void intervalsCollectionNot_All_Success() {
    assertThat(collection.intervals())
        .toIterable()
        .containsExactly(
            intervalTo(0),
            interval(5, 10),
            interval(15, 20),
            intervalFrom(25)
        );
  }

  @Test
  void intervalsCollectionNot_Window_forward_Success() {
    assertThat(collection.intervals(interval(4, 11), FORWARD))
        .toIterable()
        .containsExactly(
            interval(5, 10)
        );
  }

  @Test
  void intervalsCollectionNot_alwaysWindow_forward_Success() {
    assertThat(collection.intervals(Intervals.always(), FORWARD))
        .toIterable()
        .containsExactly(
            intervalTo(0),
            interval(5, 10),
            interval(15, 20),
            intervalFrom(25)
        );
  }

  @Test
  void intervalsCollectionNot_Window_forward_backward() {
    assertThat(collection.intervals(interval(4, 11), BACKWARD))
        .toIterable()
        .containsExactly(
            interval(5, 10)
        );
  }

  @Test
  void intervalsCollectionNot_alwaysWindow_backward_Success() {
    assertThat(collection.intervals(Intervals.always(), BACKWARD))
        .toIterable()
        .containsExactly(
            intervalFrom(25),
            interval(15, 20),
            interval(5, 10),
            intervalTo(0)
        );
  }

  @Test
  void intervalsCollectionNot_emptySetIgnored_Success() {
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
    var set4 = IntervalSets.disjoint();
    collection = IntervalCollections.not(set1, set2, set3, set4);

    assertThat(collection.intervals(Intervals.always(), FORWARD))
        .toIterable()
        .containsExactly(
            intervalTo(0),
            interval(5, 10),
            interval(15, 20),
            intervalFrom(25)
        );
  }

}


