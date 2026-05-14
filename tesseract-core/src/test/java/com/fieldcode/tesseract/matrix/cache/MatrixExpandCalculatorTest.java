package com.fieldcode.tesseract.matrix.cache;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.TreeSet;
import static com.fieldcode.tesseract.matrix.DirectionMatrices.dimensions;
import static org.assertj.core.api.Assertions.assertThat;

class MatrixExpandCalculatorTest extends DirectionMatrixTestSupport {

  @Test
  void calculate() {

    var l = generateLocationArray(10);

    var original = new TreeSet<>(List.of(l[1], l[5]));
    var required = new TreeSet<>(List.of(l[0], l[1], l[2], l[3], l[4], l[5], l[6], l[7]));
    var missing = new TreeSet<>(List.of(l[0], l[2], l[3], l[4], l[6], l[7]));

    var requests = MatrixExpandCalculator.calculate(original, required);

    var expected = List.of(
        dimensions(missing, original),
        dimensions(original, missing),
        dimensions(missing, missing)
    );

    assertThat(requests).containsExactlyElementsOf(expected);
  }


}
