package com.fieldcode.tesseract.utils.iterator;

import com.fieldcode.tesseract.test_utils.SignalTestSupport;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TakeWhileIteratorTest extends SignalTestSupport {

  @Test
  void iterate_Simple_Success() {
    var iterator = List.of(0, 1, 2, 3, 4, 5, 6, 7, 8, 9).iterator();

    var takeWhileIterator = TakeWhileIterator.of(iterator, i -> i < 5);

    assertThat(takeWhileIterator)
        .toIterable()
        .containsExactly(0, 1, 2, 3, 4);
  }

  @Test
  void iterate_Empty_Success() {
    //noinspection RedundantOperationOnEmptyContainer
    var iterator = Collections.<Integer>emptyIterator();

    var takeWhileIterator = TakeWhileIterator.of(iterator, i -> i < 5);

    assertThat(takeWhileIterator)
        .toIterable()
        .isEmpty();
  }

  @Test
  void iterate_All_Success() {
    var iterator = List.of(0, 1, 2, 3, 4, 5, 6, 7, 8, 9).iterator();

    var takeWhileIterator = TakeWhileIterator.of(iterator, i -> true);

    assertThat(takeWhileIterator)
        .toIterable()
        .containsExactly(0, 1, 2, 3, 4, 5, 6, 7, 8, 9);
  }

  @Test
  void iterate_FailedPredicate_ReturnEmpty() {

    var iterator = createSignalMap().signals();

    var takeWhileIterator = TakeWhileIterator.of(iterator, s -> s.getKey() < MIN);

    assertThat(takeWhileIterator)
        .toIterable()
        .isEmpty();
  }

  @Test
  void iterate_Next_NoSuchElementException() {
    var iterator = Collections.<Integer>emptyIterator();
    var takeWhileIterator = TakeWhileIterator.of(iterator, i -> i < 5);
    assertThatThrownBy(takeWhileIterator::next).isInstanceOf(NoSuchElementException.class);
  }


}
