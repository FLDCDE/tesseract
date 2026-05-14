package com.fieldcode.tesseract.jackson.serializer.timeline;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fieldcode.tesseract.IntervalCollection;
import com.fieldcode.tesseract.PresenceSet;
import com.fieldcode.tesseract.Taggable.TagHolder;
import com.fieldcode.tesseract.jackson.utils.FunctionalDeserializer;
import com.fieldcode.tesseract.timeline.LayerType;
import com.fieldcode.tesseract.timeline.ModifiableTimeline;
import com.fieldcode.tesseract.timeline.Timeline;
import com.fieldcode.tesseract.timeline.TimelineBuilder;
import com.fieldcode.tesseract.timeline.TimelineEvent;
import com.fieldcode.tesseract.timeline.TimelineEvents;

import lombok.Data;

public class TimelineDeserializer extends FunctionalDeserializer<Timeline> {

  private final ModifiableTimeline timeline;

  public TimelineDeserializer(JsonParser parser, DeserializationContext ctx) {
    super(parser, ctx);
    this.timeline = TimelineBuilder.of().build();
  }

  public static JsonDeserializer<Timeline> deserializer() {
    return wrap(TimelineDeserializer::new);
  }

  @Override
  public Timeline get() {
    array("layers", RawLayer.class)
        .forEach(this::addLayer);
    return timeline;
  }

  private void addLayer(RawLayer layer) {

    timeline.layers().addLayer(layer.id, layer.type, layer.tags);

    switch (layer.type) {
      case EVENT_STRICT:
      case EVENT_OVERLAP:
      case EVENT_MERGE:
        layer.events.forEach(event -> timeline.put(layer.id, TimelineEvents.event(event.getInterval(), event.getTags())));
        break;
      case INTERVAL:
        layer
            .intervals
            .intervals()
            .forEach(interval -> timeline.put(layer.id, interval));
        break;
      case PRESENCE:
        layer
            .presences
            .presences()
            .forEach(presence -> timeline.put(layer.id, presence));
        break;
      default:
        throw new IllegalArgumentException("Unsupported layer type: " + layer.type);
    }

  }


  @Data
  @JsonIgnoreProperties(ignoreUnknown = true)
  public static class RawLayer {

    String id;
    LayerType type;
    TagHolder tags;
    PresenceSet presences;
    List<TimelineEvent> events;
    IntervalCollection intervals;

  }

}
