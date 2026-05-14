package com.fieldcode.tesseract.timeline;

import org.immutables.value.Value.Immutable;
import org.immutables.value.Value.Parameter;

import com.fieldcode.tesseract.Interval;
import com.fieldcode.tesseract.Taggable.TagHolder;

@Immutable
abstract class TimelineEventSkeleton implements TimelineEvent {

  @Override
  @Parameter
  public abstract Interval getInterval();

  @Override
  @Parameter
  public abstract TagHolder getTags();

}
