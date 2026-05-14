package com.fieldcode.tesseract.jackson.map;

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

class SignalMapTest extends SignalTestSupport {

  @Test
  void signalMap_Deserialize_Test() {

    var signalMap = TreeSignalMap.of(Moments.ninf(), Moments.inf(), Duration.ofHours(-100));
    signalMap.put(moment(-10), Duration.ofHours(-10));
    signalMap.put(moment(0), Duration.ZERO);
    signalMap.put(moment(10), Duration.ofHours(10));
    signalMap.put(Moments.inf(), Duration.ofHours(100));

    var json = Jsons.stringify(signalMap, true);

    var deserialized = Jsons.parse(json, new TypeReference<SignalMap<Moment, Duration>>() {});

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
  void signalMap_EmptyDeserialize_Test() {

    var signalMap = TreeSignalMap.of(Moments.ninf(), Moments.inf(), Duration.ofHours(-100));

    var json = Jsons.stringify(signalMap, true);

    var deserialized = Jsons.parse(json, new TypeReference<SignalMap<Moment, Duration>>() {});

    assertThat(deserialized.stream())
        .containsExactly(
            signal(Moments.ninf(), Duration.ofHours(-100)),
            signal(Moments.inf(), Duration.ofHours(-100))
        );
  }

}
