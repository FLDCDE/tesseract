package com.fieldcode.tesseract.matrix.cache;

import com.fieldcode.tesseract.DirectionMatrixDimensions;
import com.fieldcode.tesseract.Location;

import java.util.SortedSet;
import java.util.stream.Stream;

interface CandidateDirectionMatrix extends Comparable<CandidateDirectionMatrix> {

  MatrixRequest getRequest();

  QueryableDirectionMatrix getStoredMatrix();

  int getCost();

  SortedSet<Location> getRequestLocations();

  String getRequestHash();

  String getStoredMatrixHash();

  Stream<DirectionMatrixDimensions> getExtenderMatrices();

  int getFullFetchCost();

  int getPartialFetchCost();

  MatrixFetchSuggestion getSuggestion();

}
