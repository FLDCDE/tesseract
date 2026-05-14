package com.fieldcode.tesseract.collection;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.fieldcode.tesseract.FluentIterator;
import com.fieldcode.tesseract.Moment;
import com.fieldcode.tesseract.Signal;
import com.fieldcode.tesseract.signal.Signals;
import com.fieldcode.tesseract.test_utils.TimeTestSupport;
import com.fieldcode.tesseract.utils.iterator.Iterators;

import static org.assertj.core.api.Assertions.assertThat;

class BoundaryLimiterForwardIteratorTest extends TimeTestSupport {

  private FluentIterator<Signal<Moment, Boolean>> emptyIterator;
  private FluentIterator<Signal<Moment, Boolean>> nonEmptyIterator;

  @BeforeEach
  void setUp() {
    List<Signal<Moment, Boolean>> signals = List.of(
        signal(0, true),
        signal(5, false),
        signal(10, true),
        signal(15, false),
        signal(20, true),
        signal(25, false)
    );
    nonEmptyIterator = Iterators.iterator(signals);
    emptyIterator = Iterators.empty();
  }

  private Signal<Moment, Boolean> signal(int hour, boolean value) {
    return Signals.signal(moment(hour), value);
  }

  @Test
  void iterator_Empty_Bounds() {
    var iterator = BoundaryLimiterForwardIterator.of(emptyIterator, interval(0, 30));
    assertThat(iterator)
        .toIterable()
        .containsExactly(
            signal(0, false),
            signal(30, false)
        );
  }

  @Test
  void iterator_Type1_BoundsOnlyBefore() {
    var iterator = BoundaryLimiterForwardIterator.of(nonEmptyIterator, interval(-10, -5));
    assertThat(iterator)
        .toIterable()
        .containsExactly(
            signal(-10, false),
            signal(-5, false)
        );
  }

  @Test
  void iterator_Type1_BoundsOnlyAfter() {
    var iterator = BoundaryLimiterForwardIterator.of(nonEmptyIterator, interval(30, 40));
    assertThat(iterator)
        .toIterable()
        .containsExactly(
            signal(30, false),
            signal(40, false)
        );
  }

  @Test
  void iterator_Type2_FirstVirtual() {
    var iterator = BoundaryLimiterForwardIterator.of(nonEmptyIterator, interval(-10, 0));
    assertThat(iterator)
        .toIterable()
        .containsExactly(
            signal(-10, false),
            signal(0, true)
        );
  }

  @Test
  void iterator_Type3_FirstLastVirtual() {
    var iterator = BoundaryLimiterForwardIterator.of(nonEmptyIterator, interval(-10, 4));
    assertThat(iterator)
        .toIterable()
        .containsExactly(
            signal(-10, false),
            signal(0, true),
            signal(4, true)
        );
  }

  @Test
  void iterator_Type4_FirstVirtual() {
    var iterator = BoundaryLimiterForwardIterator.of(nonEmptyIterator, interval(-10, 5));
    assertThat(iterator)
        .toIterable()
        .containsExactly(
            signal(-10, false),
            signal(0, true),
            signal(5, false)
        );
  }

  @Test
  void iterator_Type5_FirstLastVirtual() {
    var iterator = BoundaryLimiterForwardIterator.of(nonEmptyIterator, interval(-10, 7));
    assertThat(iterator)
        .toIterable()
        .containsExactly(
            signal(-10, false),
            signal(0, true),
            signal(5, false),
            signal(7, false)
        );
  }

  @Test
  void iterator_Type6_FirstLastVirtual() {
    var iterator = BoundaryLimiterForwardIterator.of(nonEmptyIterator, interval(-10, 20));
    assertThat(iterator)
        .toIterable()
        .containsExactly(
            signal(-10, false),
            signal(0, true),
            signal(5, false),
            signal(10, true),
            signal(15, false),
            signal(20, true)
        );
  }

  @Test
  void iterator_Type7_FirstLastVirtual() {
    var iterator = BoundaryLimiterForwardIterator.of(nonEmptyIterator, interval(-10, 30));
    assertThat(iterator)
        .toIterable()
        .containsExactly(
            signal(-10, false),
            signal(0, true),
            signal(5, false),
            signal(10, true),
            signal(15, false),
            signal(20, true),
            signal(25, false),
            signal(30, false)
        );
  }

  @Test
  void iterator_Type8_LastVirtual() {
    var iterator = BoundaryLimiterForwardIterator.of(nonEmptyIterator, interval(0, 30));
    assertThat(iterator)
        .toIterable()
        .containsExactly(
            signal(0, true),
            signal(5, false),
            signal(10, true),
            signal(15, false),
            signal(20, true),
            signal(25, false),
            signal(30, false)
        );
  }

  @Test
  void iterator_Type9_FirstLastVirtual() {
    var iterator = BoundaryLimiterForwardIterator.of(nonEmptyIterator, interval(4, 30));
    assertThat(iterator)
        .toIterable()
        .containsExactly(
            signal(4, true),
            signal(5, false),
            signal(10, true),
            signal(15, false),
            signal(20, true),
            signal(25, false),
            signal(30, false)
        );
  }

  @Test
  void iterator_Type10_LastVirtual() {
    var iterator = BoundaryLimiterForwardIterator.of(nonEmptyIterator, interval(5, 30));
    assertThat(iterator)
        .toIterable()
        .containsExactly(
            signal(5, false),
            signal(10, true),
            signal(15, false),
            signal(20, true),
            signal(25, false),
            signal(30, false)
        );
  }

  @Test
  void iterator_Type11_FirstLastVirtual() {
    var iterator = BoundaryLimiterForwardIterator.of(nonEmptyIterator, interval(7, 30));
    assertThat(iterator)
        .toIterable()
        .containsExactly(
            signal(7, false),
            signal(10, true),
            signal(15, false),
            signal(20, true),
            signal(25, false),
            signal(30, false)
        );
  }

  @Test
  void iterator_SameSignals_Success() {

    List<Signal<Moment, Boolean>> signals = List.of(
        signal(0, true),
        signal(5, false),
        signal(5, true),
        signal(5, false),
        signal(10, true),
        signal(15, false),
        signal(20, true),
        signal(25, false)
    );
    var signalFluentIterator = Iterators.iterator(signals);

    var iterator = BoundaryLimiterForwardIterator.of(signalFluentIterator, interval(5, 30));
    assertThat(iterator)
        .toIterable()
        .containsExactly(
            signal(5, false),
            signal(5, true),
            signal(5, false),
            signal(10, true),
            signal(15, false),
            signal(20, true),
            signal(25, false),
            signal(30, false)
        );
  }

}

