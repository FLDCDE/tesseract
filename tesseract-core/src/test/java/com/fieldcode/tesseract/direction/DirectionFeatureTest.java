package com.fieldcode.tesseract.direction;

import java.time.Duration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.fieldcode.tesseract.DirectionFeature;
import com.fieldcode.tesseract.Distance;
import com.fieldcode.tesseract.Location;
import com.fieldcode.tesseract.distance.Distances;
import com.fieldcode.tesseract.location.Locations;

import static org.assertj.core.api.Assertions.assertThat;


public class DirectionFeatureTest {

  private final Distance distance = Distances.oneKilometer();
  private final Location origin = Locations.location(10, 20);
  private final Location destination = Locations.location(50, 60);

  private DirectionFeature directionFeature;


  @BeforeEach
  public void setUp() {
    directionFeature = Directions.feature(origin, destination, distance, Duration.ofSeconds(60));
  }


  @Test
  void directionFeature_getOrigin_Success() {
    assertThat(directionFeature.getOrigin()).isEqualTo(
        Locations.location(10.0, 20.0)
    );
  }

  @Test
  void directionFeature_getDestination_Success() {
    assertThat(directionFeature.getDestination()).isEqualTo(
        Locations.location(50.0, 60.0));
  }

  @Test
  void directionFeature_getDistance_Success() {
    assertThat(directionFeature.getDistance()).isEqualTo(
        Distances.oneKilometer()
    );
  }


  @Test
  void directionFeature_getDuration_Success() {
    assertThat(directionFeature.getDuration()).isEqualTo(
        Duration.ofSeconds(60)
    );
  }

}
