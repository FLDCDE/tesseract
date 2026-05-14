package com.fieldcode.tesseract.duration;

import java.time.Duration;

import org.junit.jupiter.api.Test;

import com.fieldcode.tesseract.interval.IntervalSets;
import com.fieldcode.tesseract.interval.IntervalTestSupport;
import com.fieldcode.tesseract.interval.Intervals;
import com.fieldcode.tesseract.utils.iterator.Iterators;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class DurationsTest extends IntervalTestSupport {

  @Test
  void total_BoundedIntervals_Success() {

    var intervals = Iterators.iterator(
        interval(0, 5),
        interval(10, 15)
    );
    var total = Durations.total(intervals);
    assertThat(total).isEqualTo(Duration.ofHours(10));
  }

  @Test
  void total_Unbounded_Exception() {

    var intervals = Iterators.iterator(
        Intervals.always()
    );

    assertThatThrownBy(() -> Durations.total(intervals))
        .isInstanceOf(IllegalStateException.class);
  }

  @Test
  void ratio_Valid_Success() {

    //     012345678901234567890
    // A = |---------|              (5 hours in window)
    // B =           |---------|    (10 hours in window)
    // W =      |--------------|
    // R = 5/10 = 0.5

    var a = IntervalSets.disjoint(
        interval(0, 10)
    );
    var b = IntervalSets.disjoint(
        interval(10, 20)
    );

    var window = interval(5, 20);
    var ratio = Durations.ratio(a, b, window);
    assertThat(ratio).isEqualTo(0.5);
  }

  @Test
  void ratio_ZeroA_Success() {

    var a = IntervalSets.disjoint();
    var b = IntervalSets.disjoint(
        interval(10, 20)
    );

    var window = interval(5, 20);
    var ratio = Durations.ratio(a, b, window);
    assertThat(ratio).isEqualTo(0);
  }

  @Test
  void ratio_ZeroB_Exception() {

    var a = IntervalSets.disjoint(
        interval(0, 10)
    );
    var b = IntervalSets.disjoint();

    var window = interval(5, 20);
    assertThatThrownBy(() -> Durations.ratio(a, b, window))
        .isInstanceOf(IllegalArgumentException.class);
  }

}
