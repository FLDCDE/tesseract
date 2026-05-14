package com.fieldcode.tesseract.map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.fieldcode.tesseract.test_utils.SignalTestSupport;

import java.util.stream.Stream;
import static org.assertj.core.api.Assertions.assertThat;


@DisplayName("Non-overlapping signal range map operations")
class NonOverlappingSignalRangeMapTest extends SignalTestSupport {

  @Test
  @DisplayName("Should successfully put non-overlapping ranges into the map")
  void signalRange_put_nonOverlapping_success() {

    var map = createNonOverlappingMap();

    var rangeStream = Stream.of(
        range(signal(0, DEFAULT_VALUE), signal(10, "10")),
        range(signal(10, DEFAULT_VALUE), signal(20, DEFAULT_VALUE))
    );

    map.putAll(rangeStream);

    assertThat(map.ranges())
        .toIterable()
        .containsExactly(
            range(signal(MIN, DEFAULT_VALUE), signal(0, DEFAULT_VALUE)),
            range(signal(0, DEFAULT_VALUE), signal(10, DEFAULT_VALUE)),
            range(signal(10, DEFAULT_VALUE), signal(20, DEFAULT_VALUE)),
            range(signal(20, DEFAULT_VALUE), signal(MAX, DEFAULT_VALUE)
            )
        );
  }

}
