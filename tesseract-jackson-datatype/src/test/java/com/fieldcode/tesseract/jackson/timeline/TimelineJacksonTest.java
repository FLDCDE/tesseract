package com.fieldcode.tesseract.jackson.timeline;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.fieldcode.tesseract.Taggable.TagHolder;
import com.fieldcode.tesseract.jackson.test_utils.Jsons;
import com.fieldcode.tesseract.jackson.test_utils.TimeTestSupport;
import com.fieldcode.tesseract.tag.Tags;
import com.fieldcode.tesseract.timeline.Timeline;
import com.fieldcode.tesseract.timeline.TimelineBuilder;

import static com.fieldcode.tesseract.timeline.TimelineEvents.event;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Timeline Jackson serialization")
class TimelineJacksonTest extends TimeTestSupport {

  public static final String STRICT_LAYER = "strict";
  public static final String MERGE_LAYER = "merge";
  public static final String OVERLAP_LAYER = "overlap";
  public static final String PRESENCE_LAYER = "presence";
  public static final String INTERVAL_LAYER = "interval";

  private TagHolder tags(String... names) {
    var builder = Tags.builder();
    for (String name : names) {
      builder.tag(name);
    }
    return builder.build();
  }

  @Test
  @DisplayName("should round-trip a timeline with strict, merge, overlap, presence and interval layers")
  void clone_Timeline_Success() {
    // Arrange: a timeline with five layers, each populated with tagged events, presences or intervals
    var timeline = TimelineBuilder.of()
        .strict(STRICT_LAYER, tags("strict-layer"))
        .merge(MERGE_LAYER, tags("merge-layer"))
        .overlap(OVERLAP_LAYER, tags("overlap-layer"))
        .presence(PRESENCE_LAYER, tags("presence-layer"))
        .interval(INTERVAL_LAYER, tags("interval-layer"))
        .build();

    timeline
        .put(STRICT_LAYER, event(interval(0, 8), tags("strict")))
        .put(STRICT_LAYER, event(interval(12, 24), tags("strict")))
        .put(MERGE_LAYER, event(interval(0, 8), tags("merge")))
        .put(MERGE_LAYER, event(interval(12, 24), tags("merge")))
        .put(OVERLAP_LAYER, event(interval(0, 8), tags("overlap", "task1")))
        .put(OVERLAP_LAYER, event(interval(0, 8), tags("overlap", "task2")))
        .put(PRESENCE_LAYER, presence(0, 0.0, 0.0))
        .put(PRESENCE_LAYER, presence(8, 0.1, 0.0))
        .put(INTERVAL_LAYER, interval(0, 8))
        .put(INTERVAL_LAYER, interval(12, 24));

    // Act: serialize the timeline, clone it via a round-trip, and serialize the clone
    var originalJson = Jsons.stringify(timeline, true);

    var cloned = Jsons.clone(timeline, Timeline.class);
    var clonedJson = Jsons.stringify(cloned, true);

    // Assert: the cloned timeline's JSON matches the original's JSON
    assertThat(clonedJson)
        .isEqualTo(originalJson);
  }

}
