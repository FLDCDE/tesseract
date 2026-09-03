package com.fieldcode.tesseract.matrix.cache;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.fieldcode.tesseract.location.Locations;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Hash")
class HashTest extends DirectionMatrixTestSupport {

  private static final String EXPECTED_HASH__FOR_LOCATIONS_0_99 = "8b1ae8420a003f4584c86704d0ca7848fdf52de14c610908b5ada8680693d2a7";

  @Test
  @DisplayName("should hash a location set to a stable, known value")
  void hash_Request_Success() {

    // Arrange: a fixed set of 100 locations
    var locations = locations(100, i -> Locations.location(0, i));

    // Act: hash the location set
    var hash = Hash.hash(locations);

    // Assert: the hash matches the known value for this exact input
    assertThat(hash).isEqualTo(EXPECTED_HASH__FOR_LOCATIONS_0_99);
  }

  @Test
  @DisplayName("should hash different location sets to different values")
  void hash_DifferentInput_Success() {

    // Arrange: two location sets of different sizes
    var locations1 = locations(100, i -> Locations.location(0, i));
    var locations2 = locations(101, i -> Locations.location(0, i));

    // Act: hash both sets
    var hash1 = Hash.hash(locations1);
    var hash2 = Hash.hash(locations2);

    // Assert: the hashes differ
    assertThat(hash1).isNotEqualTo(hash2);
  }

}
