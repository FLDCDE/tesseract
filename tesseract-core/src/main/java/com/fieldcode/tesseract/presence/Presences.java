package com.fieldcode.tesseract.presence;

import java.time.OffsetDateTime;
import java.util.Set;

import com.fieldcode.tesseract.Location;
import com.fieldcode.tesseract.Moment;
import com.fieldcode.tesseract.Presence;
import com.fieldcode.tesseract.Taggable.Tag;
import com.fieldcode.tesseract.Taggable.TagHolder;
import com.fieldcode.tesseract.location.Locations;
import com.fieldcode.tesseract.moment.Moments;
import com.fieldcode.tesseract.tag.ImmutableTagHolder;
import com.fieldcode.tesseract.tag.Tags;


public final class Presences {

  private Presences() {}

  public static Presence parse(String presence) {
    return PresenceParser.parse(presence);
  }

  public static Presence presence(Moment at, double latitude, double longitude) {
    return presence(at, Locations.location(latitude, longitude));
  }

  public static Presence presence(Moment at, String locationCode) {
    return presence(at, Locations.parse(locationCode));
  }

  public static Presence presence(OffsetDateTime at, Location location) {
    return presence(Moments.moment(at), location);
  }

  public static Presence presence(Moment at, Location location) {
    return ImmutablePresence.of(at, location, Tags.holder());
  }

  public static Presence presence(Moment at, Location location, Set<Tag> tags) {
    return ImmutablePresence.of(at, location, ImmutableTagHolder.of(tags));
  }

  public static Presence presence(Moment at, Location location, TagHolder tags) {
    return ImmutablePresence.of(at, location, tags);
  }

}
