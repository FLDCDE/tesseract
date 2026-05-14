package com.fieldcode.tesseract.timeline.events;

import org.immutables.value.Value.Immutable;

import com.fieldcode.tesseract.Taggable.TagHolder;
import com.fieldcode.tesseract.tag.Tags;

@Immutable(singleton = true)
abstract class UndefinedIntervalEventSkeleton implements InternalTimelineEvent {

  @Override
  public TagHolder getTags() {
    return Tags.holder();
  }

  @Override
  public boolean isUndefined() {
    return true;
  }

}
