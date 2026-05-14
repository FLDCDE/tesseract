package com.fieldcode.tesseract.jackson.intervalmap.model;

import org.immutables.value.Value.Immutable;
import org.immutables.value.Value.Parameter;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@Immutable
@JsonDeserialize(builder = ImmutableTaskEvent.Builder.class)
public abstract class TaskEventSkeleton implements TaskEvent {

  @Override
  @Parameter
  public abstract String getCode();

}
