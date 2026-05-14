package com.fieldcode.tesseract.map;

import com.fieldcode.tesseract.SignalMap;
import com.fieldcode.tesseract.test_utils.SignalTestSupport;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.stream.IntStream;

import static com.fieldcode.tesseract.QueryDirection.BACKWARD;
import static com.fieldcode.tesseract.QueryDirection.FORWARD;
import static org.assertj.core.api.Assertions.assertThat;

@Disabled("Performance test")
class TreeSignalMapPerformanceTest extends SignalTestSupport {

  public static final int NUMBER_OF_ELEMENTS = 10000000;
  public static final Duration DURATION_THRESHOLD = Duration.ofMillis(500);

  private SignalMap<Integer, String> createBigSignalMap() {
    var map = TreeSignalMap.of(0, NUMBER_OF_ELEMENTS, DEFAULT_VALUE);

    IntStream
        .range(0, NUMBER_OF_ELEMENTS)
        .forEach(i -> map.put(i, RANDOM_VALUE));

    return map;
  }

  @Test
  void iterator_Traversal_Get_Performance() {
    var map = createBigSignalMap();

    var time = timeOf(
        () -> {
          for (int i = 0; i < NUMBER_OF_ELEMENTS; i++) {
            map.get(i);
          }
        }
    );

    var averageTime = time.dividedBy(NUMBER_OF_ELEMENTS);

    assertThat(averageTime).isLessThan(Duration.ofMillis(1));
    System.out.printf("Iterator fetch 1 element time : %s nanos %n", averageTime.toNanos());
  }

  @Test
  void iterator_Traversal_Performance() {

    var map = createBigSignalMap();

    var it = map.getFirst().iterator(FORWARD);

    var time = timeOf(
        () -> {
          while (it.hasNext()) {
            it.next();
          }
        }
    );

    assertThat(time).isLessThan(DURATION_THRESHOLD);
    System.out.printf("Iterator traversal time : %d millis %n", time.toMillis());
  }

  @Test
  void iterator_From_Traversal_Performance() {
    var map = createBigSignalMap();

    var iterator = map.signals(0, FORWARD);
    var time = timeOf(
        () -> {
          while (iterator.hasNext()) {
            iterator.next();
          }
        }
    );

    assertThat(time).isLessThan(DURATION_THRESHOLD);
    System.out.printf("IteratorFrom traversal time : %d millis %n", time.toMillis());

  }

  @Test
  void iterator_UpperLower_Traversal_Performance() {
    var map = createBigSignalMap();

    var it = map.signals(0, FORWARD);

    var time = timeOf(
        () -> {
          while (it.hasNext()) {
            it.next();
          }
        }
    );

    assertThat(time).isLessThan(DURATION_THRESHOLD);
    System.out.printf("Iterator UpperLower traversal time : %d millis %n", time.toMillis());

  }


  @Test
  void iterator_Backward_Traversal_Performance() {
    var map = createBigSignalMap();

    var it = map.getLast().iterator(BACKWARD);

    var time = timeOf(
        () -> {
          while (it.hasNext()) {
            it.next();
          }
        }
    );

    assertThat(time).isLessThan(DURATION_THRESHOLD);
    System.out.printf("Iterator Backward traversal time : %d millis %n", time.toMillis());

  }

  @Test
  void iterator_Backward_From_Traversal_Performance() {
    var map = createBigSignalMap();

    var it = map.signals(0, NUMBER_OF_ELEMENTS, BACKWARD);

    var time = timeOf(
        () -> {
          while (it.hasNext()) {
            it.next();
          }
        }
    );

    assertThat(time).isLessThan(DURATION_THRESHOLD);
    System.out.printf("Iterator BackwardFrom traversal time : %d millis %n", time.toMillis());
  }

  @Test
  void iterator_Backward_UpperLower_Traversal_Performance() {
    var map = createBigSignalMap();

    var it = map.signals(0, NUMBER_OF_ELEMENTS, BACKWARD);

    var time = timeOf(
        () -> {
          while (it.hasNext()) {
            it.next();
          }
        }
    );

    assertThat(time).isLessThan(DURATION_THRESHOLD);
    System.out.printf("Iterator Backward UpperLower traversal time : %d millis %n", time.toMillis());
  }

}
