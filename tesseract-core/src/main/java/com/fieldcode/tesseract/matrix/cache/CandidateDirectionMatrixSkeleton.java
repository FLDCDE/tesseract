package com.fieldcode.tesseract.matrix.cache;

import org.immutables.value.Value.Derived;
import org.immutables.value.Value.Immutable;
import org.immutables.value.Value.Parameter;

import com.fieldcode.tesseract.DirectionMatrixDimensions;
import com.fieldcode.tesseract.Location;

import java.util.Comparator;
import java.util.SortedSet;
import java.util.stream.Stream;
import static com.fieldcode.tesseract.matrix.cache.MatrixFetchSuggestion.COVERED;
import static com.fieldcode.tesseract.matrix.cache.MatrixFetchSuggestion.FULL;
import static com.fieldcode.tesseract.matrix.cache.MatrixFetchSuggestion.PARTIAL;


@Immutable
abstract class CandidateDirectionMatrixSkeleton implements CandidateDirectionMatrix {

  private static final Comparator<CandidateDirectionMatrix> CANDIDATE_ORDER = Comparator
      .comparing(CandidateDirectionMatrix::getSuggestion)
      .thenComparingInt(CandidateDirectionMatrix::getCost);

  @Override
  @Parameter
  public abstract MatrixRequest getRequest();

  @Override
  @Parameter
  public abstract QueryableDirectionMatrix getStoredMatrix();

  @Override
  @Derived
  public int getCost() {
    return getSuggestion().selectCost(this);
  }

  @Override
  public SortedSet<Location> getRequestLocations() {
    return getRequest().getLocations();
  }

  @Override
  @Derived
  public String getRequestHash() {
    return getRequest().getHash();
  }

  @Override
  public String getStoredMatrixHash() {
    return getStoredMatrix().getHash();
  }

  @Override
  public Stream<DirectionMatrixDimensions> getExtenderMatrices() {
    return getStoredMatrix().getExtenderMatrices(getRequestLocations());
  }

  @Override
  public int getFullFetchCost() {
    var requestLocationSize = getRequestLocations().size();
    return requestLocationSize * requestLocationSize;
  }

  @Override
  public int getPartialFetchCost() {
    var missing = getStoredMatrix().getMissingLocations(getRequestLocations()).size();
    var original = getStoredMatrix().getLocations().size();
    return missing * (2 * original + missing);
  }

  @Override
  @Derived
  public MatrixFetchSuggestion getSuggestion() {
    var full = getFullFetchCost();
    var part = getPartialFetchCost();

    if (part == 0) {
      return COVERED;
    }

    return part <= full
        ? PARTIAL
        : FULL;
  }

  @Override
  public int compareTo(CandidateDirectionMatrix o) {
    return CANDIDATE_ORDER.compare(this, o);
  }

}
