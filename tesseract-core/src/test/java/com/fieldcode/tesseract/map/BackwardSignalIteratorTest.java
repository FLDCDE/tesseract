package com.fieldcode.tesseract.map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.fieldcode.tesseract.test_utils.SignalTestSupport;

import static org.assertj.core.api.Assertions.assertThat;

class BackwardSignalIteratorTest extends SignalTestSupport {

  private TreeSignalMap<Integer, String> map;

  @BeforeEach
  void setUp() {
    map = (TreeSignalMap<Integer, String>) createSampleMap();
  }

  @Test
  void iterator_All_Success() {

    var iterator = BackwardSignalIterator.of(map, MIN, MAX);

    assertThat(iterator)
        .toIterable()
        .containsExactly(
            signal(MAX, "50"),
            signal(50, "10"),
            signal(10, "0"),
            signal(0, "-10"),
            signal(-10, "-50"),
            signal(-50, DEFAULT_VALUE),
            signal(MIN, DEFAULT_VALUE)
        );
  }

  @Test
  void iterator_ForwardFloor_Success() {

    var iterator = BackwardSignalIterator.of(map, -50, 50);

    assertThat(iterator)
        .toIterable()
        .containsExactly(
            signal(50, "10"),
            signal(10, "0"),
            signal(0, "-10"),
            signal(-10, "-50"),
            signal(-50, DEFAULT_VALUE)
        );
  }

  @Test
  void iterator_ForwardNonFloor_Success() {

    var iterator = BackwardSignalIterator.of(map, -35, 35);

    assertThat(iterator)
        .toIterable()
        .containsExactly(
            signal(35, "10"),
            signal(10, "0"),
            signal(0, "-10"),
            signal(-10, "-50"),
            signal(-35, "-50")
        );
  }

  @Test
  void iterator_ForwardSameSignalFloor_Success() {
    var iterator = BackwardSignalIterator.of(map, 7, 8);

    assertThat(iterator)
        .toIterable()
        .containsExactly(
            signal(8, "0"),
            signal(7, "0")
        );
  }

  @Test
  void iterator_ForwardSuccessiveNodes_Success() {

    var iterator = BackwardSignalIterator.of(map, 0, 10);

    assertThat(iterator)
        .toIterable()
        .containsExactly(
            signal(10, "0"),
            signal(0, "-10")
        );
  }

}
