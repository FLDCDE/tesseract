package com.fieldcode.tesseract.collection;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.fieldcode.tesseract.IntervalCollection;
import com.fieldcode.tesseract.interval.IntervalTestSupport;
import com.fieldcode.tesseract.signal.Signals;

import static com.fieldcode.tesseract.QueryDirection.BACKWARD;
import static com.fieldcode.tesseract.QueryDirection.FORWARD;
import static org.assertj.core.api.Assertions.assertThat;

class EmptyCollectionTest extends IntervalTestSupport {

  private IntervalCollection empty;

  @BeforeEach
  void setUp() {
    empty = IntervalCollections.empty();
  }

  @Test
  void intervals_Empty() {
    assertThat(empty.intervals())
        .toIterable()
        .isEmpty();
  }

  @Test
  void intervals_WindowForward_Empty() {
    assertThat(empty.intervals(interval(0, 10), FORWARD))
        .toIterable()
        .isEmpty();
  }

  @Test
  void intervals_WindowBackward_Empty() {
    assertThat(empty.intervals(interval(0, 10), BACKWARD))
        .toIterable()
        .isEmpty();
  }

  @Test
  void bounds_WindowForward_Empty() {
    assertThat(empty.bounds(interval(0, 10), FORWARD))
        .toIterable()
        .containsExactly(
            Signals.signal(moment(0), false),
            Signals.signal(moment(10), false)
        );
  }

  @Test
  void bounds_WindowBackward_Empty() {
    assertThat(empty.bounds(interval(0, 10), BACKWARD))
        .toIterable()
        .containsExactly(
            Signals.signal(moment(10), false),
            Signals.signal(moment(0), false)
        );
  }

}
