package com.fieldcode.tesseract.timeline.events;

import com.fieldcode.tesseract.FluentIterator;
import com.fieldcode.tesseract.IntervalMap;

class EventHolderToString {

  static final String LINE_SEPARATOR = System.lineSeparator();

  private EventHolderToString() {
  }

  static String toString(FluentIterator<IntervalMap<InternalTimelineEvent>> iterator) {
    var layers = iterator.list();
    var out = new StringBuilder();
    for (int i = 0; i < layers.size(); i++) {
      var layer = layers.get(i);
      out.append("Layer ").append(i).append(":");
      layer.entries()
          .forEach(entry -> out.append(LINE_SEPARATOR).append(" * ").append(entry));
      out.append(LINE_SEPARATOR);
    }
    return out.toString();
  }

}
