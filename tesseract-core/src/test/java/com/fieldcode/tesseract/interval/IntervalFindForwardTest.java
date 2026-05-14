package com.fieldcode.tesseract.interval;

import java.time.Duration;

import org.junit.jupiter.api.Test;

import static com.fieldcode.tesseract.QueryDirection.FORWARD;
import static org.assertj.core.api.Assertions.assertThat;

class IntervalFindForwardTest extends IntervalTestSupport {

  public static final Duration TWO_HOURS = Duration.ofHours(2);

  @Test
  void find_Type1Forward_Empty() {

    var interval = interval(10, 20);
    var window = interval(0, 5);

    var result = interval.find(window, FORWARD, TWO_HOURS);
    assertThat(result).isEmpty();

  }

  @Test
  void find_Type2Forward_Empty() {

    var interval = interval(10, 20);
    var window = interval(0, 10);

    var result = interval.find(window, FORWARD, TWO_HOURS);
    assertThat(result).isEmpty();

  }

  @Test
  void find_Type3LessForward_Empty() {

    var interval = interval(10, 20);
    var window = interval(0, 11);

    var result = interval.find(window, FORWARD, TWO_HOURS);
    assertThat(result).isEmpty();

  }

  @Test
  void find_Type3Forward_Success() {

    var interval = interval(10, 20);
    var window = interval(0, 12);

    var result = interval.find(window, FORWARD, TWO_HOURS);
    assertThat(result).contains(interval(10, 12));

  }

  @Test
  void find_Type4Forward_Success() {

    var interval = interval(10, 20);
    var window = interval(0, 20);

    var result = interval.find(window, FORWARD, TWO_HOURS);
    assertThat(result).contains(interval(10, 12));

  }

  @Test
  void find_Type5Forward_Success() {

    var interval = interval(10, 20);
    var window = interval(0, 25);

    var result = interval.find(window, FORWARD, TWO_HOURS);
    assertThat(result).contains(interval(10, 12));

  }

  @Test
  void find_Type6LessForward_Success() {

    var interval = interval(10, 20);
    var window = interval(10, 11);

    var result = interval.find(window, FORWARD, TWO_HOURS);
    assertThat(result).isEmpty();

  }

  @Test
  void find_Type6Forward_Success() {

    var interval = interval(10, 20);
    var window = interval(10, 12);

    var result = interval.find(window, FORWARD, TWO_HOURS);
    assertThat(result).contains(interval(10, 12));

  }

  @Test
  void find_Type7Forward_Success() {

    var interval = interval(10, 20);
    var window = interval(10, 20);

    var result = interval.find(window, FORWARD, TWO_HOURS);
    assertThat(result).contains(interval(10, 12));

  }

  @Test
  void find_Type8Forward_Success() {

    var interval = interval(10, 20);
    var window = interval(10, 25);

    var result = interval.find(window, FORWARD, TWO_HOURS);
    assertThat(result).contains(interval(10, 12));

  }

  @Test
  void find_Type9LessForward_Success() {

    var interval = interval(10, 20);
    var window = interval(11, 12);

    var result = interval.find(window, FORWARD, TWO_HOURS);
    assertThat(result).isEmpty();

  }

  @Test
  void find_Type9Forward_Success() {

    var interval = interval(10, 20);
    var window = interval(11, 13);

    var result = interval.find(window, FORWARD, TWO_HOURS);
    assertThat(result).contains(interval(11, 13));

  }

  @Test
  void find_Type10Forward_Success() {

    var interval = interval(10, 20);
    var window = interval(11, 20);

    var result = interval.find(window, FORWARD, TWO_HOURS);
    assertThat(result).contains(interval(11, 13));

  }

  @Test
  void find_Type11Forward_Success() {

    var interval = interval(10, 20);
    var window = interval(11, 25);

    var result = interval.find(window, FORWARD, TWO_HOURS);
    assertThat(result).contains(interval(11, 13));

  }

  @Test
  void find_Type12Forward_Success() {

    var interval = interval(10, 20);
    var window = interval(20, 25);

    var result = interval.find(window, FORWARD, TWO_HOURS);
    assertThat(result).isEmpty();

  }

  @Test
  void find_Type13Forward_Success() {

    var interval = interval(10, 20);
    var window = interval(24, 25);

    var result = interval.find(window, FORWARD, TWO_HOURS);
    assertThat(result).isEmpty();

  }

  @Test
  void find_AlwaysForward_Success() {

    var interval = Intervals.always();
    var window = interval(0, 2);

    var result = interval.find(window, FORWARD, TWO_HOURS);
    assertThat(result)
        .contains(interval(0, 2));

  }

  @Test
  void find_FromForward_Success() {

    var interval = Intervals.intervalFrom(moment(0));
    var window = Intervals.always();

    var result = interval.find(window, FORWARD, TWO_HOURS);
    assertThat(result)
        .contains(interval(0, 2));

  }

  @Test
  void find_ToForward_Empty() {

    var interval = Intervals.intervalTo(moment(0));
    var window = Intervals.always();

    assertThat(interval.find(window, FORWARD, TWO_HOURS))
        .isEmpty();

  }

  @Test
  void find_AlwaysAlwaysForward_Empty() {

    var interval = Intervals.always();
    var window = Intervals.always();

    assertThat(interval.find(window, FORWARD, TWO_HOURS))
        .isEmpty();

  }

}
