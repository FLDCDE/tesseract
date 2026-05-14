package com.fieldcode.tesseract.collection;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.fieldcode.tesseract.FluentIterator;
import com.fieldcode.tesseract.Moment;
import com.fieldcode.tesseract.Signal;
import com.fieldcode.tesseract.signal.Signals;
import com.fieldcode.tesseract.test_utils.TimeTestSupport;
import com.fieldcode.tesseract.utils.iterator.Iterators;

import static com.fieldcode.tesseract.QueryDirection.BACKWARD;
import static com.fieldcode.tesseract.QueryDirection.FORWARD;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("Boundary merge iterator operations")
class BoundaryMergeIteratorTest extends TimeTestSupport {


  private Signal<Moment, Boolean> signal(int hour, boolean value) {
    return Signals.signal(moment(hour), value);
  }

  @Test
  @DisplayName("Should successfully merge forward-ordered boundary signals")
  void iterator_SimpleValuesForward_Success() {

    var signals = Iterators.iterator(
        signal(0, true),
        signal(5, false),
        signal(10, true),
        signal(15, false),
        signal(20, true),
        signal(25, false),
        signal(25, true),
        signal(30, false)

    );

    var merged = BoundaryMergeIterator.fluent(signals, FORWARD).list();

    assertThat(merged)
        .containsExactly(
            signal(0, true),
            signal(5, false),
            signal(10, true),
            signal(15, false),
            signal(20, true),
            signal(30, false)
        );
  }

  @Test
  @DisplayName("Should successfully merge backward-ordered boundary signals")
  void iterator_SimpleValuesBackward_Success() {

    var signals = Iterators.iterator(
        signal(15, false),
        signal(10, true),
        signal(10, false),
        signal(10, true),
        signal(5, false),
        signal(0, true)
    );

    var merged = BoundaryMergeIterator.fluent(signals, BACKWARD).list();

    assertThat(merged)
        .containsExactly(
            signal(15, false),
            signal(10, true),
            signal(5, false),
            signal(0, true)
        );
  }

  @Test
  @DisplayName("Should return empty result when input is empty in forward direction")
  void iterator_EmptyForward_Empty() {

    FluentIterator<Signal<Moment, Boolean>> signals = Iterators.empty();

    var merged = BoundaryMergeIterator.fluent(signals, FORWARD).list();

    assertThat(merged)
        .isEmpty();
  }

  @Test
  @DisplayName("Should return empty result when input is empty in backward direction")
  void iterator_EmptyBackward_Empty() {

    FluentIterator<Signal<Moment, Boolean>> signals = Iterators.empty();

    var merged = BoundaryMergeIterator.fluent(signals, BACKWARD).list();

    assertThat(merged)
        .isEmpty();
  }

  @Test
  @DisplayName("Should throw exception when signals are in wrong order for forward iteration")
  void iterator_WrongOrderForward_Exception() {

    var signals = Iterators.iterator(
        signal(15, false),
        signal(10, true),
        signal(10, false),
        signal(10, true),
        signal(5, false),
        signal(0, true),
        signal(5, true)
    );

    Runnable action = () -> BoundaryMergeIterator.fluent(signals, FORWARD).list();

    assertThatThrownBy(action::run)
        .isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  @DisplayName("Should throw exception when signals are in wrong order for backward iteration")
  void iterator_WrongOrderBackward_Exception() {

    var signals = Iterators.iterator(
        signal(5, true),
        signal(0, true),
        signal(5, false),
        signal(10, true),
        signal(10, false),
        signal(10, true),
        signal(15, false)
    );

    Runnable action = () -> BoundaryMergeIterator.fluent(signals, FORWARD).list();

    assertThatThrownBy(action::run)
        .isInstanceOf(IllegalArgumentException.class);
  }

}
