package com.fieldcode.tesseract.matrix;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.fieldcode.tesseract.distance.Distances;
import com.fieldcode.tesseract.location.Locations;

import java.time.Duration;
import static org.assertj.core.api.Assertions.assertThat;

class PythagoreanResolverTest {

  @Test
  @DisplayName("Resolver should return the correct distance and duration for two locations")
  void resolver_Simple_Success() {

    var location1 = Locations.location(3, 0);
    var location2 = Locations.location(0, 4);

    var resolver = PythagoreanResolver.of(Distances.ofKilometers(1), Duration.ofHours(1), Distances.ofKilometers(10));

    var feature = resolver.apply(location1, location2);
    var neighbors = resolver.neighbors(location2, location1);

    assertThat(feature.getDistance()).isEqualTo(Distances.ofKilometers(5));
    assertThat(feature.getDuration()).isEqualTo(Duration.ofHours(5));
    assertThat(neighbors).isTrue();

  }

}
