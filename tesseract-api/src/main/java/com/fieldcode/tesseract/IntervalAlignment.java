package com.fieldcode.tesseract;

public enum IntervalAlignment {

  ALIGN_BEFORE {
    @Override
    public <T> T accept(IntervalAlignmentVisitor<T> visitor) {
      return visitor.alignBefore();
    }
  },
  ALIGN_STARTS {
    @Override
    public <T> T accept(IntervalAlignmentVisitor<T> visitor) {
      return visitor.alignStarts();
    }
  },
  ALIGN_ENDS {
    @Override
    public <T> T accept(IntervalAlignmentVisitor<T> visitor) {
      return visitor.alignEnds();
    }
  },
  ALIGN_AFTER {
    @Override
    public <T> T accept(IntervalAlignmentVisitor<T> visitor) {
      return visitor.alignAfter();
    }
  };

  public abstract <T> T accept(IntervalAlignmentVisitor<T> visitor);

  public interface IntervalAlignmentVisitor<T> {

    T alignBefore();

    T alignStarts();

    T alignEnds();

    T alignAfter();

  }

}
