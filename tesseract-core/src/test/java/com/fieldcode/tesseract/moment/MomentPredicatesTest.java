package com.fieldcode.tesseract.moment;

import org.junit.jupiter.api.Test;

import com.fieldcode.tesseract.test_utils.TimeTestSupport;

import static org.assertj.core.api.Assertions.assertThat;

class MomentPredicatesTest extends TimeTestSupport {

  @Test
  void between_InclusiveInclusive_Success() {

    var predicate = MomentPredicates.between(moment(0), true, moment(2), true);

    assertThat(predicate.test(moment(-1))).isFalse();
    assertThat(predicate.test(moment(0))).isTrue();
    assertThat(predicate.test(moment(1))).isTrue();
    assertThat(predicate.test(moment(2))).isTrue();
    assertThat(predicate.test(moment(3))).isFalse();

  }

  @Test
  void between_ExclusiveInclusive_Success() {

    var predicate = MomentPredicates.between(moment(0), false, moment(2), true);

    assertThat(predicate.test(moment(-1))).isFalse();
    assertThat(predicate.test(moment(0))).isFalse();
    assertThat(predicate.test(moment(1))).isTrue();
    assertThat(predicate.test(moment(2))).isTrue();
    assertThat(predicate.test(moment(3))).isFalse();

  }

  @Test
  void between_InclusiveExclusive_Success() {

    var predicate = MomentPredicates.between(moment(0), true, moment(2), false);

    assertThat(predicate.test(moment(-1))).isFalse();
    assertThat(predicate.test(moment(0))).isTrue();
    assertThat(predicate.test(moment(1))).isTrue();
    assertThat(predicate.test(moment(2))).isFalse();
    assertThat(predicate.test(moment(3))).isFalse();

  }

  @Test
  void between_ExclusiveExclusive_Success() {

    var predicate = MomentPredicates.between(moment(0), false, moment(2), false);

    assertThat(predicate.test(moment(-1))).isFalse();
    assertThat(predicate.test(moment(0))).isFalse();
    assertThat(predicate.test(moment(1))).isTrue();
    assertThat(predicate.test(moment(2))).isFalse();
    assertThat(predicate.test(moment(3))).isFalse();

  }


}
