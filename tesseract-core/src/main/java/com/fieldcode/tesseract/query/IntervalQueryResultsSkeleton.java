package com.fieldcode.tesseract.query;

import java.util.List;
import java.util.stream.Stream;

import org.immutables.value.Value.Derived;
import org.immutables.value.Value.Immutable;
import org.immutables.value.Value.Parameter;

import com.fieldcode.tesseract.Interval;
import com.fieldcode.tesseract.IntervalQueryResult;
import com.fieldcode.tesseract.IntervalQueryResults;

@Immutable
abstract class IntervalQueryResultsSkeleton implements IntervalQueryResults {

  @Override
  @Parameter
  public abstract List<IntervalQueryResult> getResults();

  @Override
  @Derived
  public boolean isSuccess() {
    for (IntervalQueryResult intervalQueryResult : getResults()) {
      if (!intervalQueryResult.isSuccess()) {
        return false;
      }
    }
    return true;
  }

  @Override
  public boolean isFailed() {
    return !isSuccess();
  }

  @Override
  public Stream<Interval> getIntervals() {
    return isFailed()
        ? Stream.of()
        : getResults().stream().map(IntervalQueryResult::getInterval);
  }

  @Override
  public Interval getInterval(int index) {
    return getResults().get(index).getInterval();
  }

}
