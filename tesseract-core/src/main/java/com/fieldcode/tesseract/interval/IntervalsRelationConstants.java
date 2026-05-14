package com.fieldcode.tesseract.interval;

@SuppressWarnings("PointlessBitwiseExpression")
public final class IntervalsRelationConstants {

  //@formatter:off
  public static final int LT                        = 1 << 0;
  public static final int EQ                        = 1 << 1;
  public static final int GT                        = 1 << 2;

  public static final int MOMENT_IS_BEFORE          = 1 << 0;    // LT, LT
  public static final int MOMENT_STARTS             = 1 << 1;    // EQ, LT
  public static final int MOMENT_IS_INSIDE          = 1 << 2;    // GT, LT
  public static final int MOMENT_FINISHES           = 1 << 3;    // GT, EQ
  public static final int MOMENT_IS_AFTER           = 1 << 4;    // GT, GT

  public static final int INTERVAL_IS_BEFORE        = 1 << 0;    // MOMENT_IS_BEFORE, MOMENT_IS_BEFORE
  public static final int INTERVAL_MEETS            = 1 << 1;    // MOMENT_IS_BEFORE, MOMENT_STARTS
  public static final int INTERVAL_OVERLAPS         = 1 << 2;    // MOMENT_IS_BEFORE, MOMENT_IS_INSIDE
  public static final int INTERVAL_STARTS           = 1 << 3;    // MOMENT_STARTS,    MOMENT_IS_INSIDE
  public static final int INTERVAL_DURING           = 1 << 4;    // MOMENT_IS_INSIDE, MOMENT_IS_INSIDE
  public static final int INTERVAL_FINISHES         = 1 << 5;    // MOMENT_IS_INSIDE, MOMENT_FINISHES
  public static final int INTERVAL_EQUALS_TO        = 1 << 6;    // MOMENT_STARTS,    MOMENT_FINISHES
  public static final int INTERVAL_IS_FINISHED_BY   = 1 << 7;    // MOMENT_IS_BEFORE, MOMENT_FINISHES
  public static final int INTERVAL_CONTAINS         = 1 << 8;    // MOMENT_IS_BEFORE, MOMENT_IS_AFTER
  public static final int INTERVAL_IS_STARTED_BY    = 1 << 9;    // MOMENT_STARTS,    MOMENT_IS_AFTER
  public static final int INTERVAL_IS_OVERLAPPED_BY = 1 << 10;   // MOMENT_IS_INSIDE, MOMENT_IS_AFTER
  public static final int INTERVAL_IS_MET_BY        = 1 << 11;   // MOMENT_FINISHES,  MOMENT_IS_AFTER
  public static final int INTERVAL_IS_AFTER         = 1 << 12;   // MOMENT_IS_AFTER,  MOMENT_IS_AFTER

  public static final int INTERVAL_ENCLOSES         = INTERVAL_IS_STARTED_BY | INTERVAL_IS_FINISHED_BY | INTERVAL_EQUALS_TO | INTERVAL_CONTAINS;
  //@formatter:on

  private IntervalsRelationConstants() {

  }

}
