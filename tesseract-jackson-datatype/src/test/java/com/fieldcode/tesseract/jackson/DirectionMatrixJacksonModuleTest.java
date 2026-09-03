package com.fieldcode.tesseract.jackson;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.fieldcode.tesseract.DirectionMatrix;
import com.fieldcode.tesseract.Distance;
import com.fieldcode.tesseract.distance.Distances;
import com.fieldcode.tesseract.jackson.test_utils.Jsons;
import com.fieldcode.tesseract.matrix.DirectionMatrices;
import com.fieldcode.tesseract.matrix.DirectionMatrixBuilder;

import java.time.Duration;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import static com.fieldcode.tesseract.location.Locations.location;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("DirectionMatrixJacksonModule")
class DirectionMatrixJacksonModuleTest {

  public static final Distance ONE_KILOMETER_INSTANCE = Distances.ofMeters(1000);

  private static final Duration TEN_MINUTES = Duration.ofMinutes(10);

  @Test
  @DisplayName("should preserve locations when a pythagorean direction matrix is serialized and deserialized")
  void directionMatrix_Clone_Success() {

    // Arrange: a pythagorean direction matrix over two locations with a fixed distance and duration
    var locationOne = location(1, 2);
    var locationTwo = location(3, 4);

    var locations = Stream.of(locationOne, locationTwo).collect(Collectors.toCollection(TreeSet::new));

    var matrix = DirectionMatrices.pythagorean(locations, ONE_KILOMETER_INSTANCE, TEN_MINUTES);

    // Act: serialize the matrix to JSON and parse it back into a DirectionMatrix
    var json = Jsons.stringify(matrix, true);

    var parsed = Jsons.parse(json, DirectionMatrix.class);

    // Assert: the round-tripped matrix has the same locations as the original
    assertThat(matrix.getLocations())
        .isEqualTo(parsed.getLocations());
  }

  @Test
  @DisplayName("should preserve locations, distances, and durations when a builder-created direction matrix is serialized and deserialized")
  void directionMatrixBuilder_Clone_Success() {

    // Arrange: a direction matrix built with distances and durations set between two locations
    var directionMatrix = DirectionMatrixBuilder.of()
        .set(location(0, 0), location(0, 0), 0, 0)
        .set(location(0, 0), location(1, 0), 1, 2)
        .set(location(1, 0), location(0, 0), 10, 20)
        .set(location(1, 0), location(1, 0), 0, 0)
        .build();

    // Act: serialize the matrix to JSON and parse it back into a DirectionMatrix
    var json = Jsons.stringify(directionMatrix, true);

    var parsed = Jsons.parse(json, DirectionMatrix.class);

    // Assert: the round-tripped matrix has the same locations, distances, and durations as the original
    assertThat(parsed.getLocations())
        .isEqualTo(directionMatrix.getLocations());
    assertThat(parsed.getDistancesInMeter())
        .isEqualTo(directionMatrix.getDistancesInMeter());
    assertThat(parsed.getDurationsInSeconds())
        .isEqualTo(directionMatrix.getDurationsInSeconds());
  }
}
