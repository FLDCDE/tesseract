package com.fieldcode.tesseract.moment;

import java.util.function.Predicate;

import com.fieldcode.tesseract.Moment;

import static com.google.common.base.Preconditions.checkArgument;

public class MomentPredicates {

  private MomentPredicates() {}

  public static Predicate<Moment> between(Moment start, boolean startInclusive, Moment end, boolean endInclusive) {
    checkArgument(start.compareTo(end) < 0, "Start must be before end [start=%s, end=%s]", start, end);
    return moment ->
    {
      var startCmp = start.compareTo(moment);
      var endCmp = end.compareTo(moment);
      return
          ((startInclusive && startCmp <= 0) || startCmp < 0) && ((endInclusive && endCmp >= 0) || endCmp > 0);
    };
  }

}
