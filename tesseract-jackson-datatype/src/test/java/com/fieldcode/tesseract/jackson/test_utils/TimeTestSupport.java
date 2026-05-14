package com.fieldcode.tesseract.jackson.test_utils;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;

import com.fieldcode.tesseract.Interval;
import com.fieldcode.tesseract.Location;
import com.fieldcode.tesseract.Moment;
import com.fieldcode.tesseract.Presence;
import com.fieldcode.tesseract.Taggable.TagHolder;
import com.fieldcode.tesseract.interval.Intervals;
import com.fieldcode.tesseract.moment.Moments;
import com.fieldcode.tesseract.presence.Presences;
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

  protected Moment ninf() {
    return Moments.ninf();
  }

  protected Moment inf() {
    return Moments.inf();
  }

  protected Interval interval(int from, int to) {
    return Intervals.interval(moment(from), moment(to));
  }

  protected Interval interval(OffsetDateTime from, OffsetDateTime to) {
    return Intervals.interval(moment(from), moment(to));
  }

  protected Presence presence(int at, double latitude, double longitude) {
    return Presences.presence(moment(at), latitude, longitude);
  }

  protected Presence presence(int at, Location location) {
    return Presences.presence(moment(at), location);
  }

  protected Presence presence(int at, Location location, TagHolder tags) {
    return Presences.presence(moment(at), location, tags);
  }

  protected Duration timeOf(Runnable runnable) {
    var sw = Stopwatch.createStarted();
    runnable.run();
    sw.stop();
    return sw.elapsed();
  }

}
