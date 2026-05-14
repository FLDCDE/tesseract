package com.fieldcode.tesseract.container;

import java.util.Collection;

import com.fieldcode.tesseract.IntervalContainer;
import com.fieldcode.tesseract.TaggedIntervalCollection;

public class IntervalContainers {

  private IntervalContainers() {}

  /**
   * Creates a new {@link IntervalContainerBuilder}.
   *
   * @return new builder
   */
  public static IntervalContainerBuilder builder() {
    return IntervalContainerBuilder.create();
  }

  /**
   * Creates a new {@link IntervalContainer} from the given collections.
   *
   * @param collections collections to add
   * @return new container
   */
  public static IntervalContainer container(TaggedIntervalCollection... collections) {
    return IntervalContainerBuilder
        .create()
        .addAll(collections)
        .build();
  }

  /**
   * Creates a new {@link IntervalContainer} from the given collections.
   *
   * @param collections collections to add
   * @return new container
   */
  public static IntervalContainer container(Collection<TaggedIntervalCollection> collections) {
    return IntervalContainerBuilder
        .create()
        .addAll(collections)
        .build();
  }

}
