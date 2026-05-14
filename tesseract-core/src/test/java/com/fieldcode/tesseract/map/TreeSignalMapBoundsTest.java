package com.fieldcode.tesseract.map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.fieldcode.tesseract.SignalMap;
import com.fieldcode.tesseract.test_utils.SignalTestSupport;

import static com.fieldcode.tesseract.QueryDirection.BACKWARD;
import static com.fieldcode.tesseract.QueryDirection.FORWARD;
import static org.assertj.core.api.Assertions.assertThat;

class TreeSignalMapBoundsTest extends SignalTestSupport {

  private SignalMap<Integer, String> map;

  @BeforeEach
  void setUp() {
    map = createSampleMap();
  }

  @Test
  void bounds_AllForward_Success() {

    var iterator = map.signals(MIN, MAX, FORWARD, true);

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
  void bounds_BoundedBoundsForward_Success() {

    var iterator = map.signals(-50, 50, FORWARD, true);

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
  void bounds_AllBackward_Reverse() {

    var iterator = map.signals(MIN, MAX, BACKWARD, true);

    assertThat(iterator)
        .toIterable()
        .containsExactly(
            signal(MAX, "50"),
            signal(50, "10"),
            signal(10, "0"),
            signal(0, "-10"),
            signal(-10, "-50"),
            signal(-50, DEFAULT_VALUE),
            signal(MIN, DEFAULT_VALUE)
        );
  }

  @Test
  void bounds_ForwardFloor_Success() {

    var iterator = map.signals(-50, 50, FORWARD, true);

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
  void bounds_BackwardFloor_Success() {

    var iterator = map.signals(-50, 50, BACKWARD, true);

    assertThat(iterator)
        .toIterable()
        .containsExactly(
            signal(50, "10"),
            signal(10, "0"),
            signal(0, "-10"),
            signal(-10, "-50"),
            signal(-50, DEFAULT_VALUE)
        );
  }

  @Test
  void bounds_ForwardNonFloor_Success() {

    var iterator = map.signals(-35, 35, FORWARD, true);

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
  void bounds_BackwardNonFloor_Success() {

    var iterator = map.signals(-35, 35, BACKWARD, true);

    assertThat(iterator)
        .toIterable()
        .containsExactly(
            signal(35, "10"),
            signal(10, "0"),
            signal(0, "-10"),
            signal(-10, "-50"),
            signal(-35, "-50")
        );
  }

  @Test
  void bounds_ForwardSameSignalFloor_Success() {
    var iterator = map.signals(7, 8, FORWARD, true);

    assertThat(iterator)
        .toIterable()
        .containsExactly(
            signal(7, "0"),
            signal(8, "0")
        );
  }

  @Test
  void bounds_BackwardSameSignalFloor_Success() {

    var iterator = map.signals(7, 8, BACKWARD, true);

    assertThat(iterator)
        .toIterable()
        .containsExactly(
            signal(8, "0"),
            signal(7, "0")
        );
  }

  @Test
  void bounds_ForwardSuccessiveNodes_Success() {

    var iterator = map.signals(0, 10, FORWARD, true);

    assertThat(iterator)
        .toIterable()
        .containsExactly(
            signal(0),
            signal(10)
        );
  }

  @Test
  void bounds_BackwardSuccessiveNodes_Success() {

    var iterator = map.signals(0, 10, BACKWARD, true);

    assertThat(iterator)
        .toIterable()
        .containsExactly(
            signal(10, "0"),
            signal(0, "-10")
        );
  }

}
