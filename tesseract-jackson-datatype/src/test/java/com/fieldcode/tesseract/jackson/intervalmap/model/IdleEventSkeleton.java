package com.fieldcode.tesseract.jackson.intervalmap.model;

import org.immutables.value.Value.Immutable;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@Immutable(singleton = true)
@JsonDeserialize(builder = ImmutableIdleEvent.Builder.class)
public abstract class IdleEventSkeleton implements IdleEvent {
}
