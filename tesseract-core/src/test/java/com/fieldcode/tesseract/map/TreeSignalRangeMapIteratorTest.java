package com.fieldcode.tesseract.map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.fieldcode.tesseract.SignalRangeMap;
import com.fieldcode.tesseract.test_utils.SignalTestSupport;

import static com.fieldcode.tesseract.QueryDirection.BACKWARD;
import static com.fieldcode.tesseract.QueryDirection.FORWARD;
import static org.assertj.core.api.Assertions.assertThat;

class TreeSignalRangeMapIteratorTest extends SignalTestSupport {

  private SignalRangeMap<Integer, String> map;

  @BeforeEach
  void setUp() {
    map = createSampleDisjointMap();
  }

  @Test
  void iterator_All_Success() {

    var iterator = map.ranges();

    assertThat(iterator)
        .toIterable()
        .containsExactly(
            range(signal(MIN, DEFAULT_VALUE), signal(-50)),
            range(signal(-50), signal(-10)),
            range(signal(-10), signal(0)),
            range(signal(0), signal(10)),
            range(signal(10), signal(50, DEFAULT_VALUE)),
            range(signal(50, DEFAULT_VALUE), signal(MAX, DEFAULT_VALUE))
        );
  }

  @Test
  void iteratorBetween_BoundsForward_All() {

    var iterator = map.ranges(MIN, MAX, FORWARD);

    assertThat(iterator)
        .toIterable()
        .containsExactly(
            range(signal(MIN, DEFAULT_VALUE), signal(-50)),
            range(signal(-50), signal(-10)),
            range(signal(-10), signal(0)),
            range(signal(0), signal(10)),
            range(signal(10), signal(50, DEFAULT_VALUE)),
            range(signal(50, DEFAULT_VALUE), signal(MAX, DEFAULT_VALUE))
        );
  }

  @Test
  void iteratorBetween_BoundsBackward_Reverse() {
    var iterator = map.ranges(MIN, MAX, BACKWARD);

    assertThat(iterator)
        .toIterable()
        .containsExactly(
            range(signal(50, DEFAULT_VALUE), signal(MAX, DEFAULT_VALUE)),
            range(signal(10), signal(50, DEFAULT_VALUE)),
            range(signal(0), signal(10)),
            range(signal(-10), signal(0)),
            range(signal(-50), signal(-10)),
            range(signal(MIN, DEFAULT_VALUE), signal(-50))
        );
  }

  @Test
  void iteratorBetween_ForwardFloor_Success() {

    var iterator = map.ranges(-50, 50, FORWARD);

    assertThat(iterator)
        .toIterable()
        .containsExactly(
            range(signal(-50), signal(-10)),
            range(signal(-10), signal(0)),
            range(signal(0), signal(10)),
            range(signal(10), signal(50, DEFAULT_VALUE))
        );
  }

  @Test
  void iteratorBetween_BackwardFloor_Success() {

    var iterator = map.ranges(-50, 50, BACKWARD);

    assertThat(iterator)
        .toIterable()
        .containsExactly(
            range(signal(10), signal(50, DEFAULT_VALUE)),
            range(signal(0), signal(10)),
            range(signal(-10), signal(0)),
            range(signal(-50, "-50"), signal(-10))
        );
  }

  @Test
  void iteratorBetween_ForwardNonFloor_Success() {

    var iterator = map.ranges(-35, 35, FORWARD);

    assertThat(iterator)
        .toIterable()
        .containsExactly(
            range(signal(-35, "-50"), signal(-10)),
            range(signal(-10), signal(0)),
            range(signal(0), signal(10)),
            range(signal(10), signal(35, "10"))
        );
  }

  @Test
  void iteratorBetween_BackwardNonFloor_Success() {

    var iterator = map.ranges(-35, 35, BACKWARD);
    assertThat(iterator)
        .toIterable()
        .containsExactly(
            range(signal(10), signal(35, "10")),
            range(signal(0), signal(10)),
            range(signal(-10), signal(0)),
            range(signal(-35, "-50"), signal(-10))
        );
  }

