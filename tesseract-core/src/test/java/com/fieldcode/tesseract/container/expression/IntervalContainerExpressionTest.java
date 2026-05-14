package com.fieldcode.tesseract.container.expression;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.fieldcode.tesseract.Interval;
import com.fieldcode.tesseract.IntervalSet;
import com.fieldcode.tesseract.container.IntervalContainers;
import com.fieldcode.tesseract.interval.IntervalSets;
import com.fieldcode.tesseract.interval.IntervalTestSupport;
import com.fieldcode.tesseract.tag.Tags;

import static com.fieldcode.tesseract.container.expression.IntervalContainerExpressions.and;
import static com.fieldcode.tesseract.container.expression.IntervalContainerExpressions.anyId;
import static com.fieldcode.tesseract.container.expression.IntervalContainerExpressions.collections;
import static com.fieldcode.tesseract.container.expression.IntervalContainerExpressions.not;
import static com.fieldcode.tesseract.container.expression.IntervalContainerExpressions.or;
import static com.fieldcode.tesseract.container.expression.IntervalContainerExpressions.tags;
import static com.fieldcode.tesseract.tag.TagPredicates.any;
import static com.fieldcode.tesseract.tag.TagPredicates.match;
import static org.assertj.core.api.Assertions.assertThat;

class IntervalContainerExpressionTest extends IntervalTestSupport {

  private IntervalSet a, d;
  private Interval b, c;

  @BeforeEach
  void setUp() {
    a = IntervalSets.disjoint(interval(0, 5), interval(10, 15));
    b = interval(3, 8);
    c = interval(2, 30);
    d = IntervalSets.disjoint(interval(12, 14), interval(20, 40));

    /*
                                 1 1 1 1 1 1 1 1 1 1 2     3     4
      time:  0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 ... 0 ... 0

      a:         |-----|         |-----|
      b:           |---------|
      c:         |------------------------------------...--|
      d:                             |-----|         |----...----|

     */
  }

  @Test
  void combine_advanceByTag_Success() {

    var container = IntervalContainers
        .builder()
        .add("a", a, Tags.tag("availability"))
        .add("b", b, Tags.tag("availability"))
        .add("c", c, Tags.tag("window"))
        .add("d", d, Tags.tag("global", "absence"))
        .build();

    var result = container.combine(
            and(
                and(
                    or(
                        collections(
                            tags(match("availability")
                            )
                        )
                    ),
                    collections(
                        tags(
                            match("window")
                        )
                    )
                ),
                not(
                    collections(
                        tags(
                            any(
                                match("global"), match("absence")
                            )
                        )
                    )
                )
            )
        )
        .intervals();

    assertThat(result)
        .toIterable()
        .containsExactly(
            interval(2, 8),
            interval(10, 12),
            interval(14, 15)
        );
  }

  @Test
  void combine_advanceById_Success() {

    var container = IntervalContainers
        .builder()
        .add("a", a, Tags.tag("availability"))
        .add("b", b, Tags.tag("availability"))
        .add("c", c, Tags.tag("window"))
        .add("d", d, Tags.tag("global", "absence"))
        .build();

    var result = container.combine(
            and(
                and(
                    or(
                        collections(anyId("a", "b"))
                    ),
                    collections(anyId("c"))
                ),
                not(
                    collections(anyId("d"))
                )
            )
        )
        .intervals();

    assertThat(result)
        .toIterable()
        .containsExactly(
            interval(2, 8),
            interval(10, 12),
            interval(14, 15)
        );
  }

}
