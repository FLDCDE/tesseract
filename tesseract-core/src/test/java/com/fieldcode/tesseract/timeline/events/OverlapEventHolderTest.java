package com.fieldcode.tesseract.timeline.events;

import org.junit.jupiter.api.Test;

import com.fieldcode.tesseract.interval.Intervals;

import static com.fieldcode.tesseract.timeline.TimelineTestSupport.interval;
import static com.fieldcode.tesseract.timeline.TimelineTestSupport.tag;
import static com.fieldcode.tesseract.timeline.events.EventHolderTestSupport.event;
import static org.assertj.core.api.Assertions.assertThat;

class OverlapEventHolderTest {

  @Test
  void put_NonOverlapping_Success() {

    var holder = OverlapEventHolder.of();

    holder.put(interval(0, 10), tag("interval1"));
    holder.put(interval(20, 30), tag("interval2"));

    var events = holder.events(Intervals.always().forward()).list();

    assertThat(events)
        .containsExactly(
            event(interval(0, 10), tag("interval1")),
            event(interval(20, 30), tag("interval2"))
        );

    assertThat(holder.events(interval(5, 25).forward()))
        .toIterable()
        .containsExactly(
            event(interval(5, 10), tag("interval1")),
            event(interval(20, 25), tag("interval2"))
        );

  }

  @Test
  void put_Overlapping_Success() {

    var holder = OverlapEventHolder.of();

    holder.put(interval(0, 15), tag("interval1"));
    holder.put(interval(10, 30), tag("interval2"));

    var events = holder.events(Intervals.always().forward()).list();

    assertThat(events)
        .containsExactly(
            event(interval(0, 15), tag("interval1")),
            event(interval(10, 30), tag("interval2"))
        );

    assertThat(holder.events(interval(5, 25).forward()))
        .toIterable()
        .containsExactly(
            event(interval(5, 15), tag("interval1")),
            event(interval(10, 25), tag("interval2"))
        );

  }

  @Test
  void remove_NonOverlapping_Success() {

    var holder = OverlapEventHolder.of();

    holder.put(interval(0, 10), tag("interval1"));
    holder.put(interval(20, 30), tag("interval2"));
    holder.remove(interval(5, 25));

    var events = holder.events(Intervals.always().forward()).list();

    assertThat(events)
        .containsExactly(
            event(interval(0, 5), tag("interval1")),
            event(interval(25, 30), tag("interval2"))
        );

  }

  @Test
  void remove_Overlapping_Success() {

    var holder = OverlapEventHolder.of();

    holder.put(interval(0, 20), tag("interval1"));
    holder.put(interval(0, 30), tag("interval2"));
    holder.remove(interval(5, 10));

    var events = holder.events(Intervals.always().forward()).list();

    assertThat(events)
        .containsExactly(
            event(interval(0, 5), tag("interval1")),
            event(interval(0, 5), tag("interval2")),
            event(interval(10, 20), tag("interval1")),
            event(interval(10, 30), tag("interval2"))
        );

  }


}
