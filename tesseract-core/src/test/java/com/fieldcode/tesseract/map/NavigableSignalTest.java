package com.fieldcode.tesseract.map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.fieldcode.tesseract.SignalMap;
import com.fieldcode.tesseract.signal.SignalPredicates;
import com.fieldcode.tesseract.test_utils.SignalTestSupport;

import static com.fieldcode.tesseract.QueryDirection.BACKWARD;
import static com.fieldcode.tesseract.QueryDirection.FORWARD;
import static org.assertj.core.api.Assertions.assertThat;

class NavigableSignalTest extends SignalTestSupport {

  private SignalMap<Integer, String> map;

  @BeforeEach
  void setUp() {
    map = createSampleMap();
  }

  @Test
  void iterator_Forward_Success() {

    var iterator = map.get(0).iterator(FORWARD);

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
  void iterator_Backward_Success() {

    var iterator = map.get(0).iterator(BACKWARD);

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
  void iterator_ForwardTakeWhile_Success() {

    var iterator = map.get(0).iterator(FORWARD, SignalPredicates.keyLessThan(50));

    assertThat(iterator)
        .toIterable()
        .containsExactly(
            signal(0),
            signal(10)
        );

  }

  @Test
  void iterator_BackwardTakeWhile_Success() {

    var iterator = map.get(0).iterator(BACKWARD, SignalPredicates.keyGreaterThan(-50));

    assertThat(iterator)
        .toIterable()
        .containsExactly(
            signal(0),
            signal(-10)
        );

  }

}
