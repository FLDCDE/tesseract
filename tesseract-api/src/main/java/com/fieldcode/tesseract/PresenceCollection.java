package com.fieldcode.tesseract;

public interface PresenceCollection {

  Presence floor(Moment moment);

  /**
   * Returns the presence with the lowest moment that is higher than or equal to the given moment.
   *
   * @param moment the moment
   * @return the presence with the lowest moment that is higher than or equal to the given moment
   */
  Presence ceiling(Moment moment);

  /**
   * Returns the presence with the highest moment that is lower than the given moment.
   *
   * @param moment the moment
   * @return the presence with the highest moment that is lower than the given moment
   */
  Presence lower(Moment moment);

  /**
   * Returns the presence with the lowest moment that is higher than the given moment.
   *
   * @param moment the moment
   * @return the presence with the lowest moment that is higher than the given moment
   */
  Presence higher(Moment moment);

  /**
   * Returns whether this presence set is empty.
   *
   * @return whether this presence set is empty
   */
  boolean isEmpty();

  /**
   * Returns the number of presences in this presence set.
   *
   * @return the number of presences in this presence set
   */
  int size();

  /**
   * Returns a fluent iterator over the presences in this presence set.
   *
   * @return a fluent iterator over the presences in this presence set
   */
  FluentIterator<Presence> presences();

  /**
   * Returns a fluent iterator over the presences in this presence set. The iterator will iterate over the presences in the given window in the given direction. <br/><br/> The
   * first and last presences are determined as follows:
   * <pre>
   *   Given a presence set with the following presences:
   *
   *   [p1(m1,l1), p2(m2,l2), p3(m3,l3), p4(m4,l4), p5(m5,l5)]
   *
   *   set.presences([m1,m5], FORWARD)  -> [p1, p2, p3, p4, p5]
   *   set.presences([m1,m5], BACKWARD) -> [p5, p4, p3, p2, p1]
   *
   *   set.presences([m2,m4], FORWARD)  -> [p2, p3, p4]
   *   set.presences([m2,m4], BACKWARD) -> [p4, p3, p2]
   *
   *   In case of l < m1 and u >m5
   *
   *   set.presences([l,u], FORWARD)  -> [pl(l, anywhere())p1, p2, p3, p4, p5, pu(u, anywhere())]
   *   set.presences([l,u], BACKWARD) -> [pu(u, anywhere()), p5, p4, p3, p2, pl(l, anywhere())]
   *
   *   In case of m1 < l < m2 and m4 < u < m5
   *
   *   set.presences([l,u], FORWARD)  -> [pl(l, l1), p2, p3, p4, pu(u, l5)]
   *   set.presences([l,u], BACKWARD) -> [pu(u, l5), p4, p3, p2, pl(l, l1)]
   *
   * </pre>
   *
   * @param window the window to iterate over
   * @param direction the direction to iterate in
   * @return a fluent iterator over the presences in this presence set
   */
  FluentIterator<Presence> presences(Interval window, QueryDirection direction);

  /**
   * Same as presences(window, direction) but takes a DirectedInterval.
   *
   * @param directed the directed interval
   * @return a fluent iterator over the presences in this presence set
   */
  FluentIterator<Presence> presences(DirectedInterval directed);

  /**
   * Returns a fluent iterator over the movements in this presence set. The iterator will iterate over all the movements in FORWARD direction.
   *
   * @return a fluent iterator over the movements in this presence set
   */
  FluentIterator<Movement> movements();

  /**
   * Returns a fluent iterator over the movements in this presence set. The iterator will iterate over the movements in the given window in the given direction.
   *
   * @param window the window to iterate over
   * @param direction the direction to iterate in
   * @return a fluent iterator over the movements in this presence set
   */
  FluentIterator<Movement> movements(Interval window, QueryDirection direction);


  /**
   * Same as movements(window, direction) but takes a DirectedInterval.
   *
   * @param directed the directed interval
   * @return a fluent iterator over the movements in this presence set
   */
  FluentIterator<Movement> movements(DirectedInterval directed);

  /**
   * Returns an immutable snapshot of this presence set.
   *
   * @return an immutable snapshot of this presence set
   */
  PresenceCollection snapshot();

}
