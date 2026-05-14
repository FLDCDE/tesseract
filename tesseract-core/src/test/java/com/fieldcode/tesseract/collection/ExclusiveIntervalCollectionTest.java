package com.fieldcode.tesseract.collection;

import org.junit.jupiter.api.Test;

import com.fieldcode.tesseract.QueryDirection;
import com.fieldcode.tesseract.interval.IntervalSets;
import com.fieldcode.tesseract.interval.IntervalTestSupport;

import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;

class ExclusiveIntervalCollectionTest extends IntervalTestSupport {

  @Test
  void intervals_TwoDisjointSets_ReturnsUnion() {
    // Exclusive of two disjoint sets [0..1), [4..5) and [2..3), [6..7) should return all four intervals
    var set1 = IntervalSets.disjoint(
        interval(0, 1),
        interval(4, 5)
    );
    var set2 = IntervalSets.disjoint(
        interval(2, 3),
        interval(6, 7)
    );

    var collection = IntervalCollections.exclusive(set1, set2);

    assertThat(collection.intervals())
        .toIterable()
        .containsExactly(
            interval(0, 1),
            interval(2, 3),
            interval(4, 5),
            interval(6, 7)
        );
  }

  @Test
  void intervals_OverlappingSets_ReturnsNonOverlappingParts() {
    // Exclusive of [0..5) and [3..8) should return [0..3) and [5..8) (exclude overlap [3..5))
    var set1 = IntervalSets.disjoint(interval(0, 5));
    var set2 = IntervalSets.disjoint(interval(3, 8));

    var collection = IntervalCollections.exclusive(set1, set2);

    assertThat(collection.intervals())
        .toIterable()
        .containsExactly(
            interval(0, 3),
            interval(5, 8)
        );
  }

  @Test
  void intervals_IdenticalSets_ReturnsEmpty() {
    // Exclusive of identical sets should return empty (everything cancels out)
    var set1 = IntervalSets.disjoint(
        interval(0, 5),
        interval(10, 15)
    );
    var set2 = IntervalSets.disjoint(
        interval(0, 5),
        interval(10, 15)
    );

    var collection = IntervalCollections.exclusive(set1, set2);

    assertThat(collection.intervals())
        .toIterable()
        .isEmpty();
  }

  @Test
  void intervals_PartialOverlap_ReturnsSymmetricDifference() {
    // Exclusive with partial overlaps
    var set1 = IntervalSets.disjoint(
        interval(0, 5),
        interval(10, 15),
        interval(20, 25)
    );
    var set2 = IntervalSets.disjoint(
        interval(1, 4),
        interval(11, 14),
        interval(21, 24)
    );

    var collection = IntervalCollections.exclusive(set1, set2);

    // Result should be: [0..1), [4..5), [10..11), [14..15), [20..21), [24..25)
    assertThat(collection.intervals())
        .toIterable()
        .containsExactly(
            interval(0, 1),
            interval(4, 5),
            interval(10, 11),
            interval(14, 15),
            interval(20, 21),
            interval(24, 25)
        );
  }

  @Test
  void intervals_WithEmptySet_ReturnsOther() {
    // Exclusive with empty set should return the other set
    var set1 = IntervalSets.disjoint(
        interval(0, 5),
        interval(10, 15)
    );
    var empty = IntervalSets.disjoint();

    var collection = IntervalCollections.exclusive(set1, empty);

    assertThat(collection.intervals())
        .toIterable()
        .containsExactly(
            interval(0, 5),
            interval(10, 15)
        );
  }

  @Test
  void intervals_ThreeSets_ReturnsCorrectExclusive() {
    // Exclusive semantics across three sets.
    var set1 = IntervalSets.disjoint(interval(0, 10));
    var set2 = IntervalSets.disjoint(interval(5, 15));
    var set3 = IntervalSets.disjoint(interval(10, 20));

    var collection = IntervalCollections.exclusive(set1, set2, set3);

    // [0..5): only in set1 (count=1) → included
    // [5..10): in set1 and set2 (count=2) → excluded
    // [10..15): in set2 and set3 (count=2) → excluded
    // [15..20): only in set3 (count=1) → included
    assertThat(collection.intervals())
        .toIterable()
        .containsExactly(
            interval(0, 5),
            interval(15, 20)
        );
  }

  @Test
  void intervals_TripleOverlap_IsExcludedForExclusiveSemantics() {
    var set1 = IntervalSets.disjoint(interval(0, 3));
    var set2 = IntervalSets.disjoint(interval(1, 4));
    var set3 = IntervalSets.disjoint(interval(2, 5));

    var collection = IntervalCollections.exclusive(set1, set2, set3);

    // [0..1): count=1 -> included
    // [1..2): count=2 -> excluded
    // [2..3): count=3 -> excluded for exclusive semantics
    // [3..4): count=2 -> excluded
    // [4..5): count=1 -> included
    assertThat(collection.intervals())
        .toIterable()
        .containsExactly(
            interval(0, 1),
            interval(4, 5)
        );
  }

  @Test
  void intervals_WindowForward_ReturnsFilteredExclusive() {
    var set1 = IntervalSets.disjoint(
        interval(0, 5),
        interval(10, 15),
        interval(20, 25)
    );
    var set2 = IntervalSets.disjoint(
        interval(1, 4),
        interval(11, 14),
        interval(21, 24)
    );

    var collection = IntervalCollections.exclusive(set1, set2);

    // Query window [5..20) in forward direction
    assertThat(collection.intervals(interval(5, 20), QueryDirection.FORWARD))
        .toIterable()
        .containsExactly(
            interval(10, 11),
            interval(14, 15)
        );
  }

