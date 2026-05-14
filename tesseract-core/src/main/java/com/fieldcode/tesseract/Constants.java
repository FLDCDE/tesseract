package com.fieldcode.tesseract;

import com.fieldcode.tesseract.moment.Moments;

public class Constants {

  public static final String INFINITE_SIGN = "∞";
  public static final String EMPTY_SIGN = "∅";
  public static final String NEGATIVE_INFINITE_SIGN = "-" + INFINITE_SIGN;
  public static final String POSITIVE_INFINITE_SIGN = INFINITE_SIGN;
  public static final String INTERVAL_ALWAYS_SIGN = "[" + NEGATIVE_INFINITE_SIGN + ".." + POSITIVE_INFINITE_SIGN + ")";
  public static final InfiniteMoment POSITIVE_INFINITE_MOMENT_INSTANCE = Moments.inf();
  public static final InfiniteMoment NEGATIVE_INFINITE_MOMENT_INSTANCE = Moments.ninf();
  public static final String INTERVAL_EMPTY_SIGN = EMPTY_SIGN;
  public static final String PRESENCE_EMPTY_SIGN = EMPTY_SIGN;

  private Constants() {
  }

}
