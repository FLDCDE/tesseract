package com.fieldcode.tesseract.moment;

import java.time.Duration;

import org.junit.jupiter.api.Test;

import com.fieldcode.tesseract.Moment;
import com.fieldcode.tesseract.test_utils.TimeTestSupport;

import static org.assertj.core.api.Assertions.assertThat;

class MomentCopyMethodTest extends TimeTestSupport {

  static final Duration ONE_HOUR = Duration.ofHours(1);

  @Test
  void shift_Finite_Success() {

    var moment = moment(0);

    var shifted = moment.shift(ONE_HOUR);
    var back = shifted.shift(ONE_HOUR.negated());

    assertThat(shifted).isEqualTo(moment(1));
    assertThat(back).isEqualTo(moment);

  }

  @Test
  void shift_PositiveInfinite_Success() {

    var moment = inf();

    var shifted = moment.shift(ONE_HOUR);

    assertThat(shifted).isEqualTo(moment);

  }

  @Test
  void shift_NegativeInfinite_Success() {

    var moment = ninf();

    var shifted = moment.shift(ONE_HOUR);

    assertThat(shifted).isEqualTo(moment);

  }

  @Test
  void pull() {
    assertPull(ninf(), ninf(), moment(0), ninf());
    assertPull(moment(0), ninf(), moment(0), moment(0));
    assertPull(inf(), ninf(), moment(0), moment(0));

    assertPull(ninf(), ninf(), inf(), ninf());
    assertPull(moment(0), ninf(), inf(), moment(0));
    assertPull(inf(), ninf(), inf(), inf());

    assertPull(ninf(), moment(0), moment(10), moment(0));
    assertPull(moment(-1), moment(0), moment(10), moment(0));
    assertPull(moment(0), moment(0), moment(10), moment(0));
    assertPull(moment(5), moment(0), moment(10), moment(5));
    assertPull(moment(10), moment(0), moment(10), moment(10));
    assertPull(inf(), moment(0), moment(10), moment(10));

    assertPull(ninf(), moment(0), inf(), moment(0));
    assertPull(moment(-1), moment(0), inf(), moment(0));
    assertPull(moment(0), moment(0), inf(), moment(0));
    assertPull(moment(5), moment(0), inf(), moment(5));
    assertPull(inf(), moment(0), inf(), inf());
  }

  private void assertPull(Moment at, Moment lower, Moment upper, Moment expected) {
    assertThat(at.pull(lower, upper)).isEqualTo(expected);
  }

}
