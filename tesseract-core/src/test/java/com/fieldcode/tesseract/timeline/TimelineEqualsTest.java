package com.fieldcode.tesseract.timeline;

import org.junit.jupiter.api.Test;

import static com.fieldcode.tesseract.timeline.InternalModifiableTimelineImplTest.PRESENCES;
import static com.fieldcode.tesseract.timeline.InternalModifiableTimelineImplTest.PUDOS;
import static com.fieldcode.tesseract.timeline.InternalModifiableTimelineImplTest.TICKETS;
import static com.fieldcode.tesseract.timeline.InternalModifiableTimelineImplTest.WORKING_HOURS;
import static com.fieldcode.tesseract.timeline.TimelineEvents.event;
import static com.fieldcode.tesseract.timeline.TimelineTestSupport.interval;
import static com.fieldcode.tesseract.timeline.TimelineTestSupport.presence;
import static com.fieldcode.tesseract.timeline.TimelineTestSupport.tag;
import static org.assertj.core.api.Assertions.assertThat;

class TimelineEqualsTest {

  @Test
  void timeline_equal_Success() {

    var timeline = TimelineBuilder.of()
        .strict(TICKETS)
        .overlap(PUDOS)
        .presence(PRESENCES)
        .interval(WORKING_HOURS)
        .build();

    var timeline2 = TimelineBuilder.of()
        .strict(TICKETS)
        .overlap(PUDOS)
        .presence(PRESENCES)
        .interval(WORKING_HOURS)
        .build();

    timeline.put(WORKING_HOURS, interval(2, 3));
    timeline.put(PUDOS, event(interval(0, 10), tag("pudo")));
    timeline.put(PUDOS, event(interval(5, 10), tag("pudo")));
    timeline.put(TICKETS, event(interval(0, 1), tag("ticket1")));
    timeline.put(PRESENCES, presence(0, 0.0, 0.0));

    timeline2.put(WORKING_HOURS, interval(2, 3));
    timeline2.put(PUDOS, event(interval(5, 10), tag("pudo")));
    timeline2.put(PUDOS, event(interval(0, 10), tag("pudo")));
    timeline2.put(TICKETS, event(interval(0, 1), tag("ticket1")));
    timeline2.put(PRESENCES, presence(0, 0.0, 0.0));

    assertThat(timeline).isEqualTo(timeline2);
  }

  @Test
  void timeline_intervalNotEquals_Success() {

    var timeline = TimelineBuilder.of()
        .interval(WORKING_HOURS)
        .build();

    var timeline2 = TimelineBuilder.of()
        .interval(WORKING_HOURS)
        .build();

    timeline.put(WORKING_HOURS, interval(2, 3));

    timeline2.put(WORKING_HOURS, interval(3, 4));

    assertThat(timeline).isNotEqualTo(timeline2);
  }

  @Test
  void timeline_strictNotEquals_Success() {

    var timeline = TimelineBuilder.of()
        .strict(TICKETS)
        .build();

    var timeline2 = TimelineBuilder.of()
        .strict(TICKETS)
        .build();

    timeline.put(TICKETS, event(interval(0, 1), tag("ticket1")));

    timeline2.put(TICKETS, event(interval(0, 2), tag("ticket1")));

    assertThat(timeline).isNotEqualTo(timeline2);
  }

  @Test
  void timeline_overlapNotEquals_Success() {

    var timeline = TimelineBuilder.of()
        .overlap(PUDOS)
        .build();

    var timeline2 = TimelineBuilder.of()
        .overlap(PUDOS)
        .build();

    timeline.put(PUDOS, event(interval(0, 10), tag("pudo")));

    timeline2.put(PUDOS, event(interval(1, 10), tag("pudo")));

    assertThat(timeline).isNotEqualTo(timeline2);
  }

  @Test
  void timeline_presenceNotEquals_Success() {

    var timeline = TimelineBuilder.of()
        .presence(PRESENCES)
        .build();

    var timeline2 = TimelineBuilder.of()
        .presence(PRESENCES)
        .build();

    timeline2.put(PRESENCES, presence(0, 0.0, 0.0));

    timeline2.put(PRESENCES, presence(1, 0.0, 0.0));

    assertThat(timeline).isNotEqualTo(timeline2);
  }
}
