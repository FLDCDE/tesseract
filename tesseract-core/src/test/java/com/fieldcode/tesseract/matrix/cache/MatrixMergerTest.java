package com.fieldcode.tesseract.matrix.cache;

import org.junit.jupiter.api.Test;

import com.fieldcode.tesseract.Location;
import com.fieldcode.tesseract.matrix.DirectionMatrices;

import java.util.List;
import java.util.TreeSet;
import java.util.stream.Stream;
import static org.assertj.core.api.Assertions.assertThat;

class MatrixMergerTest extends DirectionMatrixTestSupport {


  @Test
  void merge() {

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

    var result = MatrixMerger.merge(elements);

    var expectedLocations = List.of(l[0], l[1], l[2], l[3], l[4]);
    var expected = DirectionMatrices.pythagorean(expectedLocations);

    assertThat(result.getOrigins()).isEqualTo(expected.getLocations());
    assertThat(result.getDestinations()).isEqualTo(expected.getLocations());
    assertThat(result.getDistancesInMeter()).isEqualTo(expected.getDistancesInMeter());
    assertThat(result.getDurationsInSeconds()).isEqualTo(expected.getDurationsInSeconds());

  }

}
