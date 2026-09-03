package com.fieldcode.tesseract.presence;

import com.fieldcode.tesseract.DirectedInterval;
import com.fieldcode.tesseract.FluentIterator;
import com.fieldcode.tesseract.Interval;
import com.fieldcode.tesseract.Moment;
import com.fieldcode.tesseract.Movement;
import com.fieldcode.tesseract.Presence;
import com.fieldcode.tesseract.PresenceCollection;
import com.fieldcode.tesseract.QueryDirection;
import com.fieldcode.tesseract.location.Locations;
import com.fieldcode.tesseract.movement.Movements;
import com.fieldcode.tesseract.utils.iterator.Iterators;

import java.util.function.BiFunction;

/**
 * The presence collection that never holds any presences. Unlike an empty {@link TreePresenceSet} or
 * {@link ImmutableTreePresenceSet}, it holds no backing map at all, so every query resolves in constant time without
 * allocating anything. There is exactly one instance, {@link #INSTANCE}, reachable via {@link PresenceSets#empty()}.
 */
class EmptyPresenceCollection implements PresenceCollection {

  private static final BiFunction<Presence, Presence, Movement> FORWARD_MOVEMENT_MAPPER = Movements::movement;
  private static final BiFunction<Presence, Presence, Movement> BACKWARD_MOVEMENT_MAPPER = (a, b) -> Movements.movement(b, a);

  static final EmptyPresenceCollection INSTANCE = new EmptyPresenceCollection();

  private EmptyPresenceCollection() {}

  @Override
  public Presence floor(Moment moment) {
    return null;
  }

  @Override
  public Presence ceiling(Moment moment) {
    return null;
  }

  @Override
  public Presence lower(Moment moment) {
    return null;
  }

  @Override
  public Presence higher(Moment moment) {
    return null;
  }

  @Override
  public boolean isEmpty() {
    return true;
  }

  @Override
  public int size() {
    return 0;
  }

  @Override
  public FluentIterator<Presence> presences() {
    return Iterators.empty();
  }

  @Override
  public FluentIterator<Presence> presences(Interval window, QueryDirection direction) {
    var lower = Presences.presence(window.getLower(), Locations.anywhere());
    var upper = Presences.presence(window.getUpper(), Locations.anywhere());

    return direction.get(
        () -> Iterators.iterator(lower, upper),
        () -> Iterators.iterator(upper, lower)
    );
  }

  @Override
  public FluentIterator<Presence> presences(DirectedInterval directed) {
    return presences(directed.getInterval(), directed.getDirection());
  }

  @Override
  public FluentIterator<Movement> movements() {
    return Iterators.empty();
  }

  @Override
  public FluentIterator<Movement> movements(Interval window, QueryDirection direction) {
    var mapper = direction.select(FORWARD_MOVEMENT_MAPPER, BACKWARD_MOVEMENT_MAPPER);

    return presences(window, direction)
        .successiveMap(mapper);
  }

  @Override
  public FluentIterator<Movement> movements(DirectedInterval directed) {
    return movements(directed.getInterval(), directed.getDirection());
  }

  @Override
  public PresenceCollection snapshot() {
    return this;
  }

  @Override
  public int hashCode() {
    return 0;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {return true;}
    return o instanceof PresenceCollection && ((PresenceCollection) o).isEmpty();
  }

}
