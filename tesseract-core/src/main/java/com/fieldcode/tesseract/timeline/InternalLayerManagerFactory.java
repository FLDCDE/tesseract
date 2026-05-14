package com.fieldcode.tesseract.timeline;

import java.util.Collection;

import static java.util.stream.Collectors.toUnmodifiableList;

class InternalLayerManagerFactory {

  private InternalLayerManagerFactory() {}

  static InternalTimelineLayersManager manager(Collection<LayerDefinition> definitions) {
    var layers = definitions.stream()
        .map(LayerFactory::layer)
        .collect(toUnmodifiableList());
    return InternalTimelineLayersManager.of(layers);
  }

}