  @Test
  void intervals_WindowBackward_ReturnsFilteredExclusiveReversed() {
    var set1 = IntervalSets.disjoint(
        interval(0, 5),
        interval(10, 15),
        interval(20, 25)
    );
    var set2 = IntervalSets.disjoint(
        interval(1, 4),
        interval(11, 14),
        interval(21, 24)
    );

    var collection = IntervalCollections.exclusive(set1, set2);

    // Query window [5..20) in backward direction
    assertThat(collection.intervals(interval(5, 20), QueryDirection.BACKWARD))
        .toIterable()
        .containsExactly(
            interval(14, 15),
            interval(10, 11)
        );
  }

  @Test
  void intervals_OneSetContainsOther_ReturnsComplement() {
    // If set1 contains set2, exclusive returns set1 minus set2
    var set1 = IntervalSets.disjoint(interval(0, 10));
    var set2 = IntervalSets.disjoint(interval(3, 7));

    var collection = IntervalCollections.exclusive(set1, set2);

    assertThat(collection.intervals())
        .toIterable()
        .containsExactly(
            interval(0, 3),
            interval(7, 10)
        );
  }

  @Test
  void intervals_AdjacentIntervals_Merge() {
    // Exclusive with adjacent intervals that don't overlap in same set
    var set1 = IntervalSets.disjoint(
        interval(0, 5),
        interval(10, 15)
    );
    var set2 = IntervalSets.disjoint(
        interval(5, 10),
        interval(15, 20)
    );

    var collection = IntervalCollections.exclusive(set1, set2);

    // All intervals are in exactly one set, so exclusive includes all of them
    // Since they're adjacent, they merge into one continuous interval
    assertThat(collection.intervals())
        .toIterable()
        .containsExactly(
            interval(0, 20)
        );
  }

  @Test
  void intervals_ComplexMultipleOverlaps_ReturnsCorrectExclusive() {
    // Complex scenario with multiple overlapping patterns
    var set1 = IntervalSets.disjoint(
        interval(0, 2),
        interval(5, 10),
        interval(15, 20)
    );
    var set2 = IntervalSets.disjoint(
        interval(1, 6),
        interval(8, 12),
        interval(18, 22)
    );

    var collection = IntervalCollections.exclusive(set1, set2);

    // [0..1): only set1
    // [1..2): both (excluded)
    // [2..5): only set2
    // [5..6): both (excluded)
    // [6..8): only set1
    // [8..10): both (excluded)
    // [10..12): only set2
    // [15..18): only set1
    // [18..20): both (excluded)
    // [20..22): only set2
    assertThat(collection.intervals())
        .toIterable()
        .containsExactly(
            interval(0, 1),
            interval(2, 5),
            interval(6, 8),
            interval(10, 12),
            interval(15, 18),
            interval(20, 22)
        );
  }

  @Test
  void intervals_NoInputCollections_ReturnsEmpty() {
    // Edge case: exclusive with no collections at all (empty varargs)
    var collection = IntervalCollections.exclusive();

    assertThat(collection.intervals())
        .toIterable()
        .isEmpty();
  }

  @Test
  void intervals_EmptyInputCollections_ReturnsEmpty() {
    // Edge case: exclusive with no collections at all (empty input)
    var collection = IntervalCollections.exclusive(List.of());

    assertThat(collection.intervals())
        .toIterable()
        .isEmpty();
  }

  @Test
  void intervals_SingleEmptyCollection_ReturnsEmpty() {
    // Edge case: exclusive with a single empty collection
    var empty = IntervalSets.disjoint();
    var collection = IntervalCollections.exclusive(empty);

    assertThat(collection.intervals())
        .toIterable()
        .isEmpty();
  }

  @Test
  void intervals_SingleNonEmptyCollection_ReturnsSame() {
    // Edge case: exclusive with a single non-empty collection returns that collection
    var set = IntervalSets.disjoint(
        interval(0, 5),
        interval(10, 15)
    );
    var collection = IntervalCollections.exclusive(set);

    assertThat(collection.intervals())
        .toIterable()
        .containsExactly(
            interval(0, 5),
            interval(10, 15)
        );
  }

  @Test
  void intervals_EmptyExclusiveEmpty_ReturnsEmpty() {
    var empty1 = IntervalSets.disjoint();
    var empty2 = IntervalSets.disjoint();

    var collection = IntervalCollections.exclusive(empty1, empty2);

    assertThat(collection.intervals())
        .toIterable()
        .isEmpty();
  }

  @Test
  void intervals_ThreeSetsWithOneEmpty_ReturnsSymmetricDifference() {
    // When one set is empty in exclusive with 3 sets, it's equivalent to exclusive of the two non-empty sets
    var set1 = IntervalSets.disjoint(
        interval(0, 5),
        interval(10, 15),
        interval(20, 25)
    );
    var set2 = IntervalSets.disjoint(
        interval(1, 4),
        interval(11, 14),
        interval(21, 24)
    );
    var empty = IntervalSets.disjoint();

    var collection = IntervalCollections.exclusive(set1, set2, empty);

    // Result should be the symmetric difference of set1 and set2
    // [0..1): only in set1
    // [1..4): both sets (count=2, excluded)
    // [4..5): only in set1
    // [10..11): only in set1
    // [11..14): both sets (count=2, excluded)
    // [14..15): only in set1
    // [20..21): only in set1
    // [21..24): both sets (count=2, excluded)
    // [24..25): only in set1
    assertThat(collection.intervals())
        .toIterable()
        .containsExactly(
            interval(0, 1),
            interval(4, 5),
            interval(10, 11),
            interval(14, 15),
            interval(20, 21),
            interval(24, 25)
        );
  }

}




