package com.fieldcode.tesseract.interval;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.fieldcode.tesseract.Interval;
import com.fieldcode.tesseract.IntervalsRelation;

import java.util.function.Function;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class IntervalRelationTest extends IntervalTestSupport {

  private Interval baseInterval;

  @BeforeEach
  void setUp() {
    baseInterval = Intervals.interval(at(-10), at(10));
  }

  private void assertRelation(IntervalsRelation relation, Function<IntervalsRelation, Boolean> mapper, boolean expected) {
    assertThat(mapper.apply(relation)).isEqualTo(expected);
  }

  @Test
  @DisplayName("Relation: isAfter - interval comes completely after another")
  void relation_IsAfter_Success() {

    var relation = baseInterval.getRelationTo(interval(-20, -11));

    assertRelation(relation, IntervalsRelation::isBefore, false);
    assertRelation(relation, IntervalsRelation::meets, false);
    assertRelation(relation, IntervalsRelation::overlaps, false);
    assertRelation(relation, IntervalsRelation::starts, false);
    assertRelation(relation, IntervalsRelation::during, false);
    assertRelation(relation, IntervalsRelation::finishes, false);
    assertRelation(relation, IntervalsRelation::equalsTo, false);
    assertRelation(relation, IntervalsRelation::isFinishedBy, false);
    assertRelation(relation, IntervalsRelation::contains, false);
    assertRelation(relation, IntervalsRelation::isStartedBy, false);
    assertRelation(relation, IntervalsRelation::isOverlappedBy, false);
    assertRelation(relation, IntervalsRelation::isMetBy, false);
    assertRelation(relation, IntervalsRelation::isAfter, true);
    assertRelation(relation, IntervalsRelation::distinct, true);
    assertRelation(relation, IntervalsRelation::intersects, false);
    assertRelation(relation, IntervalsRelation::adjacent, false);
    assertRelation(relation, IntervalsRelation::connects, false);

    assertRelation(baseInterval.getRelationTo(interval(-20, -15)), IntervalsRelation::isAfter, true);
    assertRelation(baseInterval.getRelationTo(interval(-20, -10)), IntervalsRelation::isAfter, false);
    assertRelation(baseInterval.getRelationTo(interval(-20, 0)), IntervalsRelation::isAfter, false);
    assertRelation(baseInterval.getRelationTo(interval(-20, 15)), IntervalsRelation::isAfter, false);
    assertRelation(baseInterval.getRelationTo(interval(-20, 10)), IntervalsRelation::isAfter, false);
    assertRelation(baseInterval.getRelationTo(interval(-10, 0)), IntervalsRelation::isAfter, false);
    assertRelation(baseInterval.getRelationTo(interval(-10, 10)), IntervalsRelation::isAfter, false);
    assertRelation(baseInterval.getRelationTo(interval(-10, 15)), IntervalsRelation::isAfter, false);
    assertRelation(baseInterval.getRelationTo(interval(0, 10)), IntervalsRelation::isAfter, false);
    assertRelation(baseInterval.getRelationTo(interval(0, 5)), IntervalsRelation::isAfter, false);
    assertRelation(baseInterval.getRelationTo(interval(0, 15)), IntervalsRelation::isAfter, false);
    assertRelation(baseInterval.getRelationTo(interval(10, 25)), IntervalsRelation::isAfter, false);
    assertRelation(baseInterval.getRelationTo(interval(15, 20)), IntervalsRelation::isAfter, false);


  }

  @Test
  @DisplayName("Relation: isMetBy - interval is adjacent to another, ending where it starts")
  void relation_IsMetBy_Success() {

    var relation = baseInterval.getRelationTo(interval(-20, -10));

    assertRelation(relation, IntervalsRelation::isBefore, false);
    assertRelation(relation, IntervalsRelation::meets, false);
    assertRelation(relation, IntervalsRelation::overlaps, false);
    assertRelation(relation, IntervalsRelation::starts, false);
    assertRelation(relation, IntervalsRelation::during, false);
    assertRelation(relation, IntervalsRelation::finishes, false);
    assertRelation(relation, IntervalsRelation::equalsTo, false);
    assertRelation(relation, IntervalsRelation::isFinishedBy, false);
    assertRelation(relation, IntervalsRelation::contains, false);
    assertRelation(relation, IntervalsRelation::isStartedBy, false);
    assertRelation(relation, IntervalsRelation::isOverlappedBy, false);
    assertRelation(relation, IntervalsRelation::isMetBy, true);
    assertRelation(relation, IntervalsRelation::isAfter, false);
    assertRelation(relation, IntervalsRelation::distinct, true);
    assertRelation(relation, IntervalsRelation::intersects, false);
    assertRelation(relation, IntervalsRelation::adjacent, true);
    assertRelation(relation, IntervalsRelation::connects, true);

    assertRelation(baseInterval.getRelationTo(interval(-20, -15)), IntervalsRelation::isMetBy, false);
    assertRelation(baseInterval.getRelationTo(interval(-20, -10)), IntervalsRelation::isMetBy, true);
    assertRelation(baseInterval.getRelationTo(interval(-20, 0)), IntervalsRelation::isMetBy, false);
    assertRelation(baseInterval.getRelationTo(interval(-20, 15)), IntervalsRelation::isMetBy, false);
    assertRelation(baseInterval.getRelationTo(interval(-20, 10)), IntervalsRelation::isMetBy, false);
    assertRelation(baseInterval.getRelationTo(interval(-10, 0)), IntervalsRelation::isMetBy, false);
    assertRelation(baseInterval.getRelationTo(interval(-10, 10)), IntervalsRelation::isMetBy, false);
    assertRelation(baseInterval.getRelationTo(interval(-10, 15)), IntervalsRelation::isMetBy, false);
    assertRelation(baseInterval.getRelationTo(interval(0, 10)), IntervalsRelation::isMetBy, false);
    assertRelation(baseInterval.getRelationTo(interval(0, 5)), IntervalsRelation::isMetBy, false);
    assertRelation(baseInterval.getRelationTo(interval(0, 15)), IntervalsRelation::isMetBy, false);
    assertRelation(baseInterval.getRelationTo(interval(10, 25)), IntervalsRelation::isMetBy, false);
    assertRelation(baseInterval.getRelationTo(interval(15, 20)), IntervalsRelation::isMetBy, false);

  }

  @Test
  @DisplayName("Relation: isOverlappedBy - interval is overlapped by another from the left")
  void relation_IsOverlappedBy_Success() {

    var relation = baseInterval.getRelationTo(interval(-20, 0));

    assertRelation(relation, IntervalsRelation::isBefore, false);
    assertRelation(relation, IntervalsRelation::meets, false);
    assertRelation(relation, IntervalsRelation::overlaps, false);
    assertRelation(relation, IntervalsRelation::starts, false);
    assertRelation(relation, IntervalsRelation::during, false);
    assertRelation(relation, IntervalsRelation::finishes, false);
    assertRelation(relation, IntervalsRelation::equalsTo, false);
    assertRelation(relation, IntervalsRelation::isFinishedBy, false);
    assertRelation(relation, IntervalsRelation::contains, false);
    assertRelation(relation, IntervalsRelation::isStartedBy, false);
    assertRelation(relation, IntervalsRelation::isOverlappedBy, true);
    assertRelation(relation, IntervalsRelation::isMetBy, false);
    assertRelation(relation, IntervalsRelation::isAfter, false);
    assertRelation(relation, IntervalsRelation::distinct, false);
    assertRelation(relation, IntervalsRelation::intersects, true);
    assertRelation(relation, IntervalsRelation::adjacent, false);
    assertRelation(relation, IntervalsRelation::connects, true);

    assertRelation(baseInterval.getRelationTo(interval(-20, -15)), IntervalsRelation::isOverlappedBy, false);
    assertRelation(baseInterval.getRelationTo(interval(-20, -10)), IntervalsRelation::isOverlappedBy, false);
    assertRelation(baseInterval.getRelationTo(interval(-20, 0)), IntervalsRelation::isOverlappedBy, true);
    assertRelation(baseInterval.getRelationTo(interval(-20, 15)), IntervalsRelation::isOverlappedBy, false);
    assertRelation(baseInterval.getRelationTo(interval(-20, 10)), IntervalsRelation::isOverlappedBy, false);
    assertRelation(baseInterval.getRelationTo(interval(-10, 0)), IntervalsRelation::isOverlappedBy, false);
    assertRelation(baseInterval.getRelationTo(interval(-10, 10)), IntervalsRelation::isOverlappedBy, false);
    assertRelation(baseInterval.getRelationTo(interval(-10, 15)), IntervalsRelation::isOverlappedBy, false);
    assertRelation(baseInterval.getRelationTo(interval(0, 10)), IntervalsRelation::isOverlappedBy, false);
    assertRelation(baseInterval.getRelationTo(interval(0, 5)), IntervalsRelation::isOverlappedBy, false);
    assertRelation(baseInterval.getRelationTo(interval(0, 15)), IntervalsRelation::isOverlappedBy, false);
    assertRelation(baseInterval.getRelationTo(interval(10, 25)), IntervalsRelation::isOverlappedBy, false);
    assertRelation(baseInterval.getRelationTo(interval(15, 20)), IntervalsRelation::isOverlappedBy, false);

  }

  @Test
  @DisplayName("Relation: isStartedBy - interval is started by a shorter interval")
  void relation_IsStartedBy_Success() {

    var relation = baseInterval.getRelationTo(interval(-10, 0));

    assertRelation(relation, IntervalsRelation::isBefore, false);
    assertRelation(relation, IntervalsRelation::meets, false);
    assertRelation(relation, IntervalsRelation::overlaps, false);
    assertRelation(relation, IntervalsRelation::starts, false);
    assertRelation(relation, IntervalsRelation::during, false);
    assertRelation(relation, IntervalsRelation::finishes, false);
    assertRelation(relation, IntervalsRelation::equalsTo, false);
    assertRelation(relation, IntervalsRelation::isFinishedBy, false);
    assertRelation(relation, IntervalsRelation::contains, false);
    assertRelation(relation, IntervalsRelation::isStartedBy, true);
    assertRelation(relation, IntervalsRelation::isOverlappedBy, false);
    assertRelation(relation, IntervalsRelation::isMetBy, false);
    assertRelation(relation, IntervalsRelation::isAfter, false);
    assertRelation(relation, IntervalsRelation::distinct, false);
    assertRelation(relation, IntervalsRelation::intersects, true);
    assertRelation(relation, IntervalsRelation::adjacent, false);
    assertRelation(relation, IntervalsRelation::connects, true);

    assertRelation(baseInterval.getRelationTo(interval(-20, -15)), IntervalsRelation::isStartedBy, false);
    assertRelation(baseInterval.getRelationTo(interval(-20, -10)), IntervalsRelation::isStartedBy, false);
    assertRelation(baseInterval.getRelationTo(interval(-20, 0)), IntervalsRelation::isStartedBy, false);
    assertRelation(baseInterval.getRelationTo(interval(-20, 15)), IntervalsRelation::isStartedBy, false);
    assertRelation(baseInterval.getRelationTo(interval(-20, 10)), IntervalsRelation::isStartedBy, false);
    assertRelation(baseInterval.getRelationTo(interval(-10, 0)), IntervalsRelation::isStartedBy, true);
    assertRelation(baseInterval.getRelationTo(interval(-10, 10)), IntervalsRelation::isStartedBy, false);
    assertRelation(baseInterval.getRelationTo(interval(-10, 15)), IntervalsRelation::isStartedBy, false);
    assertRelation(baseInterval.getRelationTo(interval(0, 10)), IntervalsRelation::isStartedBy, false);
    assertRelation(baseInterval.getRelationTo(interval(0, 5)), IntervalsRelation::isStartedBy, false);
    assertRelation(baseInterval.getRelationTo(interval(0, 15)), IntervalsRelation::isStartedBy, false);
    assertRelation(baseInterval.getRelationTo(interval(10, 25)), IntervalsRelation::isStartedBy, false);
    assertRelation(baseInterval.getRelationTo(interval(15, 20)), IntervalsRelation::isStartedBy, false);

  }

  @Test
  @DisplayName("Relation: contains - interval completely contains another")
  void relation_Contains_Success() {

    var relation = baseInterval.getRelationTo(interval(-9, 9));

    assertRelation(relation, IntervalsRelation::isBefore, false);
    assertRelation(relation, IntervalsRelation::meets, false);
    assertRelation(relation, IntervalsRelation::overlaps, false);
    assertRelation(relation, IntervalsRelation::starts, false);
    assertRelation(relation, IntervalsRelation::during, false);
    assertRelation(relation, IntervalsRelation::finishes, false);
    assertRelation(relation, IntervalsRelation::equalsTo, false);
    assertRelation(relation, IntervalsRelation::isFinishedBy, false);
    assertRelation(relation, IntervalsRelation::contains, true);
    assertRelation(relation, IntervalsRelation::isStartedBy, false);
    assertRelation(relation, IntervalsRelation::isOverlappedBy, false);
    assertRelation(relation, IntervalsRelation::isMetBy, false);
    assertRelation(relation, IntervalsRelation::isAfter, false);
    assertRelation(relation, IntervalsRelation::distinct, false);
    assertRelation(relation, IntervalsRelation::intersects, true);
    assertRelation(relation, IntervalsRelation::adjacent, false);
    assertRelation(relation, IntervalsRelation::connects, true);

    assertRelation(baseInterval.getRelationTo(interval(-20, -15)), IntervalsRelation::contains, false);
    assertRelation(baseInterval.getRelationTo(interval(-20, -10)), IntervalsRelation::contains, false);
    assertRelation(baseInterval.getRelationTo(interval(-20, 0)), IntervalsRelation::contains, false);
    assertRelation(baseInterval.getRelationTo(interval(-20, 15)), IntervalsRelation::contains, false);
    assertRelation(baseInterval.getRelationTo(interval(-20, 10)), IntervalsRelation::contains, false);
    assertRelation(baseInterval.getRelationTo(interval(-10, 0)), IntervalsRelation::contains, false);
    assertRelation(baseInterval.getRelationTo(interval(-10, 10)), IntervalsRelation::contains, false);
    assertRelation(baseInterval.getRelationTo(interval(-10, 15)), IntervalsRelation::contains, false);
    assertRelation(baseInterval.getRelationTo(interval(0, 10)), IntervalsRelation::contains, false);
    assertRelation(baseInterval.getRelationTo(interval(0, 5)), IntervalsRelation::contains, true);
    assertRelation(baseInterval.getRelationTo(interval(0, 15)), IntervalsRelation::contains, false);
    assertRelation(baseInterval.getRelationTo(interval(10, 25)), IntervalsRelation::contains, false);
    assertRelation(baseInterval.getRelationTo(interval(15, 20)), IntervalsRelation::contains, false);

  }

  @Test
  @DisplayName("Relation: isFinishedBy - interval is finished by a shorter interval")
  void relation_IsFinishedBy_Success() {

    var relation = baseInterval.getRelationTo(interval(-9, 10));

    assertRelation(relation, IntervalsRelation::isBefore, false);
    assertRelation(relation, IntervalsRelation::meets, false);
    assertRelation(relation, IntervalsRelation::overlaps, false);
    assertRelation(relation, IntervalsRelation::starts, false);
    assertRelation(relation, IntervalsRelation::during, false);
    assertRelation(relation, IntervalsRelation::finishes, false);
    assertRelation(relation, IntervalsRelation::equalsTo, false);
    assertRelation(relation, IntervalsRelation::isFinishedBy, true);
    assertRelation(relation, IntervalsRelation::contains, false);
    assertRelation(relation, IntervalsRelation::isStartedBy, false);
    assertRelation(relation, IntervalsRelation::isOverlappedBy, false);
    assertRelation(relation, IntervalsRelation::isMetBy, false);
    assertRelation(relation, IntervalsRelation::isAfter, false);
    assertRelation(relation, IntervalsRelation::distinct, false);
    assertRelation(relation, IntervalsRelation::intersects, true);
    assertRelation(relation, IntervalsRelation::adjacent, false);
    assertRelation(relation, IntervalsRelation::connects, true);

    assertRelation(baseInterval.getRelationTo(interval(-20, -15)), IntervalsRelation::isFinishedBy, false);
    assertRelation(baseInterval.getRelationTo(interval(-20, -10)), IntervalsRelation::isFinishedBy, false);
    assertRelation(baseInterval.getRelationTo(interval(-20, 0)), IntervalsRelation::isFinishedBy, false);
    assertRelation(baseInterval.getRelationTo(interval(-20, 15)), IntervalsRelation::isFinishedBy, false);
    assertRelation(baseInterval.getRelationTo(interval(-20, 10)), IntervalsRelation::isFinishedBy, false);
    assertRelation(baseInterval.getRelationTo(interval(-10, 0)), IntervalsRelation::isFinishedBy, false);
    assertRelation(baseInterval.getRelationTo(interval(-10, 10)), IntervalsRelation::isFinishedBy, false);
    assertRelation(baseInterval.getRelationTo(interval(-10, 15)), IntervalsRelation::isFinishedBy, false);
    assertRelation(baseInterval.getRelationTo(interval(0, 10)), IntervalsRelation::isFinishedBy, true);
    assertRelation(baseInterval.getRelationTo(interval(0, 5)), IntervalsRelation::isFinishedBy, false);
    assertRelation(baseInterval.getRelationTo(interval(0, 15)), IntervalsRelation::isFinishedBy, false);
    assertRelation(baseInterval.getRelationTo(interval(10, 25)), IntervalsRelation::isFinishedBy, false);
    assertRelation(baseInterval.getRelationTo(interval(15, 20)), IntervalsRelation::isFinishedBy, false);

  }

  @Test
  @DisplayName("Relation: equalsTo - intervals are equal")
  void relation_EqualsTo_Success() {

    var relation = baseInterval.getRelationTo(interval(-10, 10));

    assertRelation(relation, IntervalsRelation::isBefore, false);
    assertRelation(relation, IntervalsRelation::meets, false);
    assertRelation(relation, IntervalsRelation::overlaps, false);
    assertRelation(relation, IntervalsRelation::starts, false);
    assertRelation(relation, IntervalsRelation::during, false);
    assertRelation(relation, IntervalsRelation::finishes, false);
    assertRelation(relation, IntervalsRelation::equalsTo, true);
    assertRelation(relation, IntervalsRelation::isFinishedBy, false);
    assertRelation(relation, IntervalsRelation::contains, false);
    assertRelation(relation, IntervalsRelation::isStartedBy, false);
    assertRelation(relation, IntervalsRelation::isOverlappedBy, false);
    assertRelation(relation, IntervalsRelation::isMetBy, false);
    assertRelation(relation, IntervalsRelation::isAfter, false);
    assertRelation(relation, IntervalsRelation::distinct, false);
    assertRelation(relation, IntervalsRelation::intersects, true);
    assertRelation(relation, IntervalsRelation::adjacent, false);
    assertRelation(relation, IntervalsRelation::connects, true);

    assertRelation(baseInterval.getRelationTo(interval(-20, -15)), IntervalsRelation::equalsTo, false);
    assertRelation(baseInterval.getRelationTo(interval(-20, -10)), IntervalsRelation::equalsTo, false);
    assertRelation(baseInterval.getRelationTo(interval(-20, 0)), IntervalsRelation::equalsTo, false);
    assertRelation(baseInterval.getRelationTo(interval(-20, 15)), IntervalsRelation::equalsTo, false);
    assertRelation(baseInterval.getRelationTo(interval(-20, 10)), IntervalsRelation::equalsTo, false);
    assertRelation(baseInterval.getRelationTo(interval(-10, 0)), IntervalsRelation::equalsTo, false);
    assertRelation(baseInterval.getRelationTo(interval(-10, 10)), IntervalsRelation::equalsTo, true);
    assertRelation(baseInterval.getRelationTo(interval(-10, 15)), IntervalsRelation::equalsTo, false);
    assertRelation(baseInterval.getRelationTo(interval(0, 10)), IntervalsRelation::equalsTo, false);
    assertRelation(baseInterval.getRelationTo(interval(0, 5)), IntervalsRelation::equalsTo, false);
    assertRelation(baseInterval.getRelationTo(interval(0, 15)), IntervalsRelation::equalsTo, false);
    assertRelation(baseInterval.getRelationTo(interval(10, 25)), IntervalsRelation::equalsTo, false);
    assertRelation(baseInterval.getRelationTo(interval(15, 20)), IntervalsRelation::equalsTo, false);

  }

  @Test
  @DisplayName("Relation: finishes - interval finishes a longer interval")
  void relation_Finishes_Success() {

    var relation = baseInterval.getRelationTo(interval(-20, 10));

    assertRelation(relation, IntervalsRelation::isBefore, false);
    assertRelation(relation, IntervalsRelation::meets, false);
    assertRelation(relation, IntervalsRelation::overlaps, false);
    assertRelation(relation, IntervalsRelation::starts, false);
    assertRelation(relation, IntervalsRelation::during, false);
    assertRelation(relation, IntervalsRelation::finishes, true);
    assertRelation(relation, IntervalsRelation::equalsTo, false);
    assertRelation(relation, IntervalsRelation::isFinishedBy, false);
    assertRelation(relation, IntervalsRelation::contains, false);
    assertRelation(relation, IntervalsRelation::isStartedBy, false);
    assertRelation(relation, IntervalsRelation::isOverlappedBy, false);
    assertRelation(relation, IntervalsRelation::isMetBy, false);
    assertRelation(relation, IntervalsRelation::isAfter, false);
    assertRelation(relation, IntervalsRelation::distinct, false);
    assertRelation(relation, IntervalsRelation::intersects, true);
    assertRelation(relation, IntervalsRelation::adjacent, false);
    assertRelation(relation, IntervalsRelation::connects, true);

    assertRelation(baseInterval.getRelationTo(interval(-20, -15)), IntervalsRelation::finishes, false);
    assertRelation(baseInterval.getRelationTo(interval(-20, -10)), IntervalsRelation::finishes, false);
    assertRelation(baseInterval.getRelationTo(interval(-20, 0)), IntervalsRelation::finishes, false);
    assertRelation(baseInterval.getRelationTo(interval(-20, 15)), IntervalsRelation::finishes, false);
    assertRelation(baseInterval.getRelationTo(interval(-20, 10)), IntervalsRelation::finishes, true);
    assertRelation(baseInterval.getRelationTo(interval(-10, 0)), IntervalsRelation::finishes, false);
    assertRelation(baseInterval.getRelationTo(interval(-10, 10)), IntervalsRelation::finishes, false);
    assertRelation(baseInterval.getRelationTo(interval(-10, 15)), IntervalsRelation::finishes, false);
    assertRelation(baseInterval.getRelationTo(interval(0, 10)), IntervalsRelation::finishes, false);
    assertRelation(baseInterval.getRelationTo(interval(0, 5)), IntervalsRelation::finishes, false);
    assertRelation(baseInterval.getRelationTo(interval(0, 15)), IntervalsRelation::finishes, false);
    assertRelation(baseInterval.getRelationTo(interval(10, 25)), IntervalsRelation::finishes, false);
    assertRelation(baseInterval.getRelationTo(interval(15, 20)), IntervalsRelation::finishes, false);

  }

  @Test
  @DisplayName("Relation: during - interval is during a longer interval")
  void relation_During_Success() {

    var relation = baseInterval.getRelationTo(interval(-20, 20));

    assertRelation(relation, IntervalsRelation::isBefore, false);
    assertRelation(relation, IntervalsRelation::meets, false);
    assertRelation(relation, IntervalsRelation::overlaps, false);
    assertRelation(relation, IntervalsRelation::starts, false);
    assertRelation(relation, IntervalsRelation::during, true);
    assertRelation(relation, IntervalsRelation::finishes, false);
    assertRelation(relation, IntervalsRelation::equalsTo, false);
    assertRelation(relation, IntervalsRelation::isFinishedBy, false);
    assertRelation(relation, IntervalsRelation::contains, false);
    assertRelation(relation, IntervalsRelation::isStartedBy, false);
    assertRelation(relation, IntervalsRelation::isOverlappedBy, false);
    assertRelation(relation, IntervalsRelation::isMetBy, false);
    assertRelation(relation, IntervalsRelation::isAfter, false);
    assertRelation(relation, IntervalsRelation::distinct, false);
    assertRelation(relation, IntervalsRelation::intersects, true);
    assertRelation(relation, IntervalsRelation::adjacent, false);
    assertRelation(relation, IntervalsRelation::connects, true);

    assertRelation(baseInterval.getRelationTo(interval(-20, -15)), IntervalsRelation::during, false);
    assertRelation(baseInterval.getRelationTo(interval(-20, -10)), IntervalsRelation::during, false);
    assertRelation(baseInterval.getRelationTo(interval(-20, 0)), IntervalsRelation::during, false);
    assertRelation(baseInterval.getRelationTo(interval(-20, 15)), IntervalsRelation::during, true);
    assertRelation(baseInterval.getRelationTo(interval(-20, 10)), IntervalsRelation::during, false);
    assertRelation(baseInterval.getRelationTo(interval(-10, 0)), IntervalsRelation::during, false);
    assertRelation(baseInterval.getRelationTo(interval(-10, 10)), IntervalsRelation::during, false);
    assertRelation(baseInterval.getRelationTo(interval(-10, 15)), IntervalsRelation::during, false);
    assertRelation(baseInterval.getRelationTo(interval(0, 10)), IntervalsRelation::during, false);
    assertRelation(baseInterval.getRelationTo(interval(0, 5)), IntervalsRelation::during, false);
    assertRelation(baseInterval.getRelationTo(interval(0, 15)), IntervalsRelation::during, false);
    assertRelation(baseInterval.getRelationTo(interval(10, 25)), IntervalsRelation::during, false);
    assertRelation(baseInterval.getRelationTo(interval(15, 20)), IntervalsRelation::during, false);

  }

  @Test
  @DisplayName("Relation: starts - interval starts a longer interval")
  void relation_Starts_Success() {

    var relation = baseInterval.getRelationTo(interval(-10, 20));

    assertRelation(relation, IntervalsRelation::isBefore, false);
    assertRelation(relation, IntervalsRelation::meets, false);
    assertRelation(relation, IntervalsRelation::overlaps, false);
    assertRelation(relation, IntervalsRelation::starts, true);
    assertRelation(relation, IntervalsRelation::during, false);
    assertRelation(relation, IntervalsRelation::finishes, false);
    assertRelation(relation, IntervalsRelation::equalsTo, false);
    assertRelation(relation, IntervalsRelation::isFinishedBy, false);
    assertRelation(relation, IntervalsRelation::contains, false);
    assertRelation(relation, IntervalsRelation::isStartedBy, false);
    assertRelation(relation, IntervalsRelation::isOverlappedBy, false);
    assertRelation(relation, IntervalsRelation::isMetBy, false);
    assertRelation(relation, IntervalsRelation::isAfter, false);
    assertRelation(relation, IntervalsRelation::distinct, false);
    assertRelation(relation, IntervalsRelation::intersects, true);
    assertRelation(relation, IntervalsRelation::adjacent, false);
    assertRelation(relation, IntervalsRelation::connects, true);

    assertRelation(baseInterval.getRelationTo(interval(-20, -15)), IntervalsRelation::starts, false);
    assertRelation(baseInterval.getRelationTo(interval(-20, -10)), IntervalsRelation::starts, false);
    assertRelation(baseInterval.getRelationTo(interval(-20, 0)), IntervalsRelation::starts, false);
    assertRelation(baseInterval.getRelationTo(interval(-20, 15)), IntervalsRelation::starts, false);
    assertRelation(baseInterval.getRelationTo(interval(-20, 10)), IntervalsRelation::starts, false);
    assertRelation(baseInterval.getRelationTo(interval(-10, 0)), IntervalsRelation::starts, false);
    assertRelation(baseInterval.getRelationTo(interval(-10, 10)), IntervalsRelation::starts, false);
    assertRelation(baseInterval.getRelationTo(interval(-10, 15)), IntervalsRelation::starts, true);
    assertRelation(baseInterval.getRelationTo(interval(0, 10)), IntervalsRelation::starts, false);
    assertRelation(baseInterval.getRelationTo(interval(0, 5)), IntervalsRelation::starts, false);
    assertRelation(baseInterval.getRelationTo(interval(0, 15)), IntervalsRelation::starts, false);
    assertRelation(baseInterval.getRelationTo(interval(10, 25)), IntervalsRelation::starts, false);
    assertRelation(baseInterval.getRelationTo(interval(15, 20)), IntervalsRelation::starts, false);

  }

  @Test
  @DisplayName("Relation: overlaps - interval overlaps another to the right")
  void relation_Overlaps_Success() {

    var relation = baseInterval.getRelationTo(interval(0, 20));

    assertRelation(relation, IntervalsRelation::isBefore, false);
    assertRelation(relation, IntervalsRelation::meets, false);
    assertRelation(relation, IntervalsRelation::overlaps, true);
    assertRelation(relation, IntervalsRelation::starts, false);
    assertRelation(relation, IntervalsRelation::during, false);
    assertRelation(relation, IntervalsRelation::finishes, false);
    assertRelation(relation, IntervalsRelation::equalsTo, false);
    assertRelation(relation, IntervalsRelation::isFinishedBy, false);
    assertRelation(relation, IntervalsRelation::contains, false);
    assertRelation(relation, IntervalsRelation::isStartedBy, false);
    assertRelation(relation, IntervalsRelation::isOverlappedBy, false);
    assertRelation(relation, IntervalsRelation::isMetBy, false);
    assertRelation(relation, IntervalsRelation::isAfter, false);
    assertRelation(relation, IntervalsRelation::distinct, false);
    assertRelation(relation, IntervalsRelation::intersects, true);
    assertRelation(relation, IntervalsRelation::adjacent, false);
    assertRelation(relation, IntervalsRelation::connects, true);

    assertRelation(baseInterval.getRelationTo(interval(-20, -15)), IntervalsRelation::overlaps, false);
    assertRelation(baseInterval.getRelationTo(interval(-20, -10)), IntervalsRelation::overlaps, false);
    assertRelation(baseInterval.getRelationTo(interval(-20, 0)), IntervalsRelation::overlaps, false);
    assertRelation(baseInterval.getRelationTo(interval(-20, 15)), IntervalsRelation::overlaps, false);
    assertRelation(baseInterval.getRelationTo(interval(-20, 10)), IntervalsRelation::overlaps, false);
    assertRelation(baseInterval.getRelationTo(interval(-10, 0)), IntervalsRelation::overlaps, false);
    assertRelation(baseInterval.getRelationTo(interval(-10, 10)), IntervalsRelation::overlaps, false);
    assertRelation(baseInterval.getRelationTo(interval(-10, 15)), IntervalsRelation::overlaps, false);
    assertRelation(baseInterval.getRelationTo(interval(0, 10)), IntervalsRelation::overlaps, false);
    assertRelation(baseInterval.getRelationTo(interval(0, 5)), IntervalsRelation::overlaps, false);
    assertRelation(baseInterval.getRelationTo(interval(0, 15)), IntervalsRelation::overlaps, true);
    assertRelation(baseInterval.getRelationTo(interval(10, 25)), IntervalsRelation::overlaps, false);
    assertRelation(baseInterval.getRelationTo(interval(15, 20)), IntervalsRelation::overlaps, false);

  }

  @Test
  @DisplayName("Relation: meets - interval meets another adjacently")
  void relation_Meets_Success() {

    var relation = baseInterval.getRelationTo(interval(10, 20));

    assertRelation(relation, IntervalsRelation::isBefore, false);
    assertRelation(relation, IntervalsRelation::meets, true);
    assertRelation(relation, IntervalsRelation::overlaps, false);
    assertRelation(relation, IntervalsRelation::starts, false);
    assertRelation(relation, IntervalsRelation::during, false);
    assertRelation(relation, IntervalsRelation::finishes, false);
    assertRelation(relation, IntervalsRelation::equalsTo, false);
    assertRelation(relation, IntervalsRelation::isFinishedBy, false);
    assertRelation(relation, IntervalsRelation::contains, false);
    assertRelation(relation, IntervalsRelation::isStartedBy, false);
    assertRelation(relation, IntervalsRelation::isOverlappedBy, false);
    assertRelation(relation, IntervalsRelation::isMetBy, false);
    assertRelation(relation, IntervalsRelation::isAfter, false);
    assertRelation(relation, IntervalsRelation::distinct, true);
    assertRelation(relation, IntervalsRelation::intersects, false);
    assertRelation(relation, IntervalsRelation::adjacent, true);
    assertRelation(relation, IntervalsRelation::connects, true);

    assertRelation(baseInterval.getRelationTo(interval(-20, -15)), IntervalsRelation::meets, false);
    assertRelation(baseInterval.getRelationTo(interval(-20, -10)), IntervalsRelation::meets, false);
    assertRelation(baseInterval.getRelationTo(interval(-20, 0)), IntervalsRelation::meets, false);
    assertRelation(baseInterval.getRelationTo(interval(-20, 15)), IntervalsRelation::meets, false);
    assertRelation(baseInterval.getRelationTo(interval(-20, 10)), IntervalsRelation::meets, false);
    assertRelation(baseInterval.getRelationTo(interval(-10, 0)), IntervalsRelation::meets, false);
    assertRelation(baseInterval.getRelationTo(interval(-10, 10)), IntervalsRelation::meets, false);
    assertRelation(baseInterval.getRelationTo(interval(-10, 15)), IntervalsRelation::meets, false);
    assertRelation(baseInterval.getRelationTo(interval(0, 10)), IntervalsRelation::meets, false);
    assertRelation(baseInterval.getRelationTo(interval(0, 5)), IntervalsRelation::meets, false);
    assertRelation(baseInterval.getRelationTo(interval(0, 15)), IntervalsRelation::meets, false);
    assertRelation(baseInterval.getRelationTo(interval(10, 25)), IntervalsRelation::meets, true);
    assertRelation(baseInterval.getRelationTo(interval(15, 20)), IntervalsRelation::meets, false);

  }

  @Test
  @DisplayName("Relation: isBefore - interval comes completely before another")
  void relation_IsBefore_Success() {

    var relation = baseInterval.getRelationTo(interval(15, 20));

    assertRelation(relation, IntervalsRelation::isBefore, true);
    assertRelation(relation, IntervalsRelation::meets, false);
    assertRelation(relation, IntervalsRelation::overlaps, false);
    assertRelation(relation, IntervalsRelation::starts, false);
    assertRelation(relation, IntervalsRelation::during, false);
    assertRelation(relation, IntervalsRelation::finishes, false);
    assertRelation(relation, IntervalsRelation::equalsTo, false);
    assertRelation(relation, IntervalsRelation::isFinishedBy, false);
    assertRelation(relation, IntervalsRelation::contains, false);
    assertRelation(relation, IntervalsRelation::isStartedBy, false);
    assertRelation(relation, IntervalsRelation::isOverlappedBy, false);
    assertRelation(relation, IntervalsRelation::isMetBy, false);
    assertRelation(relation, IntervalsRelation::isAfter, false);
    assertRelation(relation, IntervalsRelation::distinct, true);
    assertRelation(relation, IntervalsRelation::intersects, false);
    assertRelation(relation, IntervalsRelation::adjacent, false);
    assertRelation(relation, IntervalsRelation::connects, false);

    assertRelation(baseInterval.getRelationTo(interval(-20, -15)), IntervalsRelation::isBefore, false);
    assertRelation(baseInterval.getRelationTo(interval(-20, -10)), IntervalsRelation::isBefore, false);
    assertRelation(baseInterval.getRelationTo(interval(-20, 0)), IntervalsRelation::isBefore, false);
    assertRelation(baseInterval.getRelationTo(interval(-20, 15)), IntervalsRelation::isBefore, false);
    assertRelation(baseInterval.getRelationTo(interval(-20, 10)), IntervalsRelation::isBefore, false);
    assertRelation(baseInterval.getRelationTo(interval(-10, 0)), IntervalsRelation::isBefore, false);
    assertRelation(baseInterval.getRelationTo(interval(-10, 10)), IntervalsRelation::isBefore, false);
    assertRelation(baseInterval.getRelationTo(interval(-10, 15)), IntervalsRelation::isBefore, false);
    assertRelation(baseInterval.getRelationTo(interval(0, 10)), IntervalsRelation::isBefore, false);
    assertRelation(baseInterval.getRelationTo(interval(0, 5)), IntervalsRelation::isBefore, false);
    assertRelation(baseInterval.getRelationTo(interval(0, 15)), IntervalsRelation::isBefore, false);
    assertRelation(baseInterval.getRelationTo(interval(10, 25)), IntervalsRelation::isBefore, false);
    assertRelation(baseInterval.getRelationTo(interval(15, 20)), IntervalsRelation::isBefore, true);

  }

  @Test
  @DisplayName("Relation: connects - intervals are connected (adjacent or intersecting)")
  void relation_Connects_Success() {
    var relation = baseInterval.getRelationTo(interval(10, 20));

    assertRelation(relation, IntervalsRelation::isBefore, false);
    assertRelation(relation, IntervalsRelation::meets, true);
    assertRelation(relation, IntervalsRelation::overlaps, false);
    assertRelation(relation, IntervalsRelation::starts, false);
    assertRelation(relation, IntervalsRelation::during, false);
    assertRelation(relation, IntervalsRelation::finishes, false);
    assertRelation(relation, IntervalsRelation::equalsTo, false);
    assertRelation(relation, IntervalsRelation::isFinishedBy, false);
    assertRelation(relation, IntervalsRelation::contains, false);
    assertRelation(relation, IntervalsRelation::isStartedBy, false);
    assertRelation(relation, IntervalsRelation::isOverlappedBy, false);
    assertRelation(relation, IntervalsRelation::isMetBy, false);
    assertRelation(relation, IntervalsRelation::isAfter, false);
    assertRelation(relation, IntervalsRelation::distinct, true);
    assertRelation(relation, IntervalsRelation::intersects, false);
    assertRelation(relation, IntervalsRelation::adjacent, true);
    assertRelation(relation, IntervalsRelation::connects, true);

    assertRelation(baseInterval.getRelationTo(interval(-20, -15)), IntervalsRelation::connects, false);
    assertRelation(baseInterval.getRelationTo(interval(-20, -10)), IntervalsRelation::connects, true);
    assertRelation(baseInterval.getRelationTo(interval(-20, 0)), IntervalsRelation::connects, true);
    assertRelation(baseInterval.getRelationTo(interval(-20, 15)), IntervalsRelation::connects, true);
    assertRelation(baseInterval.getRelationTo(interval(-20, 10)), IntervalsRelation::connects, true);
    assertRelation(baseInterval.getRelationTo(interval(-10, 0)), IntervalsRelation::connects, true);
    assertRelation(baseInterval.getRelationTo(interval(-10, 10)), IntervalsRelation::connects, true);
    assertRelation(baseInterval.getRelationTo(interval(-10, 15)), IntervalsRelation::connects, true);
    assertRelation(baseInterval.getRelationTo(interval(0, 10)), IntervalsRelation::connects, true);
    assertRelation(baseInterval.getRelationTo(interval(0, 5)), IntervalsRelation::connects, true);
    assertRelation(baseInterval.getRelationTo(interval(0, 15)), IntervalsRelation::connects, true);
    assertRelation(baseInterval.getRelationTo(interval(10, 25)), IntervalsRelation::connects, true);
    assertRelation(baseInterval.getRelationTo(interval(15, 20)), IntervalsRelation::connects, false);

  }

  @Test
  @DisplayName("Relation: adjacent - intervals are adjacent (touching)")
  void relation_Adjacent_Success() {

    assertRelation(baseInterval.getRelationTo(interval(-20, -15)), IntervalsRelation::adjacent, false);
    assertRelation(baseInterval.getRelationTo(interval(-20, -10)), IntervalsRelation::adjacent, true);
    assertRelation(baseInterval.getRelationTo(interval(-20, 0)), IntervalsRelation::adjacent, false);
    assertRelation(baseInterval.getRelationTo(interval(-20, 15)), IntervalsRelation::adjacent, false);
    assertRelation(baseInterval.getRelationTo(interval(-20, 10)), IntervalsRelation::adjacent, false);
    assertRelation(baseInterval.getRelationTo(interval(-10, 0)), IntervalsRelation::adjacent, false);
    assertRelation(baseInterval.getRelationTo(interval(-10, 10)), IntervalsRelation::adjacent, false);
    assertRelation(baseInterval.getRelationTo(interval(-10, 15)), IntervalsRelation::adjacent, false);
    assertRelation(baseInterval.getRelationTo(interval(0, 10)), IntervalsRelation::adjacent, false);
    assertRelation(baseInterval.getRelationTo(interval(0, 5)), IntervalsRelation::adjacent, false);
    assertRelation(baseInterval.getRelationTo(interval(0, 15)), IntervalsRelation::adjacent, false);
    assertRelation(baseInterval.getRelationTo(interval(10, 25)), IntervalsRelation::adjacent, true);
    assertRelation(baseInterval.getRelationTo(interval(15, 20)), IntervalsRelation::adjacent, false);

  }

  @Test
  @DisplayName("Relation: distinct - intervals are distinct (not intersecting)")
  void relation_Distinct_Success() {

    assertRelation(baseInterval.getRelationTo(interval(-20, -15)), IntervalsRelation::distinct, true);
    assertRelation(baseInterval.getRelationTo(interval(-20, -10)), IntervalsRelation::distinct, true);
    assertRelation(baseInterval.getRelationTo(interval(-20, 0)), IntervalsRelation::distinct, false);
    assertRelation(baseInterval.getRelationTo(interval(-20, 15)), IntervalsRelation::distinct, false);
    assertRelation(baseInterval.getRelationTo(interval(-20, 10)), IntervalsRelation::distinct, false);
    assertRelation(baseInterval.getRelationTo(interval(-10, 0)), IntervalsRelation::distinct, false);
    assertRelation(baseInterval.getRelationTo(interval(-10, 10)), IntervalsRelation::distinct, false);
    assertRelation(baseInterval.getRelationTo(interval(-10, 15)), IntervalsRelation::distinct, false);
    assertRelation(baseInterval.getRelationTo(interval(0, 10)), IntervalsRelation::distinct, false);
    assertRelation(baseInterval.getRelationTo(interval(0, 5)), IntervalsRelation::distinct, false);
    assertRelation(baseInterval.getRelationTo(interval(0, 15)), IntervalsRelation::distinct, false);
    assertRelation(baseInterval.getRelationTo(interval(10, 25)), IntervalsRelation::distinct, true);
    assertRelation(baseInterval.getRelationTo(interval(15, 20)), IntervalsRelation::distinct, true);
  }

  @Test
  @DisplayName("Relation: intersects - intervals intersect")
  void relation_Intersects_Success() {

    assertRelation(baseInterval.getRelationTo(interval(-20, -15)), IntervalsRelation::intersects, false);
    assertRelation(baseInterval.getRelationTo(interval(-20, -10)), IntervalsRelation::intersects, false);
    assertRelation(baseInterval.getRelationTo(interval(-20, 0)), IntervalsRelation::intersects, true);
    assertRelation(baseInterval.getRelationTo(interval(-20, 15)), IntervalsRelation::intersects, true);
    assertRelation(baseInterval.getRelationTo(interval(-20, 10)), IntervalsRelation::intersects, true);
    assertRelation(baseInterval.getRelationTo(interval(-10, 0)), IntervalsRelation::intersects, true);
    assertRelation(baseInterval.getRelationTo(interval(-10, 10)), IntervalsRelation::intersects, true);
    assertRelation(baseInterval.getRelationTo(interval(-10, 15)), IntervalsRelation::intersects, true);
    assertRelation(baseInterval.getRelationTo(interval(0, 10)), IntervalsRelation::intersects, true);
    assertRelation(baseInterval.getRelationTo(interval(0, 5)), IntervalsRelation::intersects, true);
    assertRelation(baseInterval.getRelationTo(interval(0, 15)), IntervalsRelation::intersects, true);
    assertRelation(baseInterval.getRelationTo(interval(10, 25)), IntervalsRelation::intersects, false);
    assertRelation(baseInterval.getRelationTo(interval(15, 20)), IntervalsRelation::intersects, false);

  }

  @Test
  @DisplayName("Relation: encloses - interval encloses another")
  void relation_Encloses_Success() {

    //-10 , 10
    assertRelation(baseInterval.getRelationTo(interval(-20, -15)), IntervalsRelation::encloses, false);
    assertRelation(baseInterval.getRelationTo(interval(-20, -10)), IntervalsRelation::encloses, false);
    assertRelation(baseInterval.getRelationTo(interval(-20, 0)), IntervalsRelation::encloses, false);
    assertRelation(baseInterval.getRelationTo(interval(-20, 15)), IntervalsRelation::encloses, false);
    assertRelation(baseInterval.getRelationTo(interval(-20, 10)), IntervalsRelation::encloses, false);
    assertRelation(baseInterval.getRelationTo(interval(-10, 0)), IntervalsRelation::encloses, true);
    assertRelation(baseInterval.getRelationTo(interval(-10, 10)), IntervalsRelation::encloses, true);
    assertRelation(baseInterval.getRelationTo(interval(-10, 15)), IntervalsRelation::encloses, false);
    assertRelation(baseInterval.getRelationTo(interval(0, 10)), IntervalsRelation::encloses, true);
    assertRelation(baseInterval.getRelationTo(interval(0, 5)), IntervalsRelation::encloses, true);
    assertRelation(baseInterval.getRelationTo(interval(0, 15)), IntervalsRelation::encloses, false);
    assertRelation(baseInterval.getRelationTo(interval(10, 25)), IntervalsRelation::encloses, false);
    assertRelation(baseInterval.getRelationTo(interval(15, 20)), IntervalsRelation::encloses, false);
  }

  @Test
  @DisplayName("Empty interval as first parameter throws IllegalArgumentException")
  void emptyInterval_AsFirstParameter_ThrowsException() {
    var emptyInterval = Intervals.empty();
    var normalInterval = interval(0, 10);

    assertThatThrownBy(() -> emptyInterval.getRelationTo(normalInterval))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("Interval A cannot be empty");
  }

  @Test
  @DisplayName("Empty interval as second parameter throws IllegalArgumentException")
  void emptyInterval_AsSecondParameter_ThrowsException() {
    var normalInterval = interval(0, 10);
    var emptyInterval = Intervals.empty();

    assertThatThrownBy(() -> normalInterval.getRelationTo(emptyInterval))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("Interval B cannot be empty");
  }

  @Test
  @DisplayName("Both intervals empty throws IllegalArgumentException")
  void emptyInterval_BothParameters_ThrowsException() {
    var emptyInterval1 = Intervals.empty();
    var emptyInterval2 = Intervals.empty();

    assertThatThrownBy(() -> emptyInterval1.getRelationTo(emptyInterval2))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("Interval A cannot be empty");
  }

}
