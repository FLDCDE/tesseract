package com.fieldcode.tesseract.matrix;

import com.fieldcode.tesseract.DirectionMatrix;
import com.fieldcode.tesseract.Location;
import org.immutables.value.Value.Check;
import org.immutables.value.Value.Immutable;
import org.immutables.value.Value.Parameter;

import java.util.SortedSet;

import static com.google.common.base.Preconditions.checkArgument;

@Immutable
public abstract class DirectionMatrixSkeleton extends AbstractDirectionMatrix implements DirectionMatrix {

  @Override
  public SortedSet<Location> getOrigins() {
    return getLocations();
  }

  @Override
  public SortedSet<Location> getDestinations() {
    return getLocations();
  }

  @Override
  @Parameter
  public abstract long[][] getDistancesInMeter();

  @Override
  @Parameter
  public abstract long[][] getDurationsInSeconds();

  @Override
  @Parameter
  public abstract SortedSet<Location> getLocations();

  @Check
  protected void check() {

    var n = getLocations().size();

    checkArgument(
        getDistancesInMeter().length == n,
        "Number of rows in distances array must equal to size of locations. [locations=%d, rows=%d]", n, getDistancesInMeter().length
    );

    checkArgument(
        getDurationsInSeconds().length == n,
        "Number of rows in durations array must equal to size of locations. [locations=%d, rows=%d]", n, getDurationsInSeconds().length
    );

    for (int i = 0; i < n; i++) {
      var row = getDistancesInMeter()[i];
      checkArgument(
          row.length == n,
          "Number of columns in all distances array rows must equal to size of locations. [locations=%d, rowIndex=%d, columns=%d]", n, i, row.length
      );
    }
    for (int i = 0; i < n; i++) {
      var row = getDurationsInSeconds()[i];
      checkArgument(
          row.length == n,
          "Number of columns in all durations array rows must equal to size of locations. [locations=%d, rowIndex=%d, columns=%d]", n, i, row.length
      );
    }

  }

}
