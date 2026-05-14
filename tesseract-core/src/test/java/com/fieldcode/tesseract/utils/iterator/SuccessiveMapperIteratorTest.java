package com.fieldcode.tesseract.utils.iterator;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SuccessiveMapperIteratorTest {

  @Test
  void iterate_Empty_Empty() {
    var empty = Iterators.<Integer>empty();
    var iterator = SuccessiveMapperIterator.of(empty, Integer::sum);
    assertThat(iterator)
        .toIterable()
        .isEmpty();
  }

  @Test
  void iterate_OneElement_Empty() {
    var empty = Iterators.iterator(1);
    var iterator = SuccessiveMapperIterator.of(empty, Integer::sum);
    assertThat(iterator)
        .toIterable()
        .isEmpty();
  }

  @Test
  void iterate_TwoElements_One() {
    var empty = Iterators.iterator(1, 2);

    var iterator = SuccessiveMapperIterator.of(empty, Integer::sum);

    assertThat(iterator)
        .toIterable()
        .containsExactly(3);
  }

  @Test
  void iterate_MoreElements_Success() {
    var empty = Iterators.iterator(1, 2, 3, 4, 5, 6, 7);

    var iterator = SuccessiveMapperIterator.of(empty, Integer::sum);

    assertThat(iterator)
        .toIterable()
        .containsExactly(3, 5, 7, 9, 11, 13);
  }

}
