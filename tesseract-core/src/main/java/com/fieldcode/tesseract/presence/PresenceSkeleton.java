package com.fieldcode.tesseract.presence;

import java.time.Duration;

import org.immutables.value.Value.Immutable;
import org.immutables.value.Value.Parameter;

import com.fieldcode.tesseract.Location;
import com.fieldcode.tesseract.Moment;
import com.fieldcode.tesseract.Presence;
import com.fieldcode.tesseract.tag.Tags;


@Immutable
abstract class PresenceSkeleton implements Presence {

  @Override
  @Parameter
  public abstract Moment getMoment();

  @Override
  @Parameter
  public abstract Location getLocation();

  @Override
  public Presence shift(Duration duration) {
    return withMoment(getMoment().shift(duration));
  }

  @Override
  public Presence move(Moment moment) {
    return withMoment(moment);
  }

  public abstract ImmutablePresence withMoment(Moment value);

  public abstract ImmutablePresence withLocation(Location value);

  @Override
  public Presence withTags(Tag... tags) {
    return withTags(Tags.holder(tags));
  }

  @Override
  public Presence mergeTags(Tag... tags) {
    var holder = Tags.builderFrom(getTags())
        .tags(tags)
        .build();
    return withTags(holder);
  }

  @Override
  @Parameter
  public abstract TagHolder getTags();

  @Override
  public String toString() {
    return
        String.format(
            "%s@%s%s",
            getLocation(),
            getMoment(),
            getTags().isEmpty() ? "" : " " + getTags()
        );
  }

}
