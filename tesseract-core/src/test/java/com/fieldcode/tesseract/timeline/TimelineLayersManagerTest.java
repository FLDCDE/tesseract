package com.fieldcode.tesseract.timeline;

import org.junit.jupiter.api.Test;

import com.fieldcode.tesseract.tag.Tags;

import static com.fieldcode.tesseract.timeline.LayerType.EVENT_MERGE;
import static com.fieldcode.tesseract.timeline.LayerType.EVENT_OVERLAP;
import static com.fieldcode.tesseract.timeline.LayerType.EVENT_STRICT;
import static com.fieldcode.tesseract.timeline.LayerType.PRESENCE;
import static com.fieldcode.tesseract.timeline.TimelineEvents.event;
import static com.fieldcode.tesseract.timeline.TimelineTestSupport.emptyTimeline;
import static com.fieldcode.tesseract.timeline.TimelineTestSupport.interval;
import static org.assertj.core.api.Assertions.assertThat;

class TimelineLayersManagerTest {

  private static final String LAYER_1 = "layer1";
  private static final String LAYER_2 = "layer2";
  private static final String LAYER_3 = "layer3";
  private static final String LAYER_4 = "layer4";

  @Test
  void addLayers_AllTypes_Success() {

    var timeline = emptyTimeline();

    var manager = timeline.layers();

    manager
        .addLayer(LAYER_1, EVENT_STRICT, Tags.holder())
        .addLayer(LAYER_2, EVENT_MERGE, Tags.holder())
        .addLayer(LAYER_3, EVENT_OVERLAP, Tags.holder())
        .addLayer(LAYER_4, PRESENCE, Tags.holder());

    var layerIds = manager.getIds();

    assertThat(layerIds)
        .containsExactlyInAnyOrder(LAYER_1, LAYER_2, LAYER_3, LAYER_4);
  }

  @Test
  void getType_AllTypes_Success() {

    var timeline = TimelineBuilder.of()
        .strict(LAYER_1)
        .merge(LAYER_2)
        .overlap(LAYER_3)
        .presence(LAYER_4)
        .build();

    var manager = timeline.layers();

    assertThat(manager.getType(LAYER_1))
        .isEqualTo(EVENT_STRICT);

    assertThat(manager.getType(LAYER_2))
        .isEqualTo(EVENT_MERGE);

    assertThat(manager.getType(LAYER_3))
        .isEqualTo(EVENT_OVERLAP);

    assertThat(manager.getType(LAYER_4))
        .isEqualTo(PRESENCE);
  }

  @Test
  void addStrictLayer_Success() {
    var manager = emptyTimeline().layers();

    var before = manager.getIds();

    manager.addLayer(LAYER_1, EVENT_STRICT, Tags.holder());

    var after = manager.getIds();

    assertThat(before).isEmpty();
    assertThat(after).containsExactly(LAYER_1);
    assertThat(manager.getType(LAYER_1)).isEqualTo(EVENT_STRICT);
  }

  @Test
  void addMergeLayer_Success() {
    var manager = emptyTimeline().layers();

    var before = manager.getIds();

    manager.addLayer(LAYER_1, EVENT_MERGE, Tags.holder());

    var after = manager.getIds();

    assertThat(before).isEmpty();
    assertThat(after).containsExactly(LAYER_1);
    assertThat(manager.getType(LAYER_1)).isEqualTo(EVENT_MERGE);
  }

  @Test
  void addOverlapLayer_Success() {
    var manager = emptyTimeline().layers();

    var before = manager.getIds();

    manager.addLayer(LAYER_1, EVENT_OVERLAP, Tags.holder());

    var after = manager.getIds();

    assertThat(before).isEmpty();
    assertThat(after).containsExactly(LAYER_1);
    assertThat(manager.getType(LAYER_1)).isEqualTo(EVENT_OVERLAP);
  }

  @Test
  void addPresenceLayer_Success() {
    var manager = emptyTimeline().layers();

    var before = manager.getIds();

    manager.addLayer(LAYER_1, PRESENCE, Tags.holder());

    var after = manager.getIds();

    assertThat(before).isEmpty();
    assertThat(after).containsExactly(LAYER_1);
    assertThat(manager.getType(LAYER_1)).isEqualTo(PRESENCE);
  }

  @Test
  void duplicate_Event_Success() {

    var timeline = emptyTimeline();
    var manager = timeline
        .layers()
        .addLayer(LAYER_1, EVENT_STRICT, Tags.holder());

    // pre-duplication
    timeline.put(LAYER_1, event(interval(0, 5)));

    manager.duplicateLayer(LAYER_1, LAYER_2);

    // post-duplication
    timeline.put(LAYER_1, event(interval(10, 15)));
    timeline.put(LAYER_2, event(interval(20, 25)));

    var intervals1 = timeline.asIntervalCollection(LAYER_1).intervals().list();
    var intervals2 = timeline.asIntervalCollection(LAYER_2).intervals().list();

    assertThat(intervals1)
        .containsExactly(
            interval(0, 5),       // pre-duplication
            interval(10, 15)      // post-duplication
        );

    assertThat(intervals2)
        .containsExactly(
            interval(0, 5),       // pre-duplication
            interval(20, 25)      // post-duplication
        );

    assertThat(manager.getIds())
        .containsExactlyInAnyOrder(LAYER_1, LAYER_2);
  }

}
