package com.fieldcode.tesseract.matrix;

import org.assertj.core.data.Percentage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.fieldcode.tesseract.Location;
import com.fieldcode.tesseract.location.Locations;
import com.fieldcode.tesseract.timeline.TimelineTestSupport;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.within;

@DisplayName("SphericalResolver")
class SphericalResolverTest {

  // Common test locations
  private static final double DEFAULT_SPEED = 50.0; // km/h
  private static final Location PARIS = Locations.location(48.8566, 2.3522);
  private static final Location BERLIN = Locations.location(52.5200, 13.4050);
  private static final Location MUNICH = Locations.location(48.1351, 11.5820);

  @Test
  @DisplayName("getDistanceInMeter() should return correct spherical distance")
  void getDistanceInMeter_ReturnsCorrectSphericalDistance() {
    // Arrange
    var resolver = SphericalResolver.of(DEFAULT_SPEED);

    // Act
    var distanceInMeters = resolver.getDistanceInMeter(BERLIN, PARIS);

    // Assert
    assertThat(distanceInMeters).isCloseTo(876_000L, within(5000L));
  }

  @Test
  @DisplayName("getDistance() should return correct distance object")
  void getDistance_ReturnsCorrectDistanceObject() {
    // Arrange
    var resolver = SphericalResolver.of(DEFAULT_SPEED);

    // Act
    var distance = resolver.getDistance(BERLIN, PARIS);

    // Assert
    assertThat(distance.toKilometers()).isCloseTo(876L, Percentage.withPercentage(0.5));
  }

  @Test
  @DisplayName("getDuration() should calculate duration based on configured speed")
  void getDuration_CalculatesDurationBasedOnSpeed() {
    // Arrange
    var resolver = SphericalResolver.of(DEFAULT_SPEED);

    // Act
    var duration = resolver.getDuration(BERLIN, PARIS);

    // Assert
    assertThat(duration.toHours()).isCloseTo(17L, Percentage.withPercentage(0.5));
  }

  @Test
  @DisplayName("getDurationInSeconds() should return correct duration in seconds")
  void getDurationInSeconds_ReturnsCorrectSeconds() {
    // Arrange
    var resolver = SphericalResolver.of(DEFAULT_SPEED);

    // Act
    var durationInSeconds = resolver.getDurationInSeconds(BERLIN, PARIS);

    // Assert
    assertThat(durationInSeconds).isCloseTo(63_000L, within(1800L));
  }

  @Test
  @DisplayName("getDirectionFeature() should return feature with correct properties")
  void getDirectionFeature_ReturnsFeatureWithCorrectProperties() {
    // Arrange
    var resolver = SphericalResolver.of(DEFAULT_SPEED);

    // Act
    var feature = resolver.getDirectionFeature(BERLIN, PARIS);

    // Assert
    assertThat(feature.getDistance().toKilometers()).isCloseTo(876L, Percentage.withPercentage(0.5));
    assertThat(feature.getDuration().toHours()).isCloseTo(17L, Percentage.withPercentage(0.5));
    assertThat(feature.getOrigin()).isEqualTo(BERLIN);
    assertThat(feature.getDestination()).isEqualTo(PARIS);
  }

  @Test
  @DisplayName("getTrack() should return track with correct start time")
  void getTrack_ReturnsTrackWithCorrectStartTime() {
    // Arrange
    var resolver = SphericalResolver.of(DEFAULT_SPEED);
    var startTime = TimelineTestSupport.moment(0);

    // Act
    var track = resolver.getTrack(BERLIN, PARIS, startTime.getAt());

    // Assert
    assertThat(track.getStart()).isEqualTo(startTime);
    assertThat(track.getEnd()).isEqualTo(startTime.shift(track.getDuration()));
    assertThat(track.getOrigin().getLocation()).isEqualTo(BERLIN);
    assertThat(track.getDestination().getLocation()).isEqualTo(PARIS);
  }

  @Test
  @DisplayName("unsupported operations should throw UnsupportedOperationException")
  void unsupportedOperations_ThrowUnsupportedOperationException() {
    // Arrange
    var resolver = SphericalResolver.of(DEFAULT_SPEED);

    // Act & Assert
    assertThatThrownBy(resolver::getDistancesInMeter)
        .isInstanceOf(UnsupportedOperationException.class)
        .hasMessageContaining("Unsupported operation");

    assertThatThrownBy(resolver::getDurationsInSecond)
        .isInstanceOf(UnsupportedOperationException.class)
        .hasMessageContaining("Unsupported operation");

    assertThatThrownBy(resolver::getLocations)
        .isInstanceOf(UnsupportedOperationException.class)
        .hasMessageContaining("Unsupported operation");
  }

  @Test
  @DisplayName("of() should create resolver with correct speed setting")
  void of_CreatesResolverWithCorrectSpeedSetting() {
    // Arrange
    double fastSpeed = 100.0; // km/h
    var fastResolver = SphericalResolver.of(fastSpeed);

    // Act
    var slowDuration = SphericalResolver.of(DEFAULT_SPEED).getDuration(BERLIN, MUNICH);
    var fastDuration = fastResolver.getDuration(BERLIN, MUNICH);

    // Assert
    assertThat(fastDuration.toMinutes()).isEqualTo(slowDuration.toMinutes() / 2);
  }

}
