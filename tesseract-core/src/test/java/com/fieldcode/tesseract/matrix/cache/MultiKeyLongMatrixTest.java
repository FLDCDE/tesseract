package com.fieldcode.tesseract.matrix.cache;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("MultiKeyLongMatrix")
class MultiKeyLongMatrixTest extends MatrixTestSupport {

  private MultiKeyLongMatrix<String> matrix;

  @BeforeEach
  void setUp() throws Exception {

    // Arrange: a 3x2 matrix (rows a,b,c; columns c,d) with every cell but "c"x"d" set
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
  @DisplayName("should throw when setting a value for a row/column not in the matrix")
  void set_InvalidIndex_Exception() {

    // Act & Assert: setting an unknown row/column pair throws
    assertThatThrownBy(() -> matrix.set("d", "d", 1))
        .isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  @DisplayName("should throw when getting a value for a row/column not in the matrix")
  void get_InvalidIndex_Exception() {

    // Act & Assert: getting an unknown row/column pair throws
    assertThatThrownBy(() -> matrix.get("d", "d"))
        .isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  @DisplayName("should store values at their row/column position, defaulting unset cells to zero")
  void set_Success() {

    // Assert: the underlying array matches what was set in setUp(), with "c"x"d" left at 0
    assertThatMatrixContainsExactly(
        matrix.getMatrix(),
        new long[]{1, 2},
        new long[]{3, 4},
        new long[]{5, 0}
    );
  }

  @Test
  @DisplayName("should return the value stored for each row/column, or zero if unset")
  void get_Success() {

    // Act & Assert: every set cell returns its value, and the unset cell returns 0
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
