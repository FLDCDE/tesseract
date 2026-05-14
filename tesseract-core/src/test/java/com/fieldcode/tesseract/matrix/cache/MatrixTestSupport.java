package com.fieldcode.tesseract.matrix.cache;

import org.assertj.core.api.Assertions;

import java.util.Arrays;
import java.util.stream.Collectors;

public class MatrixTestSupport {
  protected void assertThatMatrixContainsExactly(long[][] actual, long[]... arrays) {
    var actualAsList = Arrays.stream(actual).collect(Collectors.toList());
    var expectedAsList = Arrays.asList(arrays);
    Assertions.assertThat(actualAsList).containsExactlyElementsOf(expectedAsList);
  }
}
