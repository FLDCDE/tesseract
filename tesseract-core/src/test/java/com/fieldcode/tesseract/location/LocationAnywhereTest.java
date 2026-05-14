package com.fieldcode.tesseract.location;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.fieldcode.tesseract.Location;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class LocationAnywhereTest {

  private Location location;


  @BeforeEach
  public void setUp() {
    location = Locations.anywhere();
  }

  @Test
  void location_getCode_Success() {
    assertThat(location.getCode()).isEqualTo("anywhere");
  }


  @Test
  void location_getLatitude_Success() {
    assertThatThrownBy(() -> location.getLatitude()).isInstanceOf(UnsupportedOperationException.class);
  }

  @Test
  void location_getLongitude_Success() {
    assertThatThrownBy(() -> location.getLongitude()).isInstanceOf(UnsupportedOperationException.class);
  }

  @Test
  void location_isAnywhere_False_Success() {
    assertThat(location.isAnywhere()).isEqualTo(true);
  }

  @Test
  void location_asLonLanList_Success() {
    assertThatThrownBy(() -> location.asLonLatList()).isInstanceOf(UnsupportedOperationException.class);
  }

  @Test
  void location_toString_Success() {
    assertThat(location.toString()).isEqualTo("anywhere");
  }

  @Test
  void location_compareTo_Success() {
    var locationOther = Locations.location(100, 100);
    assertThat(location.compareTo(locationOther)).isEqualTo(0);
  }

}
