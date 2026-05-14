package com.fieldcode.tesseract.utils.iterator;

import java.util.List;
import java.util.NoSuchElementException;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CompositeLazyIteratorTest {

  @Test
  void create_ThreeIterators_Success() {

    var list1 = List.of(1, 2);
    var list2 = List.of(3, 4);
    var list3 = List.of(5, 6);

    var iterator = CompositeLazyIterator.<Integer>of(List.of(list1::iterator, list2::iterator, list3::iterator));

    assertThat(iterator)
        .toIterable()
        .containsExactly(1, 2, 3, 4, 5, 6);
  }

  @Test
  void create_Empty_Success() {
    var iterator = CompositeLazyIterator.<Integer>of(List.of());

    assertThat(iterator)
        .toIterable()
        .isEmpty();
  }

  @Test
  void hasNext_EmptyMultipleCall_Success() {
    var iterator = CompositeLazyIterator.<Integer>of(List.of());

    assertThat(iterator.hasNext()).isFalse();
    //noinspection ConstantValue
    assertThat(iterator.hasNext()).isFalse();

  }

  @Test
  void hasNext_NonEmptyMultipleCall_Success() {
    var list = List.of(1);
    var iterator = CompositeLazyIterator.<Integer>of(List.of(list::iterator));

    iterator.next();

    assertThat(iterator.hasNext()).isFalse();
    //noinspection ConstantValue
    assertThat(iterator.hasNext()).isFalse();

  }

  @Test
  void next_Empty_Exception() {
    var iterator = CompositeLazyIterator.<Integer>of(List.of());
    assertThatThrownBy(iterator::next).isInstanceOf(NoSuchElementException.class);
  }

  @Test
  void next_AfterLast_Exception() {
    var list = List.of(1);
    var iterator = CompositeLazyIterator.<Integer>of(List.of(list::iterator));
    iterator.next();
    assertThatThrownBy(iterator::next).isInstanceOf(NoSuchElementException.class);
  }

}
