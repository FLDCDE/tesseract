package com.fieldcode.tesseract.matrix.cache;

import com.fieldcode.tesseract.DirectionMatrix;
import com.fieldcode.tesseract.DirectionMatrixDimensions;
import com.fieldcode.tesseract.Location;

import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Stream;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.function.Predicate.not;
import static java.util.stream.Collectors.toCollection;


public class QueryableDirectionMatrix {

  private final String hash;
  private final DirectionMatrix matrix;

  private QueryableDirectionMatrix(DirectionMatrix matrix) {
    checkNotNull(matrix, "DirectionMatrix must be non null");
    checkArgument(!matrix.getLocations().isEmpty(), "DirectionMatrix must be non empty");

    this.matrix = matrix;
    this.hash = Hash.hash(matrix.getLocations());
  }

  public static QueryableDirectionMatrix of(DirectionMatrix matrix) {
    return new QueryableDirectionMatrix(matrix);
  }

  public String getHash() {
    return hash;
  }

  public SortedSet<Location> getLocations() {
    return matrix.getLocations();
  }

  public long[][] getDistancesInMeter() {
    return matrix.getDistancesInMeter();
  }

  public long[][] getDurationsInSeconds() {
    return matrix.getDurationsInSeconds();
  }

  public SortedSet<Location> getOrigins() {
    return matrix.getLocations();
  }

  public SortedSet<Location> getDestinations() {
    return matrix.getLocations();
  }

  public boolean contains(Location location) {
    return matrix.getLocations().contains(location);
  }

  public SortedSet<Location> getMissingLocations(SortedSet<Location> expected) {
    return expected.stream()
        .filter(not(this::contains))
        .collect(toCollection(TreeSet::new));
  }

  @Override
  public String toString() {
    return matrix.toString();
  }

  public Stream<DirectionMatrixDimensions> getExtenderMatrices(SortedSet<Location> expected) {
    return MatrixExpandCalculator.calculate(getLocations(), expected);
  }

  public CandidateDirectionMatrix candidate(MatrixRequest request) {
    return ImmutableCandidateDirectionMatrix.of(request, this);
  }

  public DirectionMatrix getMatrix() {
    return matrix;
  }

}
