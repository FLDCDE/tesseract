package com.fieldcode.tesseract.timeline.events;

import com.fieldcode.tesseract.DirectedInterval;
import com.fieldcode.tesseract.FluentIterator;
import com.fieldcode.tesseract.Interval;
import com.fieldcode.tesseract.IntervalCollection;
import com.fieldcode.tesseract.Taggable.TagHolder;
import com.fieldcode.tesseract.timeline.LayerType;
import com.fieldcode.tesseract.timeline.TimelineEvent;
import com.google.errorprone.annotations.CanIgnoreReturnValue;

public interface EventHolder {

  @CanIgnoreReturnValue
  EventHolder put(Interval window, TagHolder tags);

  EventHolder put(TimelineEvent event);

  @CanIgnoreReturnValue
  EventHolder remove(Interval window);

  FluentIterator<TimelineEvent> events(DirectedInterval window);

  IntervalCollection intervals();

  boolean isEmpty(Interval window);

  LayerType getType();

  EventHolder copy();

}
