package com.fieldcode.tesseract.query;

import org.junit.jupiter.api.Test;

import com.fieldcode.tesseract.interval.IntervalSets;
import com.fieldcode.tesseract.interval.IntervalTestSupport;

import static com.fieldcode.tesseract.query.IntervalQueries.query;
import static com.fieldcode.tesseract.query.IntervalQueries.schedule;
import static org.assertj.core.api.Assertions.assertThat;

class IntervalQueryTest extends IntervalTestSupport {

  @Test
  void execute_OneDuration_Success() {

    var set = IntervalSets.disjoint(
        interval(0, 24)
    );

    var query = query(
        schedule(ONE_HOUR, set)
    );

    var results = IntervalQueryExecutor.execute(query);

    assertThat(results.isSuccess()).isTrue();
    assertThat(results.isFailed()).isFalse();

    assertThat(results.getIntervals())
        .containsExactly(
            interval(0, 1)
        );

  }

  @Test
  void execute_OneDurationWithWindow_Success() {

    var set = IntervalSets.disjoint(
        interval(0, 24)
    );

    var query = query(
        schedule(ONE_HOUR, set).window(interval(1, 20))
    );

    var results = IntervalQueryExecutor.execute(query);

    assertThat(results.isSuccess()).isTrue();
    assertThat(results.isFailed()).isFalse();

    assertThat(results.getIntervals())
        .containsExactly(
            interval(1, 2)
        );

  }

  @Test
  void execute_MoreDurations_Success() {

    var set = IntervalSets.disjoint(
        interval(0, 24)
    );

    var query = query(
        schedule(ONE_HOUR, set),
        schedule(hours(2), set).window(interval(5, 8)),
        schedule(hours(3), set)
    );

    var results = IntervalQueryExecutor.execute(query);

    assertThat(results.isSuccess()).isTrue();
    assertThat(results.isFailed()).isFalse();

    assertThat(results.getIntervals())
        .containsExactly(
            interval(0, 1),
            interval(5, 7),
            interval(7, 10)
        );

  }

  @Test
  void execute_MoreDurationsWithCenter_Success() {

    var set = IntervalSets.disjoint(
        interval(0, 24)
    );

    var query = query(
        schedule(ONE_HOUR, set),
        schedule(hours(2), set).window(interval(5, 8)).center(),
        schedule(hours(3), set)
    );

    var results = IntervalQueryExecutor.execute(query);

    assertThat(results.isSuccess()).isTrue();
    assertThat(results.isFailed()).isFalse();

    assertThat(results.getIntervals())
        .containsExactly(
            interval(4, 5),
            interval(5, 7),
            interval(7, 10)
        );

  }

  @Test
  void execute_MoreDurationsWithCenterAndGlobalWindow_Success() {

    var set = IntervalSets.disjoint(
        interval(0, 24)
    );

    var query = query(
        interval(5, 24),
        schedule(ONE_HOUR, set),
        schedule(hours(2), set).window(interval(5, 8)).center(),
        schedule(hours(3), set)
    );
    var results = IntervalQueryExecutor.execute(query);

    assertThat(results.isSuccess()).isTrue();
    assertThat(results.isFailed()).isFalse();

    assertThat(results.getIntervals())
        .containsExactly(
            interval(5, 6),
            interval(6, 8),
            interval(8, 11)
        );

  }

  @Test
  void execute_MoreDurationsWithCenterComplexAvailability_Success() {

    var set = IntervalSets.disjoint(
        interval(0, 2),
        interval(3, 5),
        interval(6, 8),
        interval(9, 13)
    );

    var query = query(
        schedule(hours(2), set),
        schedule(ONE_HOUR, set),
        schedule(hours(2), set).window(interval(4, 13)).center(),
        schedule(ONE_HOUR, set).window(interval(10, 24))
    );

    var results = IntervalQueryExecutor.execute(query);

    assertThat(results.isSuccess()).isTrue();
    assertThat(results.isFailed()).isFalse();

    assertThat(results.getIntervals())
        .containsExactly(
            interval(0, 2),
            interval(4, 5),
            interval(6, 8),
            interval(10, 11)
        );

  }

  @Test
  void execute_MoreDurationsWithLockedComplexAvailability_Success() {

    var set = IntervalSets.disjoint(
        interval(0, 2),
        interval(3, 5),
        interval(6, 8),
        interval(9, 13)
    );

    var query = query(
        schedule(ONE_HOUR, set),
        schedule(ONE_HOUR, set),
        schedule(hours(1), set).window(interval(5, 6)).locked(),
        schedule(ONE_HOUR, set).window(interval(10, 11))
    );

    var results = IntervalQueryExecutor.execute(query);

    assertThat(results.isSuccess()).isTrue();
    assertThat(results.isFailed()).isFalse();

    assertThat(results.getIntervals())
        .containsExactly(
            interval(0, 1),
            interval(1, 2),
            interval(5, 6),
            interval(10, 11)
        );

  }

  @Test
  void execute_MoreDurationsDifferentIntervals_Success() {

    var set1 = IntervalSets.disjoint(
        interval(0, 12)
    );

    var set2 = IntervalSets.disjoint(
        interval(17, 24)
    );

    var query = query(
        schedule(ONE_HOUR, set1),
        schedule(ONE_HOUR, set2).window(interval(16, 20)).center()
    );

    var results = IntervalQueryExecutor.execute(query);

    assertThat(results.isSuccess()).isTrue();
    assertThat(results.isFailed()).isFalse();

    assertThat(results.getIntervals())
        .containsExactly(
            interval(11, 12),
            interval(17, 18)
        );
  }

}
