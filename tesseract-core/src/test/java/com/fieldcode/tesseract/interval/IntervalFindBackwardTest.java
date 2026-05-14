package com.fieldcode.tesseract.interval;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.Duration;
import java.util.stream.Stream;

import static com.fieldcode.tesseract.QueryDirection.BACKWARD;
import static org.assertj.core.api.Assertions.assertThat;

class IntervalFindBackwardTest extends IntervalTestSupport {

  public static final Duration TWO_HOURS = Duration.ofHours(2);

  static Stream<int[]> windowsEmpty() {
    return Stream.of(
        new int[]{0, 5},
        new int[]{0, 10},
        new int[]{0, 11},
        new int[]{10, 11},
        new int[]{11, 12},
        new int[]{20, 25},
        new int[]{24, 25}
    );
  }
  static Stream<int[]> windowsSuccess() {
    return Stream.of(
        new int[]{0, 12},
        new int[]{0, 20},
        new int[]{0, 25},
        new int[]{10, 12},
        new int[]{10, 20},
        new int[]{10, 25},
        new int[]{11, 13},
        new int[]{11, 20},
        new int[]{11, 25}
    );
  }

  @ParameterizedTest
  @MethodSource("windowsEmpty")
  void find_TypeBackward_Empty(int[] window) {

    var interval = interval(10, 20);

    var result = interval.find(interval(window[0], window[1]), BACKWARD, TWO_HOURS);
    assertThat(result).isEmpty();
  }

  @ParameterizedTest
  @MethodSource("windowsSuccess")
  void find_TypeBackward_Success(int[] window) {

    var duration = 2;
    var interval = interval(10, 20);

    var expectedUpper = Math.min(window[1], 20);
    var expectedLower = expectedUpper - duration;

    var result = interval.find(interval(window[0], window[1]), BACKWARD, TWO_HOURS);

    assertThat(result).contains(interval(expectedLower, expectedUpper));

  }


  @ParameterizedTest
  @MethodSource("windowsSuccess")
  void find_AlwaysBackward_Success(int[] source) {

    var duration = 2;
    var interval = Intervals.always();
    var window = interval(source[0], source[1]);

    var expectedLower = source[1] - duration;

    var result = interval.find(window, BACKWARD, TWO_HOURS);
    assertThat(result)
        .contains(interval(expectedLower, source[1]));

  }

  @Test
  void find_FromBackward_Empty() {

    var interval = Intervals.intervalFrom(moment(0));
    var window = Intervals.always();

    var result = interval.find(window, BACKWARD, TWO_HOURS);
    assertThat(result)
        .isEmpty();

  }

  @Test
  void find_ToBackward_Success() {

    var interval = Intervals.intervalTo(moment(0));
    var window = Intervals.always();

    assertThat(interval.find(window, BACKWARD, TWO_HOURS))
        .contains(interval(-2, 0));

  }

  @Test
  void find_AlwaysAlwaysBackward_Exception() {

    var interval = Intervals.always();
    var window = Intervals.always();

    assertThat(interval.find(window, BACKWARD, TWO_HOURS))
        .isEmpty();

  }

}
