package com.fieldcode.tesseract.map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.fieldcode.tesseract.SignalMap;
import com.fieldcode.tesseract.test_utils.SignalTestSupport;

import static com.fieldcode.tesseract.QueryDirection.BACKWARD;
import static com.fieldcode.tesseract.QueryDirection.FORWARD;
import static org.assertj.core.api.Assertions.assertThat;

class TreeSignalMapIteratorTest extends SignalTestSupport {

  private SignalMap<Integer, String> map;

  @BeforeEach
  void setUp() {
    map = createSampleMap();
  }

  @Test
  void iterator_All_Success() {

    var iterator = map.signals();

    assertThat(iterator)
        .toIterable()
        .containsExactly(
            signal(MIN, DEFAULT_VALUE),
            signal(-50),
            signal(-10),
            signal(0),
            signal(10),
            signal(50),
            signal(MAX, DEFAULT_VALUE)
        );
  }


  @Test
  void iteratorBetween_BoundsForward_All() {

    var iterator = map.signals(MIN, MAX, FORWARD);

    assertThat(iterator)
        .toIterable()
        .containsExactly(
            signal(MIN, DEFAULT_VALUE),
            signal(-50),
            signal(-10),
            signal(0),
            signal(10),
            signal(50),
            signal(MAX, DEFAULT_VALUE)
        );
  }

  @Test
  void iteratorBetween_BoundsBackward_Reverse() {

    var iterator = map.signals(MIN, MAX, BACKWARD);

    assertThat(iterator)
        .toIterable()
        .containsExactly(
            signal(MAX, DEFAULT_VALUE),
            signal(50),
            signal(10),
            signal(0),
            signal(-10),
            signal(-50),
            signal(MIN, DEFAULT_VALUE)
        );
  }

  @Test
  void iteratorBetween_ForwardFloor_Success() {

    var iterator = map.signals(-50, 50, FORWARD);

    assertThat(iterator)
        .toIterable()
        .containsExactly(
            signal(-50),
            signal(-10),
            signal(0),
            signal(10),
            signal(50)
        );
  }

  @Test
  void iteratorBetween_BackwardFloor_Success() {

    var iterator = map.signals(-50, 50, BACKWARD);

    assertThat(iterator)
        .toIterable()
        .containsExactly(
            signal(50),
            signal(10),
            signal(0),
            signal(-10),
            signal(-50)
        );
  }

  @Test
  void iteratorBetween_ForwardNonFloor_Success() {

    var iterator = map.signals(-35, 35, FORWARD);

    assertThat(iterator)
        .toIterable()
        .containsExactly(
            signal(-35, "-50"),
            signal(-10),
            signal(0),
            signal(10),
            signal(35, "10")
        );
  }

  @Test
  void iteratorBetween_BackwardNonFloor_Success() {

    var iterator = map.signals(-35, 35, BACKWARD);

    assertThat(iterator)
        .toIterable()
        .containsExactly(
            signal(35, "10"),
            signal(10),
            signal(0),
            signal(-10),
            signal(-35, "-50")
        );
  }

  @Test
  void iteratorBetween_ForwardSameSignalFloor_Success() {
    var iterator = map.signals(7, 8, FORWARD);

    assertThat(iterator)
        .toIterable()
        .containsExactly(
            signal(7, "0"),
            signal(8, "0")
        );
  }

  @Test
  void iteratorBetween_BackwardSameSignalFloor_Success() {

    var iterator = map.signals(7, 8, BACKWARD);

    assertThat(iterator)
        .toIterable()
        .containsExactly(
            signal(8, "0"),
            signal(7, "0")
        );
  }

  @Test
  void iteratorBetween_ForwardSuccessiveNodes_Success() {

    var iterator = map.signals(0, 10, FORWARD);

    assertThat(iterator)
        .toIterable()
        .containsExactly(
            signal(0),
            signal(10)
        );
  }

  @Test
  void iteratorBetween_BackwardSuccessiveNodes_Success() {

    var iterator = map.signals(0, 10, BACKWARD);

    assertThat(iterator)
        .toIterable()
        .containsExactly(
            signal(10),
            signal(0)
        );
  }

  @Test
  void iteratorFrom_ForwardMin_All() {

    var iterator = map.signals(MIN, FORWARD);

    assertThat(iterator)
        .toIterable()
        .containsExactly(
            signal(MIN, DEFAULT_VALUE),
            signal(-50),
            signal(-10),
            signal(0),
            signal(10),
            signal(50),
            signal(MAX, DEFAULT_VALUE)
        );
  }

  @Test
  void iteratorFrom_BackwardMax_All() {

    var iterator = map.signals(MAX, BACKWARD);

    assertThat(iterator)
        .toIterable()
        .containsExactly(
            signal(MAX, DEFAULT_VALUE),
            signal(50),
            signal(10),
            signal(0),
            signal(-10),
            signal(-50),
            signal(MIN, DEFAULT_VALUE)
        );
  }

  @Test
  void iteratorFrom_ForwardFloor_All() {

    var iterator = map.signals(0, FORWARD);

    assertThat(iterator)
        .toIterable()
        .containsExactly(
            signal(0),
            signal(10),
            signal(50),
            signal(MAX, DEFAULT_VALUE)
        );
  }

  @Test
  void iteratorFrom_BackwardFloor_All() {

    var iterator = map.signals(0, BACKWARD);

    assertThat(iterator)
        .toIterable()
        .containsExactly(
            signal(0),
            signal(-10),
            signal(-50),
            signal(MIN, DEFAULT_VALUE)
        );
  }

  @Test
  void iteratorFrom_ForwardNonFloor_All() {

    var iterator = map.signals(5, FORWARD);

    assertThat(iterator)
        .toIterable()
        .containsExactly(
            signal(5, "0"),
            signal(10),
            signal(50),
            signal(MAX, DEFAULT_VALUE)
        );
  }

  @Test
  void iteratorFrom_BackwardNonFloor_All() {

    var iterator = map.signals(5, BACKWARD);

    assertThat(iterator)
        .toIterable()
        .containsExactly(
            signal(5, "0"),
            signal(0),
            signal(-10),
            signal(-50),
            signal(MIN, DEFAULT_VALUE)
        );
  }

}
