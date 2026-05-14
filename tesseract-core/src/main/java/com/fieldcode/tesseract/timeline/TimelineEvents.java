package com.fieldcode.tesseract.timeline;

import java.util.Collection;

import com.fieldcode.tesseract.Interval;
import com.fieldcode.tesseract.Taggable.Tag;
import com.fieldcode.tesseract.Taggable.TagHolder;
import com.fieldcode.tesseract.tag.Tags;

public class TimelineEvents {

  private TimelineEvents() {}

  public static TimelineEvent event(Interval interval, TagHolder tags) {
    return ImmutableTimelineEvent.of(interval, tags);
  }

  public static TimelineEvent event(Interval interval, Tag... tags) {
    return ImmutableTimelineEvent.of(interval, Tags.holder(tags));
  }

  public static TimelineEvent event(Interval interval, Collection<Tag> tags) {
    return ImmutableTimelineEvent.of(interval, Tags.holder(tags));
  }

}
