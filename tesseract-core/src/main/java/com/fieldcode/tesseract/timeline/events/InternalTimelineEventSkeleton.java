package com.fieldcode.tesseract.timeline.events;

import org.immutables.value.Value.Immutable;
import org.immutables.value.Value.Parameter;

import com.fieldcode.tesseract.Taggable.TagHolder;

@Immutable
abstract class InternalTimelineEventSkeleton implements InternalTimelineEvent {

  @Override
  @Parameter
  public abstract TagHolder getTags();

  @Override
  public boolean isUndefined() {
    return false;
  }

}
