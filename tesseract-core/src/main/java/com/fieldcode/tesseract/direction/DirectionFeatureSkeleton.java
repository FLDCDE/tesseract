package com.fieldcode.tesseract.direction;

import com.fieldcode.tesseract.DirectionFeature;
import com.fieldcode.tesseract.Distance;
import com.fieldcode.tesseract.Location;
import org.immutables.value.Value.Immutable;
import org.immutables.value.Value.Parameter;

import java.time.Duration;


@Immutable
abstract class DirectionFeatureSkeleton implements DirectionFeature {

  @Parameter
  public abstract Duration getDuration();

  @Parameter
  public abstract Distance getDistance();

  @Override
  @Parameter
  public abstract Location getOrigin();

  @Override
  @Parameter
  public abstract Location getDestination();

}
