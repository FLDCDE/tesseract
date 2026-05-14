package com.fieldcode.tesseract.jackson;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fieldcode.tesseract.Location;
import com.fieldcode.tesseract.jackson.test_utils.Jsons;
import com.fieldcode.tesseract.location.Locations;

import java.util.SortedSet;
import java.util.TreeSet;
import static org.assertj.core.api.Assertions.assertThat;

class LocationJacksonModuleTest {

  @Test
  void location_serialize_Deserialize_Success() {

    var location = Locations.location(1, 2);

    var json = Jsons.stringify(location, true);

    var parsed = Jsons.parse(json, Location.class);

    assertThat(parsed).isEqualTo(Locations.location(1, 2));
  }

  @Test
  void locations_sortedSet_serialize_Deserialize_Success() {

    var locationOne = Locations.location(1, 2);
    var locationTwo = Locations.location(2, 3);

    var set = new TreeSet<>() {{
      add(locationOne);
      add(locationTwo);
    }};

    var json = Jsons.stringify(set, true);

    var parsed = Jsons.parse(json, new TypeReference<SortedSet<Location>>() {});

    assertThat(parsed).containsExactly(
        Locations.location(1, 2),
        Locations.location(2, 3)
    );
  }
}
