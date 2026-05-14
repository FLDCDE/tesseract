package com.fieldcode.tesseract.map;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

import com.fieldcode.tesseract.test_utils.SignalTestSupport;

import static org.assertj.core.api.Assertions.assertThat;

class SignalMapsTest extends SignalTestSupport {

  @Test
  void signalMap_Modifiable_Success() {

    var modifiable = SignalMaps.modifiable(-1000, 1000, DEFAULT_VALUE);

    assertThat(modifiable.stream())
        .containsExactly(
            signal(-1000, DEFAULT_VALUE),
            signal(1000, DEFAULT_VALUE)
        );
  }

  @Test
  void signalMap_ModifiableCollection_Success() {
    var collection = List.of(
        signal(-10),
        signal(0),
        signal(10)
    );

    var modifiable = SignalMaps.modifiable(-1000, 1000, DEFAULT_VALUE, collection);

    assertThat(modifiable.stream())
        .containsExactly(

            signal(-1000, DEFAULT_VALUE),
            signal(-10),
            signal(0),
            signal(10),
            signal(1000, DEFAULT_VALUE)
        );
  }

  @Test
  void signalMap_ModifiableMap_Success() {

    var signals = Map.of(
        -100, "-100",
        100, "100"
    );

    var modifiable = SignalMaps.modifiable(-1000, 1000, DEFAULT_VALUE, signals);

    assertThat(modifiable.stream())
        .containsExactly(
            signal(-1000, DEFAULT_VALUE),
            signal(-100),
            signal(100),
            signal(1000, DEFAULT_VALUE)
        );
  }

}
