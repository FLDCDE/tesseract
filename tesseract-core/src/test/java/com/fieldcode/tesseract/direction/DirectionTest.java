package com.fieldcode.tesseract.direction;

import com.fieldcode.tesseract.Direction;
import com.fieldcode.tesseract.Location;
import com.fieldcode.tesseract.location.Locations;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class DirectionTest {

  private final Location origin = Locations.location(10, 20);
  private final Location destination = Locations.location(50, 60);

  private Direction direction;

  @BeforeEach
  public void setUp() {
    direction = Directions.direction(origin, destination);
  }

  @Test
  void direction_getOrigin_Success() {
    assertThat(direction.getOrigin()).isEqualTo(
        Locations.location(10, 20)
    );
  }

  @Test
  void direction_getDestination_Success() {
    assertThat(direction.getDestination()).isEqualTo(
        Locations.location(50, 60)
    );
  }
}
