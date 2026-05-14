package com.fieldcode.tesseract.jackson;

import java.util.stream.Stream;

import org.junit.jupiter.api.Test;

import com.fieldcode.tesseract.IntervalSet;
import com.fieldcode.tesseract.interval.IntervalSets;
import com.fieldcode.tesseract.jackson.test_utils.Jsons;
import com.fieldcode.tesseract.jackson.test_utils.TimeTestSupport;

import static org.assertj.core.api.Assertions.assertThat;

class IntervalSetJacksonModuleTest extends TimeTestSupport {

  @Test
  void intervalSet_Jackson() {
    var set = IntervalSets.disjoint(
        Stream.of(
            interval(0, 2),
            interval(5, 10),
            interval(11, 15)
        )
    );

    var json = Jsons.stringify(set, true);

    var parsed = Jsons.parse(json, IntervalSet.class);

    assertThat(parsed.intervals().stream()).containsExactly(
        interval(0, 2),
        interval(5, 10),
        interval(11, 15)
    );
  }

}
