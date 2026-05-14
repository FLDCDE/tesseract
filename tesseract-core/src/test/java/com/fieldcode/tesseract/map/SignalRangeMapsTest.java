package com.fieldcode.tesseract.map;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;

import com.fieldcode.tesseract.test_utils.SignalTestSupport;

import static org.assertj.core.api.Assertions.assertThat;

public class SignalRangeMapsTest extends SignalTestSupport {

  @Test
  void signalMap_disjoint_Modifiable_Success() {

    var modifiable = SignalRangeMaps.disjoint(-1000, 1000, DEFAULT_VALUE);

    modifiable.put(range(
        signal(-500, DEFAULT_VALUE),
        signal(500)
    ));

    assertThat(modifiable.stream())
        .containsExactly(
            range(-1000, DEFAULT_VALUE, 1000, DEFAULT_VALUE)
        );
  }

  @Test
  void signalMap_disjoint_ModifiableCollection_Success() {
    var collection = List.of(
        range(signal(-10), signal(0, "-10")),
        range(signal(0, "-10"), signal(10))
    );

    var modifiable = SignalRangeMaps.disjoint(-1000, 1000, DEFAULT_VALUE, collection);
    assertThat(modifiable.stream())
        .containsExactly(
            range(-1000, DEFAULT_VALUE, -10, "-10"),
            range(-10, "-10", 10, DEFAULT_VALUE),
            range(10, DEFAULT_VALUE, 1000, DEFAULT_VALUE)
        );
  }

  @Test
  void signalMap_disjoint_ModifiableMap_Success() {

    var signals = Stream.of(
        range(signal(-10), signal(0, "-10")),
        range(signal(0, "-10"), signal(10))
    );

    var modifiable = SignalRangeMaps.disjoint(-1000, 1000, DEFAULT_VALUE, signals);

    assertThat(modifiable.stream())
        .containsExactly(
            range(-1000, DEFAULT_VALUE, -10, "-10"),
            range(-10, "-10", 10, DEFAULT_VALUE),
            range(10, DEFAULT_VALUE, 1000, DEFAULT_VALUE)
        );
  }

  @Test
  void signalMap_nonOverlapping_Modifiable_Success() {

    var modifiable = SignalRangeMaps.nonOverlapping(-1000, 1000, DEFAULT_VALUE);

    modifiable.put(range(
        signal(-500, DEFAULT_VALUE),
        signal(500)
    ));

    assertThat(modifiable.stream())
        .containsExactly(
            range(-1000, DEFAULT_VALUE, -500, DEFAULT_VALUE),
            range(-500, DEFAULT_VALUE, 500, DEFAULT_VALUE),
            range(500, DEFAULT_VALUE, 1000, DEFAULT_VALUE)
        );
  }

  @Test
  void signalMap_nonOverlapping_ModifiableCollection_Success() {
    var collection = List.of(
        range(signal(-10), signal(0, "-10")),
        range(signal(0, "-10"), signal(10))
    );

    var modifiable = SignalRangeMaps.nonOverlapping(-1000, 1000, DEFAULT_VALUE, collection);
    assertThat(modifiable.stream())
        .containsExactly(
            range(-1000, DEFAULT_VALUE, -10, "-10"),
            range(-10, "-10", 0, "-10"),
            range(0, "-10", 10, DEFAULT_VALUE),
            range(10, DEFAULT_VALUE, 1000, DEFAULT_VALUE)
        );
  }

  @Test
  void signalMap_nonOverlapping_ModifiableMap_Success() {

    var signals = Stream.of(
        range(-10, "-10", 0, "-10"),
        range(0, "-10", 10, "10")
    );

    var modifiable = SignalRangeMaps.nonOverlapping(-1000, 1000, DEFAULT_VALUE, signals);

    assertThat(modifiable.stream())
        .containsExactly(
            range(-1000, DEFAULT_VALUE, -10, "-10"),
            range(-10, "-10", 0, "-10"),
            range(0, "-10", 10, DEFAULT_VALUE),
            range(10, DEFAULT_VALUE, 1000, DEFAULT_VALUE)
        );
  }

}
