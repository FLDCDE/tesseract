package com.fieldcode.tesseract.location;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.fieldcode.tesseract.Distance;
import com.fieldcode.tesseract.Location;

import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.function.BiPredicate;
import java.util.function.Function;
import lombok.Value;
import static com.fieldcode.tesseract.distance.Distances.ofMeters;
import static com.fieldcode.tesseract.location.Locations.location;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("LocationCluster Tests")
class LocationClusterTest {

  // --- Test Data Constants ---
  private static final Location BERLIN_CENTER = location("52.5200,13.4050");
  private static final Location POTSDAMER_PLATZ = location("52.5096,13.3739");
  private static final Location ALEXANDERPLATZ = location("52.5219,13.4132");
  private static final Location MUNICH_CENTER = location("48.1351,11.5820");

  private static final Distance CLUSTERING_RADIUS_3KM = ofMeters(3000);
  private static final BiPredicate<Location, Location> ALWAYS_SIMILAR = (a, b) -> true;
  private static final BiPredicate<Object, Object> NEVER_SIMILAR = (a, b) -> false;

  // --- Helper Classes for Testing ---
  @Value(staticConstructor = "of") // Using Lombok for NamedLocation
  private static class NamedLocation {

    String name;
    Location location;
  }

  @Value // Using Lombok for DummyItem
  private static class DummyItem {

    Location location;
  }

  // --- Test Cases ---

  @Nested
  @DisplayName("Spherical Clustering (Generic Type)")
  class SphericalGenericTests {

    @Test
    @DisplayName("should cluster similar and nearby locations together")
    void clusterSimilarAndNearby() {
      // Arrange
      var locations = List.of(BERLIN_CENTER, POTSDAMER_PLATZ, ALEXANDERPLATZ, MUNICH_CENTER);
      Function<Location, Location> identityMapper = loc -> loc;

      // Act
      var clusters = LocationCluster.spherical(
          locations, identityMapper, ALWAYS_SIMILAR, CLUSTERING_RADIUS_3KM
      );

      // Assert
      var clusterContents = clusters.asMap().values();

      assertThat(clusterContents)
          .as("Should form exactly 2 clusters")
          .hasSize(2)
          .anySatisfy(cluster -> assertThat(cluster)
              .containsExactlyInAnyOrder(BERLIN_CENTER, POTSDAMER_PLATZ, ALEXANDERPLATZ))
          .anySatisfy(cluster -> assertThat(cluster)
              .containsExactly(MUNICH_CENTER));
    }

    @Test
    @DisplayName("should not cluster similar but distant locations")
    void doNotClusterSimilarButDistant() {
      // Arrange
      var locations = List.of(BERLIN_CENTER, MUNICH_CENTER);
      Function<Location, Location> identityMapper = loc -> loc;

      // Act
      var clusters = LocationCluster.spherical(
          locations, identityMapper, ALWAYS_SIMILAR, CLUSTERING_RADIUS_3KM
      );

      // Assert
      var clusterContents = clusters.asMap().values();

      assertThat(clusterContents)
          .as("Should form 2 separate clusters for distant locations")
          .hasSize(2)
          .allSatisfy(cluster -> assertThat(cluster).hasSize(1));

      assertThat(clusters.values()).containsExactlyInAnyOrder(BERLIN_CENTER, MUNICH_CENTER);
    }

    @Test
    @DisplayName("should not cluster nearby but dissimilar locations")
    void doNotClusterNearbyButDissimilar() {
      // Arrange
      var locations = List.of(BERLIN_CENTER, ALEXANDERPLATZ);
      Function<Location, Location> identityMapper = loc -> loc;
      BiPredicate<Location, Location> neverSimilarPredicate = (a, b) -> false;

      // Act
      var clusters = LocationCluster.spherical(
          locations, identityMapper, neverSimilarPredicate, CLUSTERING_RADIUS_3KM
      );

      // Assert
      var clusterContents = clusters.asMap().values();

      assertThat(clusterContents)
          .as("Should form 2 separate clusters for dissimilar locations")
          .hasSize(2)
          .allSatisfy(cluster -> assertThat(cluster).hasSize(1));

      assertThat(clusters.values()).containsExactlyInAnyOrder(BERLIN_CENTER, ALEXANDERPLATZ);
    }

