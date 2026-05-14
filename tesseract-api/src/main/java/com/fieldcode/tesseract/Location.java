package com.fieldcode.tesseract;

import java.util.List;

public interface Location extends Comparable<Location> {

  String getCode();

  double getLatitude();

  double getLongitude();

  boolean isAnywhere();

  List<Double> asLonLatList();

}