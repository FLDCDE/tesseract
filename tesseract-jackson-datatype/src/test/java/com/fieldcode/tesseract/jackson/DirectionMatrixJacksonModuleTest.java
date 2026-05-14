package com.fieldcode.tesseract.jackson;

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

class DirectionMatrixJacksonModuleTest {

  public static final Distance ONE_KILOMETER_INSTANCE = Distances.ofMeters(1000);

  private static final Duration TEN_MINUTES = Duration.ofMinutes(10);

  @Test
  void directionMatrix_SERDES_Success() {

    var locationOne = location(1, 2);
    var locationTwo = location(3, 4);

    var locations = Stream.of(locationOne, locationTwo).collect(Collectors.toCollection(TreeSet::new));

    var matrix = DirectionMatrices.pythagorean(locations, ONE_KILOMETER_INSTANCE, TEN_MINUTES);

    var json = Jsons.stringify(matrix, true);

    var parsed = Jsons.parse(json, DirectionMatrix.class);

    assertThat(matrix.getLocations()).isEqualTo(parsed.getLocations());
  }

  @Test
  void directionMatrixBuilder_SERDES_Success() {

    var directionMatrix = DirectionMatrixBuilder.of()
        .set(location(0, 0), location(0, 0), 0, 0)
        .set(location(0, 0), location(1, 0), 1, 2)
        .set(location(1, 0), location(0, 0), 10, 20)
        .set(location(1, 0), location(1, 0), 0, 0)
        .build();

    var json = Jsons.stringify(directionMatrix, true);

    var parsed = Jsons.parse(json, DirectionMatrix.class);

    assertThat(parsed.getLocations()).isEqualTo(directionMatrix.getLocations());
    assertThat(parsed.getDistancesInMeter()).isEqualTo(directionMatrix.getDistancesInMeter());
    assertThat(parsed.getDurationsInSeconds()).isEqualTo(directionMatrix.getDurationsInSeconds());
  }
}
