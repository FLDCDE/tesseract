package com.fieldcode.tesseract.jackson.intervalmap.model;

import org.immutables.value.Value.Immutable;
import org.immutables.value.Value.Parameter;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fieldcode.tesseract.Location;

@Immutable
@JsonDeserialize(builder = ImmutableDriveEvent.Builder.class)
public abstract class DriveEventSkeleton implements DriveEvent {

  @Override
  @Parameter
  public abstract Location getOrigin();

  @Override
  @Parameter
  public abstract Location getDestination();

}
