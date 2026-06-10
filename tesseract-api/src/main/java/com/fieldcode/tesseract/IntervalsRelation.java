package com.fieldcode.tesseract;

/**
 * Represents one Allen temporal relation between interval {@code A} and interval {@code B}.
 *
 * <pre>
 *
 *    -------------------------------------------------------------
 *    #1 A is before B (INTERVAL_IS_BEFORE)
 *
 *        A|-------|
 *                    B|-------|
 *
 *    -------------------------------------------------------------
 *    #2 A meets B (INTERVAL_MEETS)
 *
 *        A|---------|
 *                  B|---------|
 *
 *    -------------------------------------------------------------
 *    #3 A overlaps B (INTERVAL_OVERLAPS)
 *
 *        A|----------|
 *                B|-----------|
 *
 *    -------------------------------------------------------------
 *    #4 A starts B (INTERVAL_STARTS)
 *        A|----------|
 *        B|-------------------|
 *
 *    -------------------------------------------------------------
 *    #5 A during B (INTERVAL_DURING)
 *
 *            A|----------|
 *        B|-------------------|
 *
 *    -------------------------------------------------------------
 *    #6 A finishes B (INTERVAL_FINISHES)
 *
 *                 A|----------|
 *        B|-------------------|
 *
 *    -------------------------------------------------------------
 *    #7 A equals to B (INTERVAL_EQUALS_TO)
 *
 *        A|-------------------|
 *        B|-------------------|
 *
 *    -------------------------------------------------------------
 *    #8 A is finished by B (INTERVAL_IS_FINISHED_BY)
 *
 *        A|-------------------|
 *                 B|----------|
 *
 *    -------------------------------------------------------------
 *    #9 A includes B (INTERVAL_CONTAINS)
 *
 *        A|-------------------|
 *            B|----------|
 *
 *    -------------------------------------------------------------
 *    #10  A is started by B (INTERVAL_STARTED_BY)
 *
 *        A|-------------------|
 *        B|----------|
 *
 *    -------------------------------------------------------------
 *    #11 A is overlapped by B (INTERVAL_IS_OVERLAPPED_BY)
 *
 *                A|-----------|
 *        B|----------|
 *
 *    -------------------------------------------------------------
 *    #12 A is met by B (INTERVAL_MET_BY)
 *
 *                  A|---------|
 *        B|---------|
 *
 *    -------------------------------------------------------------
 *    #13 B is after A (INTERVAL_IS_AFTER)
 *
 *                    A|-------|
 *        B|-------|
 *
 *    -------------------------------------------------------------
 *
 * </pre>
 *
 * @see <a href="https://en.wikipedia.org/wiki/Allen%27s_interval_algebra">Allen's interval algebra</a>
 */
public interface IntervalsRelation {

  /**
   * Returns the first {@code Interval} temporal interval of the relation.
   */
  Interval getA();

  /**
   * Returns the second {@code Interval} temporal interval of the relation.
   */
  Interval getB();

  /**
   * Returns the bit representation of the relation between intervals A and B. Every relation type is identified by a bit in flags.
   * <p>
   * Bit order:
   *
   * <pre>
   *   0  : INTERVAL_IS_BEFORE
   *   1  : INTERVAL_MEETS
   *   2  : INTERVAL_OVERLAPS
   *   3  : INTERVAL_STARTS
   *   4  : INTERVAL_DURING
   *   5  : INTERVAL_FINISHES
   *   6  : INTERVAL_EQUALS_TO
   *   7  : INTERVAL_IS_FINISHED_BY
   *   8  : INTERVAL_CONTAINS
   *   9  : INTERVAL_IS_STARTED_BY
   *   10 : INTERVAL_IS_OVERLAPPED_BY
   *   11 : INTERVAL_IS_MET_BY
   *   12 : INTERVAL_IS_AFTER
   * </pre>
   * <p>
   * Return int contains one and only one bit.
   */
  int getFlags();

  /**
   * Returns true if interval A is before B.
   *
   * <pre>
   *
   *        A|-------|
   *                    B|-------|
   *
   * </pre>
   *
   * @return true if and only if A upper < B lower
   */
  boolean isBefore();

  /**
   * Returns true if interval A meets B.
   *
   * <pre>
   *
   *        A|---------|
   *                  B|---------|
   *
   * </pre>
   *
   * @return true if and only if A upper == B lower
   */
  boolean meets();

  /**
   * Returns true if interval A overlaps B.
   *
   * <pre>
   *
   *        A|----------|
   *                B|-----------|
   *
   * </pre>
   *
   * @return true if and only if A lower < B lower < A upper < B upper
   */
  boolean overlaps();

