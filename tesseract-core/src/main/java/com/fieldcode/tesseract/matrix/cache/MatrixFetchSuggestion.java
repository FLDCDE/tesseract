package com.fieldcode.tesseract.matrix.cache;

import static com.google.common.base.Preconditions.checkState;

enum MatrixFetchSuggestion implements Comparable<MatrixFetchSuggestion> {

  COVERED(0) {
    @Override
    public int selectCost(CandidateDirectionMatrix cost) {
      return 0;
    }
  },
  PARTIAL(1) {
    @Override
    public int selectCost(CandidateDirectionMatrix cost) {
      return cost.getPartialFetchCost();
    }
  },
  FULL(2) {
    @Override
    public int selectCost(CandidateDirectionMatrix cost) {
      return 0;
    }
  };

  MatrixFetchSuggestion(int order) {
    checkState(ordinal() == order, "Ordinal must be equal to order. [ordinal=%d, order=%d]", ordinal(), order);
  }

  public abstract int selectCost(CandidateDirectionMatrix cost);

}
