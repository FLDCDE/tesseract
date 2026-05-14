package com.fieldcode.tesseract.utils.iterator;

import org.junit.jupiter.api.Test;

import static com.fieldcode.tesseract.utils.iterator.ExtendedIterator.EMPTY_EXTENDED_ITERATOR_INSTANCE;
import static org.assertj.core.api.Assertions.assertThat;

class ExtendedIteratorFactoryBuilderTest {

  @Test
  void build_AllOperators_Success() {

    var interval = Iterators.iterator(0, 1, 2, 3, 4, 5, 6, 7, 8, 9);

    var factory = Iterators.factoryBuilder(Integer.class)
        .map(i -> i * 10)
        .filter(i -> i >= 20)
        .takeWhile(i -> i <= 50)
        .limit(3)
        .factory();

    assertThat(factory.apply(interval))
        .toIterable()
        .containsExactly(20, 30, 40);

  }

  @Test
  void build_CheckEmpty_Success() {

    var factory = Iterators.factoryBuilder(Integer.class)
        .map(i -> i * 10)
        .filter(i -> i >= 20)
        .takeWhile(i -> i <= 50)
        .limit(3)
        .factory();

    assertThat(factory.apply(Iterators.empty()))
        .isSameAs(EMPTY_EXTENDED_ITERATOR_INSTANCE);

  }

}
