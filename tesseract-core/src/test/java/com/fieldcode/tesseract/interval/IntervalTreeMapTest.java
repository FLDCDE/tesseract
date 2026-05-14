package com.fieldcode.tesseract.interval;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.fieldcode.tesseract.IntervalMap;
import com.fieldcode.tesseract.IntervalMap.IntervalEntry;
import com.fieldcode.tesseract.test_utils.TimeTestSupport;

import static com.fieldcode.tesseract.QueryDirection.BACKWARD;
import static com.fieldcode.tesseract.QueryDirection.FORWARD;
import static org.assertj.core.api.Assertions.assertThat;

class IntervalTreeMapTest extends TimeTestSupport {

  private IntervalMap<String> map;

  // TODO: 2023. 09. 15. Add tests for IntervalTreeMap

  private IntervalEntry<String> entry(int start, int end, String value) {
    return IntervalMaps.entry(interval(start, end), value);
  }

  private IntervalEntry<String> entry(int start, int end) {
    return IntervalMaps.entry(interval(start, end), "default");
  }

  @BeforeEach
  void setUp() {
    map = IntervalMaps.disjoint("default")
        .put(interval(0, 5), "event1")
        .put(interval(10, 15), "event2")
        .put(interval(20, 25), "event2");
  }

  @Test
  void entries_WindowForward_Success() {
    var result = map.entries(interval(-10, 22), FORWARD);

    assertThat(result)
        .toIterable()
        .containsExactly(
            entry(-10, 0),
            entry(0, 5, "event1"),
            entry(5, 10),
            entry(10, 15, "event2"),
            entry(15, 20),
            entry(20, 22, "event2")
        );

  }

  @Test
  void entries_WindowBackward_Success() {
    var result = map.entries(interval(-10, 22), BACKWARD);

    assertThat(result)
        .toIterable()
        .containsExactly(
            entry(20, 22, "event2"),
            entry(15, 20),
            entry(10, 15, "event2"),
            entry(5, 10),
            entry(0, 5, "event1"),
            entry(-10, 0)
        );

  }

}