  @Test
  void iteratorBetween_ForwardSameSignalFloor_Success() {
    var iterator = map.ranges(7, 8, FORWARD);

    assertThat(iterator)
        .toIterable()
        .containsExactly(
            range(signal(7, "0"), signal(8, "0"))
        );
  }

  @Test
  void iteratorBetween_BackwardSameSignalFloor_Success() {
    var iterator = map.ranges(7, 8, BACKWARD);

    assertThat(iterator)
        .toIterable()
        .containsExactly(
            range(signal(7, "0"), signal(8, "0"))
        );
  }

  @Test
  void iteratorBetween_ForwardSuccessiveNodes_Success() {

    var iterator = map.ranges(0, 10, FORWARD);

    assertThat(iterator)
        .toIterable()
        .containsExactly(
            range(signal(0, "0"), signal(10, "10"))
        );
  }

  @Test
  void iteratorBetween_BackwardSuccessiveNodes_Success() {

    var iterator = map.ranges(0, 10, BACKWARD);

    assertThat(iterator)
        .toIterable()
        .containsExactly(
            range(signal(0, "0"), signal(10, "10"))
        );
  }

  @Test
  void iteratorFrom_ForwardMin_All() {

    var iterator = map.ranges(MIN, FORWARD);

    assertThat(iterator)
        .toIterable()
        .containsExactly(
            range(signal(MIN, DEFAULT_VALUE), signal(-50)),
            range(signal(-50), signal(-10)),
            range(signal(-10), signal(0)),
            range(signal(0), signal(10)),
            range(signal(10), signal(50, DEFAULT_VALUE)),
            range(signal(50, DEFAULT_VALUE), signal(MAX, DEFAULT_VALUE))
        );
  }

  @Test
  void iteratorFrom_BackwardMax_All() {

    var iterator = map.ranges(MAX, BACKWARD);

    assertThat(iterator)
        .toIterable()
        .containsExactly(
            range(signal(50, DEFAULT_VALUE), signal(MAX, DEFAULT_VALUE)),
            range(signal(10), signal(50, DEFAULT_VALUE)),
            range(signal(0), signal(10)),
            range(signal(-10), signal(0)),
            range(signal(-50), signal(-10)),
            range(signal(MIN, DEFAULT_VALUE), signal(-50))
        );
  }

  @Test
  void iteratorFrom_ForwardFloor_All() {

    var iterator = map.ranges(0, FORWARD);

    assertThat(iterator)
        .toIterable()
        .containsExactly(
            range(signal(0), signal(10)),
            range(signal(10), signal(50, DEFAULT_VALUE)),
            range(signal(50, DEFAULT_VALUE), signal(MAX, DEFAULT_VALUE))
        );
  }

  @Test
  void iteratorFrom_BackwardFloor_All() {

    var iterator = map.ranges(0, BACKWARD);

    assertThat(iterator)
        .toIterable()
        .containsExactly(
            range(signal(-10), signal(0)),
            range(signal(-50), signal(-10)),
            range(signal(MIN, DEFAULT_VALUE), signal(-50))
        );
  }

  @Test
  void iteratorFrom_ForwardNonFloor_All() {

    var iterator = map.ranges(5, FORWARD);

    assertThat(iterator)
        .toIterable()
        .containsExactly(
            range(signal(5, "0"), signal(10)),
            range(signal(10), signal(50, DEFAULT_VALUE)),
            range(signal(50, DEFAULT_VALUE), signal(MAX, DEFAULT_VALUE))
        );
  }

  @Test
  void iteratorFrom_BackwardNonFloor_All() {

    var iterator = map.ranges(5, BACKWARD);

    assertThat(iterator)
        .toIterable()
        .containsExactly(
            range(signal(0), signal(5, "0")),
            range(signal(-10), signal(0)),
            range(signal(-50), signal(-10)),
            range(signal(MIN, DEFAULT_VALUE), signal(-50))
        );
  }

}
