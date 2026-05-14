package com.fieldcode.tesseract;

import java.util.SortedSet;

public interface DirectionMatrixDimensions {

  SortedSet<Location> getOrigins();

  SortedSet<Location> getDestinations();

}
