package com.fieldcode.tesseract.container;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.fieldcode.tesseract.IntervalContainer;
import com.fieldcode.tesseract.collection.ImmutableTaggedIntervalCollection;
import com.fieldcode.tesseract.interval.IntervalSets;
import com.fieldcode.tesseract.interval.IntervalTestSupport;
import com.fieldcode.tesseract.interval.Intervals;
import com.fieldcode.tesseract.tag.Tags;

import static com.fieldcode.tesseract.tag.TagPredicates.all;
import static com.fieldcode.tesseract.tag.TagPredicates.any;
import static com.fieldcode.tesseract.tag.TagPredicates.match;
import static com.fieldcode.tesseract.tag.Tags.holder;
import static com.fieldcode.tesseract.tag.Tags.tag;
import static org.assertj.core.api.Assertions.assertThat;

class IntervalContainerTest extends IntervalTestSupport {

  private IntervalContainer container;

  @BeforeEach
  public void setUp() {
    var a = interval(0, 15);
    var b = interval(5, 20);
    var c = IntervalSets.disjoint(interval(10, 25));

    var tag1 = tag("1");
    var tag2 = Tags.builder().tag("2").tag("3").build();
    var tag3 = Tags.builder().tag("1").tag("2").tag("3").build();
    container = IntervalContainers
        .builder()
        .add("a", a, tag1)
        .add("b", b, tag2)
        .add("c", c, tag3)
        .build();
  }

  @Test
  void create_taggedIntervalCollection_Success() {
    var collection = IntervalSets.disjoint(
        interval(0, 2),
        interval(5, 10),
        interval(11, 15)
    );

    var collection2 = IntervalSets.disjoint(
        interval(16, 20)
    );

    var tags1 = Tags.builder().tag("tag1").tag("tag2").build();
    var tagged1 = ImmutableTaggedIntervalCollection.of("1", collection, tags1);

    var tags2 = Tags.builder().tag("tag3").tag("4").build();
    var tagged2 = ImmutableTaggedIntervalCollection.of("2", collection2, tags2);

    var container = IntervalContainers.container(tagged1, tagged2);

    assertThat(container.get("1").intervals()).toIterable().containsExactly(
        interval(0, 2),
        interval(5, 10),
        interval(11, 15)
    );
    assertThat(container.get("2").intervals()).toIterable().containsExactly(
        interval(16, 20)
    );

  }

  @Test
  void get_AllCases_Success() {

    assertThat(container.get("a").intervals())
        .toIterable()
        .containsExactly(
            interval(0, 15)
        );

    assertThat(container.get("b").intervals())
        .toIterable()
        .containsExactly(
            interval(5, 20)
        );

    assertThat(container.get("c").intervals())
        .toIterable()
        .containsExactly(
            interval(10, 25)
        );

    assertThat(container.get("d").intervals())
        .toIterable()
        .isEmpty();

  }

  @Test
  void and_Any_Success() {
    assertThat(container.and(any(match("1"))).intervals())
        .toIterable()
        .containsExactly(
            interval(10, 15)
        );
  }

  @Test
  void and_AnyEmpty_Success() {
    assertThat(container.and(t -> false).intervals())
        .toIterable()
        .isEmpty();
  }

  @Test
  void and_All_Success() {
    assertThat(container.and(all(match("1"))).intervals())
        .toIterable()
        .containsExactly(
            interval(10, 15)
        );
  }

  @Test
  void and_AllEmpty_Success() {
    assertThat(container.and(t -> false).intervals())
        .toIterable()
        .isEmpty();
  }

  @Test
  void or_Any_Success() {
    assertThat(container.or(any(match("1"))).intervals())
        .toIterable()
        .containsExactly(
            interval(0, 25)
        );
  }

  @Test
  void or_AnyEmpty_Success() {
    assertThat(container.or(t -> false).intervals())
        .toIterable()
        .isEmpty();
  }

  @Test
  void or_All_Success() {
    assertThat(container.or(all(match("1"), match("2"))).intervals())
        .toIterable()
        .containsExactly(
            interval(10, 25)
        );
  }

  @Test
  void or_AllEmpty_Success() {
    assertThat(container.or(t -> true).intervals())
        .toIterable()
        .containsExactly(
            interval(0, 25)
        );
  }

  @Test
  void not_Any_Success() {
    assertThat(container.not(any(match("1"), match("2"))).intervals())
        .toIterable()
        .containsExactly(
            intervalTo(0),
            intervalFrom(25)
        );
  }

  @Test
  void not_AnyEmpty_Success() {
    assertThat(container.not(t -> true).intervals())
        .toIterable()
        .containsExactly(
            intervalTo(0),
            intervalFrom(25)
        );
  }

  @Test
  void not_All_Success() {
    assertThat(container.not(all(match("1"))).intervals())
        .toIterable()
        .containsExactly(
            intervalTo(0),
            intervalFrom(25)
        );
  }

  @Test
  void not_AllEmpty_Success() {
    assertThat(container.not(t -> false).intervals())
        .toIterable()
        .containsExactly(
            Intervals.always()
        );
  }

  @Test
  void equals_Success() {
    var container1 = IntervalContainers
        .builder()
        .add("a", interval(0, 5), holder(tag("1")))
        .add("b", interval(5, 10), holder(tag("2")))
        .add("c", interval(10, 15), holder(tag("3")))
        .build();

    var container2 = IntervalContainers
        .builder()
        .add("c", interval(10, 15), holder(tag("3")))
        .add("b", interval(5, 10), holder(tag("2")))
        .add("a", interval(0, 5), holder(tag("1")))
        .build();

    var equality = container1.equals(container2);
    assertThat(equality).isTrue();
  }

}
