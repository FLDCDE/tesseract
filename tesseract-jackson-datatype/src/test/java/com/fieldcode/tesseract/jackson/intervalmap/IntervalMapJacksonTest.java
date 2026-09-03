package com.fieldcode.tesseract.jackson.intervalmap;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fieldcode.tesseract.IntervalMap;
import com.fieldcode.tesseract.Location;
import com.fieldcode.tesseract.interval.IntervalMaps;
import com.fieldcode.tesseract.jackson.intervalmap.model.Event;
import com.fieldcode.tesseract.jackson.intervalmap.model.ImmutableDriveEvent;
import com.fieldcode.tesseract.jackson.intervalmap.model.ImmutableIdleEvent;
import com.fieldcode.tesseract.jackson.intervalmap.model.ImmutableTaskEvent;
import com.fieldcode.tesseract.jackson.test_utils.Jsons;
import com.fieldcode.tesseract.jackson.test_utils.TimeTestSupport;

import static com.fieldcode.tesseract.location.Locations.location;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("IntervalMap Jackson serialization")
class IntervalMapJacksonTest extends TimeTestSupport {

  private Event idle() {
    return ImmutableIdleEvent.of();
  }

  private Event drive(Location origin, Location destination) {
    return ImmutableDriveEvent.of(origin, destination);
  }

  private Event task(String code) {
    return ImmutableTaskEvent.of(code);
  }

  @Test
  @DisplayName("should round-trip a disjoint interval map of drive, task and idle events")
  void clone_Success() {
    // Arrange: a disjoint interval map with two drive events and one task event over three intervals
    var map = IntervalMaps.disjoint(idle());

    map.put(interval(8, 9), drive(location(0, 0), location(1, 0)));
    map.put(interval(9, 10), task("task"));
    map.put(interval(10, 11), drive(location(1, 0), location(2, 0)));

    // Act: serialize and deserialize it back
    var cloned = Jsons.clone(map, new TypeReference<IntervalMap<Event>>() {});

    // Assert: the round-tripped map equals the original
    assertThat(cloned)
        .isEqualTo(map);
  }

}
