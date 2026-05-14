package com.fieldcode.tesseract.distance;

import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.fieldcode.tesseract.Distance;
import com.fieldcode.tesseract.DistanceUnit;

import static org.assertj.core.api.Assertions.assertThat;

public class DistanceTest {

  private Distance distance;

  @BeforeEach
  public void setUp() {
    distance = Distances.ofMeters(1);
  }

  @Test
  void Distance_zeroDistance_Success() {
    assertThat(Distances.zero()).isEqualTo(Distances.distance(0, DistanceUnit.CENTIMETER));
    assertThat(Distances.zero()).isEqualTo(Distances.distance(0, DistanceUnit.METER));
    assertThat(Distances.zero()).isEqualTo(Distances.distance(0, DistanceUnit.KILOMETER));
    assertThat(Distances.zero()).isEqualTo(Distances.distance(0, DistanceUnit.MILE));
  }

  @Test
  void Distance_oneMeter_Success() {
    assertThat(Distances.oneMeter()).isEqualTo(Distances.distance(100, DistanceUnit.CENTIMETER));
    assertThat(Distances.oneMeter()).isEqualTo(Distances.distance(1, DistanceUnit.METER));
  }

  @Test
  void Distance_oneKiloMeter_Success() {
    assertThat(Distances.oneKilometer()).isEqualTo(Distances.distance(100000, DistanceUnit.CENTIMETER));
    assertThat(Distances.oneKilometer()).isEqualTo(Distances.distance(1000, DistanceUnit.METER));
  }

  @Test
  void Distance_ofKilometers_Success() {
    assertThat(Distances.ofKilometers(10)).isEqualTo(Distances.distance(1000000, DistanceUnit.CENTIMETER));
    assertThat(Distances.ofKilometers(10)).isEqualTo(Distances.distance(10000, DistanceUnit.METER));
  }

  @Test
  void Distance_ofMiles_Success() {
    assertThat(Distances.ofMile(10)).isEqualTo(Distances.distance((long) (10.0 * 1609.34), DistanceUnit.METER));
    assertThat(Distances.ofMile(10)).isEqualTo(Distances.distance((long) (10.0 * 1609.34 * 100), DistanceUnit.CENTIMETER));
  }

  @Test
  void Distance_toCentimeters_Success() {
    assertThat(distance.toCentimeters()).isEqualTo(100);
  }

  @Test
  void Distance_toKilometers_Success() {
    assertThat(distance.toKilometers()).isEqualTo(0);
  }

  @Test
  void Distance_toMeters_Success() {
    assertThat(distance.toMeters()).isEqualTo(1);
  }

  @Test
  void compare() {

    var distances = Stream.of(
        Distances.zero(),
        Distances.ofMeters(3),
        Distances.ofMeters(2),
        Distances.ofMeters(1)
    ).sorted();

    assertThat(distances).containsExactly(
        Distances.zero(),
        Distances.ofMeters(1),
        Distances.ofMeters(2),
        Distances.ofMeters(3)
    );

  }

}
