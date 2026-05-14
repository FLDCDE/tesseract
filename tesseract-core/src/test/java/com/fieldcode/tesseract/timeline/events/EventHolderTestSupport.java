package com.fieldcode.tesseract.timeline.events;

import com.fieldcode.tesseract.Interval;
import com.fieldcode.tesseract.Taggable.TagHolder;
import com.fieldcode.tesseract.timeline.ImmutableTimelineEvent;
import com.fieldcode.tesseract.timeline.TimelineEvent;

public class EventHolderTestSupport {

  private EventHolderTestSupport() {
  }

  public static TimelineEvent event(Interval interval, TagHolder tags) {
    return ImmutableTimelineEvent.of(interval, tags);
  }

}
