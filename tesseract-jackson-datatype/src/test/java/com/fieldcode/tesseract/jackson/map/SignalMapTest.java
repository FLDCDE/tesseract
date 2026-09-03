package com.fieldcode.tesseract.jackson.map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fieldcode.tesseract.Moment;
import com.fieldcode.tesseract.SignalMap;
import com.fieldcode.tesseract.jackson.test_utils.Jsons;
import com.fieldcode.tesseract.jackson.test_utils.SignalTestSupport;
import com.fieldcode.tesseract.map.TreeSignalMap;
import com.fieldcode.tesseract.moment.Moments;

import java.time.Duration;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("SignalMap Jackson serialization")
class SignalMapTest extends SignalTestSupport {

  @Test
  @DisplayName("should deserialize a signal map with intermediate signals into the original sequence")
  void signalMap_Deserialize_Test() {
    // Arrange: a signal map spanning -inf to +inf with three intermediate signals
    var signalMap = TreeSignalMap.of(Moments.ninf(), Moments.inf(), Duration.ofHours(-100));
    signalMap.put(moment(-10), Duration.ofHours(-10));
    signalMap.put(moment(0), Duration.ZERO);
    signalMap.put(moment(10), Duration.ofHours(10));
    signalMap.put(Moments.inf(), Duration.ofHours(100));

    // Act: serialize the map to JSON and parse it back
    var json = Jsons.stringify(signalMap, true);

    var deserialized = Jsons.parse(json, new TypeReference<SignalMap<Moment, Duration>>() {});

    // Assert: the deserialized signal stream matches the original signals in order
    assertThat(deserialized.stream())
        .containsExactly(
            signal(Moments.ninf(), Duration.ofHours(-100)),
            signal(moment(-10), Duration.ofHours(-10)),
            signal(moment(0), Duration.ZERO),
            signal(moment(10), Duration.ofHours(10)),
            signal(Moments.inf(), Duration.ofHours(100))
        );
  }

  @Test
  @DisplayName("should deserialize a signal map with no intermediate signals into just the bounds")
  void signalMap_EmptyDeserialize_Test() {
    // Arrange: a signal map spanning -inf to +inf with no intermediate signals
    var signalMap = TreeSignalMap.of(Moments.ninf(), Moments.inf(), Duration.ofHours(-100));

    // Act: serialize the map to JSON and parse it back
    var json = Jsons.stringify(signalMap, true);

    var deserialized = Jsons.parse(json, new TypeReference<SignalMap<Moment, Duration>>() {});

    // Assert: the deserialized signal stream contains only the two boundary signals
    assertThat(deserialized.stream())
        .containsExactly(
            signal(Moments.ninf(), Duration.ofHours(-100)),
            signal(Moments.inf(), Duration.ofHours(-100))
        );
  }

}
