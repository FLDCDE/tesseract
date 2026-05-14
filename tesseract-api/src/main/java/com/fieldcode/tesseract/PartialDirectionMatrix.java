package com.fieldcode.tesseract;

import java.util.stream.Stream;

public interface PartialDirectionMatrix extends DirectionMatrixDimensions {


  long[][] getDistancesInMeter();

  long[][] getDurationsInSeconds();


  long getDistanceInMeters(Direction direction);

  long getDistanceInMeters(Location origin, Location destination);

  long getDurationInSeconds(Direction direction);

  long getDurationInSeconds(Location origin, Location destination);

  Stream<DirectionFeature> getAllFeatures();
}
