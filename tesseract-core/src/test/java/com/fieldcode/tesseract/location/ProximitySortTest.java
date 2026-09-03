package com.fieldcode.tesseract.location;

import com.fieldcode.tesseract.Location;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("DistanceSorter")
class ProximitySortTest {

  private static final Location ORIGIN = Locations.location(0.0, 0.0);
  private static final Function<Location, Location> IDENTITY = Function.identity();

  private static class NamedLocation {
    final String name;
    final Location location;

    NamedLocation(String name, Location location) {
      this.name = name;
      this.location = location;
    }
  }

  @Test
  @DisplayName("returns empty stream for empty input")
  void sort_EmptyInput_ReturnsEmptyStream() {
    // Act
    var result = ProximitySort.sort(List.of(), ORIGIN, IDENTITY, LocationDistances::pythagoreanDistance, false);

    // Assert
    assertThat(result).isEmpty();
  }

  @Test
  @DisplayName("returns the single element unchanged")
  void sort_SingleElement_ReturnsSingleElement() {
    // Arrange
    var loc = Locations.location(1.0, 1.0);

    // Act
    var result = ProximitySort.sort(List.of(loc), ORIGIN, IDENTITY, LocationDistances::pythagoreanDistance, false);

    // Assert
    assertThat(result).containsExactly(loc);
  }

  @Test
  @DisplayName("sorts multiple elements nearest first")
  void sort_MultipleElements_SortsNearestFirst() {
    // Arrange
    var near = Locations.location(1.0, 1.0);
    var mid = Locations.location(3.0, 3.0);
    var far = Locations.location(5.0, 5.0);

    // Act
    var result = ProximitySort.sort(List.of(far, near, mid), ORIGIN, IDENTITY, LocationDistances::pythagoreanDistance, false);

    // Assert
    assertThat(result).containsExactly(near, mid, far);
  }

  @Test
  @DisplayName("sorts multiple elements farthest first when reversed")
  void sort_MultipleElements_SortsFarthestFirst() {
    // Arrange
    var near = Locations.location(1.0, 1.0);
    var mid = Locations.location(3.0, 3.0);
    var far = Locations.location(5.0, 5.0);

    // Act
    var result = ProximitySort.sort(List.of(near, far, mid), ORIGIN, IDENTITY, LocationDistances::pythagoreanDistance, true);

    // Assert
    assertThat(result).containsExactly(far, mid, near);
  }

  @Test
  @DisplayName("extracts location from item using mapper function")
  void sort_WithMapper_ExtractsLocationFromItem() {
    // Arrange
    var a = new NamedLocation("near", Locations.location(1.0, 0.0));
    var b = new NamedLocation("far", Locations.location(5.0, 0.0));

    // Act
    var result = ProximitySort.sort(List.of(b, a), ORIGIN, n -> n.location, LocationDistances::pythagoreanDistance, false);

    // Assert
    assertThat(result).extracting(n -> n.name).containsExactly("near", "far");
  }

  @Test
  @DisplayName("preserves relative order for equidistant elements")
  void sort_EquidistantElements_PreservesRelativeOrder() {
    // Arrange
    var east = Locations.location(0.0, 3.0);
    var north = Locations.location(3.0, 0.0);

    // Act
    var result = ProximitySort.sort(List.of(east, north), ORIGIN, IDENTITY, LocationDistances::pythagoreanDistance, false);

    // Assert
    assertThat(result).containsExactly(east, north);
  }

  @Test
  @DisplayName("uses the provided custom distance function")
  void sort_CustomResolver_UsesProvidedDistanceFunction() {
    // Arrange
    var a = Locations.location(1.0, 0.0);
    var b = Locations.location(0.0, 2.0);

    // Act
    var result = ProximitySort.sort(List.of(a, b), ORIGIN, IDENTITY, (ref, loc) -> Math.abs(loc.getLongitude() - ref.getLongitude()), false);

    // Assert
    assertThat(result).containsExactly(a, b);
  }

  @Test
  @DisplayName("computes distance from the given reference point")
  void sort_NonZeroReference_ComputesDistanceFromReference() {
    // Arrange
    var reference = Locations.location(10.0, 10.0);
    var close = Locations.location(11.0, 10.0);
    var far = Locations.location(15.0, 10.0);

    // Act
    var result = ProximitySort.sort(List.of(far, close), reference, IDENTITY, LocationDistances::pythagoreanDistance, false);

    // Assert
    assertThat(result).containsExactly(close, far);
  }

}
