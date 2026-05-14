package com.fieldcode.tesseract.utils.iterator;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SkipIteratorTest {

  @Test
  void skip_Empty_Empty() {

    var iterator = Iterators.empty()
        .skip(10);

    assertThat(iterator)
        .toIterable()
        .isEmpty();
  }

  @Test
  void skip_SkippMoreElements_Empty() {

    var iterator = Iterators.iterator(1, 2, 3)
        .skip(10);

    assertThat(iterator)
        .toIterable()
        .isEmpty();
  }

  @Test
  void skip_SkipAllElements_Empty() {

    var iterator = Iterators.iterator(1, 2, 3)
        .skip(3);

    assertThat(iterator)
        .toIterable()
        .isEmpty();
  }

  @Test
  void skip_SkipLessElements_Empty() {

    var iterator = Iterators.iterator(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
        .skip(5);

    assertThat(iterator)
        .toIterable()
        .containsExactly(6, 7, 8, 9, 10);
  }

  @Test
  void skip_SkipZeroElements_Empty() {

    var iterator = Iterators.iterator(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
        .skip(0);

    assertThat(iterator)
        .toIterable()
        .containsExactly(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
  }

  @Test
  void skip_SkipNegativeElements_Exception() {
    assertThatThrownBy(() -> Iterators.empty().skip(-1))
        .isInstanceOf(IllegalArgumentException.class);
  }

}
