package com.fieldcode.tesseract.location;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.fieldcode.tesseract.Location;

public class LocationTest {


  private Location location;


  @BeforeEach
  public void setUp() {
    location = Locations.location(50, 100);
  }

  @Test
  void location_getCode_Success() {
    Assertions.assertThat(location.getCode()).isEqualTo("50.0,100.0");
  }


  @Test
  void location_getLatitude_Success() {
    Assertions.assertThat(location.getLatitude()).isEqualTo(50.0);
  }

  @Test
  void location_getLongitude_Success() {
    Assertions.assertThat(location.getLongitude()).isEqualTo(100);
  }

  @Test
  void location_isAnywhere_False_Success() {
    Assertions.assertThat(location.isAnywhere()).isFalse();
  }

  @Test
  void location_asLonLanList_Success() {
    Assertions.assertThat(location.asLonLatList()).isEqualTo(List.of(100.0, 50.0));
  }

}
