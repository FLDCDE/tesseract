package com.fieldcode.tesseract.interval;

import org.junit.jupiter.api.Test;

import static com.fieldcode.tesseract.QueryDirection.BACKWARD;
import static com.fieldcode.tesseract.QueryDirection.FORWARD;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class IntervalCreationTestTest extends IntervalTestSupport {


  @Test
  void parse_Always_Success() {

    var interval = Intervals.always();
    var str = interval.toString();

    assertThat(Intervals.parse(str))
        .isEqualTo(interval);
  }

  @Test
  void parse_Bounded_Success() {

    var interval = interval(0, 10);
    var str = interval.toString();

    assertThat(Intervals.parse(str))
        .isEqualTo(interval);
  }

  @Test
  void parse_To_Success() {

    var interval = intervalTo(0);
    var str = interval.toString();

    assertThat(Intervals.parse(str))
        .isEqualTo(interval);
  }

  @Test
  void parse_From_Success() {

    var interval = intervalFrom(0);
    var str = interval.toString();

    assertThat(Intervals.parse(str))
        .isEqualTo(interval);
  }

  @Test
  void interval_NinfMoment_IntervalTo() {
    var interval = Intervals.interval(ninf(), moment(0));

    assertThat(interval)
        .matches(a -> a.getLower().equals(ninf()))
        .matches(a -> a.getUpper().equals(moment(0)))
        .isInstanceOf(ImmutableIntervalTo.class);
  }

  @Test
  void interval_NinfInf_Always() {
    var interval = Intervals.interval(ninf(), inf());

    assertThat(interval)
        .matches(a -> a.getLower().equals(ninf()))
        .matches(a -> a.getUpper().equals(inf()))
        .isInstanceOf(ImmutableAlwaysInterval.class);
  }

  @Test
  void interval_MomentMoment_BoundedInterval() {
    var interval = Intervals.interval(moment(0), moment(1));

    assertThat(interval)
        .matches(a -> a.getLower().equals(moment(0)))
        .matches(a -> a.getUpper().equals(moment(1)))
        .isInstanceOf(ImmutableBoundedInterval.class);
  }

  @Test
  void interval_MomentInf_IntervalFrom() {
    var interval = Intervals.interval(moment(0), inf());

    assertThat(interval)
        .matches(a -> a.getLower().equals(moment(0)))
        .matches(a -> a.getUpper().equals(inf()))
        .isInstanceOf(ImmutableIntervalFrom.class);
  }

  @Test
  void interval_NinfNinf_Exception() {
    assertThatThrownBy(() -> Intervals.interval(ninf(), ninf()))
        .isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  void interval_InfInf_Exception() {
    assertThatThrownBy(() -> Intervals.interval(inf(), inf()))
        .isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  void interval_InfNinf_Exception() {
    assertThatThrownBy(() -> Intervals.interval(inf(), ninf()))
        .isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  void interval_WrongOrder_Exception() {
    assertThatThrownBy(() -> Intervals.interval(moment(10), moment(0)))
        .isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  void interval_TimeTime_BoundedInterval() {
    var interval = Intervals.interval(at(0), at(10));

    assertThat(interval)
        .matches(a -> a.getStart().equals(at(0)))
        .matches(a -> a.getEnd().equals(at(10)))
        .isInstanceOf(ImmutableBoundedInterval.class);
  }

  @Test
  void interval_TimeTimeWrongOrder_Exception() {
    assertThatThrownBy(() -> Intervals.interval(at(10), at(0)))
        .isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  void intervalDirection_NinfForward_Always() {
    var interval = Intervals.interval(ninf(), FORWARD);

    assertThat(interval)
        .matches(a -> a.getLower().equals(ninf()))
        .matches(a -> a.getUpper().equals(inf()))
        .isInstanceOf(ImmutableAlwaysInterval.class);
  }

  @Test
  void intervalDirection_MomentForward_IntervalFrom() {
    var interval = Intervals.interval(moment(0), FORWARD);

    assertThat(interval)
        .matches(a -> a.getLower().equals(moment(0)))
        .matches(a -> a.getUpper().equals(inf()))
        .isInstanceOf(ImmutableIntervalFrom.class);
  }

  @Test
  void intervalDirection_InfForward_Exception() {
    assertThatThrownBy(() -> Intervals.interval(inf(), FORWARD))
        .isInstanceOf(IllegalArgumentException.class);
  }


  @Test
  void intervalDirection_InfBackward_Always() {
    var interval = Intervals.interval(inf(), BACKWARD);

    assertThat(interval)
        .matches(a -> a.getLower().equals(ninf()))
        .matches(a -> a.getUpper().equals(inf()))
        .isInstanceOf(ImmutableAlwaysInterval.class);
  }

  @Test
  void intervalDirection_MomentBackward_IntervalTo() {
    var interval = Intervals.interval(moment(0), BACKWARD);

    assertThat(interval)
        .matches(a -> a.getLower().equals(ninf()))
        .matches(a -> a.getUpper().equals(moment(0)))
        .isInstanceOf(ImmutableIntervalTo.class);
  }

  @Test
  void intervalDirection_NInfBackward_Exception() {
    assertThatThrownBy(() -> Intervals.interval(ninf(), BACKWARD))
        .isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  void intervalTo_Ninf_Exception() {
    assertThatThrownBy(() -> Intervals.intervalTo(ninf()))
        .isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  void intervalTo_Moment_IntervalTo() {
    var interval = Intervals.intervalTo(moment(0));

    assertThat(interval)
        .matches(a -> a.getLower().equals(ninf()))
        .matches(a -> a.getUpper().equals(moment(0)))
        .isInstanceOf(ImmutableIntervalTo.class);
  }

  @Test
  void intervalTo_Inf_Always() {
    var interval = Intervals.intervalTo(inf());
    assertThat(interval)
        .matches(a -> a.getLower().equals(ninf()))
        .matches(a -> a.getUpper().equals(inf()))
        .isInstanceOf(ImmutableAlwaysInterval.class);
  }

  @Test
  void intervalTo_Time_IntervalTo() {
    var interval = Intervals.intervalTo(at(0));
    assertThat(interval)
        .matches(a -> a.getLower().equals(ninf()))
        .matches(a -> a.getUpper().equals(moment(0)))
        .isInstanceOf(ImmutableIntervalTo.class);
  }


  @Test
  void intervalFrom_Inf_Exception() {
    assertThatThrownBy(() -> Intervals.intervalFrom(inf()))
        .isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  void intervalFrom_Moment_IntervalFrom() {
    var interval = Intervals.intervalFrom(moment(0));

    assertThat(interval)
        .matches(a -> a.getLower().equals(moment(0)))
        .matches(a -> a.getUpper().equals(inf()))
        .isInstanceOf(ImmutableIntervalFrom.class);
  }

  @Test
  void intervalFrom_Ninf_Always() {
    var interval = Intervals.intervalFrom(ninf());
    assertThat(interval)
        .matches(a -> a.getLower().equals(ninf()))
        .matches(a -> a.getUpper().equals(inf()))
        .isInstanceOf(ImmutableAlwaysInterval.class);
  }

  @Test
  void intervalFrom_Time_IntervalFrom() {
    var interval = Intervals.intervalFrom(at(0));
    assertThat(interval)
        .matches(a -> a.getLower().equals(moment(0)))
        .matches(a -> a.getUpper().equals(inf()))
        .isInstanceOf(ImmutableIntervalFrom.class);
  }

  @Test
  void always_Singleton() {

    var interval = Intervals.always();

    assertThat(interval)
        .matches(a -> a.getLower().equals(ninf()))
        .matches(a -> a.getUpper().equals(inf()))
        .isInstanceOf(ImmutableAlwaysInterval.class)
        .isSameAs(Intervals.always()); // Because it's a singleton
  }

}
