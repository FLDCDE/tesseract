package com.fieldcode.tesseract.timeline;

import java.util.List;
import java.util.Set;

import com.fieldcode.tesseract.Taggable.Tag;

public interface TimelineLayers {

  /**
   * Get all layer ids.
   * @return set of layer ids
   */
  Set<String> getIds();

  /**
   * Get type of the layer.
   * @param layerId layer id
   * @return type of the layer
   */
  LayerType getType(String layerId);

  /**
   * Returns all layers.
   * @return list of layers
   */
  List<Layer> all();

  /**
   * Find layers with given tags. All tags must be present in the layer tags.
   * @param tag tags to search for
   * @return list of layers
   */
  List<Layer> find(Tag... tag);

}
