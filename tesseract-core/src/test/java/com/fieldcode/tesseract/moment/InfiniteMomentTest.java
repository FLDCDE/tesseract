package com.fieldcode.tesseract.moment;

import org.junit.jupiter.api.Test;

import com.fieldcode.tesseract.test_utils.TimeTestSupport;

import static org.assertj.core.api.Assertions.assertThat;

class InfiniteMomentTest extends TimeTestSupport {

  @Test
  void ninf_Methods_Success() {

    var moment = ninf();

    assertThat(moment.isInfinite()).isTrue();
    assertThat(moment.isNegativeInfinite()).isTrue();
    assertThat(moment.isPositiveInfinite()).isFalse();
    assertThat(moment.signum()).isEqualTo(-1);
    assertThat(moment.abs()).isEqualTo(inf());
    assertThat(moment.negate()).isEqualTo(inf());

  }

  @Test
  void inf_Methods_Success() {

    var moment = inf();

    assertThat(moment.isInfinite()).isTrue();
    assertThat(moment.isNegativeInfinite()).isFalse();
    assertThat(moment.isPositiveInfinite()).isTrue();
    assertThat(moment.signum()).isEqualTo(1);
    assertThat(moment.abs()).isEqualTo(inf());
    assertThat(moment.negate()).isEqualTo(ninf());

  }


}
