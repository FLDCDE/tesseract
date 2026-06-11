package com.fieldcode.tesseract.location;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.fieldcode.tesseract.DirectionMatrixResolver;
import com.fieldcode.tesseract.Location;
import com.fieldcode.tesseract.distance.Distances;

import java.util.List;
import java.util.function.Function;
import static com.fieldcode.tesseract.location.Locations.location;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.data.Percentage.withPercentage;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@DisplayName("LocationDistances")
class LocationDistancesTest {

  private static final Location FAR = location(5.0, 5.0);
  private static final Location NEAR = location(1.0, 1.0);
  private static final Location ORIGIN = location(0.0, 0.0);
  private static final Function<Location, Location> IDENTITY = Function.identity();

  @Test
  @DisplayName("calculates correct spherical distance between two locations")
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
  @DisplayName("throws exception when origin is null")
  void sphericalDistance_shouldThrowException_whenOriginIsNull() {
    var paris = location("48.8566,2.3522");

    assertThatThrownBy(() -> LocationDistances.sphericalDistance(null, paris))
        .isInstanceOf(NullPointerException.class);
  }

  @Test
  @DisplayName("throws exception when destination is null")
  void sphericalDistance_shouldThrowException_whenDestinationIsNull() {
    var berlin = location("52.5200,13.4050");

    assertThatThrownBy(() -> LocationDistances.sphericalDistance(berlin, null))
        .isInstanceOf(NullPointerException.class)
        .hasMessage("The destination location must not be null.");
  }

  @Test
  @DisplayName("sort: sorts items by custom distance function")
  void sort_sortsItemsByCustomDistanceFunction() {
    // Arrange
    var items = List.of(FAR, NEAR);

    // Act
    var result = LocationDistances.sort(items, ORIGIN, IDENTITY, LocationDistances::pythagoreanDistance, false);

    // Assert
    assertThat(result).containsExactly(NEAR, FAR);
  }

  @Test
  @DisplayName("sortByNearest: sorts items nearest first using resolver")
  void sortByNearest_sortsNearestFirstUsingResolver() {
    // Arrange
    var resolver = mock(DirectionMatrixResolver.class);
    when(resolver.getDistance(any(), any())).thenAnswer(invocation -> {
      Location a = invocation.getArgument(0);
      Location b = invocation.getArgument(1);
      return Distances.ofMeters((long) LocationDistances.pythagoreanDistance(a, b));
    });

    // Act
    var result = LocationDistances.sortByNearest(List.of(FAR, NEAR), ORIGIN, IDENTITY, resolver);

    // Assert
    assertThat(result).containsExactly(NEAR, FAR);
  }

  @Test
  @DisplayName("sortByNearestPythagorean: sorts items nearest first")
  void sortByNearestPythagorean_sortsNearestFirst() {
    // Act
    var result = LocationDistances.sortByNearestPythagorean(List.of(FAR, NEAR), ORIGIN, IDENTITY);

    // Assert
    assertThat(result).containsExactly(NEAR, FAR);
  }

  @Test
  @DisplayName("sortByNearestSpherical: sorts items nearest first")
  void sortByNearestSpherical_sortsNearestFirst() {
    // Act
    var result = LocationDistances.sortByNearestSpherical(List.of(FAR, NEAR), ORIGIN, IDENTITY);

    // Assert
    assertThat(result).containsExactly(NEAR, FAR);
  }

  @Test
  @DisplayName("sortByFarthest: sorts items farthest first using resolver")
  void sortByFarthest_sortsFarthestFirstUsingResolver() {
    // Arrange
    var resolver = mock(DirectionMatrixResolver.class);
    when(resolver.getDistance(any(), any()))
        .thenAnswer(invocation ->
            {
              Location a = invocation.getArgument(0);
              Location b = invocation.getArgument(1);
              return Distances.ofMeters((long) LocationDistances.pythagoreanDistance(a, b));
            }
        );

    // Act
    var result = LocationDistances.sortByFarthest(List.of(NEAR, FAR), ORIGIN, IDENTITY, resolver);

    // Assert
    assertThat(result).containsExactly(FAR, NEAR);
  }

  @Test
  @DisplayName("sortByFarthestPythagorean: sorts items farthest first")
  void sortByFarthestPythagorean_sortsFarthestFirst() {
    // Act
    var result = LocationDistances.sortByFarthestPythagorean(List.of(NEAR, FAR), ORIGIN, IDENTITY);

    // Assert
    assertThat(result).containsExactly(FAR, NEAR);
  }

  @Test
  @DisplayName("sortByFarthestSpherical: sorts items farthest first")
  void sortByFarthestSpherical_sortsFarthestFirst() {
    // Act
    var result = LocationDistances.sortByFarthestSpherical(List.of(NEAR, FAR), ORIGIN, IDENTITY);

    // Assert
    assertThat(result).containsExactly(FAR, NEAR);
  }

}
