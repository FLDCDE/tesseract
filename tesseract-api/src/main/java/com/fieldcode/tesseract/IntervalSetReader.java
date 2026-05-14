package com.fieldcode.tesseract;

import java.util.Optional;

public interface IntervalSetReader extends IntervalCollection {

  Interval get(Moment moment);


  /**
   * <pre>
   * Fetches the enclosing interval if defined.
   *
   * Given a set with the defined intervals A(a1, a2) and B(b1, b2)
   * and a moment c where
   *
   *  * a1 < a2 < b1 < b2
   *  * a1 < a < a2
   *  * a2 < b < b1
   *  * b1 < c < b2
   *
   *                           a1  a2  b1     b2
   *                           -----   --------
   *                              a  b  c
   *
   * {@code Interval interval = intervalset.getIfContains(c); }
   *
   * will return the intervals:
   *  * getIfContains(a) -> A
   *  * getIfContains(b) -> empty
   *  * getIfContains(c) -> B
   *
   * @param moment on which the closest interval will be fetched
   *
   * @return the closest interval of the set
   *
   * @since 1.3.0
   *
   * </pre>
   */
  Optional<Interval> getIfContains(Moment moment);

  /**
   * <pre>
   * Searches the whole set to find if the given moment is included
   *
   * Given a set with the defined intervals A(a1, a2) and B(b1, b2)
   * and moment c where a2 < c < b1 and moment d where b1 <= d <= b2.
   *
   *                           a1  a2  b1     b2
   *                           -----   --------
   *                                 c     d
   *
   * {@code Boolean momentNotIncluded = intervalset.contains(c); }
   *
   * will return <strong>false</strong> whereas
   *
   * {@code Boolean momentIncluded = intervalset.contains(d); }
   *
   * will return <strong>true</strong>
   *
   * @param moment which is searched throughout the set
   *
   * @return true or false depending on the inclusion of the moment
   *
   * @since 1.3.0
   *
   * </pre>
   */
  boolean contains(Moment moment);

  /**
   * <pre>
   * Searches the whole set to find if the given interval is enclosed.
   * An interval is considered enclosed when it's lower moment bound is higher
   * or equal than the lower bound moment of a set and while it's higher moment
   * bound is less or equal than the upper bound moment of the same set's
   * interval.
   *
   *
   * Given a set with the defined intervals:
   * A(a1, a2) and B(b1, b2) and intervals C(c1, c2) where
   * a1 < c1 and c2 > a2, D(d1, d2) &
   * b1 <= d1 < b2 and b1 < d2 <= b2
   *
   *
   *                           a1    a2     b1      b2
   *                           -------      ----------
   *                              C -------
   *                                        D -------
   *
   * {@code Boolean intervalNotEnclosed = intervalset.encloses(C); }
   *
   * will return false whereas
   *
   * {@code Boolean intervalEnclosed = intervalset.encloses(D); }
   *
   * will return true
   * </pre>
   *
   * @param other interval which is searched throughout the set
   * @return true or false depending on the enclosing of the interval
   * @since 1.3.0
   */
  boolean encloses(Interval other);

}
