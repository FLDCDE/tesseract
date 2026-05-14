package com.fieldcode.tesseract.timeline.presences;

import com.fieldcode.tesseract.DirectedInterval;
import com.fieldcode.tesseract.FluentIterator;
import com.fieldcode.tesseract.Interval;
import com.fieldcode.tesseract.Moment;
import com.fieldcode.tesseract.Movement;
import com.fieldcode.tesseract.Presence;
import com.fieldcode.tesseract.PresenceSet;
import com.fieldcode.tesseract.presence.PresenceSets;
import com.fieldcode.tesseract.timeline.InternalLayer;
import com.fieldcode.tesseract.timeline.LayerDefinition;
import com.fieldcode.tesseract.timeline.ModifiableLayer;
import com.google.common.base.Objects;

import static com.fieldcode.tesseract.timeline.LayerType.PRESENCE;
import static com.google.common.base.Preconditions.checkState;

public class PresenceLayer extends InternalLayer {

  private final PresenceSet presences;

  private PresenceLayer(LayerDefinition definition, PresenceSet presences) {
    super(definition);
    checkState(definition.getType() == PRESENCE, "Layer type must be %s", PRESENCE);
    this.presences = presences;
  }

  public static PresenceLayer of(LayerDefinition definition, PresenceSet presences) {
    return new PresenceLayer(definition, presences);
  }

  public static PresenceLayer of(LayerDefinition definition) {
    return new PresenceLayer(definition, PresenceSets.presences());
  }

  @Override
  public InternalLayer copy() {
    var copied = PresenceSets.presences(presences);
    return of(getDefinition(), copied);
  }

  @Override
  public InternalLayer duplicate(String newId) {
    var copied = PresenceSets.presences(presences);
    return new PresenceLayer(copyDefinition(newId), copied);
  }

  @Override
  public FluentIterator<Presence> selectPresences(DirectedInterval directed) {
    return presences.presences(directed.getInterval(), directed.getDirection());
  }

  @Override
  public FluentIterator<Movement> selectMovements(DirectedInterval directed) {
    return presences.movements(directed.getInterval(), directed.getDirection());
  }

  @Override
  public PresenceSet asPresenceSet() {
    return PresenceSets.presences(presences);
  }

  @Override
  public ModifiableLayer put(Presence presence) {
    presences.put(presence);
    return this;
  }

  @Override
  public ModifiableLayer putAll(PresenceSet presences) {
    presences
        .presences()
        .forEach(this::put);
    return this;
  }

  @Override
  public ModifiableLayer remove(Moment at) {
    presences.remove(at);
    return this;
  }

  @Override
  public ModifiableLayer remove(Interval interval) {
    presences.remove(interval.getLower(), true, interval.getUpper(), false);
    return this;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {return true;}
    if (o == null || getClass() != o.getClass()) {return false;}
    PresenceLayer that = (PresenceLayer) o;
    return Objects.equal(presences, that.presences);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(presences);
  }
}
