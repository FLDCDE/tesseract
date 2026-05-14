package com.fieldcode.tesseract.timeline;

import org.junit.jupiter.api.Test;

import com.fieldcode.tesseract.Presence;
import com.fieldcode.tesseract.presence.Presences;

import static com.fieldcode.tesseract.interval.Intervals.always;
import static com.fieldcode.tesseract.location.Locations.anywhere;
import static com.fieldcode.tesseract.location.Locations.location;
import static com.fieldcode.tesseract.moment.Moments.inf;
import static com.fieldcode.tesseract.moment.Moments.ninf;
import static com.fieldcode.tesseract.timeline.TimelineEvents.event;
import static com.fieldcode.tesseract.timeline.TimelineTestSupport.anywhere;
import static com.fieldcode.tesseract.timeline.TimelineTestSupport.interval;
import static com.fieldcode.tesseract.timeline.TimelineTestSupport.moment;
import static com.fieldcode.tesseract.timeline.TimelineTestSupport.presence;
import static com.fieldcode.tesseract.timeline.TimelineTestSupport.tag;
import static org.assertj.core.api.Assertions.assertThat;

class InternalModifiableTimelineImplTest {

  public static final String TICKETS = "TICKETS";
  public static final String PUDOS = "PUDOS";
  public static final String WORKING_HOURS = "WORKING_HOURS";
  public static final String PRESENCES = "PRESENCES";


  @Test
  void presence_Set_Success() {

    var timeline = TimelineBuilder.of()
        .presence(PRESENCES)
        .presence("P2")
        .build();

    timeline.put(PRESENCES, presence(0, 0.0, 0.0));
    timeline.put(PRESENCES, presence(10, 1.0, 1.0));
    timeline.put("P2", presence(10, 10.0, 10.0));

    var p1 = timeline.selectPresences(PRESENCES, interval(5, 15).forward()).list();
    var p2 = timeline.selectPresences("P2", interval(5, 15).forward()).list();

    assertThat(p1).containsExactly(
        presence(5, 0.0, 0.0),
        presence(10, 1.0, 1.0),
        anywhere(15)
    );

    assertThat(p2).containsExactly(
        anywhere(5),
        presence(10, 10.0, 10.0),
        anywhere(15)
    );

  }

  @Test
  void copy_Success() {

    var timeline = TimelineBuilder.of()
        .strict(TICKETS)
        .overlap(PUDOS)
        .interval(WORKING_HOURS)
        .presence(PRESENCES)
        .build();

    timeline.put(TICKETS, event(interval(0, 1), tag("ticket")));
    timeline.put(PUDOS, event(interval(1, 2), tag("pudo")));
    timeline.put(WORKING_HOURS, interval(2, 3));
    timeline.put(PRESENCES, presence(10, 0.0, 0.0));

    var copied = timeline.copy();

    assertThat(copied.layers().getIds())
        .containsExactlyInAnyOrder(
            TICKETS, PUDOS, WORKING_HOURS, PRESENCES
        );

    var tickets = copied.selectEvents(TICKETS, always().forward()).list();

    assertThat(tickets)
        .extracting(TimelineEvent::getInterval)
        .containsExactly(
            interval(0, 1)
        );
    assertThat(tickets)
        .extracting(TimelineEvent::getTags)
        .containsExactly(
            tag("ticket")
        );

    var pudos = copied.selectEvents(PUDOS, always().forward()).list();

    assertThat(pudos)
        .extracting(TimelineEvent::getInterval)
        .containsExactly(
            interval(1, 2)
        );

    assertThat(pudos)
        .extracting(TimelineEvent::getTags)
        .containsExactly(
            tag("pudo")
        );

    var workingHours = copied.selectIntervals(WORKING_HOURS, always().forward()).list();

    assertThat(workingHours)
        .containsExactly(
            interval(2, 3)
        );

    var presences = copied.selectPresences(PRESENCES, always().forward()).list();

    assertThat(presences)
        .containsExactly(
            Presences.presence(ninf(), anywhere()),
            presence(10, 0.0, 0.0),
            Presences.presence(inf(), anywhere())
        );

  }

  @Test
  void remove_Events_Success() {

    var timeline = TimelineBuilder.of()
        .overlap(PUDOS)
        .build();

    timeline.put(PUDOS, event(interval(0, 10), tag("pudo")));
    timeline.put(PUDOS, event(interval(5, 15), tag("pudo")));

    timeline.remove(PUDOS, interval(5, 10));

    var events = timeline.selectEvents(PUDOS, always().forward()).list();

    assertThat(events)
        .extracting(TimelineEvent::getInterval)
        .containsExactly(
            interval(0, 5),
            interval(10, 15)
        );
  }

  @Test
  void remove_Presences_Success() {

    var timeline = TimelineBuilder.of()
        .presence(PRESENCES)
        .build();

    timeline.put(PRESENCES, presence(0, 0.0, 0.0));
    timeline.put(PRESENCES, presence(5, 5.0, 0.0));
    timeline.put(PRESENCES, presence(10, 10.0, 0.0));
    timeline.put(PRESENCES, presence(15, 15.0, 0.0));

    timeline.remove(PRESENCES, interval(5, 10));

    var presences = timeline.selectPresences(PRESENCES, always().forward()).list();

    assertThat(presences)
        .extracting(Presence::getMoment)
        .containsExactly(
            ninf(),
            moment(0),
            moment(10),
            moment(15),
            inf()
        );

    assertThat(presences)
        .extracting(Presence::getLocation)
        .containsExactly(
            anywhere(),
            location(0.0, 0.0),
            location(10.0, 0.0),
            location(15.0, 0.0),
            anywhere()
        );
  }

  @Test
  void put_Overlap_Success() {
    var timeline = TimelineBuilder.of()
        .overlap(TICKETS)
        .build();

    timeline.put(TICKETS, event(interval(0, 1), tag("ticket1")));
    timeline.put(TICKETS, event(interval(0, 1), tag("ticket2")));
    timeline.put(TICKETS, event(interval(1, 2), tag("ticket3")));

    var intervals = timeline.selectEvents(TICKETS, always().forward())
        .map(TimelineEvent::getInterval)
        .list();

    assertThat(intervals)
        .containsExactly(
            interval(0, 1),
            interval(0, 1),
            interval(1, 2)
        );
  }


}
