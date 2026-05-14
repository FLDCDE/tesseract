package com.fieldcode.tesseract.timeline;

import com.fieldcode.tesseract.Interval;
import com.fieldcode.tesseract.Taggable;

public interface TimelineEvent extends Taggable {

  TagHolder getTags();

  Interval getInterval();

}
