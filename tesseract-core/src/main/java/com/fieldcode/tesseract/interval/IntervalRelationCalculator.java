package com.fieldcode.tesseract.interval;

import com.fieldcode.tesseract.Interval;
import com.fieldcode.tesseract.IntervalsRelation;
import com.fieldcode.tesseract.Moment;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Computes Allen interval relations using branchless bitwise decoding.
 *
 * <p>The algorithm compares the bounds of the first interval ({@code a.lower} and
 * {@code a.upper}) against the second interval ({@code b}), converts each comparison to sign flags, packs them into compact integer keys, and resolves the final relation through
 * decoder lookup methods.
 */
public class IntervalRelationCalculator {

  private IntervalRelationCalculator() {
  }

  /**
   * Returns the Allen relation object for {@code a} relative to {@code b}.
   *
   * <p>For flag semantics and validation rules, see {@link #flags(Interval, Interval)}.
   */
  public static IntervalsRelation relation(Interval a, Interval b) {
    return ImmutableIntervalsRelation.of(a, b, flags(a, b));
  }

  /**
   * Computes the Allen interval relation between {@code a} and {@code b} as a one-hot flag.
   *
   * <p>Allen relation flag mapping:
   * <pre>
   * before        ->    1 - "a starts before b starts and a ends before b starts"
   * meets         ->    2 - "a starts before b starts and a ends exactly when b starts"
   * overlaps      ->    4 - "a starts before b starts and a ends after b starts but before b ends"
   * starts        ->    8 - "a starts exactly when b starts and a ends before b ends"
   * during        ->   16 - "a starts after b starts and a ends before b ends"
   * finishes      ->   32 - "a starts after b starts and a ends exactly when b ends"
   * equals        ->   64 - "a starts exactly when b starts and a ends exactly when b ends"
   * finished-by   ->  128 - "a starts before b starts and a ends exactly when b ends"
   * contains      ->  256 - "a starts before b starts and a ends after b ends"
   * started-by    ->  512 - "a starts exactly when b starts and a ends after b ends"
   * overlapped-by -> 1024 - "a starts after b starts but before b ends and a ends after b ends"
   * met-by        -> 2048 - "a starts exactly when b ends and a ends after b ends"
   * after         -> 4096 - "a starts after b ends and a ends after b ends"
   * </pre>
   *
   * @param a first interval
   * @param b second interval used as reference
   * @return one-hot Allen relation flag for {@code a} relative to {@code b}. Result is one of: 1, 2, 4, 8, 16, 32, 64, 128, 256, 512, 1024, 2048, 4096.
   * @throws NullPointerException if either interval is null
   * @throws IllegalArgumentException if either interval is empty
   */
  public static int flags(Interval a, Interval b) {
    checkNotNull(a, "Interval A cannot be null");
    checkNotNull(b, "Interval B cannot be null");
    checkArgument(!a.isEmpty(), "Interval A cannot be empty");
    checkArgument(!b.isEmpty(), "Interval B cannot be empty");

    var aLowerToBFlags = momentToIntervalFlags(a.getLower(), b);
    var aUpperToBFlags = momentToIntervalFlags(a.getUpper(), b);
    var key = aLowerToBFlags << 5 | aUpperToBFlags;

    // Only 13 valid states (not 5x5=25): with interval invariants a.lower < a.upper and b.lower < b.upper,
    // ordering constraints exclude combinations (e.g. a.upper < b.lower => a.lower < b.lower).

    return BranchlessOperators.decodeIntervalToIntervalRelationFlag(key);
  }

  /**
   * Classifies a moment relative to an interval and returns a one-hot relation flag.
   *
   * <p>Compares {@code moment} to both interval bounds, packs the two sign flags into a key,
   * and decodes that key using this mapping:
   * <pre>
   * before   ->  1 (0b00001)
   * starts   ->  2 (0b00010)
   * inside   ->  4 (0b00100)
   * finishes ->  8 (0b01000)
   * after    -> 16 (0b10000)
   * </pre>
   *
   * Examples:
   * <pre>
   *
   *   Moment:    |
   *   Interval:     |-----|
   *   Flags: 1 (before)
   *
   *   Moment:    |
   *   Interval:  |-----|
   *   Flags: 2 (starts)
   *
   *   Moment:      |
   *   Interval:  |-------|
   *   Flags: 4 (inside)
   *
   *   Moment:          |
   *   Interval:  |-----|
   *   Flags: 8 (finishes)
   *
   *   Moment:            |
   *   Interval:  |-----|
   *   Flags: 16 (after)
   *
   * </pre>
   *
   * @param moment the moment to classify (must be non-null)
   * @param interval the interval to compare against (must be non-null)
   * @return one-hot relation flag for {@code moment} relative to {@code interval}. Result is one of: 1, 2, 4, 8, 16.
   */
  private static int momentToIntervalFlags(Moment moment, Interval interval) {
    var lower = interval.getLower();
    var upper = interval.getUpper();

    var momentToLowerFlag = BranchlessOperators.signFlag(moment.compareTo(lower));
    var momentToUpperFlag = BranchlessOperators.signFlag(moment.compareTo(upper));

    var key = momentToLowerFlag << 3 | momentToUpperFlag;

    // Only 5 valid states (not 3x3=9): with interval invariant lower < upper,
    // ordering constraints exclude combinations (e.g. moment < lower => moment < upper,
    // and moment > upper => moment > lower).

    return BranchlessOperators.decodeMomentToIntervalRelationFlag(key);
  }

}
