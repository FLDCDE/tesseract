package com.fieldcode.tesseract.matrix.cache;

import com.fieldcode.tesseract.*;
import com.fieldcode.tesseract.direction.Directions;
import com.fieldcode.tesseract.matrix.DirectionMatrices;

import java.util.Collection;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.stream.Collectors.toList;


public class MatrixMerger {

  private final Collection<PartialDirectionMatrix> matrices;
  private final MultiKeyLongMatrix<Location> distances;
  private final MultiKeyLongMatrix<Location> durations;
  private final MultiKeyLongMatrix<Location> coverage;
  private final SortedSet<Location> origins;
  private final SortedSet<Location> destinations;

  private MatrixMerger(Collection<PartialDirectionMatrix> matrices) {
    this.matrices = matrices;
    this.origins = getAllOrigins(matrices);
    this.destinations = getAllDestinations(matrices);
    this.distances = MultiKeyLongMatrix.of(origins, destinations);
    this.durations = MultiKeyLongMatrix.of(origins, destinations);
    this.coverage = MultiKeyLongMatrix.of(origins, destinations);
  }

  private static SortedSet<Location> getAllOrigins(Collection<PartialDirectionMatrix> matrices) {
    return matrices
        .stream()
        .map(PartialDirectionMatrix::getOrigins)
        .flatMap(Collection::stream)
        .collect(TreeSet::new, TreeSet::add, TreeSet::addAll);
  }

  private static SortedSet<Location> getAllDestinations(Collection<PartialDirectionMatrix> matrices) {
    return matrices
        .stream()
        .map(PartialDirectionMatrix::getDestinations)
        .flatMap(Collection::stream)
        .collect(TreeSet::new, TreeSet::add, TreeSet::addAll);
  }

  public static PartialDirectionMatrix merge(Collection<PartialDirectionMatrix> matrices) {
    return new MatrixMerger(matrices).merge();
  }

  public static PartialDirectionMatrix merge(DirectionMatrix original, Stream<PartialDirectionMatrix> fetched) {
    return merge(concat(fetched, original));
  }

  private static <T> List<T> concat(Stream<T> stream, T addition) {
    return Stream.concat(stream, Stream.of(addition))
        .collect(toList());
  }


  private PartialDirectionMatrix merge() {
    matrices.forEach(this::copyMatrix);
    checkCoverage();
    return DirectionMatrices.partial(origins, destinations, distances.getMatrix(), durations.getMatrix());
  }

  private void copyMatrix(PartialDirectionMatrix matrix) {
    matrix.getAllFeatures().forEach(this::copyFeature);
  }

  private void copyFeature(DirectionFeature feature) {
    var origin = feature.getOrigin();
    var destination = feature.getDestination();
    var duration = feature.getDuration().toSeconds();
    var distance = feature.getDistance().toMeters();

    durations.set(origin, destination, duration);
    distances.set(origin, destination, distance);
    coverage.set(origin, destination, 1);
  }

  private void checkCoverage() {
    var notCoveredDirections = Directions.getAllDirections(origins, destinations)
        .filter(this::notCovered)
        .collect(Collectors.toList());
    checkArgument(notCoveredDirections.isEmpty(), "Not all cells are covered. [notCoveredDirections=%s]", notCoveredDirections);
  }

  private boolean notCovered(Direction direction) {
    return coverage.get(direction.getOrigin(), direction.getDestination()) == 0;
  }

}
