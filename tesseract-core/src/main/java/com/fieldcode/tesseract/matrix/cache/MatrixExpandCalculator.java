package com.fieldcode.tesseract.matrix.cache;

import com.fieldcode.tesseract.DirectionMatrixDimensions;
import com.fieldcode.tesseract.Location;
import com.google.common.collect.Sets;

import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Stream;

import static com.fieldcode.tesseract.matrix.DirectionMatrices.dimensions;
import static com.google.common.base.Preconditions.checkArgument;

public class MatrixExpandCalculator {


  private MatrixExpandCalculator() {
  }

  public static Stream<DirectionMatrixDimensions> calculate(SortedSet<Location> originalLocations, SortedSet<Location> locations) {

    var missingLocations = new TreeSet<>(Sets.difference(locations, originalLocations));

    checkArgument(!missingLocations.isEmpty(), "All locations are covered [originalLocations=%s, location=%s]", originalLocations, locations);
    return Stream.of(
        dimensions(missingLocations, originalLocations),
        dimensions(originalLocations, missingLocations),
        dimensions(missingLocations, missingLocations)
    );

  }

}
