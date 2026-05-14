package com.fieldcode.tesseract.signal;

import org.junit.jupiter.api.Test;

import com.fieldcode.tesseract.test_utils.SignalTestSupport;

import static org.assertj.core.api.Assertions.assertThat;

class SignalRangePredicatesTest extends SignalTestSupport {

  @Test
  void lowerKeyLessThan_Success() {

    var a = range(signal(0), signal(10));
    var b = range(signal(10), signal(20));
    var c = range(signal(20), signal(30));

    var predicate = SignalRangePredicates.<Integer, String>lowerKeyLessThan(10);

    assertThat(predicate.test(a)).isTrue();
    assertThat(predicate.test(b)).isFalse();
    assertThat(predicate.test(c)).isFalse();

  }

  @Test
  void lowerKeyLessThanOrEquals_Success() {

    var a = range(signal(0), signal(10));
    var b = range(signal(10), signal(20));
    var c = range(signal(20), signal(30));

    var predicate = SignalRangePredicates.<Integer, String>lowerKeyLessThanOrEquals(10);

    assertThat(predicate.test(a)).isTrue();
    assertThat(predicate.test(b)).isTrue();
    assertThat(predicate.test(c)).isFalse();

  }

  @Test
  void lowerKeyGreaterThanOrEquals_Success() {
    var a = range(signal(0), signal(10));
    var b = range(signal(10), signal(20));
    var c = range(signal(20), signal(30));

    var predicate = SignalRangePredicates.<Integer, String>lowerKeyGreaterThanOrEquals(10);

    assertThat(predicate.test(a)).isFalse();
    assertThat(predicate.test(b)).isTrue();
    assertThat(predicate.test(c)).isTrue();
  }

  @Test
  void upperKeyLessThanOrEquals_Success() {
    var a = range(signal(0), signal(10));
    var b = range(signal(10), signal(20));
    var c = range(signal(20), signal(30));

    var predicate = SignalRangePredicates.<Integer, String>upperKeyLessThanOrEquals(10);

    assertThat(predicate.test(a)).isTrue();
    assertThat(predicate.test(b)).isFalse();
    assertThat(predicate.test(c)).isFalse();
  }

  @Test
  void upperKeyGreaterThanOrEquals_Success() {
    var a = range(signal(0), signal(10));
    var b = range(signal(10), signal(20));
    var c = range(signal(20), signal(30));

    var predicate = SignalRangePredicates.<Integer, String>upperKeyGreaterThanOrEquals(10);

    assertThat(predicate.test(a)).isTrue();
    assertThat(predicate.test(b)).isTrue();
    assertThat(predicate.test(c)).isTrue();
  }

  @Test
  void upperKeyGreaterThan_Success() {
    var a = range(signal(0), signal(10));
    var b = range(signal(10), signal(20));
    var c = range(signal(20), signal(30));

    var predicate = SignalRangePredicates.<Integer, String>upperKeyGreaterThan(10);

    assertThat(predicate.test(a)).isFalse();
    assertThat(predicate.test(b)).isTrue();
    assertThat(predicate.test(c)).isTrue();
  }

}