    @Test
    @DisplayName("should handle custom objects with location mapping")
    void clusterCustomObjects() {
      // Arrange
      var berlinNamed = NamedLocation.of("A-Berlin", BERLIN_CENTER);
      var potsdamNamed = NamedLocation.of("B-Potsdam", POTSDAMER_PLATZ);
      var munichNamed = NamedLocation.of("C-Munich", MUNICH_CENTER);
      var alexanderNamed = NamedLocation.of("D-Alexander", ALEXANDERPLATZ);

      var namedLocations = List.of(berlinNamed, potsdamNamed, munichNamed, alexanderNamed);
      BiPredicate<NamedLocation, NamedLocation> namedLocationAlwaysSimilar = (a, b) -> true;

      // Act
      // Use method reference to Lombok-generated getter
      var clusters = LocationCluster.spherical(
          namedLocations, NamedLocation::getLocation, namedLocationAlwaysSimilar, CLUSTERING_RADIUS_3KM
      );

      // Assert
      var clusterContents = clusters.asMap().values();

      assertThat(clusterContents)
          .as("Should form exactly 2 clusters for NamedLocation")
          .hasSize(2)
          .anySatisfy(cluster -> assertThat(cluster)
              .containsExactlyInAnyOrder(berlinNamed, potsdamNamed, alexanderNamed))
          .anySatisfy(cluster -> assertThat(cluster)
              .containsExactly(munichNamed));
    }
  }

  @Nested
  @DisplayName("Spherical Clustering (SortedSet<Location> Only)")
  class SphericalSortedSetTests {

    @Test
    @DisplayName("should cluster nearby locations from SortedSet")
    void clusterNearbyFromSortedSet() {
      // Arrange
      var locations = new TreeSet<Location>();
      locations.add(BERLIN_CENTER);
      locations.add(POTSDAMER_PLATZ);
      locations.add(MUNICH_CENTER);

      // Act
      var clusters = LocationCluster.spherical(
          locations, CLUSTERING_RADIUS_3KM
      );

      // Assert
      var clusterContents = clusters.asMap().values();

      assertThat(clusterContents)
          .as("Should form 2 clusters from SortedSet")
          .hasSize(2)
          .anySatisfy(cluster -> assertThat(cluster)
              .containsExactlyInAnyOrder(BERLIN_CENTER, POTSDAMER_PLATZ))
          .anySatisfy(cluster -> assertThat(cluster)
              .containsExactly(MUNICH_CENTER));
    }

    @Test
    @DisplayName("should handle empty SortedSet input")
    void clusterEmptySortedSet() {
      // Arrange
      var locations = new TreeSet<Location>();

      // Act
      var clusters = LocationCluster.spherical(
          locations, CLUSTERING_RADIUS_3KM
      );

      // Assert
      assertThat(clusters.isEmpty()).isTrue();
      assertThat(clusters.keySet()).isEmpty();
    }
  }

  @Nested
  @DisplayName("Error Handling")
  class ErrorHandlingTests {

    // Use the Lombok-generated getter in the method reference
    private final Function<DummyItem, Location> DUMMY_MAPPER = DummyItem::getLocation;
    private final BiPredicate<DummyItem, DummyItem> DUMMY_SIMILARITY = (a, b) -> true;

    @Test
    @DisplayName("should throw NullPointerException for null input collection")
    void throwNPEForNullInput() {
      assertThatThrownBy(() ->
          LocationCluster.spherical(
              null, DUMMY_MAPPER, DUMMY_SIMILARITY, CLUSTERING_RADIUS_3KM
          ))
          .isInstanceOf(NullPointerException.class)
          .hasMessageContaining("input collection must not be null");
    }

    @Test
    @DisplayName("should throw NullPointerException for null location mapper")
    void throwNPEForNullMapper() {
      var input = List.of(new DummyItem(BERLIN_CENTER));
      assertThatThrownBy(() ->
          LocationCluster.spherical(
              input, null, DUMMY_SIMILARITY, CLUSTERING_RADIUS_3KM
          ))
          .isInstanceOf(NullPointerException.class);
    }

    @Test
    @DisplayName("should throw NullPointerException for null similarity predicate")
    void throwNPEForNullSimilarity() {
      var input = List.of(new DummyItem(BERLIN_CENTER));
      assertThatThrownBy(() ->
          LocationCluster.spherical(
              input, DUMMY_MAPPER, null, CLUSTERING_RADIUS_3KM
          ))
          .isInstanceOf(NullPointerException.class)
          .hasMessageContaining("similarity predicate must not be null");
    }

    @Test
    @DisplayName("should throw NullPointerException for null radius")
    void throwNPEForNullRadius() {
      var input = List.of(new DummyItem(BERLIN_CENTER));
      assertThatThrownBy(() ->
          LocationCluster.spherical(
              input, DUMMY_MAPPER, DUMMY_SIMILARITY, null
          ))
          .isInstanceOf(NullPointerException.class);
    }

    @Test
    @DisplayName("should throw NullPointerException for null SortedSet input")
    void throwNPEForNullSortedSet() {
      assertThatThrownBy(() ->
          LocationCluster.spherical((SortedSet<Location>) null, CLUSTERING_RADIUS_3KM)
      )
          .isInstanceOf(NullPointerException.class)
          .hasMessageContaining("input collection must not be null");
    }
  }
}
