package com.fieldcode.tesseract.presence;

import com.fieldcode.tesseract.DirectedInterval;
import com.fieldcode.tesseract.FluentIterator;
import com.fieldcode.tesseract.Interval;
import com.fieldcode.tesseract.Location;
import com.fieldcode.tesseract.Moment;
import com.fieldcode.tesseract.Movement;
import com.fieldcode.tesseract.Presence;
import com.fieldcode.tesseract.PresenceCollection;
import com.fieldcode.tesseract.PresenceSet;
import com.fieldcode.tesseract.QueryDirection;
import com.fieldcode.tesseract.location.Locations;
import com.fieldcode.tesseract.movement.Movements;
import com.fieldcode.tesseract.utils.iterator.Iterators;
import com.google.common.collect.ImmutableSortedMap;
import com.google.common.collect.Streams;

import java.time.OffsetDateTime;
import java.util.Map.Entry;
import java.util.NavigableMap;
import java.util.function.BiFunction;
import java.util.stream.Collector;
import java.util.stream.Stream;

class ImmutableTreePresenceSet implements PresenceSet {

  private static final Collector<Presence, ?, ImmutableSortedMap<Moment, Presence>> TO_IMMUTABLE_SORTED_MAP = ImmutableSortedMap.toImmutableSortedMap(
      Moment::compareTo,
      Presence::getMoment,
      presence -> presence
  );
  private static final BiFunction<Presence, Presence, Movement> FORWARD_MOVEMENT_MAPPER = Movements::movement;
  private static final BiFunction<Presence, Presence, Movement> BACKWARD_MOVEMENT_MAPPER = (a, b) -> Movements.movement(b, a);
  private final NavigableMap<Moment, Presence> map;

  private ImmutableTreePresenceSet(Stream<Presence> presences) {

    this.map = presences.collect(TO_IMMUTABLE_SORTED_MAP);

  }

  static ImmutableTreePresenceSet of() {
    return new ImmutableTreePresenceSet(Stream.of());
  }

  static ImmutableTreePresenceSet of(PresenceSet set) {
    return new ImmutableTreePresenceSet(set.presences().stream());
  }

  static ImmutableTreePresenceSet of(Iterable<Presence> presences) {
    return new ImmutableTreePresenceSet(Streams.stream(presences));
  }

  static ImmutableTreePresenceSet of(Stream<Presence> presences) {
    return new ImmutableTreePresenceSet(presences);
  }

  private static Presence getValueOrNull(Entry<Moment, Presence> entry) {
    return entry == null
        ? null
        : entry.getValue();
  }

  @Override
  public PresenceSet put(Moment moment, Location location) {
    throw new UnsupportedOperationException();
  }

  @Override
  public PresenceSet put(OffsetDateTime moment, Location location) {
    throw new UnsupportedOperationException();
  }

  @Override
  public PresenceSet put(Presence presence) {
    throw new UnsupportedOperationException();
  }

  @Override
  public PresenceSet remove(Moment moment) {
    throw new UnsupportedOperationException();
  }

  @Override
  public PresenceSet remove(Moment lower, boolean lowerInclusive, Moment upper, boolean upperInclusive) {
    throw new UnsupportedOperationException();
  }

  @Override
  public Presence floor(Moment moment) {
    var entry = map.floorEntry(moment);
    return getValueOrNull(entry);
  }

  @Override
  public Presence ceiling(Moment moment) {
    var entry = map.ceilingEntry(moment);
    return getValueOrNull(entry);
  }

  @Override
  public Presence lower(Moment moment) {
    var entry = map.lowerEntry(moment);
    return getValueOrNull(entry);
  }

  @Override
  public Presence higher(Moment moment) {
    var entry = map.higherEntry(moment);
    return getValueOrNull(entry);
  }

  @Override
  public boolean isEmpty() {
    return map.isEmpty();
  }

  @Override
  public int size() {
    return map.size();
  }

  @Override
  public FluentIterator<Presence> presences() {
    return Iterators.iterator(map.values());
  }

  @Override
  public FluentIterator<Presence> presences(Interval window, QueryDirection direction) {
    return direction.get(
        () -> presencesForward(window),
        () -> presencesBackward(window)
    );
  }

  @Override
  public FluentIterator<Presence> presences(DirectedInterval directed) {
    return presences(directed.getInterval(), directed.getDirection());
  }

  @Override
  public FluentIterator<Movement> movements() {
    return presences()
        .successiveMap(FORWARD_MOVEMENT_MAPPER);
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
  public PresenceSet snapshot() {
    return this;
  }

  private FluentIterator<Presence> presencesForward(Interval window) {
    var lower = window.getLower();
    var upper = window.getUpper();

    var floor = floor(lower);

    var first = floor == null
        ? Presences.presence(lower, Locations.anywhere())
        : floor.move(lower);

    var ceilingEntry = ceiling(upper);
    var last = ceilingEntry == null
        ? Presences.presence(upper, Locations.anywhere())
        : ceilingEntry.move(upper);

    return Iterators.concat(
        Iterators.iterator(first),
        Iterators.iterator(map.subMap(lower, false, upper, false).values()),
        Iterators.iterator(last)
    );

  }

  private FluentIterator<Presence> presencesBackward(Interval window) {
    var lower = window.getLower();
    var upper = window.getUpper();

    var ceilingEntry = ceiling(upper);

    var last = ceilingEntry == null
        ? Presences.presence(upper, Locations.anywhere())
        : ceilingEntry.move(upper);

    var floorEntry = floor(lower);
    var first = floorEntry == null
        ? Presences.presence(lower, Locations.anywhere())
        : floorEntry.move(lower);

    return Iterators.concat(
        Iterators.iterator(last),
        Iterators.iterator(map.subMap(lower, false, upper, false).descendingMap().values()),
        Iterators.iterator(first)
    );
  }

  @Override
  public int hashCode() {
    return presences().set().hashCode();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {return true;}
    if (o == null) {return false;}

    if (o instanceof PresenceCollection) {
      var presences = ((PresenceCollection) o).presences().set();
      return map.values().containsAll(presences) && presences.containsAll(map.values());
    }
    return false;
  }

}
