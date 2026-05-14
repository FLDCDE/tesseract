package com.fieldcode.tesseract.moment;

import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.Collections;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;

import com.fieldcode.tesseract.test_utils.TimeTestSupport;
import com.google.common.collect.ImmutableList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MomentTest extends TimeTestSupport {

  @Test
  void compareTo_FiniteSort_Success() {

    var moments = IntStream.range(0, 48)
        .mapToObj(this::moment)
        .collect(Collectors.toList());

    var originalOrder = ImmutableList.copyOf(moments);

    Collections.shuffle(moments);

    var sorted = moments.stream().sorted();

    assertThat(sorted).containsExactlyElementsOf(originalOrder);

  }

  @Test
  void compareTo_Mixed_Success() {
    var sortedMoments = Stream.of(
            inf(),
            ninf(),
            moment(5),
            inf(),
            ninf()
        )
        .sorted();

    assertThat(sortedMoments).containsExactly(
        ninf(),
        ninf(),
        moment(5),
        inf(),
        inf()
    );

  }

  @Test
  void create_Finite_Success() {

    var moment = Moments.moment(OffsetDateTime.now());

    assertThat(moment.isFinite()).isTrue();
    assertThat(moment.isInfinite()).isFalse();
    assertThat(moment.isPositiveInfinite()).isFalse();
    assertThat(moment.isNegativeInfinite()).isFalse();

  }

  @Test
  void create_PositiveInfinite_Success() {

    var moment = inf();

    assertThat(moment.isFinite()).isFalse();
    assertThat(moment.isInfinite()).isTrue();
    assertThat(moment.isPositiveInfinite()).isTrue();
    assertThat(moment.isNegativeInfinite()).isFalse();

  }

  @Test
  void create_NegativeInfinite_Success() {

    var moment = ninf();

    assertThat(moment.isFinite()).isFalse();
    assertThat(moment.isInfinite()).isTrue();
    assertThat(moment.isPositiveInfinite()).isFalse();
    assertThat(moment.isNegativeInfinite()).isTrue();

  }

  @Test
  void getAt_Finite_Success() {
    var at = at(0);
    var moment = Moments.moment(at);
    assertThat(moment.getAt()).isEqualTo(at);
  }

  @Test
  void getAt_PositiveInfinite_Exception() {
    var moment = inf();
    assertThatThrownBy(moment::getAt)
        .isExactlyInstanceOf(UnsupportedOperationException.class);
  }

  @Test
  void getAt_NegativeInfinite_Exception() {
    var moment = ninf();
    assertThatThrownBy(moment::getAt)
        .isExactlyInstanceOf(UnsupportedOperationException.class);
  }

  @Test
  void withZoneIdSameMoment_Finite_Success() {
    var moment = moment(0).withZoneIdSameMoment("UTC");
    var converted = moment.withZoneIdSameMoment("America/New_York");
    assertThat(converted).isEqualTo(moment);
  }

  @Test
  void withZoneIdSameMoment_PositiveInfinite_Success() {
    var moment = inf();
    var converted = moment.withZoneIdSameMoment("America/New_York");
    assertThat(converted).isSameAs(moment);
  }

  @Test
  void withZoneIdSameMoment_NegativeInfinite_Success() {
    var moment = ninf();
    var converted = moment.withZoneIdSameMoment("America/New_York");
    assertThat(converted).isSameAs(moment);
  }

}
