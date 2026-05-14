package com.fieldcode.tesseract.matrix.cache;

import org.junit.jupiter.api.Test;

import com.fieldcode.tesseract.location.Locations;
import com.fieldcode.tesseract.matrix.DirectionMatrices;

import static org.assertj.core.api.Assertions.assertThat;

class HashTest extends DirectionMatrixTestSupport {

  private static final String EXPECTED_HASH__FOR_LOCATIONS_0_99 = "8b1ae8420a003f4584c86704d0ca7848fdf52de14c610908b5ada8680693d2a7";

  @Test
  void hash_Request_Success() {
    var locations = locations(100, i -> Locations.location(0, i));
    var hash = Hash.hash(locations);
    assertThat(hash).isEqualTo(EXPECTED_HASH__FOR_LOCATIONS_0_99);
  }

  @Test
  void hash_Matrix_Success() {
    var locations = locations(100, i -> Locations.location(0, i));
    var matrix = DirectionMatrices.pythagorean(locations);

    var hash = Hash.hash(matrix);
    assertThat(hash).isEqualTo(EXPECTED_HASH__FOR_LOCATIONS_0_99);
  }

  @Test
  void hash_DifferentInput_Success() {
    var locations1 = locations(100, i -> Locations.location(0, i));
    var locations2 = locations(101, i -> Locations.location(0, i));
    var hash1 = Hash.hash(locations1);
    var hash2 = Hash.hash(locations2);
    assertThat(hash1).isNotEqualTo(hash2);
  }

}
