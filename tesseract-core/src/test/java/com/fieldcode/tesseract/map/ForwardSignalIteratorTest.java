package com.fieldcode.tesseract.map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.fieldcode.tesseract.test_utils.SignalTestSupport;

import static org.assertj.core.api.Assertions.assertThat;

class ForwardSignalIteratorTest extends SignalTestSupport {

  private TreeSignalMap<Integer, String> map;

  @BeforeEach
  void setUp() {
    map = (TreeSignalMap<Integer, String>) createSampleMap();
  }

  @Test
  void iterator_All_Success() {

    var iterator = ForwardSignalIterator.of(map, MIN, MAX);

    assertThat(iterator)
        .toIterable()
        .containsExactly(
            signal(MIN, DEFAULT_VALUE),
            signal(-50),
            signal(-10),
            signal(0),
            signal(10),
            signal(50),
            signal(MAX, DEFAULT_VALUE)
        );
  }

  @Test
  void iteratorBetween_ForwardFloor_Success() {

    var iterator = ForwardSignalIterator.of(map, -50, 50);

    assertThat(iterator)
        .toIterable()
        .containsExactly(
            signal(-50),
            signal(-10),
            signal(0),
            signal(10),
            signal(50)
        );
  }

  @Test
  void iteratorBetween_ForwardNonFloor_Success() {

    var iterator = ForwardSignalIterator.of(map, -35, 35);

    assertThat(iterator)
        .toIterable()
        .containsExactly(
            signal(-35, "-50"),
            signal(-10),
            signal(0),
            signal(10),
            signal(35, "10")
        );
  }

  @Test
  void iteratorBetween_ForwardSameSignalFloor_Success() {
    var iterator = ForwardSignalIterator.of(map, 7, 8);

    assertThat(iterator)
        .toIterable()
        .containsExactly(
            signal(7, "0"),
            signal(8, "0")
        );
  }

  @Test
  void iteratorBetween_ForwardSuccessiveNodes_Success() {

    var iterator = ForwardSignalIterator.of(map, 0, 10);

    assertThat(iterator)
        .toIterable()
        .containsExactly(
            signal(0),
            signal(10)
        );
  }

}
