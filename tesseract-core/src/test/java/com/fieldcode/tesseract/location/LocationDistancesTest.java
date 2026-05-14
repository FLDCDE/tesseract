package com.fieldcode.tesseract.location;

import org.junit.jupiter.api.Test;

import static com.fieldcode.tesseract.location.Locations.location;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.data.Percentage.withPercentage;

class LocationDistancesTest {

  @Test
  void sphericalDistance_shouldCalculateCorrectDistance() {
    // Arrange
    var berlin = location("52.5200,13.4050");     // Berlin
    var paris = location("48.8566,2.3522");       // Paris
    var expectedDistance = 878_000L;              // ~878km

    // Act
    var distance = LocationDistances.sphericalDistance(berlin, paris);

    // Assert
    assertThat(distance.toMeters())
        .isCloseTo(expectedDistance, withPercentage(1.0));
  }

  @Test
  void sphericalDistance_shouldThrowException_whenOriginIsNull() {
    var paris = location("48.8566,2.3522");

    assertThatThrownBy(() -> LocationDistances.sphericalDistance(null, paris))
        .isInstanceOf(NullPointerException.class);
  }

  @Test
  void sphericalDistance_shouldThrowException_whenDestinationIsNull() {
    var berlin = location("52.5200,13.4050");

    assertThatThrownBy(() -> LocationDistances.sphericalDistance(berlin, null))
        .isInstanceOf(NullPointerException.class)
        .hasMessage("The destination location must not be null.");
  }

}
