package com.fieldcode.tesseract.timeline;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.fieldcode.tesseract.Taggable.Tag;
import com.fieldcode.tesseract.Taggable.TagHolder;
import com.google.common.base.Objects;

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.stream.Collectors.toUnmodifiableList;

public class InternalTimelineLayersManager implements TimelineLayersManager {

  private final Map<String, InternalLayer> layers = new HashMap<>();

  private InternalTimelineLayersManager(List<InternalLayer> layers) {
    layers.forEach(this::addLayer);
  }

  static InternalTimelineLayersManager of(List<InternalLayer> layers) {
    return new InternalTimelineLayersManager(layers);
  }

  static InternalTimelineLayersManager of() {
    return new InternalTimelineLayersManager(List.of());
  }

  private void addLayer(InternalLayer layer) {
    layers.put(layer.getId(), layer);
  }

  InternalLayer layer(String layerId) {
    checkDefined(layerId);
    return layers.get(layerId);
  }

  void checkDefined(String layerId) {
    checkArgument(layers.containsKey(layerId), "Layer not defined [layerId=%s, layers=%s]", layerId, layers.keySet());
  }

  private void checkNotDefined(String layerId) {
    checkArgument(!layers.containsKey(layerId), "Layer already defined [layerId=%s]", layerId);
  }

  @Override
  public TimelineLayersManager addLayer(String id, LayerType type, TagHolder tags) {
    checkNotDefined(id);
    layers.put(id, LayerFactory.layer(id, type, tags));
    return this;
  }

  @Override
  public TimelineLayersManager removeLayer(String id) {
    checkDefined(id);
    layers.remove(id);
    return this;
  }

  @Override
  public InternalTimelineLayersManager duplicateLayer(String layerId, String newLayerId) {
    checkNotDefined(newLayerId);
    var layer = layer(layerId);
    layers.put(newLayerId, layer.duplicate(newLayerId));
    return this;
  }

  public InternalTimelineLayersManager copy() {

    var newLayers = layers.values()
        .stream()
        .map(InternalLayer::copy)
        .collect(toUnmodifiableList());

    return new InternalTimelineLayersManager(newLayers);
  }

  @Override
  public Set<String> getIds() {
    return Set.copyOf(layers.keySet());
  }

  @Override
  public LayerType getType(String layerId) {
    return layer(layerId).getType();
  }

  @Override
  public List<Layer> all() {
    return layers.values()
        .stream()
        .map(Layer.class::cast)
        .collect(toUnmodifiableList());
  }

  @Override
  public List<Layer> find(Tag... tags) {
    return layers.values()
        .stream()
        .filter(layer -> layer.hasAllTags(tags))
        .map(Layer.class::cast)
        .collect(toUnmodifiableList());
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {return true;}
    if (o == null || getClass() != o.getClass()) {return false;}
    InternalTimelineLayersManager that = (InternalTimelineLayersManager) o;
    return Objects.equal(layers, that.layers);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(layers);
  }

}
