package com.fieldcode.tesseract.interval;

import java.time.Duration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.fieldcode.tesseract.IntervalSet;

import static com.fieldcode.tesseract.QueryDirection.BACKWARD;
import static com.fieldcode.tesseract.QueryDirection.FORWARD;
import static org.assertj.core.api.Assertions.assertThat;

class DisjointIntervalSetTest extends IntervalTestSupport {

  private IntervalSet set;

  @BeforeEach
  void setUp() {
    set = IntervalSets.disjoint(
        interval(0, 2),
        interval(4, 6),
        interval(8, 11)
    );
  }

  @Test
  void intervalSet_includeInterval_success() {

    // TODO: 2023. 03. 06. Reformat all other method body like this

    set.include(interval(7, 12));
    set.include(interval(24, 48));
    set.include(interval(12, 24));

    assertThat(set.intervals().stream())
        .containsExactly(
            interval(0, 2),
            interval(4, 6),
            interval(7, 48)
        );
  }

  @Test
  void intervalSet_includeOffsetDateTime_success() {

    set.include(interval(7, 12));
    set.include(interval(12, 24));
    set.include(interval(0, 24).shift(ONE_DAY));

    assertThat(set.intervals().stream())
        .containsExactly(
            interval(0, 2),
            interval(4, 6),
            interval(7, 48)
        );
  }

  @Test
  void intervalSet_includeMoment_success() {

    set.include(
        moment(7), moment(12)
    );

    set.include(
        moment(24), moment(48)
    );

    set.include(
        moment(12), moment(24)
    );

    assertThat(set.intervals().stream())
        .containsExactly(
            interval(0, 2),
            interval(4, 6),
            interval(7, 48)
        );
  }

  @Test
  void intervalSet_excludeInterval_success() {
    set.exclude(
        interval(5, 12)
    );

    assertThat(
        set.intervals().stream()
    ).containsExactly(
        interval(0, 2),
        interval(4, 5)
    );
  }

  @Test
  void intervalSet_excludeInterval_splitting_success() {
    set.exclude(
        interval(9, 10)
    );

    assertThat(
        set.intervals().stream()
    ).containsExactly(
        interval(0, 2),
        interval(4, 6),
        interval(8, 9),
        interval(10, 11)
    );
  }

  @Test
  void intervalSet_excludeMoment_success() {
    set.exclude(
        moment(5), moment(12)
    );

    assertThat(
        set.intervals().stream()
    ).containsExactly(
        interval(0, 2),
        interval(4, 5)
    );
  }

  @Test
  void intervalSet_excludeOffsetDateTime_success() {
    set.exclude(
        moment(5).getAt(),
        moment(12).getAt());

    assertThat(
        set.intervals().stream()
    ).containsExactly(
        interval(0, 2),
        interval(4, 5)
    );
  }

  @Test
  void intervalSet_resolve_success() {

    assertThat(
        set.getIfContains(
            moment(1)
        )
    ).contains(
        interval(0, 2)
    );

    assertThat(
        set.getIfContains(
            moment(3)
        )
    ).isEmpty();
  }


  @Test
  void intervalSet_contains_success() {

    // Reformat

    assertThat(set.contains(moment(-1)))
        .isFalse();

    assertThat(set.contains(moment(3)))
        .isFalse();

    assertThat(
        set.contains(moment(12))
    ).isFalse();

    assertThat(
        set.contains(moment(0))
    ).isTrue();

    assertThat(
        set.contains(moment(5))
    ).isTrue();

    assertThat(
        set.contains(moment(11))
    ).isFalse();

  }

  @Test
  void intervalSet_encloses_success() {

    assertThat(
        set.encloses(
            interval(-10, -5)
        )
    ).isFalse();

    assertThat(
        set.encloses(
            interval(-10, 0)
        )
    ).isFalse();

    assertThat(
        set.encloses(
            interval(-10, 3)
        )
    ).isFalse();

    assertThat(
        set.encloses(
            interval(-10, 4)
        )
    ).isFalse();

    assertThat(
        set.encloses(
            interval(-10, 10)
        )
    ).isFalse();

    assertThat(
        set.encloses(
            interval(-10, 15)
        )
    ).isFalse();

    assertThat(
        set.encloses(
            interval(0, 2)
        )
    ).isTrue();

    assertThat(
        set.encloses(
            interval(0, 4)
        )
    ).isFalse();

    assertThat(
        set.encloses(
            interval(0, 7)
        )
    ).isFalse();

    assertThat(
        set.encloses(
            interval(0, 10)
        )
    ).isFalse();

    assertThat(
        set.encloses(
            interval(0, 15)
        )
    ).isFalse();

    assertThat(
        set.encloses(
            interval(5, 6)
        )
    ).isTrue();

    assertThat(
        set.encloses(
            interval(5, 7)
        )
    ).isFalse();

    assertThat(
        set.encloses(
            interval(5, 10)
        )
    ).isFalse();

    assertThat(
        set.encloses(interval(5, 15)
        )
    ).isFalse();

    assertThat(
        set.encloses(
            interval(10, 11)
        )
    ).isTrue();

    assertThat(
        set.encloses(interval(10, 15)
        )
    ).isFalse();

  }

