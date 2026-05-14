package com.fieldcode.tesseract.matrix.cache;

import com.fieldcode.tesseract.DirectionMatrixDimensions;
import com.fieldcode.tesseract.Location;

import java.util.SortedSet;

interface MatrixRequest extends DirectionMatrixDimensions {

  String getHash();

  SortedSet<Location> getLocations();

}
