package com.fieldcode.tesseract.timeline;

import java.time.OffsetDateTime;

import com.fieldcode.tesseract.FiniteMoment;
import com.fieldcode.tesseract.Interval;
import com.fieldcode.tesseract.Presence;
import com.fieldcode.tesseract.Taggable.TagHolder;
import com.fieldcode.tesseract.interval.Intervals;
import com.fieldcode.tesseract.location.Locations;
import com.fieldcode.tesseract.moment.Moments;
import com.fieldcode.tesseract.presence.Presences;
import com.fieldcode.tesseract.tag.Tags;

import static java.time.ZoneOffset.UTC;
import static java.time.temporal.ChronoUnit.DAYS;

public class TimelineTestSupport {


  private static final OffsetDateTime reference = OffsetDateTime.now()
      .withOffsetSameInstant(UTC)
      .truncatedTo(DAYS);

  private TimelineTestSupport() {
  }

  public static TagHolder tag(String key, String value) {
    return Tags.holder(Tags.tag(key, value));
  }

  public static TagHolder tag(String key) {
    return Tags.holder(Tags.tag(key));
  }

  public static OffsetDateTime at(int hours) {
    return reference.plusHours(hours);
  }

  public static FiniteMoment moment(int hours) {
    return Moments.moment(reference.plusHours(hours));
  }

  public static Interval intervalTo(int end) {
    return Intervals.intervalTo(at(end));
  }

  public static Interval intervalFrom(int start) {
    return Intervals.intervalFrom(at(start));
  }

  public static Interval interval(int start, int end) {
    return Intervals.interval(at(start), at(end));
  }

  public static Presence presence(int at, double latitude, double longitude) {
    return Presences.presence(moment(at), latitude, longitude);
  }

  public static Presence anywhere(int at) {
    return Presences.presence(moment(at), Locations.anywhere());
  }

  public static ModifiableTimeline emptyTimeline() {
    return TimelineBuilder.of()
        .build();
  }

}
