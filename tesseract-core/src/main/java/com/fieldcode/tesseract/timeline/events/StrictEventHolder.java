package com.fieldcode.tesseract.timeline.events;

import com.fieldcode.tesseract.Interval;
import com.fieldcode.tesseract.IntervalMap;
import com.fieldcode.tesseract.Taggable.TagHolder;
import com.fieldcode.tesseract.interval.IntervalMaps;
import com.fieldcode.tesseract.interval.Intervals;
import com.fieldcode.tesseract.timeline.LayerType;
import com.google.common.base.Objects;

import static com.fieldcode.tesseract.timeline.LayerType.EVENT_STRICT;
import static com.google.common.base.Preconditions.checkState;

class StrictEventHolder extends MergeEventHolder {

  protected StrictEventHolder(IntervalMap<InternalTimelineEvent> events) {
    super(events);
  }

  public static EventHolder of() {
    return new StrictEventHolder(IntervalMaps.disjoint(UNDEFINED_VALUE));
  }

  @Override
  public EventHolder put(Interval window, TagHolder tags) {
    checkState(isEmpty(window), "Window is not empty. Strict holder cannot store overlapping event. [window=%s]", window);
    super.put(window, tags);
    return this;
  }

  @Override
  public LayerType getType() {
    return EVENT_STRICT;
  }

  @Override
  public EventHolder copy() {
    return new StrictEventHolder(copy(getEventsMap()));
  }

  @Override
  public int hashCode() {
    var a = events(Intervals.always().forward()).list();
    return Objects.hashCode(a);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {return true;}
    if (o == null || getClass() != o.getClass()) {return false;}
    StrictEventHolder that = (StrictEventHolder) o;

    var a = events(Intervals.always().forward()).list();
    var b = that.events(Intervals.always().forward()).list();

    return Objects.equal(a, b);
  }
}