  //set

  @Test
  void find_forward_success() {

    //Moment Before set
    assertThat(
        set.find(moment(-5), Duration.ofHours(1), FORWARD)
    )
        .contains(interval(0, 1)
        );

    assertThat(
        set.find(
            moment(-5), Duration.ofHours(3), FORWARD)
    )
        .contains(
            interval(8, 11)
        );

    assertThat(
        set.find(moment(-5), Duration.ofHours(4), FORWARD)
    )
        .isEmpty();

    //Moment Meets set's interval
    assertThat(
        set.find(
            moment(0), Duration.ofHours(1), FORWARD)
    )
        .contains(
            interval(0, 1)
        );

    assertThat(
        set.find(
            moment(0), Duration.ofHours(2), FORWARD)
    )
        .contains(
            interval(0, 2)
        );

    assertThat(
        set.find(
            moment(0), Duration.ofHours(3), FORWARD)
    )
        .contains(
            interval(8, 11)
        );

    //Moment within set's interval
    assertThat(
        set.find(moment(1), Duration.ofHours(1), FORWARD)
    )
        .contains(
            interval(1, 2)
        );

    assertThat(
        set.find(
            moment(1), Duration.ofHours(2), FORWARD)
    )
        .contains(
            interval(4, 6)
        );

    assertThat(
        set.find(
            moment(1), Duration.ofHours(3), FORWARD)
    )
        .contains(
            interval(8, 11)
        );

    //Moment finishes interval of set
    assertThat(
        set.find(
            moment(2), Duration.ofHours(1), FORWARD)
    )
        .contains(
            interval(4, 5)
        );

    assertThat(
        set.find(
            moment(2), Duration.ofHours(3), FORWARD)
    )
        .contains(
            interval(8, 11)
        );

    //Moment between disjoint intervals
    assertThat(
        set.find(
            moment(3), Duration.ofHours(1), FORWARD)
    )
        .contains(
            interval(4, 5)
        );

    assertThat(
        set.find(
            moment(3), Duration.ofHours(3), FORWARD)
    )
        .contains(
            interval(8, 11)
        );

    //Set is met by moment
    assertThat(
        set.find(
            moment(11), Duration.ofHours(1), FORWARD)
    )
        .isEmpty();

  }

  @Test
  void find_backward_success() {

    //Moment Before set

    assertThat(
        set.find(
            moment(-5),
            Duration.ofHours(1),
            BACKWARD
        )
    ).isEmpty();

    assertThat(
        set.find(
            moment(-5),
            Duration.ofHours(3),
            BACKWARD))
        .isEmpty();

    assertThat(
        set.find(
            moment(-5),
            Duration.ofHours(4),
            BACKWARD))
        .isEmpty();

    //Moment Meets set's interval

    assertThat(
        set.find(
            moment(0),
            Duration.ofHours(1),
            BACKWARD))
        .isEmpty();

    assertThat(
        set.find(
            moment(0),
            Duration.ofHours(2),
            BACKWARD))
        .isEmpty();

    assertThat(
        set.find(
            moment(0),
            Duration.ofHours(3),
            BACKWARD))
        .isEmpty();

    //Moment within set's interval

    assertThat(
        set.find(
            moment(1),
            Duration.ofHours(1),
            BACKWARD))
        .contains(
            interval(0, 1)
        );

    assertThat(
        set.find(
            moment(1),
            Duration.ofHours(2),
            BACKWARD))
        .isEmpty();

    assertThat(
        set.find(
            moment(1),
            Duration.ofHours(3),
            BACKWARD))
        .isEmpty();

    //Moment finishes interval of set

    assertThat(
        set.find(
            moment(2),
            Duration.ofHours(1),
            BACKWARD))
        .contains(
            interval(1, 2)
        );

    assertThat(
        set.find(
            moment(2),
            Duration.ofHours(3),
            BACKWARD))
        .isEmpty();

    //Moment between disjoint intervals

    assertThat(
        set.find(
            moment(3),
            Duration.ofHours(1),
            BACKWARD))
        .contains(
            interval(1, 2)
        );

    assertThat(
        set.find(
            moment(3),
            Duration.ofHours(3),
            BACKWARD))
        .isEmpty();

    //Set is met by moment

    assertThat(
        set.find(
            moment(11),
            Duration.ofHours(1),
            BACKWARD))
        .contains(
            interval(10, 11)
        );
  }

