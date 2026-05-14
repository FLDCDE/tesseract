package com.fieldcode.tesseract.map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.fieldcode.tesseract.SignalRangeMap;
import com.fieldcode.tesseract.test_utils.SignalTestSupport;

import java.util.List;
import java.util.stream.Stream;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for the {@link DisjointSignalRangeMap} implementation.
 * <p>
 * Verifies operations for disjoint signal range maps, including range insertion, modification, and removal operations.
 */
@DisplayName("Disjoint signal range map operations")
class DisjointSignalRangeMapTest extends SignalTestSupport {

  private SignalRangeMap<Integer, String> map;

  @BeforeEach
  void setUp() {
    map = createSampleDisjointMap();
  }

  @Test
  @DisplayName("Should successfully put values in disjoint map")
  void disjoint_put_success() {
    var disjointMap = SignalRangeMaps.disjoint(0, 100, DEFAULT_VALUE);
    disjointMap.put(10, "10");
    disjointMap.put(0, "0");
    disjointMap.put(10, "0");

    assertThat(disjointMap.ranges())
        .toIterable()
        .containsExactly
            (
                range(
                    signal(0, "0"),
                    signal(100, DEFAULT_VALUE)
                )
            );
  }

  @Test
  @DisplayName("Should successfully put values with lower and upper bounds")
  void put_lowerUpper_Success() {
    map.put(-10, 0, "-10");
    map.put(0, 10, "0");

    assertThat(map.ranges())
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
  @DisplayName("Should successfully put all values from a stream")
  void putAll_stream_Success() {
    var rangeStream = Stream.of(
        range(signal(-10), signal(0)),
        range(signal(0), signal(10))
    );
    map.putAll(rangeStream);

    assertThat(map.ranges())
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
  @DisplayName("Should successfully put all values from an iterable")
  void putAll_iterable_Success() {

    var rangeStream = List.of(
        range(signal(-10), signal(0)),
        range(signal(0), signal(10))
    );
    map.putAll(rangeStream);

    assertThat(map.ranges())
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
  @DisplayName("Should remove bounded range successfully")
  void remove_lowerUpper_Bounded_Success() {
    map.put(-10, 0, "-10");
    map.put(0, 10, "0");

    map.remove(-50, 50);

    assertThat(map.ranges())
        .toIterable()
        .containsExactly(
            range(signal(MIN, DEFAULT_VALUE), signal(MAX, DEFAULT_VALUE)
            ));
  }

  @Test
  @DisplayName("Should remove range with lower and upper bounds successfully")
  void remove_lowerUpper_Success() {
    map.put(-10, 0, "-10");
    map.put(0, 10, "0");

    map.remove(-35, 35);

    assertThat(map.ranges())
        .toIterable()
        .containsExactly(
            range(signal(MIN, DEFAULT_VALUE), signal(-50)),
            range(signal(-50), signal(-35, DEFAULT_VALUE)),
            range(signal(-35, DEFAULT_VALUE), signal(35, "10")),
            range(signal(35, "10"), signal(50, DEFAULT_VALUE)),
            range(signal(50, DEFAULT_VALUE), signal(MAX, DEFAULT_VALUE))
        );
  }

}
