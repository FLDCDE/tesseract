package com.fieldcode.tesseract.test_utils;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;

import com.fieldcode.tesseract.InfiniteMoment;
import com.fieldcode.tesseract.Interval;
import com.fieldcode.tesseract.Moment;
import com.fieldcode.tesseract.Movement;
import com.fieldcode.tesseract.Presence;
import com.fieldcode.tesseract.interval.Intervals;
import com.fieldcode.tesseract.moment.Moments;
import com.fieldcode.tesseract.movement.Movements;
import com.google.common.base.Stopwatch;

public class TimeTestSupport {

  private final OffsetDateTime reference = OffsetDateTime.now()
      .withOffsetSameLocal(ZoneOffset.UTC)
      .truncatedTo(ChronoUnit.DAYS);

  protected OffsetDateTime at(int hours) {
    return reference.plusHours(hours);
  }

  protected Moment moment(OffsetDateTime at) {
    return Moments.moment(at);
  }

  protected Moment moment(int hours) {
    return Moments.moment(at(hours));
  }

  protected Moment moment() {
    return moment(0);
  }

  protected Movement movement(Presence origin, Presence destination) {
    return Movements.movement(origin, destination);
  }

  protected Interval interval(int fromHours, int toHours) {
    return Intervals.interval(moment(fromHours), moment(toHours));
  }

  protected Interval intervalTo(int toHours) {
    return Intervals.intervalTo(moment(toHours));
  }

  protected Interval intervalFrom(int fromHours) {
    return Intervals.intervalFrom(moment(fromHours));
  }

  protected InfiniteMoment ninf() {
    return Moments.ninf();
  }

  protected InfiniteMoment inf() {
    return Moments.inf();
  }

  protected Duration timeOf(Runnable runnable) {
    var sw = Stopwatch.createStarted();
    runnable.run();
    sw.stop();
    return sw.elapsed();
  }

}
