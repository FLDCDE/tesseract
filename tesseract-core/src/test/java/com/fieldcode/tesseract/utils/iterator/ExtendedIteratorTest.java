package com.fieldcode.tesseract.utils.iterator;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ExtendedIteratorTest {

  @Test
  void empty_Empty_Empty() {
    var iterator = Iterators.empty();

    assertThat(iterator)
        .toIterable()
        .isEmpty();
  }

  @Test
  void filter_Success() {

    var iterator = Iterators
        .iterator(0, 1, 2, 3, 4, 5)
        .filter(i -> i < 3);

    assertThat(iterator)
        .toIterable()
        .containsExactly(0, 1, 2);
  }

  @Test
  void map_Success() {
    var iterator = Iterators
        .iterator(0, 1, 2, 3, 4, 5)
        .map(i -> i + 1);

    assertThat(iterator)
        .toIterable()
        .containsExactly(1, 2, 3, 4, 5, 6);
  }

  @Test
  void mapAndFilter_Success() {
    var iterator = Iterators
        .iterator(0, 1, 2, 3, 4, 5)
        .map(i -> i * 2)
        .filter(i -> i < 5);

    assertThat(iterator)
        .toIterable()
        .containsExactly(0, 2, 4);
  }

  @Test
  void takeWhile_Success() {
    var iterator = Iterators
        .iterator(0, 1, 2, 3, 4, 5)
        .takeWhile(i -> i <= 2);

    assertThat(iterator)
        .toIterable()
        .containsExactly(0, 1, 2);
  }

  @Test
  void limit_Success() {
    var iterator = Iterators
        .iterator(0, 1, 2, 3, 4, 5)
        .limit(3);

    assertThat(iterator)
        .toIterable()
        .containsExactly(0, 1, 2);
  }

  @Test
  void findFirst_Success() {
    var iterator = Iterators
        .iterator(0, 1, 2, 3, 4, 5)
        .filter(i -> i >= 3)
        .limit(3)
        .findFirst();

    assertThat(iterator)
        .contains(3);
  }

  @Test
  void list_Success() {
    var iterator = Iterators
        .iterator(0, 1, 2, 3, 4, 5);

    assertThat(iterator.list())
        .containsExactly(0, 1, 2, 3, 4, 5);
  }

  @Test
  void concat_Success() {
    var iterator1 = Iterators
        .iterator(0, 1, 2);

    var iterator2 = Iterators
        .iterator(3, 4, 5);

    var iterator3 = Iterators
        .iterator(6, 7, 8);

    assertThat(Iterators.concat(iterator1, iterator2, iterator3))
        .toIterable()
        .containsExactly(0, 1, 2, 3, 4, 5, 6, 7, 8);
  }

}
