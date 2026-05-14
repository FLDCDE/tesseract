package com.fieldcode.tesseract.signal;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.fieldcode.tesseract.SignalRange;
import com.fieldcode.tesseract.test_utils.SignalTestSupport;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SignalRangeTest extends SignalTestSupport {

  private SignalRange<Integer, String> signalRange;

  @BeforeEach
  void setUp() {
    signalRange = range(signal(-50), signal(50));
  }

  @Test
  void create_Valid_Success() {

    var lower = signal(1);
    var upper = signal(10);

    var range = range(lower, upper);

    assertThat(range.getLowerKey())
        .isEqualTo(1);

    assertThat(range.getUpperKey())
        .isEqualTo(10);

    assertThat(range.getLowerValue())
        .isEqualTo("1");

    assertThat(range.getUpperValue())
        .isEqualTo("10");
  }

  @Test
  void create_Invalid_Exception() {

    var lower = signal(10);
    var upper = signal(1);

    assertThatThrownBy(() -> range(lower, upper))
        .isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  void range_containsKey_withLowerKey_Success() {
    assertThat(signalRange.withLowerKey(-35)).isEqualTo(
        range(signal(-35, "-50"), signal(50, "50"))
    );
  }

  @Test
  void rangeUpperKey_isBeforeKey_withLowerValue_Failure() {

    assertThatThrownBy(
        () -> signalRange.withLowerKey(55)
    ).isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  void rangeUpperKey_isEqualsKey_withLowerKey_Failure() {
    assertThatThrownBy(
        () -> signalRange.withLowerKey(50)
    ).isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  void range_containsKey_withUpperKey_Success() {
    assertThat(signalRange.withUpperKey(-35)).isEqualTo(
        range(signal(-50, "-50"), signal(-35, "50"))
    );
  }

  @Test
  void rangeUpperKey_isBeforeKey_withUpperKey_Success() {
    assertThat(signalRange.withUpperKey(55)).isEqualTo(
        range(signal(-50, "-50"), signal(55, "50"))
    );
  }

  @Test
  void withLowerValue_Success() {
    assertThat(signalRange.withLowerValue("0")).isEqualTo(
        range(signal(-50, "0"), signal(50, "50"))
    );
  }

  @Test
  void withUpperValue_Success() {
    assertThat(signalRange.withUpperValue("0")).isEqualTo(
        range(signal(-50, "-50"), signal(50, "0"))
    );
  }

  @Test
  void mapIfContains_Contains_withUpperKey_Success() {
    var mappedRange = signalRange.mapIfContains(
        -40,
        SignalRange::withUpperKey
    );

    Assertions.assertThat(mappedRange).isEqualTo(
        range(signal(-50, "-50"), signal(-40, "50"))
    );
  }

  @Test
  void mapIfContains_notContains_returnThis_Success() {
    var mappedRange = signalRange.mapIfContains(
        60,
        SignalRange::withUpperKey
    );

    Assertions.assertThat(mappedRange).isEqualTo(
        range(signal(-50, "-50"), signal(50, "50"))
    );
  }

  @Test
  void mapByRelation_Contains_withLowerKey_Success() {
    var mappedRange = signalRange.mapByRelation(
        -40,
        SignalRange::withUpperKey,
        SignalRange::withLowerKey,
        SignalRange::withUpperKey
    );

    Assertions.assertThat(mappedRange).isEqualTo(
        range(signal(-40, "-50"), signal(50, "50"))
    );
  }

  @Test
  void mapByRelation_isBeforeThan_withUpperKey_Success() {
    var mappedRange = signalRange.mapByRelation(
        60,
        SignalRange::withUpperKey,
        SignalRange::withLowerKey,
        SignalRange::withLowerKey
    );

    Assertions.assertThat(mappedRange).isEqualTo(
        range(signal(-50, "-50"), signal(60, "50"))
    );
  }

  @Test
  void mapByRelation_isAfter_withLowerKey_Success() {
    var mappedRange = signalRange.mapByRelation(
        -60,
        SignalRange::withUpperKey,
        SignalRange::withUpperKey,
        SignalRange::withLowerKey
    );

    Assertions.assertThat(mappedRange).isEqualTo(
        range(signal(-60, "-50"), signal(50, "50"))
    );
  }


}
