package com.fieldcode.tesseract.moment;

import java.time.format.DateTimeParseException;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;

import com.fieldcode.tesseract.test_utils.TimeTestSupport;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MomentParserTest extends TimeTestSupport {

  @Test
  void parse_Invalid_Exception() {
    assertThatThrownBy(() -> MomentParser.parse("invalid"))
        .isExactlyInstanceOf(DateTimeParseException.class);
  }

  @Test
  void parse_Ninf_Success() {
    var ninfMoments = Stream.of(
            "ninf",
            "-infinite",
            "-inf",
            "-∞",
            " ninf ",
            " -inf ",
            " -infinite ",
            " -∞ "
        )
        .map(Moments::parse)
        .distinct();

    assertThat(ninfMoments).containsExactly(ninf());
  }

  @Test
  void parse_Inf_Success() {
    var ninfMoments = Stream.of(
            "inf",
            "infinite",
            "+inf",
            "+infinite",
            "∞",
            " inf ",
            " infinite ",
            " +inf ",
            " +infinite ",
            " ∞ "
        )
        .map(Moments::parse)
        .distinct();

    assertThat(ninfMoments).containsExactly(inf());
  }


}
