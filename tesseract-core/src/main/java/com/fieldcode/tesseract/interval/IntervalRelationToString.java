package com.fieldcode.tesseract.interval;

import java.util.Map;

import com.fieldcode.tesseract.IntervalsRelation;
import com.google.common.collect.ImmutableSortedMap;

import static com.fieldcode.tesseract.interval.IntervalsRelationConstants.INTERVAL_CONTAINS;
import static com.fieldcode.tesseract.interval.IntervalsRelationConstants.INTERVAL_DURING;
import static com.fieldcode.tesseract.interval.IntervalsRelationConstants.INTERVAL_EQUALS_TO;
import static com.fieldcode.tesseract.interval.IntervalsRelationConstants.INTERVAL_FINISHES;
import static com.fieldcode.tesseract.interval.IntervalsRelationConstants.INTERVAL_IS_AFTER;
import static com.fieldcode.tesseract.interval.IntervalsRelationConstants.INTERVAL_IS_BEFORE;
import static com.fieldcode.tesseract.interval.IntervalsRelationConstants.INTERVAL_IS_FINISHED_BY;
import static com.fieldcode.tesseract.interval.IntervalsRelationConstants.INTERVAL_IS_MET_BY;
import static com.fieldcode.tesseract.interval.IntervalsRelationConstants.INTERVAL_IS_OVERLAPPED_BY;
import static com.fieldcode.tesseract.interval.IntervalsRelationConstants.INTERVAL_IS_STARTED_BY;
import static com.fieldcode.tesseract.interval.IntervalsRelationConstants.INTERVAL_MEETS;
import static com.fieldcode.tesseract.interval.IntervalsRelationConstants.INTERVAL_OVERLAPS;
import static com.fieldcode.tesseract.interval.IntervalsRelationConstants.INTERVAL_STARTS;


public final class IntervalRelationToString {

  //@formatter:on
  static final Map<Integer, String> RELATION_TO_STRING_DECODER = ImmutableSortedMap.<Integer, String>naturalOrder()
      .put(INTERVAL_IS_BEFORE, "is before")
      .put(INTERVAL_MEETS, "meets")
      .put(INTERVAL_OVERLAPS, "overlaps")
      .put(INTERVAL_STARTS, "starts")
      .put(INTERVAL_DURING, "during")
      .put(INTERVAL_FINISHES, "finishes")
      .put(INTERVAL_EQUALS_TO, "equals to")
      .put(INTERVAL_IS_FINISHED_BY, "is finished by")
      .put(INTERVAL_CONTAINS, "contains")
      .put(INTERVAL_IS_STARTED_BY, "is started by")
      .put(INTERVAL_IS_OVERLAPPED_BY, "is overlapped by")
      .put(INTERVAL_IS_MET_BY, "is met by")
      .put(INTERVAL_IS_AFTER, "is after")
      .build();
  //@formatter:on

  private IntervalRelationToString() {
  }

  public static String generateToString(IntervalsRelation relation) {
    var relationDescription = RELATION_TO_STRING_DECODER.get(relation.getFlags());
    return String.format("%s %s %s", relation.getA(), relationDescription, relation.getB());
  }

}
