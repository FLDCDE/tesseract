package com.fieldcode.tesseract.matrix;

import com.fieldcode.tesseract.ClusteredDirectionMatrixResolver;
import com.fieldcode.tesseract.DirectionFeature;
import com.fieldcode.tesseract.DirectionMatrixResolver;
import com.fieldcode.tesseract.Distance;
import com.fieldcode.tesseract.Location;
import com.fieldcode.tesseract.Track;
import com.fieldcode.tesseract.location.LocationClusterUtils;
import com.google.common.collect.Multimap;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiPredicate;

class ClusteredResolver implements ClusteredDirectionMatrixResolver {

  private final DirectionMatrixResolver delegate;
  private final BiPredicate<Location, Location> neighbors;
  private final Map<Location, Integer> ids;

  private ClusteredResolver(DirectionMatrixResolver delegate, Multimap<Integer, Location> clusters) {
    this.delegate = delegate;
    this.ids = LocationClusterUtils.invert(clusters);
    this.neighbors = LocationClusterUtils.neighbors(clusters);
  }

  static ClusteredResolver of(DirectionMatrixResolver delegate, Multimap<Integer, Location> clusters) {
    return new ClusteredResolver(delegate, clusters);
  }

  @Override
  public long getDistanceInMeter(Location origin, Location destination) {return delegate.getDistanceInMeter(origin, destination);}

  @Override
  public Distance getDistance(Location origin, Location destination) {return delegate.getDistance(origin, destination);}

  @Override
  public long[][] getDistancesInMeter() {return delegate.getDistancesInMeter();}

  @Override
  public long getDurationInSeconds(Location origin, Location destination) {return delegate.getDurationInSeconds(origin, destination);}

  @Override
  public Duration getDuration(Location origin, Location destination) {return delegate.getDuration(origin, destination);}

  @Override
  public long[][] getDurationsInSecond() {return delegate.getDurationsInSecond();}

  @Override
  public Location[] getLocations() {return delegate.getLocations();}

  @Override
  public Track getTrack(Location origin, Location destination, OffsetDateTime start) {return delegate.getTrack(origin, destination, start);}

  @Override
  public DirectionFeature getDirectionFeature(Location origin, Location destination) {return delegate.getDirectionFeature(origin, destination);}

  @Override
  public DirectionFeature apply(Location location, Location location2) {return delegate.apply(location, location2);}

  @Override
  public boolean neighbors(Location a, Location b) {
    return neighbors.test(a, b);
  }

  @Override
  public Optional<Integer> getClusterId(Location location) {
    return Optional.ofNullable(ids.get(location));
  }

}
