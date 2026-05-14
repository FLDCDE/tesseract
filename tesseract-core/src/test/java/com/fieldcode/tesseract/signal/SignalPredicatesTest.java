package com.fieldcode.tesseract.signal;

import org.junit.jupiter.api.Test;

import com.fieldcode.tesseract.test_utils.SignalTestSupport;

import static org.assertj.core.api.Assertions.assertThat;

class SignalPredicatesTest extends SignalTestSupport {

  @Test
  void keyLessThan() {

    var a = signal(0, "0");
    var b = signal(10, "10");
    var c = signal(20, "20");

    var predicate = SignalPredicates.<Integer, String>keyLessThan(10);

    assertThat(predicate.test(a)).isTrue();
    assertThat(predicate.test(b)).isFalse();
    assertThat(predicate.test(c)).isFalse();

  }

  @Test
  void keyGreaterThan() {

    var a = signal(0, "0");
    var b = signal(10, "10");
    var c = signal(20, "20");

    var predicate = SignalPredicates.<Integer, String>keyGreaterThan(10);

    assertThat(predicate.test(a)).isFalse();
    assertThat(predicate.test(b)).isFalse();
    assertThat(predicate.test(c)).isTrue();

  }

  @Test
  void keyEqualsTo() {

    var a = signal(0, "0");
    var b = signal(10, "10");
    var c = signal(20, "20");

    var predicate = SignalPredicates.<Integer, String>keyEqualsTo(10);

    assertThat(predicate.test(a)).isFalse();
    assertThat(predicate.test(b)).isTrue();
    assertThat(predicate.test(c)).isFalse();

  }

  @Test
  void predicate_HasNext_True() {
    var predicate = SignalPredicates.<Integer, String>hasNext();
    var map = createSampleMap();
    var a = map.get(MIN);
    assertThat(predicate.test(a)).isTrue();
  }

  @Test
  void predicate_HasNext_False() {
    var predicate = SignalPredicates.<Integer, String>hasNext();
    var map = createSampleMap();
    var a = map.get(MAX);
    assertThat(predicate.test(a)).isFalse();
  }


  @Test
  void predicate_HasPrevious_True() {
    var predicate = SignalPredicates.<Integer, String>hasPrevious();
    var map = createSampleMap();
    var a = map.get(MAX);
    assertThat(predicate.test(a)).isTrue();
  }

  @Test
  void predicate_HasPrevious_False() {
    var predicate = SignalPredicates.<Integer, String>hasPrevious();
    var map = createSampleMap();
    var a = map.get(MIN);
    assertThat(predicate.test(a)).isFalse();
  }

  @Test
  void predicate_NextIs_Success() {
    var predicate = SignalPredicates.<Integer, String>nextIs(signal -> signal.getKey() > 0);
    var map = createSampleMap();
    var a = map.get(0);
    assertThat(predicate.test(a)).isEqualTo(true);
  }

  @Test
  void predicate_PreviousIs_Success() {
    var predicate = SignalPredicates.<Integer, String>previousIs(signal -> signal.getKey() < 0);
    var map = createSampleMap();
    var a = map.get(0);
    assertThat(predicate.test(a)).isEqualTo(true);
  }


}
