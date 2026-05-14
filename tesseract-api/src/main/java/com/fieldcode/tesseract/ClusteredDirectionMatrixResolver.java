package com.fieldcode.tesseract;


import java.util.Optional;

public interface ClusteredDirectionMatrixResolver extends DirectionMatrixResolver {

  /**
   * Returns true if the two locations are neighbors (are in the same cluster).
   *
   * @param a first location
   * @param b second location
   * @return true if the two locations are neighbors
   */
  boolean neighbors(Location a, Location b);

  /**
   * Returns the cluster id of the location if it exists, otherwise returns empty.
   *
   * @param location the location
   * @return optional cluster id
   */
  Optional<Integer> getClusterId(Location location);

}
