package com.fieldcode.tesseract.timeline.events;

import java.util.ArrayList;
import java.util.List;

import com.fieldcode.tesseract.FluentIterator;
import com.fieldcode.tesseract.Interval;
import com.fieldcode.tesseract.IntervalMap;
import com.fieldcode.tesseract.Taggable.TagHolder;
import com.fieldcode.tesseract.interval.IntervalMaps;
import com.fieldcode.tesseract.interval.Intervals;
import com.fieldcode.tesseract.timeline.LayerType;
import com.fieldcode.tesseract.utils.iterator.Iterators;
import com.google.common.base.Objects;

import static com.fieldcode.tesseract.timeline.LayerType.EVENT_OVERLAP;

class OverlapEventHolder extends AbstractEventHolder {

  private final List<IntervalMap<InternalTimelineEvent>> layers;

  private OverlapEventHolder(List<IntervalMap<InternalTimelineEvent>> layers) {
    super();
    this.layers = new ArrayList<>(layers);
  }

  public static OverlapEventHolder of() {
    return new OverlapEventHolder(List.of());
  }

  private IntervalMap<InternalTimelineEvent> getEmptyLayer(Interval window) {
    for (var layer : layers) {
      if (isLayerEmptyBetween(layer, window)) {
        return layer;
      }
    }
    var layer = IntervalMaps.disjoint(UNDEFINED_VALUE);
    layers.add(layer);
    return layer;
  }

  @Override
  public EventHolder put(Interval window, TagHolder tags) {
    var layer = getEmptyLayer(window);
    layer.put(window, event(tags));
    return this;
  }

  @Override
  public EventHolder remove(Interval window) {
    for (var layer : layers) {
      layer.remove(window);
    }
    cleanup();
    return this;
  }

  @Override
  public boolean isEmpty(Interval window) {
    for (var layer : layers) {
      if (!isLayerEmptyBetween(layer, window)) {
        return false;
      }
    }
    return true;
  }

  @Override
  public LayerType getType() {
    return EVENT_OVERLAP;
  }

  @Override
  public EventHolder copy() {
    return new OverlapEventHolder(copy(layers));
  }

  @Override
  protected FluentIterator<IntervalMap<InternalTimelineEvent>> getLayers() {
    return Iterators.iterator(layers);
  }

  private void cleanup() {
    var layersToRemove = new ArrayList<IntervalMap<InternalTimelineEvent>>();
    for (IntervalMap<InternalTimelineEvent> layer : layers) {
      if (isLayerEmpty(layer)) {
        layersToRemove.add(layer);
      }
    }
    layers.removeAll(layersToRemove);
  }

  @Override
  public int hashCode() {
    var a = events(Intervals.always().forward()).list();
    return Objects.hashCode(a);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {return true;}
    if (o == null || getClass() != o.getClass()) {return false;}
    OverlapEventHolder that = (OverlapEventHolder) o;

    var a = events(Intervals.always().forward()).list();
    var b = that.events(Intervals.always().forward()).list();

    return Objects.equal(a, b);
  }

}
