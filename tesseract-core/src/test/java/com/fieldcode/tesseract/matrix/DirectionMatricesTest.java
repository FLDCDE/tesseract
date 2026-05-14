package com.fieldcode.tesseract.matrix;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.fieldcode.tesseract.matrix.cache.DirectionMatrixTestSupport;

import java.time.Duration;
import java.util.List;
import static com.fieldcode.tesseract.distance.Distances.ONE_KILOMETER_INSTANCE;
import static com.fieldcode.tesseract.location.Locations.location;
import static com.fieldcode.tesseract.matrix.DirectionMatrices.dimensions;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Direction matrices utility operations")
class DirectionMatricesTest extends DirectionMatrixTestSupport {

  private static final Duration TEN_MINUTES = Duration.ofMinutes(10);

  @Test
  @DisplayName("Should calculate pythagorean distances and durations correctly")
  void pythagorean() {

    var locations = List.of(
        location(0, 0),
        location(0, 1),
        location(1, 1)
    );

    var matrix = DirectionMatrices.pythagorean(locations, ONE_KILOMETER_INSTANCE, TEN_MINUTES);

    assertThatMatrixContainsExactly(
        matrix.getDistancesInMeter(),
        new long[]{0, 1000, 1414},
        new long[]{1000, 0, 1000},
        new long[]{1414, 1000, 0});

    assertThatMatrixContainsExactly(
        matrix.getDurationsInSeconds(),
        new long[]{0, 600, 848},
        new long[]{600, 0, 600},
        new long[]{848, 600, 0}
    );
  }

  @Test
  @DisplayName("Should split matrix into multiple chunks when dimensions exceed limits")
  void split_Simple_Success() {

    var partial = dimensions(locations(5), locations(7));

    var split = DirectionMatrices.split(partial, 4, 3);

    assertThat(split).containsExactly(
        dimensions(locations(0, 4), locations(0, 3)),
        dimensions(locations(0, 4), locations(3, 6)),
        dimensions(locations(0, 4), locations(6, 7)),
        dimensions(locations(4, 5), locations(0, 3)),
        dimensions(locations(4, 5), locations(3, 6)),
        dimensions(locations(4, 5), locations(6, 7))
    );

  }

  @Test
  @DisplayName("Should keep matrix intact when dimensions are below limits")
  void split_LessThanLimit_Success() {

    var partial = dimensions(locations(5), locations(7));

    var split = DirectionMatrices.split(partial, 10, 10);

    assertThat(split).containsExactly(
        dimensions(locations(5), locations(7))
    );

  }


}
