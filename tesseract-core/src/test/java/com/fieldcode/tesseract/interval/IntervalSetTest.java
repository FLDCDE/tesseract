package com.fieldcode.tesseract.interval;

import java.util.List;
import java.util.stream.Stream;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class IntervalSetTest extends IntervalTestSupport {

  @Test
  void intervalSets_stream_modifiable_success() {
    var stream = Stream.of(
        interval(-10, 0),
        interval(5, 10),
        interval(15, 30),
        interval(30, 50)
    );

    var set = IntervalSets.disjoint(stream);
    Assertions.assertThat(set.intervals())
        .toIterable()
        .containsExactly(
            interval(-10, 0),
            interval(5, 10),
            interval(15, 50)
        );
  }

  @Test
  void intervalSets_collection_modifiable_success() {
    var stream = List.of(
        interval(-10, 0),
        interval(5, 10),
        interval(15, 30),
        interval(30, 50)
    );

    var set = IntervalSets.disjoint(stream);
    Assertions.assertThat(set.intervals())
        .toIterable()
        .containsExactly(
            interval(-10, 0),
            interval(5, 10),
            interval(15, 50)
        );
  }

}
