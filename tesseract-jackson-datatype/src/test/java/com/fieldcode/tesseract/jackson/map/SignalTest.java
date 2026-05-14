package com.fieldcode.tesseract.jackson.map;

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

class SignalTest extends SignalTestSupport {

  @Test
  void signal_Deserialize_Test() {
    var signal = signal(0, "0");

    var json = Jsons.stringify(signal);
    var deserialized = Jsons.parse(json, new TypeReference<Signal<Integer, String>>() {
    });

    assertThat(deserialized.getKey()).isEqualTo(0);
    assertThat(deserialized.getValue()).isEqualTo("0");

  }

  @Test
  void signal_List_Deserialize_Test() {
    var signalList = List.of(signal(-10, "-10"), signal(0, "0"), signal(10, "10"));

    var json = Jsons.stringify(signalList);
    var deserialized = Jsons.parse(json, new TypeReference<List<Signal<Integer, String>>>() {
    });

    assertThat(deserialized).containsExactly(
        signal(-10, "-10"),
        signal(0, "0"),
        signal(10, "10")
    );
  }


  @Test
  void signal_SERDES_success() {
    var signal = Signals.signal(Moments.ninf(), Duration.ofHours(-100));
    var json = Jsons.stringify(signal, true);
    var parsed = Jsons.parse(json, new TypeReference<Signal<Moment, Duration>>() {
    });

    assertThat(parsed).isEqualTo(signal);
  }
}
