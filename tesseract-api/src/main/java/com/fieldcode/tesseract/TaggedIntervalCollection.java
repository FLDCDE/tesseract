package com.fieldcode.tesseract;

public interface TaggedIntervalCollection extends IntervalCollection, Taggable {

  /**
   * Returns the id of the collection.
   *
   * @return the id of the collection
   */
  String getId();

}
