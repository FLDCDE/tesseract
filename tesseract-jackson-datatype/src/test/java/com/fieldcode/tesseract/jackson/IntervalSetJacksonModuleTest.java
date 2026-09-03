package com.fieldcode.tesseract.jackson;

import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.fieldcode.tesseract.IntervalSet;
import com.fieldcode.tesseract.interval.IntervalSets;
import com.fieldcode.tesseract.jackson.test_utils.Jsons;
import com.fieldcode.tesseract.jackson.test_utils.TimeTestSupport;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("IntervalSetJacksonModule")
class IntervalSetJacksonModuleTest extends TimeTestSupport {

  @Test
  @DisplayName("should preserve all intervals when a disjoint interval set is serialized and deserialized")
  void intervalSet_Jackson() {
    // Arrange: a disjoint interval set with three separate intervals
    var set = IntervalSets.disjoint(
        Stream.of(
            interval(0, 2),
            interval(5, 10),
            interval(11, 15)
        )
    );

    // Act: serialize the set to JSON and parse it back into an IntervalSet
    var json = Jsons.stringify(set, true);

    var parsed = Jsons.parse(json, IntervalSet.class);

    // Assert: the round-tripped set contains the same intervals in the same order
    assertThat(parsed.intervals().stream()).containsExactly(
        interval(0, 2),
        interval(5, 10),
        interval(11, 15)
    );
  }

}
