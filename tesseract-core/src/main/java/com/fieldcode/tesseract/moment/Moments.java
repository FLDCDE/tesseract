package com.fieldcode.tesseract.moment;

import com.fieldcode.tesseract.FiniteMoment;
import com.fieldcode.tesseract.InfiniteMoment;
import com.fieldcode.tesseract.Moment;

import java.time.OffsetDateTime;
import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Factory methods for creating and parsing {@link Moment} instances.
 * <p>
 * Provides static utilities for moment creation, parsing, and comparison operations.
 */
public class Moments {

  private Moments() {
  }

  /**
   * Parses a string into a {@link Moment}.
   *
   * @param input the string to parse
   * @return the parsed moment
   * @throws NullPointerException if input is null
   */
  public static Moment parse(String input) {
    return MomentParser.parse(input);
  }

  /**
   * Parses a string into a {@link FiniteMoment}.
   *
   * @param input the string to parse
   * @return the parsed finite moment
   * @throws NullPointerException if input is null
   * @throws IllegalArgumentException if the parsed moment is not finite
   */
  public static FiniteMoment parseFinite(String input) {
    var moment = MomentParser.parse(input);
    checkArgument(moment instanceof FiniteMoment, "Moment is not finite [input=%s]", input);
    return (FiniteMoment) moment;
  }

  /**
   * Parses a string into an {@link InfiniteMoment}.
   *
   * @param input the string to parse
   * @return the parsed infinite moment
   * @throws NullPointerException if input is null
   * @throws IllegalArgumentException if the parsed moment is not infinite
   */
  public static InfiniteMoment parseInfinite(String input) {
    var moment = MomentParser.parse(input);
    checkArgument(moment instanceof InfiniteMoment, "Moment is not infinite [input=%s]", input);
    return (InfiniteMoment) moment;
  }

  /**
   * Creates a finite moment from the given date-time.
   *
   * @param at the date-time
   * @return the finite moment
   * @throws NullPointerException if at is null
   */
  public static FiniteMoment moment(OffsetDateTime at) {
    checkNotNull(at, "at cannot be null");
    return ImmutableFiniteMoment.of(at);
  }

  /**
   * Returns positive infinity. Singleton instance is returned for all calls to this method.
   *
   * @return positive infinite moment
   */
  public static InfiniteMoment inf() {
    return ImmutablePositiveInfiniteMoment.of();
  }

  /**
   * Returns negative infinity. Singleton instance is returned for all calls to this method.
   *
   * @return negative infinite moment
   */
  public static InfiniteMoment ninf() {
    return ImmutableNegativeInfiniteMoment.of();
  }

  /**
   * Returns the current moment.
   *
   * @return the current moment
   */
  public static FiniteMoment now() {
    return moment(OffsetDateTime.now());
  }

  /**
   * Returns the minimum of two moments.
   *
   * @param a first moment
   * @param b second moment
   * @return the minimum moment
   * @throws NullPointerException if either moment is null
   */
  public static Moment min(Moment a, Moment b) {
    checkNotNull(a, "a cannot be null");
    checkNotNull(b, "b cannot be null");
    var comp = a.compareTo(b);
    if (comp == 0) {
      return a;
    }
    return comp < 0 ? a : b;
  }

  /**
   * Returns the maximum of two moments.
   *
   * @param a first moment
   * @param b second moment
   * @return the maximum moment
   * @throws NullPointerException if either moment is null
   */
  public static Moment max(Moment a, Moment b) {
    checkNotNull(a, "a cannot be null");
    checkNotNull(b, "b cannot be null");
    var comp = a.compareTo(b);
    if (comp == 0) {
      return a;
    }
    return comp > 0 ? a : b;
  }

}