  @Test
  void find_always_success() {
    var alwaysSet = IntervalSets.disjoint(Intervals.always());
    assertThat(
        alwaysSet.find(
            moment(1),
            Duration.ofHours(1),
            FORWARD
        )
    ).contains(
        Intervals.interval(
            moment(1).getAt(),
            moment(1).getAt().plus(Duration.ofHours(1))
        )
    );

    assertThat(
        alwaysSet.find(
            moment(1),
            Duration.ofHours(1),
            BACKWARD
        )
    ).contains(
        Intervals.interval(
            moment(1).getAt().plus(Duration.ofHours(1).negated()),
            moment(1).getAt()
        )
    );
  }

  @Test
  void find_intervalFrom_success() {
    var fromSet = IntervalSets.disjoint(
        Intervals.intervalFrom(moment(1).getAt())
    );
    assertThat(
        fromSet.find(
            moment(1),
            Duration.ofHours(1),
            FORWARD))
        .contains(
            Intervals.interval(
                moment(1).getAt(),
                moment(1).getAt().plus(Duration.ofHours(1)))
        );

    assertThat(
        fromSet.find(
            moment(1),
            Duration.ofHours(1),
            BACKWARD))
        .isEmpty();
  }

  @Test
  void find_intervalTo_success() {
    var set = IntervalSets.disjoint(
        intervalTo(1)
    );

    assertThat(
        set.find(
            moment(1),
            Duration.ofHours(1),
            FORWARD)
    ).isEmpty();

    assertThat(
        set.find(
            moment(1),
            Duration.ofHours(1),
            BACKWARD)
    ).contains(
        Intervals.interval(
            moment(1).shift(Duration.ofHours(1).negated()),
            moment(1))
    );
  }

  @Test
  void intervals_Empty_Empty() {
    var set = IntervalSets.disjoint();

    var iterator = set.intervals();
    assertThat(iterator.hasNext()).isFalse();
  }

  @Test
  void intervals_OneElement_Success() {
    var set = IntervalSets.disjoint(
        interval(0, 12)
    );

    var iterator = set.intervals();
    assertThat(iterator)
        .toIterable()
        .containsExactly(
            interval(0, 12)
        );
  }

  @Test
  void intervals_MoreElements_Success() {
    var set = IntervalSets.disjoint(
        interval(0, 2),
        interval(4, 6),
        interval(8, 10)
    );

    var iterator = set.intervals();
    assertThat(iterator)
        .toIterable()
        .containsExactly(
            interval(0, 2),
            interval(4, 6),
            interval(8, 10)
        );
  }

  @Test
  void intervals_WindowForward_Success() {
    var set = IntervalSets.disjoint(
        interval(0, 5),
        interval(10, 15),
        interval(20, 25)
    );

    var iterator = set.intervals(interval(4, 21), FORWARD);
    assertThat(iterator)
        .toIterable()
        .containsExactly(
            interval(4, 5),
            interval(10, 15),
            interval(20, 21)
        );
  }

  @Test
  void intervals_WindowBackward_Success() {
    var set = IntervalSets.disjoint(
        interval(0, 5),
        interval(10, 15),
        interval(20, 25)
    );

    var iterator = set.intervals(interval(4, 21), BACKWARD);
    assertThat(iterator)
        .toIterable()
        .containsExactly(
            interval(20, 21),
            interval(10, 15),
            interval(4, 5)
        );
  }

  @Test
  void intervals_WindowAlwaysForward_Success() {
    var set = IntervalSets.disjoint(
        interval(0, 5),
        interval(10, 15),
        interval(20, 25)
    );

    var iterator = set.intervals(Intervals.always(), FORWARD);
    assertThat(iterator)
        .toIterable()
        .containsExactly(
            interval(0, 5),
            interval(10, 15),
            interval(20, 25)
        );
  }

  @Test
  void intervals_WindowAlwaysBackward_Success() {
    var set = IntervalSets.disjoint(
        interval(0, 5),
        interval(10, 15),
        interval(20, 25)
    );

    var iterator = set.intervals(Intervals.always(), BACKWARD);
    assertThat(iterator)
        .toIterable()
        .containsExactly(
            interval(20, 25),
            interval(10, 15),
            interval(0, 5)
        );
  }

  @Test
  void intervals_WindowEmptyForward_Success() {
    var set = IntervalSets.disjoint(
        interval(0, 5),
        interval(10, 15),
        interval(20, 25)
    );

    var iterator = set.intervals(interval(30, 40), FORWARD);
    assertThat(iterator)
        .toIterable()
        .isEmpty();
  }

  @Test
  void intervals_WindowEmptyBackward_Success() {
    var set = IntervalSets.disjoint(
        interval(0, 5),
        interval(10, 15),
        interval(20, 25)
    );

    var iterator = set.intervals(interval(30, 40), BACKWARD);
    assertThat(iterator)
        .toIterable()
        .isEmpty();
  }

}
