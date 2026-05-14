package com.fieldcode.tesseract.utils.iterator;

import java.util.NoSuchElementException;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ChainIteratorTest {

  @Test
  void iterate_Simple_Success() {
    var iterator = ChainIterator.of(0, i -> i >= 5 ? null : i + 1);
    assertThat(iterator).toIterable()
        .containsExactly(0, 1, 2, 3, 4, 5);
  }

  @Test
  void next_Empty_Empty() {
    var iterator = ChainIterator.of(0, i -> null);
    assertThat(iterator)
        .toIterable()
        .containsExactly(0);
  }

  @Test
  void next_Empty_Exception() {
    var iterator = ChainIterator.of(0, i -> i >= 5 ? null : i + 1);
    assertThat(iterator).toIterable()
        .containsExactly(0, 1, 2, 3, 4, 5);
    assertThatThrownBy(iterator::next).isInstanceOf(NoSuchElementException.class);
    assertThat(iterator.hasNext()).isFalse();
  }

}
