package com.fieldcode.tesseract.matrix;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.fieldcode.tesseract.Location;
import com.fieldcode.tesseract.location.Locations;
import com.fieldcode.tesseract.matrix.cache.MatrixTestSupport;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("Direction matrix builder operations")
class DirectionMatrixBuilderTest extends MatrixTestSupport {

  private Location location(int latitude, int longitude) {
    return Locations.location(latitude, longitude);
  }

  @Test
  @DisplayName("Should throw exception when direction matrix has missing destination")
  void build_MissingDestinationInput_Exception() {
    assertThrows(IllegalStateException.class, () ->
        DirectionMatrixBuilder.of()
            .set(location(0, 0), location(0, 0), 0, 0)
            .set(location(0, 1), location(0, 0), 0, 0)
            .build());
  }

  @Test
  @DisplayName("Should throw exception when direction matrix has missing origin")
  void build_MissingOriginInput_Exception() {
    assertThrows(IllegalStateException.class, () ->
        DirectionMatrixBuilder.of()
            .set(location(0, 0), location(0, 0), 0, 0)
            .set(location(0, 0), location(0, 1), 0, 0)
            .build());
  }

  @Test
  @DisplayName("Should correctly build and reconstruct a 3x3 direction matrix")
  void build_3N_Success() {
    var location1 = location(0, 0);
    var location2 = location(0, 1);
    var location3 = location(0, 2);

    var matrix = DirectionMatrixBuilder.of()
        .set(location1, location1, 0, 0)
        .set(location1, location2, 1, 2)
        .set(location1, location3, 2, 4)

        .set(location2, location1, 10, 20)
        .set(location2, location2, 0, 0)
        .set(location2, location3, 1, 2)

        .set(location3, location1, 20, 40)
        .set(location3, location2, 10, 20)
        .set(location3, location3, 0, 0)

        .build();

    assertThat(matrix.getLocations()).containsExactly(
        location1,
        location2,
        location3
    );

    assertThatMatrixContainsExactly(
        matrix.getDistancesInMeter(),
        new long[]{0, 1, 2},
        new long[]{10, 0, 1},
        new long[]{20, 10, 0});

    assertThatMatrixContainsExactly(
        matrix.getDurationsInSeconds(),
        new long[]{0, 2, 4},
        new long[]{20, 0, 2},
        new long[]{40, 20, 0}
    );

    var reconstructed = DirectionMatrixBuilder.of(matrix).build();

    assertThat(reconstructed.getLocations()).containsExactly(
        location1,
        location2,
        location3
    );

    assertThatMatrixContainsExactly(
        reconstructed.getDistancesInMeter(),
        new long[]{0, 1, 2},
        new long[]{10, 0, 1},
        new long[]{20, 10, 0}
    );

    assertThatMatrixContainsExactly(
        reconstructed.getDurationsInSeconds(),
        new long[]{0, 2, 4},
        new long[]{20, 0, 2},
        new long[]{40, 20, 0}
    );
  }
}
