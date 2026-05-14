package com.fieldcode.tesseract.map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.fieldcode.tesseract.SignalRangeMap;
import com.fieldcode.tesseract.signal.SignalRangePredicates;
import com.fieldcode.tesseract.test_utils.SignalTestSupport;

import static com.fieldcode.tesseract.QueryDirection.BACKWARD;
import static com.fieldcode.tesseract.QueryDirection.FORWARD;
import static org.assertj.core.api.Assertions.assertThat;

class NavigableSignalRangeTest extends SignalTestSupport {

  private SignalRangeMap<Integer, String> map;

  @BeforeEach
  void setUp() {
    map = createSampleDisjointMap();
  }

  @Test
  void iterator_Forward_Success() {

    var iterator = map.get(0).iterator(FORWARD);

    assertThat(iterator)
        .toIterable()
        .containsExactly(
            range(signal(0), signal(10)),
            range(signal(10), signal(50, DEFAULT_VALUE)),
            range(signal(50, DEFAULT_VALUE), signal(MAX, DEFAULT_VALUE))
        );

  }

  @Test
  void iterator_Backward_Success() {

    var iterator = map.get(0).iterator(BACKWARD);

    assertThat(iterator)
        .toIterable()
        .containsExactly(
            range(signal(0), signal(10)),
            range(signal(-10), signal(0)),
            range(signal(-50), signal(-10)),
            range(signal(MIN, DEFAULT_VALUE), signal(-50))
        );

  }

  @Test
  void iterator_ForwardTakeWhile_Success() {

    var iterator = map.get(0).iterator(FORWARD, SignalRangePredicates.lowerKeyLessThan(50));

    assertThat(iterator)
        .toIterable()
        .containsExactly(
            range(signal(0), signal(10)),
            range(signal(10), signal(50, DEFAULT_VALUE))
        );

  }

  @Test
  void iterator_BackwardTakeWhile_Success() {

    var iterator = map.get(0).iterator(BACKWARD, SignalRangePredicates.upperKeyGreaterThan(-50));

    assertThat(iterator)
        .toIterable()
        .containsExactly(
            range(signal(0), signal(10)),
            range(signal(-10), signal(0)),
            range(signal(-50), signal(-10))
        );

  }

}