  /**
   * Returns true if interval A starts B.
   *
   * <pre>
   *
   *        A|----------|
   *        B|-------------------|
   *
   * </pre>
   *
   * @return true if and only if A lower == B lower & A upper < B upper
   */
  boolean starts();

  /**
   * Returns true if interval A is during B.
   *
   * <pre>
   *
   *            A|----------|
   *        B|-------------------|
   *
   * </pre>
   *
   * @return true if and only if A lower > B lower & A upper < B upper
   */
  boolean during();

  /**
   * Returns true if interval A finishes B.
   *
   * <pre>
   *
   *                 A|----------|
   *        B|-------------------|
   *
   * </pre>
   *
   * @return true if and only if A lower > B lower & A upper == B upper
   */
  boolean finishes();

  /**
   * Returns true if interval A equals to B.
   *
   * <pre>
   *
   *        A|-------------------|
   *        B|-------------------|
   *
   * </pre>
   *
   * @return true if and only if A lower == B lower & A upper == B upper
   */
  boolean equalsTo();

  /**
   * Returns true if interval A is finished by B.
   *
   * <pre>
   *
   *        A|-------------------|
   *                 B|----------|
   *
   * </pre>
   *
   * @return true if and only if A lower < B lower & A upper == B upper
   */
  boolean isFinishedBy();

  /**
   * Returns true if interval A contains B.
   *
   * <pre>
   *
   *        A|-------------------|
   *            B|----------|
   *
   * </pre>
   *
   * @return true if and only if A lower < B lower & A upper > B upper
   */
  boolean contains();

  /**
   * Returns true if interval A is started by B.
   *
   * <pre>
   *
   *        A|-------------------|
   *        B|----------|
   *
   * </pre>
   *
   * @return true if and only if A lower == B lower & A upper > B upper
   */
  boolean isStartedBy();

  /**
   * Returns true if interval A is overlapped by B.
   *
   * <pre>
   *
   *                A|-----------|
   *        B|----------|
   *
   * </pre>
   *
   * @return true if and only if B lower < A lower < B upper < A upper
   */
  boolean isOverlappedBy();

  /**
   * Returns true if interval A is met by B.
   *
   * <pre>
   *
   *                  A|---------|
   *        B|---------|
   *
   * </pre>
   *
   * @return true if and only if A lower == B upper
   */
  boolean isMetBy();

  /**
   * Returns true if interval A is after B.
   *
   * <pre>
   *
   *                    A|-------|
   *        B|-------|
   *
   * </pre>
   *
   * @return true if and only if A lower > B upper
   */
  boolean isAfter();

  /**
   * Auxiliary relation. <BR/> Returns true if intervals has common subintervals.
   * <p>
   * Examples (A B* intersects):
   * <pre>
   *
   *         A|-------|
   *        B1|-------|
   *  B2|-------|
   *              B3|-------|
   *       B4|--|
   *            B5|--|
   *         B6|--|
   *    B7|-------------|
   *
   * </pre>
   *
   * @return true if two intervals has intersection
   */
  boolean intersects();

  /**
   * Auxiliary relation. <BR/> Returns true if intervals has NO common subintervals.
   * <p>
   * Examples (A B* distinct):
   * <pre>
   *
   *             A|-------|
   *         B1|--|
   *   B2|--|
   *                    B3|--|
   *                          B4|--|
   * </pre>
   *
   * @return true if two interval has NO intersection
   */
  boolean distinct();

  /**
   * Auxiliary relation. Returns true if A and B are adjacent (touching but not intersecting).
   *
   * <p>Examples (A B* adjacent):</p>
   * <pre>
   *             A|-------|
   *         B1|--|
   *                    B3|--|
   * </pre>
   *
   * @return true if and only if A upper == B lower or A lower == B upper
   */
  boolean adjacent();

  /**
   * Auxiliary relation. <BR/> Returns true if A,B intervals are intersecting or adjacent. Useful for checking two intervals are mergeable.
   *
   * @return true if A intersects B or A is adjacent to B
   */
  boolean connects();

  /**
   * <pre>
   *
   * 1) A interval is Started By B interval
   *
   *      A|-------------------|
   *
   *      B|----------|
   *
   * 2) A interval is Finished By B interval
   *
   *      A|-------------------|
   *
   *               B|----------|
   *
   * 3) A interval Contains B interval
   *
   *      A|-------------------|
   *
   *          B|----------|
   *
   * 4) A interval is Equal To B interval
   *
   *      A|-------------------|
   *
   *      B|-------------------|
   *
   * </pre>
   *
   * @return true if one of the following cases apply
   */
  boolean encloses();

}
