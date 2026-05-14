package com.fieldcode.tesseract.map;

import com.fieldcode.tesseract.SignalRangeMap;
import com.fieldcode.tesseract.test_utils.SignalTestSupport;
import com.google.common.base.Stopwatch;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.Collections;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.fieldcode.tesseract.QueryDirection.BACKWARD;
import static com.fieldcode.tesseract.QueryDirection.FORWARD;
import static org.assertj.core.api.Assertions.assertThat;

@Disabled("Performance test")
class TreeSignalRangeMapPerformanceTest extends SignalTestSupport {

  public static final int NUMBER_OF_ELEMENTS = 10000000;
  public static final Duration DURATION_THRESHOLD = Duration.ofMillis(500);

  private SignalRangeMap<Integer, String> createBigSignalRangeMap() {
    var map = DisjointSignalRangeMap.of(0, NUMBER_OF_ELEMENTS, DEFAULT_VALUE);

    IntStream
        .range(0, NUMBER_OF_ELEMENTS)
        .forEach(i ->
            map.put(range(signal(i), signal(i + 1)))
        );

    return map;
  }

  @Test
  void iterator_Traversal_Get_Performance() {
    var map = createBigSignalRangeMap();

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

    var map = createBigSignalRangeMap();

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
    var map = createBigSignalRangeMap();

    var iterator = map.ranges(0, FORWARD);
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
    var map = createBigSignalRangeMap();

    var it = map.ranges(0, FORWARD);

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
    var map = createBigSignalRangeMap();

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
    var map = createBigSignalRangeMap();

    var it = map.ranges(0, NUMBER_OF_ELEMENTS, BACKWARD);

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
    var map = createBigSignalRangeMap();

    var it = map.ranges(0, NUMBER_OF_ELEMENTS, BACKWARD);

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

  @Test
  void perf() {

    var iterations = 100;
    var elements = 100000;
    var expectedRange = range(signal(0, "0"), signal(elements, DEFAULT_VALUE));
    var rnd = new Random();
    var sum = new AtomicLong();

    for (int i = 0; i < iterations; i++) {
      var map = DisjointSignalRangeMap.of(0, elements, "default");

      var starts = IntStream.range(0, elements)
          .boxed()
          .collect(Collectors.toList());

      Collections.shuffle(starts);

      var sw = Stopwatch.createStarted();
//      starts.forEach(a -> map.put(a, a + 1, "0"));

      int min = elements;
      int max = 0;
      for (int j = 0; j < elements; j++) {
        var a = starts.get(j);
        var d = Math.min(rnd.nextInt(100) + 1, elements - a);
        var b = a + d;
        var lower = Math.min(a, b);
        var upper = Math.max(a, b);

        min = Math.min(min, d);
        max = Math.max(max, d);

        try {
          map.put(lower, upper, "0");
        } catch (Exception e) {
          throw new RuntimeException(e);
        }
      }

      sw.stop();
      var elapsedInNanos = sw.elapsed().toNanos();
      System.out.printf("avg time=%d, min/max=%d..%d%n", elapsedInNanos / elements, min, max);

      sum.addAndGet(elapsedInNanos);

      assertThat(map.stream())
          .containsExactly(expectedRange);
    }

    System.out.printf("Avg time for adding a random range is %d nanos", sum.get() / iterations / elements);

  }

}
