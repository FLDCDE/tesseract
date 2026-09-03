package com.fieldcode.tesseract.matrix.cache;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.TreeSet;
import static com.fieldcode.tesseract.matrix.DirectionMatrices.dimensions;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("MatrixExpandCalculator")
class MatrixExpandCalculatorTest extends DirectionMatrixTestSupport {

  @Test
  @DisplayName("should compute the row, column and corner dimensions needed to cover the missing locations")
  void calculate() {

    // Arrange: an original two-location matrix, a wider set of required locations,
    // and the locations missing between the two
    var l = generateLocationArray(10);

    var original = new TreeSet<>(List.of(l[1], l[5]));
    var required = new TreeSet<>(List.of(l[0], l[1], l[2], l[3], l[4], l[5], l[6], l[7]));
    var missing = new TreeSet<>(List.of(l[0], l[2], l[3], l[4], l[6], l[7]));

    // Act: calculate the expansion dimensions
    var requests = MatrixExpandCalculator.calculate(original, required);

    // Assert: the row (missing x original), column (original x missing) and
    // corner (missing x missing) dimensions are produced, in that order
    var expected = List.of(
        dimensions(missing, original),
        dimensions(original, missing),
        dimensions(missing, missing)
    );

    assertThat(requests).containsExactlyElementsOf(expected);
  }

}
