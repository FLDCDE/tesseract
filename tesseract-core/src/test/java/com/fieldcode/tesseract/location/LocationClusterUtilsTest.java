package com.fieldcode.tesseract.location;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

import com.fieldcode.tesseract.Location;
import com.google.common.collect.ImmutableMultimap;

import java.util.Map;
import java.util.function.BiPredicate;
import java.util.function.Function;
import static com.fieldcode.tesseract.location.Locations.location;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("LocationClusterUtils")
class LocationClusterUtilsTest {

  // Location constants for better readability
  private static final Location LOC_1 = location("0.0,0.0");
  private static final Location LOC_2 = location("0.0,1.0");
  private static final Location LOC_3 = location("0.0,2.0");
  private static final Location LOC_4 = location("0.0,3.0");

  private ImmutableMultimap<Integer, Location> validClusters;

  @BeforeEach
  void setUp(TestInfo testInfo) {
    // Arrange
    System.out.println("Running: " + testInfo.getDisplayName());
    validClusters = ImmutableMultimap.<Integer, Location>builder()
        .put(0, LOC_1)
        .put(0, LOC_2)
        .put(1, LOC_3)
        .put(1, LOC_4)
        .build();
  }

  @Test
  @DisplayName("invert() should successfully invert valid clusters")
  void invert_Success() {
    // Act
    var inverted = LocationClusterUtils.invert(validClusters);

    // Assert
    assertThat(inverted)
        .as("Inverted map should contain all locations with correct cluster IDs")
        .containsExactlyInAnyOrderEntriesOf(
            Map.of(
                LOC_1, 0,
                LOC_2, 0,
                LOC_3, 1,
                LOC_4, 1
            )
        );
  }

  @Test
  @DisplayName("validate() should reject clusters with duplicate locations")
  void validate_Duplicates_Exception() {
    // Arrange
    var invalidClusters = ImmutableMultimap.<Integer, Location>builder()
        .put(0, LOC_1)
        .put(1, LOC_1)  // Duplicate location
        .build();

    // Act & Assert
    assertThatThrownBy(() -> LocationClusterUtils.validate(invalidClusters))
        .as("Should throw exception when a location appears in multiple clusters")
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("Duplicate locations found");
  }

  @Test
  @DisplayName("validate() should reject null input")
  void validate_Null_Exception() {
    // Act & Assert
    assertThatThrownBy(() -> LocationClusterUtils.validate(null))
        .as("Should throw exception with null input")
        .isInstanceOf(NullPointerException.class)
        .hasMessageContaining("must not be null");
  }

  @Test
  @DisplayName("neighbors() should identify locations in the same cluster as neighbors")
  void neighbors_Success() {
    // Act
    var neighbors = LocationClusterUtils.neighbors(validClusters);

    // Assert
    assertThat(neighbors.test(LOC_1, LOC_2))
        .as("Locations in the same cluster should be neighbors")
        .isTrue();

    assertThat(neighbors.test(LOC_1, LOC_3))
        .as("Locations in different clusters should not be neighbors")
        .isFalse();

    assertThat(neighbors.test(LOC_3, LOC_4))
        .as("Locations in the same cluster should be neighbors")
        .isTrue();
  }

  @Test
  @DisplayName("neighbors() should handle unknown locations gracefully")
  void neighbors_UnknownLocations() {
    // Arrange
    var unknownLocation = location("99.0,99.0");

    // Act
    var neighbors = LocationClusterUtils.neighbors(validClusters);

    // Assert
    assertThat(neighbors.test(LOC_1, unknownLocation))
        .as("Known and unknown locations should not be neighbors")
        .isFalse();

    assertThat(neighbors.test(unknownLocation, unknownLocation))
        .as("Unknown locations should not be neighbors")
        .isFalse();
  }

  @Test
  @DisplayName("neighbors() with mapper and similarity should identify elements in the same cluster as neighbors")
  void neighbors_WithMapperAndSimilarity_Success() {
    // Arrange
    var mapper = (Function<Location, Location>) location -> location;
    var similarity = (BiPredicate<Location, Location>) (a, b) -> true;

    // Act
    var neighbors = LocationClusterUtils.neighbors(validClusters, mapper, similarity);

    // Assert
    assertThat(neighbors.test(LOC_1, LOC_2))
        .as("Elements in the same cluster should be neighbors")
        .isTrue();

    assertThat(neighbors.test(LOC_1, LOC_3))
        .as("Elements in different clusters should not be neighbors")
        .isFalse();

    assertThat(neighbors.test(LOC_3, LOC_4))
        .as("Elements in the same cluster should be neighbors")
        .isTrue();
  }

  @Test
  @DisplayName("neighbors() with mapper and similarity should handle unknown elements gracefully")
  void neighbors_WithMapperAndSimilarity_UnknownElements() {
    // Arrange
    var unknownLocation = location("99.0,99.0");
    var mapper = (Function<Location, Location>) location -> location;
    var similarity = (BiPredicate<Location, Location>) (a, b) -> true;

    // Act
    var neighbors = LocationClusterUtils.neighbors(validClusters, mapper, similarity);

    // Assert
    assertThat(neighbors.test(LOC_1, unknownLocation))
        .as("Known and unknown elements should not be neighbors")
        .isFalse();

    assertThat(neighbors.test(unknownLocation, unknownLocation))
        .as("Unknown elements should not be neighbors")
        .isFalse();
  }

  @Test
  @DisplayName("neighbors() with mapper and similarity should respect the similarity predicate")
  void neighbors_WithMapperAndSimilarity_RespectSimilarity() {
    // Arrange
    var mapper = (Function<Location, Location>) location -> location;
    var similarity = (BiPredicate<Location, Location>) (a, b) -> a.equals(LOC_1) && b.equals(LOC_2);

    // Act
    var neighbors = LocationClusterUtils.neighbors(validClusters, mapper, similarity);

    // Assert
    assertThat(neighbors.test(LOC_1, LOC_2))
        .as("Elements in the same cluster and matching similarity predicate should be neighbors")
        .isTrue();

    assertThat(neighbors.test(LOC_1, LOC_3))
        .as("Elements in different clusters should not be neighbors")
        .isFalse();

    assertThat(neighbors.test(LOC_3, LOC_4))
        .as("Elements in the same cluster but not matching similarity predicate should not be neighbors")
        .isFalse();
  }


}
