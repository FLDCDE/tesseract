package com.fieldcode.tesseract.timeline;

import com.fieldcode.tesseract.Taggable.TagHolder;

public interface TimelineLayersManager extends TimelineLayers {

  TimelineLayersManager addLayer(String id, LayerType type, TagHolder tags);

  TimelineLayersManager removeLayer(String id);

  TimelineLayersManager duplicateLayer(String id, String duplicateId);

}
