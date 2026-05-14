package com.fieldcode.tesseract.collection;

import java.time.Duration;

import org.junit.jupiter.api.Test;

import com.fieldcode.tesseract.IntervalCollection;
import com.fieldcode.tesseract.interval.IntervalSets;
import com.fieldcode.tesseract.interval.IntervalTestSupport;
import com.fieldcode.tesseract.interval.Intervals;

import static com.fieldcode.tesseract.QueryDirection.BACKWARD;
import static com.fieldcode.tesseract.QueryDirection.FORWARD;
import static org.assertj.core.api.Assertions.assertThat;

class IntervalCollectionFinderTest extends IntervalTestSupport {

  public static final Duration ONE_HOUR = Duration.ofHours(1);

  private static IntervalCollection emptyCollection() {
    return IntervalSets.disjoint();
  }

  private IntervalCollection collection() {
    return IntervalSets.disjoint(
        interval(0, 1),
        interval(5, 10),
        interval(20, 100)
    );
  }

  @Test
  void find_EmptyForward_Empty() {
    var set = emptyCollection();
    var result = IntervalCollectionFinder.find(set, Intervals.always(), FORWARD, ONE_HOUR);
    assertThat(result).isEmpty();
  }

  @Test
  void find_EmptyBackward_Empty() {
    var set = emptyCollection();
    var result = IntervalCollectionFinder.find(set, Intervals.always(), BACKWARD, ONE_HOUR);
    assertThat(result).isEmpty();
  }

  @Test
  void find_Forward_Success() {
    var result = IntervalCollectionFinder.find(collection(), Intervals.always(), FORWARD, ONE_HOUR);
    assertThat(result).contains(interval(0, 1));
  }

  @Test
  void find_Backward_Success() {
    var result = IntervalCollectionFinder.find(collection(), Intervals.always(), BACKWARD, ONE_HOUR);
    assertThat(result).contains(interval(99, 100));
  }

  @Test
  void find_ForwardWindow_Success() {
    var result = IntervalCollectionFinder.find(collection(), interval(7, 20), FORWARD, ONE_HOUR);
    assertThat(result).contains(interval(7, 8));
  }

  @Test
  void find_BackwardWindow_Success() {
    var result = IntervalCollectionFinder.find(collection(), interval(7, 20), BACKWARD, ONE_HOUR);
    assertThat(result).contains(interval(9, 10));
  }

}
