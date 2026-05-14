package com.fieldcode.tesseract.jackson.serializer.timeline;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fieldcode.tesseract.Interval;
import com.fieldcode.tesseract.Taggable.TagHolder;
import com.fieldcode.tesseract.jackson.utils.FunctionalDeserializer;
import com.fieldcode.tesseract.timeline.ImmutableTimelineEvent;
import com.fieldcode.tesseract.timeline.TimelineEvent;

public class TimelineEventDeserializer extends FunctionalDeserializer<TimelineEvent> {

  public TimelineEventDeserializer(JsonParser parser, DeserializationContext ctx) {
    super(parser, ctx);
  }

  public static JsonDeserializer<TimelineEvent> deserializer() {
    return wrap(TimelineEventDeserializer::new);
  }

  @Override
  public TimelineEvent get() {
    return ImmutableTimelineEvent.of(
        field("interval", Interval.class),
        field("tags", TagHolder.class)
    );
  }

}
