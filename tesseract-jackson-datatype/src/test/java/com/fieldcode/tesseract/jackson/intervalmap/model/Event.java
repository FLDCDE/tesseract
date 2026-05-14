package com.fieldcode.tesseract.jackson.intervalmap.model;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import static com.fasterxml.jackson.annotation.JsonTypeInfo.Id.DEDUCTION;

@JsonTypeInfo(use = DEDUCTION)
@JsonSubTypes(
    {
        @Type(ImmutableIdleEvent.class),
        @Type(ImmutableTaskEvent.class),
        @Type(ImmutableDriveEvent.class)
    }
)
public interface Event {}
