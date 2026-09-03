package com.fieldcode.tesseract.jackson;

import java.time.Duration;

import org.junit.jupiter.api.DisplayName;
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

@DisplayName("IntervalContainerJackson")
public class IntervalContainerJacksonTest extends TimeTestSupport {

  public static final Duration DAY = Duration.ofDays(1);

  @Test
  @DisplayName("should round-trip an interval container holding tagged availabilities and absences")
  void clone_intervalContainer_Success() {
    // Arrange: a container with two tagged entries, availabilities and absences, each spanning two days
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

    // Act: serialize and deserialize the container back into an IntervalContainer
    var cloned = Jsons.clone(container, IntervalContainer.class);

    // Assert: both named entries retain their original intervals after the round trip
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
