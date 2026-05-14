package com.fieldcode.tesseract.moment;

import java.time.OffsetDateTime;

import org.junit.jupiter.api.Test;

import com.fieldcode.tesseract.Moment;
import com.fieldcode.tesseract.test_utils.TimeTestSupport;

import static org.assertj.core.api.Assertions.assertThat;

class MomentRelationTest extends TimeTestSupport {

  @Test
  void relationEqNe_AllCases_Success() {
    assertEqNe(inf(), inf(), true);
    assertEqNe(inf(), ninf(), false);
    assertEqNe(inf(), moment(0), false);

    assertEqNe(ninf(), inf(), false);
    assertEqNe(ninf(), ninf(), true);
    assertEqNe(ninf(), moment(0), false);

    assertEqNe(moment(0), inf(), false);
    assertEqNe(moment(0), ninf(), false);
    assertEqNe(moment(0), moment(0), true);
    assertEqNe(moment(0), moment(5), false);
  }

  private void assertEqNe(Moment a, Moment b, boolean expected) {
    assertThat(a.eq(b)).isEqualTo(expected);
    assertThat(a.ne(b)).isEqualTo(!expected);
  }

  @Test
  void relationLt_AllCases_Success() {

    assertLt(inf(), inf(), false);
    assertLt(inf(), ninf(), false);
    assertLt(inf(), moment(0), false);

    assertLt(ninf(), inf(), true);
    assertLt(ninf(), ninf(), false);
    assertLt(ninf(), moment(0), true);

    assertLt(moment(0), inf(), true);
    assertLt(moment(0), ninf(), false);
    assertLt(moment(0), moment(0), false);
    assertLt(moment(0), moment(5), true);
    assertLt(moment(0), moment(-5), false);

  }

  private void assertLt(Moment a, Moment b, boolean expected) {
    assertThat(a.lt(b)).isEqualTo(expected);
  }

  @Test
  void relationLe_AllCases_Success() {

    assertLe(inf(), inf(), true);
    assertLe(inf(), ninf(), false);
    assertLe(inf(), moment(0), false);

    assertLe(ninf(), inf(), true);
    assertLe(ninf(), ninf(), true);
    assertLe(ninf(), moment(0), true);

    assertLe(moment(0), inf(), true);
    assertLe(moment(0), ninf(), false);
    assertLe(moment(0), moment(0), true);
    assertLe(moment(0), moment(5), true);
    assertLe(moment(0), moment(-5), false);

  }

  private void assertLe(Moment a, Moment b, boolean expected) {
    assertThat(a.le(b)).isEqualTo(expected);
  }

  @Test
  void relationGt_AllCases_Success() {

    assertGt(inf(), inf(), false);
    assertGt(inf(), ninf(), true);
    assertGt(inf(), moment(0), true);

    assertGt(ninf(), inf(), false);
    assertGt(ninf(), ninf(), false);
    assertGt(ninf(), moment(0), false);

    assertGt(moment(0), inf(), false);
    assertGt(moment(0), ninf(), true);
    assertGt(moment(0), moment(0), false);
    assertGt(moment(0), moment(5), false);
    assertGt(moment(0), moment(-5), true);

  }

  private void assertGt(Moment a, Moment b, boolean expected) {
    assertThat(a.gt(b)).isEqualTo(expected);
  }

  @Test
  void relationGe_AllCases_Success() {

    assertGe(inf(), inf(), true);
    assertGe(inf(), ninf(), true);
    assertGe(inf(), moment(0), true);

    assertGe(ninf(), inf(), false);
    assertGe(ninf(), ninf(), true);
    assertGe(ninf(), moment(0), false);

    assertGe(moment(0), inf(), false);
    assertGe(moment(0), ninf(), true);
    assertGe(moment(0), moment(0), true);
    assertGe(moment(0), moment(5), false);
    assertGe(moment(0), moment(-5), true);

  }

  private void assertGe(Moment a, Moment b, boolean expected) {
    assertThat(a.ge(b)).isEqualTo(expected);
  }

  @Test
  void min_AllCases_Success() {

    assertMin(inf(), inf(), inf());
    assertMin(inf(), ninf(), ninf());
    assertMin(inf(), moment(0), moment(0));

    assertMin(ninf(), inf(), ninf());
    assertMin(ninf(), ninf(), ninf());
    assertMin(ninf(), moment(0), ninf());

    assertMin(moment(0), inf(), moment(0));
    assertMin(moment(0), ninf(), ninf());
    assertMin(moment(0), moment(0), moment(0));
    assertMin(moment(0), moment(5), moment(0));
    assertMin(moment(0), moment(-5), moment(-5));

  }

  private void assertMin(Moment a, Moment b, Moment expected) {
    assertThat(a.min(b)).isEqualTo(expected);
  }

  @Test
  void max_AllCases_Success() {

    assertMax(inf(), inf(), inf());
    assertMax(inf(), ninf(), inf());
    assertMax(inf(), moment(0), inf());

    assertMax(ninf(), inf(), inf());
    assertMax(ninf(), ninf(), ninf());
    assertMax(ninf(), moment(0), moment(0));

    assertMax(moment(0), inf(), inf());
    assertMax(moment(0), ninf(), moment(0));
    assertMax(moment(0), moment(0), moment(0));
    assertMax(moment(0), moment(5), moment(5));
    assertMax(moment(0), moment(-5), moment(0));

  }

  private void assertMax(Moment a, Moment b, Moment expected) {
    assertThat(a.max(b)).isEqualTo(expected);
  }

  @Test
  void compareTo_DifferentOffset_Success() {
    var a = moment(OffsetDateTime.parse("2024-01-27T00:00-05:00"));
    var b = moment(OffsetDateTime.parse("2024-01-27T05:00-00:00"));
    assertThat(a.compareTo(b))
        .isEqualTo(0);
  }

}
