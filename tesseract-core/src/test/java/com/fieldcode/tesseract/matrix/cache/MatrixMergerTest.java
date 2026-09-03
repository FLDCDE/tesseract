package com.fieldcode.tesseract.matrix.cache;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.fieldcode.tesseract.Location;
import com.fieldcode.tesseract.PartialDirectionMatrix;
import com.fieldcode.tesseract.matrix.DirectionMatrices;

import java.util.List;
import java.util.TreeSet;
import java.util.stream.Stream;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("MatrixMerger")
class MatrixMergerTest extends DirectionMatrixTestSupport {

  @Test
  @DisplayName("should merge row, column and corner matrices into one full matrix")
  void merge() {

    // Arrange: an original matrix plus the row, column and corner matrices needed
    // to extend it to five locations
    var l = generateLocationArray(5);

    var originalLocations = List.of(
        l[0],
        l[1],
        l[2]
    );

    var missingLocations = Stream.of(
            l[3],
            l[4]
        )
        .collect(TreeSet<Location>::new, TreeSet::add, TreeSet::addAll);

    var originalMatrix = DirectionMatrices.pythagorean(originalLocations);

    var missingRowMatrix = DirectionMatrices.pythagorean(missingLocations, originalLocations);
    var missingColumnMatrix = DirectionMatrices.pythagorean(originalLocations, missingLocations);
    var missingMatrix = DirectionMatrices.pythagorean(missingLocations);

    var elements = List.of(
        originalMatrix,
        missingRowMatrix,
        missingColumnMatrix,
        missingMatrix
    );

    // Act: merge the four matrices into one
    var result = MatrixMerger.merge(elements);

    // Assert: the merged matrix matches a full matrix computed directly over all five locations
    var expectedLocations = List.of(l[0], l[1], l[2], l[3], l[4]);
    var expected = DirectionMatrices.pythagorean(expectedLocations);

    assertThat(result.getOrigins()).isEqualTo(expected.getLocations());
    assertThat(result.getDestinations()).isEqualTo(expected.getLocations());
    assertThat(result.getDistancesInMeter()).isEqualTo(expected.getDistancesInMeter());
    assertThat(result.getDurationsInSeconds()).isEqualTo(expected.getDurationsInSeconds());

  }

  @Test
  @DisplayName("should throw when the merged matrices do not cover every origin-destination pair")
  void merge_IncompleteCoverage_Exception() {

    // Arrange: an origin-only matrix and a destination-only matrix, with no matrix
    // supplied for the cross pairs between them (original x missing, missing x original)
    var l = generateLocationArray(5);

    var originalLocations = List.of(l[0], l[1], l[2]);
    var missingLocations = Stream.of(l[3], l[4])
        .collect(TreeSet<Location>::new, TreeSet::add, TreeSet::addAll);

    var originalMatrix = DirectionMatrices.pythagorean(originalLocations);
    var missingMatrix = DirectionMatrices.pythagorean(missingLocations);

    var elements = List.<PartialDirectionMatrix>of(originalMatrix, missingMatrix);

    // Act & Assert: merging throws because the cross pairs are never covered
    assertThatThrownBy(() -> MatrixMerger.merge(elements))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("Not all cells are covered");
  }

}
