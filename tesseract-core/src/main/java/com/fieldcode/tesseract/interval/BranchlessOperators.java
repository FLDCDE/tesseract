package com.fieldcode.tesseract.interval;

class BranchlessOperators {

  private BranchlessOperators() {
  }

  /**
   * Converts a compare-style integer to a one-hot sign flag.
   *
   * <p>Mapping: {@code value < 0 -> 1}, {@code value == 0 -> 2}, {@code value > 0 -> 4}.
   *
   * @param value compare-style integer (negative, zero, or positive)
   * @return one-hot sign flag ({@code 1}, {@code 2}, or {@code 4})
   */
  static int signFlag(int value) {
    // Branchless sign computation: avoids Integer.signum() method call overhead
    // Maps to equivalent of 1 << (Integer.signum(value) + 1)
    // Key insight: use conditional ternary which compiles to branchless code (cmov instructions)

    // For efficiency: check positive values first (most common), then zero, then negative
    // This allows compiler to optimize the ternary chains
    return (value > 0) ? 4 : (value == 0 ? 2 : 1);
  }

  /**
   * Decodes a packed moment-vs-interval key to a moment relation flag.
   *
   * <p>Valid mapping: {@code 9->1, 17->2, 33->4, 34->8, 36->16}.
   *
   * @param x packed key produced by moment/interval comparison flags
   * @return one-hot moment relation flag
   */
  static int decodeMomentToIntervalRelationFlag(int x) {
    // Unique buckets for: 33, 34, 36, 17, 9
    int h = (x * 3 >>> 2) & 0x7;

    // 8 nibbles (4-bit exponents), indexed by h
    // nibble[h] = exp, output = 1 << exp
    int lut = 0x14032;

    int exp = (lut >>> (h << 2)) & 0xF;
    return 1 << exp;
  }

  /**
   * Decodes a packed lower/upper interval key to an interval relation flag.
   *
   * <p>Valid mapping: {@code 33->1, 34->2, 36->4, 40->128, 48->256, 68->8, 72->64, 80->512,
   * 132->16, 136->32, 144->1024, 272->2048, 528->4096}.
   *
   * @param x packed key produced by combining lower/upper moment relation flags
   * @return one-hot interval relation flag
   */
  static int decodeIntervalToIntervalRelationFlag(int x) {
    int h = (x * 925 >>> 7) & 0xF;
    long lut = 0x0B03846C512097AL;
    int exp = (int) ((lut >>> (h << 2)) & 0xF);
    return 1 << exp;
  }

}
