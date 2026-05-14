package com.fieldcode.tesseract.directionmatrix;

import com.fieldcode.tesseract.DirectionMatrix;
import com.fieldcode.tesseract.Location;
import com.fieldcode.tesseract.location.Locations;
import com.fieldcode.tesseract.matrix.DirectionMatrices;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.SortedSet;
import java.util.TreeSet;

class DirectionMatrixTest {

  private DirectionMatrix directionMatrix;

  private SortedSet<Location> locations;

  private final Location location = Locations.location(10, 20);
  private final Location locationOther = Locations.location(50, 60);


  @BeforeEach
  public void setUp() {

    locations = new TreeSet<>();
    locations.add(location);
    locations.add(locationOther);
    long[][] distancesInMeters = {
        {10, 20},
        {30, 40}
    };
    long[][] distancesInSeconds = {
        {40, 30},
        {20, 10}
    };

    directionMatrix = DirectionMatrices.matrix(
        locations,
        distancesInMeters,
        distancesInSeconds
    );
  }

  @Test
  void getLocations_Contains_Success() {
    Assertions.assertThat(directionMatrix.getLocations()).containsExactly(
        Locations.location(10, 20),
        Locations.location(50, 60)
    );
  }

  @Test
  void getDistancesInMeters_Success() {
    Assertions.assertThat(directionMatrix.getDistancesInMeter()).isEqualTo(
        new long[][]{
            {10, 20},
            {30, 40}
        }
    );
  }

  @Test
  void getDistancesInSeconds_Success() {
    Assertions.assertThat(directionMatrix.getDurationsInSeconds()).isEqualTo(
        new long[][]{
            {40, 30},
            {20, 10}
        }
    );
  }

}
