package com.fieldcode.tesseract.jackson;

import java.time.Duration;

import org.junit.jupiter.api.Test;

import com.fieldcode.tesseract.Interval;
import com.fieldcode.tesseract.IntervalContainer;
import com.fieldcode.tesseract.container.IntervalContainers;
import com.fieldcode.tesseract.interval.IntervalSets;
import com.fieldcode.tesseract.interval.Intervals;
import com.fieldcode.tesseract.jackson.test_utils.Jsons;
import com.fieldcode.tesseract.jackson.test_utils.TimeTestSupport;
import com.fieldcode.tesseract.tag.Tags;

import static org.assertj.core.api.Assertions.assertThat;

public class IntervalContainerJacksonTest extends TimeTestSupport {

  public static final Duration DAY = Duration.ofDays(1);

  @Test
  void clone_intervalContainer_Success() {
    var availabilities = IntervalSets.disjoint(
        interval(8, 17),
        interval(8, 17).shift(DAY)
    );

    var absences = IntervalSets.disjoint(
        interval(12, 13),
        interval(12, 13).shift(DAY)
    );

    var container = IntervalContainers.builder()
        .add("availabilities", availabilities, Tags.tag("available"))
        .add("absences", absences, Tags.tag("absent"))
        .build();

    var cloned = Jsons.clone(container, IntervalContainer.class);

    assertThat(cloned.get("availabilities").intervals())
        .toIterable()
        .containsExactly(
            interval(8, 17),
            interval(8, 17).shift(DAY)
        );

    assertThat(cloned.get("absences").intervals())
        .toIterable()
        .containsExactly(
            interval(12, 13),
            interval(12, 13).shift(DAY)
        );

  }

}
