package com.fieldcode.tesseract.jackson.map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fieldcode.tesseract.Moment;
import com.fieldcode.tesseract.Signal;
import com.fieldcode.tesseract.jackson.test_utils.Jsons;
import com.fieldcode.tesseract.jackson.test_utils.SignalTestSupport;
import com.fieldcode.tesseract.moment.Moments;
import com.fieldcode.tesseract.signal.Signals;

import java.time.Duration;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Signal Jackson serialization")
class SignalTest extends SignalTestSupport {

  @Test
  @DisplayName("should deserialize a single signal's key and value")
  void signal_Deserialize_Test() {
    // Arrange: a single signal with an integer key and string value
    var signal = signal(0, "0");

    // Act: serialize the signal to JSON and parse it back
    var json = Jsons.stringify(signal);
    var deserialized = Jsons.parse(json, new TypeReference<Signal<Integer, String>>() {
    });

    // Assert: the deserialized signal's key and value match the original
    assertThat(deserialized.getKey())
        .isEqualTo(0);
    assertThat(deserialized.getValue())
        .isEqualTo("0");

  }

  @Test
  @DisplayName("should deserialize a list of signals in order")
  void signal_List_Deserialize_Test() {
    // Arrange: a list of three signals
    var signalList = List.of(signal(-10, "-10"), signal(0, "0"), signal(10, "10"));

    // Act: serialize the list to JSON and parse it back
    var json = Jsons.stringify(signalList);
    var deserialized = Jsons.parse(json, new TypeReference<List<Signal<Integer, String>>>() {
    });

    // Assert: the deserialized list contains the same signals in the same order
    assertThat(deserialized).containsExactly(
        signal(-10, "-10"),
        signal(0, "0"),
        signal(10, "10")
    );
  }


  @Test
  @DisplayName("should round-trip a signal built from a moment and duration")
  void signal_Clone_success() {
    // Arrange: a signal keyed by negative infinity with a negative duration value
    var signal = Signals.signal(Moments.ninf(), Duration.ofHours(-100));

    // Act: serialize and deserialize it back
    var json = Jsons.stringify(signal, true);
    var parsed = Jsons.parse(json, new TypeReference<Signal<Moment, Duration>>() {
    });

    // Assert: the round-tripped signal equals the original
    assertThat(parsed)
        .isEqualTo(signal);
  }
}
