package com.fieldcode.tesseract.signal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.fieldcode.tesseract.Signal;
import com.fieldcode.tesseract.test_utils.SignalTestSupport;

import static org.assertj.core.api.Assertions.assertThat;

public class SignalTest extends SignalTestSupport {

  private Signal<Integer, String> signal;


  @BeforeEach
  public void setUp() {
    signal = signal(0, RANDOM_VALUE);
  }

  @Test
  void signal_getKey_Success() {
    assertThat(signal.keyEqualsTo(0)).isTrue();
  }

  @Test
  void signal_getValue_Success() {
    assertThat(signal.valueEqualsTo(RANDOM_VALUE)).isTrue();
  }

  @Test
  void signal_Compare_isLess_Success() {
    var comparisonSignal = signal(10, RANDOM_VALUE);

    assertThat(signal.compareTo(comparisonSignal))
        .isEqualTo(-1);
  }

  @Test
  public void signal_Compare_isGreater_Success() {
    var comparisonSignal = signal(-10, RANDOM_VALUE);

    assertThat(signal.compareTo(comparisonSignal))
        .isEqualTo(1);
  }

  @Test
  public void signal_Compare_isEqual_Success() {
    var comparisonSignal = signal(0, RANDOM_VALUE);

    assertThat(signal.compareTo(comparisonSignal))
        .isEqualTo(0);
  }

}
