package com.fieldcode.tesseract.matrix.cache;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MultiKeyLongMatrixTest extends MatrixTestSupport {

  private MultiKeyLongMatrix<String> matrix;

  @BeforeEach
  void setUp() throws Exception {
    var rows = List.of("a", "b", "c");
    var columns = List.of("c", "d");

    matrix = MultiKeyLongMatrix.of(rows, columns);

    matrix.set("a", "c", 1);
    matrix.set("a", "d", 2);
    matrix.set("b", "c", 3);
    matrix.set("b", "d", 4);
    matrix.set("c", "c", 5);
  }

  @Test
  void set_InvalidIndex_Exception() {
    assertThatThrownBy(() -> matrix.set("d", "d", 1))
        .isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  void get_InvalidIndex_Exception() {
    assertThatThrownBy(() -> matrix.get("d", "d"))
        .isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  void set_Success() {

    assertThatMatrixContainsExactly(
        matrix.getMatrix(),
        new long[]{1, 2},
        new long[]{3, 4},
        new long[]{5, 0}
    );
  }


  @Test
  void get_Success() {
    assertGet("a", "c", 1);
    assertGet("a", "d", 2);
    assertGet("b", "c", 3);
    assertGet("b", "d", 4);
    assertGet("c", "c", 5);
    assertGet("c", "d", 0);
  }

  private void assertGet(String row, String column, long expected) {
    assertThat(matrix.get(row, column)).isEqualTo(expected);
  }

}
