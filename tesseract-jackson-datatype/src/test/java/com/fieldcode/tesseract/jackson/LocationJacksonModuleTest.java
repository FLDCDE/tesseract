package com.fieldcode.tesseract.jackson;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fieldcode.tesseract.Location;
import com.fieldcode.tesseract.jackson.test_utils.Jsons;
import com.fieldcode.tesseract.location.Locations;

import java.util.SortedSet;
import java.util.TreeSet;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("LocationJacksonModule")
class LocationJacksonModuleTest {

  @Test
  @DisplayName("should round-trip a single location through serialization and deserialization")
  void location_serialize_Deserialize_Success() {

    // Arrange: a single location
    var location = Locations.location(1, 2);

    // Act: serialize it to JSON and parse it back
    var json = Jsons.stringify(location, true);

    var parsed = Jsons.parse(json, Location.class);

    // Assert: the round-tripped location equals the original
    assertThat(parsed)
        .isEqualTo(Locations.location(1, 2));
  }

  @Test
  @DisplayName("should round-trip a sorted set of locations, preserving their order")
  void locations_sortedSet_serialize_Deserialize_Success() {

    // Arrange: a sorted set containing two locations
    var locationOne = Locations.location(1, 2);
    var locationTwo = Locations.location(2, 3);

    var set = new TreeSet<>() {{
      add(locationOne);
      add(locationTwo);
    }};

    // Act: serialize the set to JSON and parse it back
    var json = Jsons.stringify(set, true);

    var parsed = Jsons.parse(json, new TypeReference<SortedSet<Location>>() {});

    // Assert: the round-tripped set contains the same locations in order
    assertThat(parsed).containsExactly(
        Locations.location(1, 2),
        Locations.location(2, 3)
    );
  }
}
