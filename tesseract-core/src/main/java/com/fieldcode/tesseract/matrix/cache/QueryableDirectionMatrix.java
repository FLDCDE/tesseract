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


class QueryableDirectionMatrix {

  private final String hash;
  private final DirectionMatrix matrix;

  private QueryableDirectionMatrix(DirectionMatrix matrix) {
    checkNotNull(matrix, "DirectionMatrix must be non null");
    checkArgument(!matrix.getLocations().isEmpty(), "DirectionMatrix must be non empty");

    this.matrix = matrix;
    this.hash = Hash.hash(matrix.getLocations());
  }

  static QueryableDirectionMatrix of(DirectionMatrix matrix) {
    return new QueryableDirectionMatrix(matrix);
  }

  String getHash() {
    return hash;
  }

  SortedSet<Location> getLocations() {
    return matrix.getLocations();
  }

  boolean contains(Location location) {
    return matrix.getLocations().contains(location);
  }

  SortedSet<Location> getMissingLocations(SortedSet<Location> expected) {
    return expected.stream()
        .filter(not(this::contains))
        .collect(toCollection(TreeSet::new));
  }

  @Override
  public String toString() {
    return matrix.toString();
  }

  Stream<DirectionMatrixDimensions> getExtenderMatrices(SortedSet<Location> expected) {
    return MatrixExpandCalculator.calculate(getLocations(), expected);
  }

  CandidateDirectionMatrix candidate(MatrixRequest request) {
    return ImmutableCandidateDirectionMatrix.of(request, this);
  }

  DirectionMatrix getMatrix() {
    return matrix;
  }

}
