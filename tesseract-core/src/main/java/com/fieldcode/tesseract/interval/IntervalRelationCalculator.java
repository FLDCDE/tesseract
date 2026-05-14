package com.fieldcode.tesseract.interval;

import com.fieldcode.tesseract.Interval;
import com.fieldcode.tesseract.IntervalsRelation;
import com.fieldcode.tesseract.Moment;
import com.google.common.collect.ImmutableSortedMap;

import java.util.Map;
import static com.fieldcode.tesseract.interval.IntervalsRelationConstants.EQ;
import static com.fieldcode.tesseract.interval.IntervalsRelationConstants.GT;
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
import static com.fieldcode.tesseract.interval.IntervalsRelationConstants.LT;
import static com.fieldcode.tesseract.interval.IntervalsRelationConstants.MOMENT_FINISHES;
import static com.fieldcode.tesseract.interval.IntervalsRelationConstants.MOMENT_IS_AFTER;
import static com.fieldcode.tesseract.interval.IntervalsRelationConstants.MOMENT_IS_BEFORE;
import static com.fieldcode.tesseract.interval.IntervalsRelationConstants.MOMENT_IS_INSIDE;
import static com.fieldcode.tesseract.interval.IntervalsRelationConstants.MOMENT_STARTS;
import static com.google.common.base.Preconditions.checkArgument;


@SuppressWarnings("PointlessBitwiseExpression")
public class IntervalRelationCalculator {

  //@formatter:off

  static final  Map<Integer, Integer> MOMENT_INTERVAL_DECODER = Map.of(
      combine3Bit(LT  , LT), 1 << 0,
      combine3Bit(EQ  , LT), 1 << 1,
      combine3Bit(GT  , LT), 1 << 2,
      combine3Bit(GT  , EQ), 1 << 3,
      combine3Bit(GT  , GT), 1 << 4
  );

  static final Map<Integer, Integer> LOWER_UPPER_INTERVAL_DECODER = ImmutableSortedMap.<Integer, Integer>naturalOrder()
      .put(combine5Bit(MOMENT_IS_BEFORE, MOMENT_IS_BEFORE), INTERVAL_IS_BEFORE)
      .put(combine5Bit(MOMENT_IS_BEFORE, MOMENT_STARTS)   , INTERVAL_MEETS)
      .put(combine5Bit(MOMENT_IS_BEFORE, MOMENT_IS_INSIDE), INTERVAL_OVERLAPS)
      .put(combine5Bit(MOMENT_STARTS,    MOMENT_IS_INSIDE), INTERVAL_STARTS)
      .put(combine5Bit(MOMENT_IS_INSIDE, MOMENT_IS_INSIDE), INTERVAL_DURING)
      .put(combine5Bit(MOMENT_IS_INSIDE, MOMENT_FINISHES) , INTERVAL_FINISHES)
      .put(combine5Bit(MOMENT_STARTS,    MOMENT_FINISHES) , INTERVAL_EQUALS_TO)
      .put(combine5Bit(MOMENT_IS_BEFORE, MOMENT_FINISHES) , INTERVAL_IS_FINISHED_BY)
      .put(combine5Bit(MOMENT_IS_BEFORE, MOMENT_IS_AFTER) , INTERVAL_CONTAINS)
      .put(combine5Bit(MOMENT_STARTS,    MOMENT_IS_AFTER) , INTERVAL_IS_STARTED_BY)
      .put(combine5Bit(MOMENT_IS_INSIDE, MOMENT_IS_AFTER) , INTERVAL_IS_OVERLAPPED_BY)
      .put(combine5Bit(MOMENT_FINISHES,  MOMENT_IS_AFTER) , INTERVAL_IS_MET_BY)
      .put(combine5Bit(MOMENT_IS_AFTER,  MOMENT_IS_AFTER) , INTERVAL_IS_AFTER)
      .build();
  //@formatter:on


  private IntervalRelationCalculator() {
  }

  private static int combine5Bit(int lower, int upper) {
    return lower << 5 | upper;
  }

  private static int combine3Bit(int lower, int upper) {
    return lower << 3 | upper;
  }

  public static IntervalsRelation relation(Interval a, Interval b) {
    return ImmutableIntervalsRelation.of(a, b, decode(a, b));
  }

  public static int flags(Interval a, Interval b) {
    return decode(a, b);
  }

  static int decode(Interval a, Interval b) {
    checkArgument(!a.isEmpty(), "Interval A cannot be empty");
    checkArgument(!b.isEmpty(), "Interval B cannot be empty");
    var aLowerToIntervalFlags = decode(a.getLower(), b);
    var aUpperToIntervalFlags = decode(a.getUpper(), b);
    return LOWER_UPPER_INTERVAL_DECODER.get(combine5Bit(aLowerToIntervalFlags, aUpperToIntervalFlags));
  }

  static int decode(Moment moment, Interval interval) {
    return MOMENT_INTERVAL_DECODER.get(flagsOf(moment, interval));
  }

  static int flagsOf(Moment moment, Interval interval) {
    var lower = interval.getLower();
    var upper = interval.getUpper();

    return combine3Bit(flagsOf(moment, lower), flagsOf(moment, upper));
  }

  static int flagsOf(Moment a, Moment b) {
    return signumBit(a.compareTo(b));
  }

  /**
   * Returns
   * <pre>
   *    0b001 | value < 0
   *    0b010 | value = 0
   *    0b100 | value > 0
   * </pre>
   *
   * @return sign
   */
  static int signumBit(int value) {
    return 1 << (Integer.signum(value) + 1);
  }

}
