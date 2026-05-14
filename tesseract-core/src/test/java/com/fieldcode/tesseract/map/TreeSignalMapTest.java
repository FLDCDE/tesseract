package com.fieldcode.tesseract.map;

import java.util.List;
import java.util.stream.Stream;

import org.assertj.core.api.Condition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.fieldcode.tesseract.Signal;
import com.fieldcode.tesseract.SignalMap;
import com.fieldcode.tesseract.test_utils.SignalTestSupport;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TreeSignalMapTest extends SignalTestSupport {

  private static final Condition<Signal<Integer, String>> SIGNAL_MIN_DEFAULT = new Condition<>(s -> s.equals(signal(MIN, DEFAULT_VALUE)), "min default signal");

  private SignalMap<Integer, String> map;

  @BeforeEach
  void setUp() {
    map = TreeSignalMap.of(MIN, MAX, DEFAULT_VALUE);
  }

  @Test
  void get() {
    var signal = map.put(5, "5");

    var s1 = map.get(0);
    var s2 = map.get(7);

    assertThat(signal).isEqualTo(signal(5));
    assertThat(s1).is(SIGNAL_MIN_DEFAULT);
    assertThat(s2).isEqualTo(signal(5));
    assertThat(signal).isSameAs(s2);

  }

  @Test
  void initialBounds_Success() {
    assertNext(map.get(MIN), signal(MAX, DEFAULT_VALUE));
    assertPrevious(map.get(MAX), signal(MIN, DEFAULT_VALUE));
  }

  @Test
  void getLowerValue_Default_Success() {
    assertThat(map.get(MIN).getValue()).isEqualTo(DEFAULT_VALUE);
  }

  @Test
  void getUpperValue_Default_Success() {
    assertThat(map.get(MAX).getValue()).isEqualTo(DEFAULT_VALUE);
  }

  @Test
  void putSignal_Previous_Next_Success() {
    var randomSignal = map.put(0, RANDOM_VALUE);

    assertNext(map.get(MIN), signal(0, RANDOM_VALUE));
    assertPrevious(randomSignal, signal(MIN, DEFAULT_VALUE));

    assertPrevious(map.get(MAX), signal(0, RANDOM_VALUE));
    assertNext(randomSignal, signal(MAX, DEFAULT_VALUE));
  }

  @Test
  void removeSignal_getFloor_Success() {
    map.put(0, RANDOM_VALUE);
    map.remove(0);
    assertThat(map.get(0)).isEqualTo(signal(MIN, DEFAULT_VALUE));
  }

  @Test
  void removeSignal_Invalidate_Success() {
    var randomSignal = map.put(0, RANDOM_VALUE);
    map.remove(0);
    assertThatThrownBy(randomSignal::getPrevious)
        .isInstanceOf(IllegalStateException.class);

    assertThatThrownBy(randomSignal::getNext)
        .isInstanceOf(IllegalStateException.class);

  }

  @Test
  void removeSignal_Previous_Next_Success() {
    map.put(-10, RANDOM_VALUE);
    map.put(0, RANDOM_VALUE);
    map.put(10, RANDOM_VALUE);

    map.remove(0);
    assertNext(map.get(-10), signal(10, RANDOM_VALUE));
    assertPrevious(map.get(10), signal(-10, RANDOM_VALUE));

    assertThat(map.stream())
        .containsExactly(
            signal(-100, DEFAULT_VALUE),
            signal(-10, RANDOM_VALUE),
            signal(10, RANDOM_VALUE),
            signal(100, DEFAULT_VALUE)
        );

  }

  @Test
  void putSignals_Success() {

    map.put(-50, RANDOM_VALUE);
    map.put(-10, RANDOM_VALUE);
    map.put(0, RANDOM_VALUE);
    map.put(10, RANDOM_VALUE);
    map.put(50, RANDOM_VALUE);

    assertPrevious(map.get(MAX), signal(50, RANDOM_VALUE));
    assertPrevious(map.get(50), signal(10, RANDOM_VALUE));
    assertPrevious(map.get(10), signal(0, RANDOM_VALUE));
    assertPrevious(map.get(0), signal(-10, RANDOM_VALUE));
    assertPrevious(map.get(-10), signal(-50, RANDOM_VALUE));
    assertPrevious(map.get(-50), signal(MIN, DEFAULT_VALUE));

    assertNext(map.get(MIN), signal(-50, RANDOM_VALUE));
    assertNext(map.get(-50), signal(-10, RANDOM_VALUE));
    assertNext(map.get(-10), signal(0, RANDOM_VALUE));
    assertNext(map.get(0), signal(10, RANDOM_VALUE));
    assertNext(map.get(10), signal(50, RANDOM_VALUE));
    assertNext(map.get(50), signal(MAX, DEFAULT_VALUE));

    assertThat(map.stream())
        .containsExactly(
            signal(-100, DEFAULT_VALUE),
            signal(-50, RANDOM_VALUE),
            signal(-10, RANDOM_VALUE),
            signal(0, RANDOM_VALUE),
            signal(10, RANDOM_VALUE),
            signal(50, RANDOM_VALUE),
            signal(100, DEFAULT_VALUE)
        );
  }

  @Test
  void signalsPresent_Success() {

    map.put(-10, RANDOM_VALUE);
    map.put(0, RANDOM_VALUE);
    map.put(10, RANDOM_VALUE);

    assertThat(map.stream())
        .containsExactly(
            signal(MIN, DEFAULT_VALUE),
            signal(-10, RANDOM_VALUE),
            signal(0, RANDOM_VALUE),
            signal(10, RANDOM_VALUE),
            signal(MAX, DEFAULT_VALUE)
        );
  }

  @Test
  void checkLowerBound_Success() {
    map.put(MIN, RANDOM_VALUE);

    map.remove(MIN);

    assertThat(map.stream())
        .containsExactly(
            signal(MIN, DEFAULT_VALUE),
            signal(MAX, DEFAULT_VALUE)
        );
  }

  @Test
  void checkUpperBound_Success() {
    map.put(MAX, RANDOM_VALUE);
    map.remove(MAX);

    assertThat(map.stream())
        .containsExactly(
            signal(MIN, DEFAULT_VALUE),
            signal(MAX, DEFAULT_VALUE)
        );
  }

  @Test
  void putAll_Stream_Success() {
    var stream = Stream.of(
        signal(-5, "-5"),
        signal(5)
    );

    map.putAll(stream);

    assertThat(map.stream())
        .containsExactly(
            signal(MIN, DEFAULT_VALUE),
            signal(-5),
            signal(5),
            signal(MAX, DEFAULT_VALUE)
        );

  }

  @Test
  void putAll_Iterable_Success() {

    var iterable = List.of(
        signal(-5, "-5"),
        signal(0, "0"),
        signal(5)
    );

    map.putAll(iterable);

    assertThat(map.stream())
        .containsExactly(
            signal(MIN, DEFAULT_VALUE),
            signal(-5),
            signal(0),
            signal(5),
            signal(MAX, DEFAULT_VALUE)
        );
  }

  @Test
  void assert_GetFirst_Success() {
    assertThat(map.getFirst()).isEqualTo(signal(MIN, DEFAULT_VALUE));
  }

  @Test
  void assert_GetLast_Success() {
    assertThat(map.getLast()).isEqualTo(signal(MAX, DEFAULT_VALUE));
  }

  @Test
  void signalStartNode_isActive_AlwaysTrue_Success() {
    map.remove(MIN);
    assertThat(map.get(MIN).isActive()).isTrue();
  }


}
