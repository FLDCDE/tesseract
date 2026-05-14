package com.fieldcode.tesseract.query;


import org.immutables.value.Value.Immutable;
import org.immutables.value.Value.Parameter;

import com.fieldcode.tesseract.Interval;
import com.fieldcode.tesseract.IntervalDurationQuery;
import com.fieldcode.tesseract.IntervalQueryResult;

@Immutable
public abstract class FailedResultSkeleton implements IntervalQueryResult {

  @Override
  @Parameter
  public abstract IntervalDurationQuery getQuery();

  @Override
  public Interval getInterval() {
    throw new IllegalStateException("Interval not found.");
  }

  @Override
  public boolean isSuccess() {
    return false;
  }

}
