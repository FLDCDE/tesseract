package com.fieldcode.tesseract.timeline.events;

import com.fieldcode.tesseract.Taggable.TagHolder;

interface InternalTimelineEvent {

  TagHolder getTags();

  boolean isUndefined();

}
