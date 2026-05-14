package com.fieldcode.tesseract.presence;

import java.util.Optional;
import java.util.regex.Pattern;

import com.fieldcode.tesseract.Presence;
import com.fieldcode.tesseract.Taggable.TagHolder;
import com.fieldcode.tesseract.location.Locations;
import com.fieldcode.tesseract.moment.Moments;
import com.fieldcode.tesseract.tag.Tags;


final class PresenceParser {

  private static final Pattern HAS_TAGS = Pattern.compile(".*@.* (\\[.*\\])");

  private PresenceParser() {
  }

  static Presence parse(String value) {
    return tryParse(value).orElseThrow(
        () -> new IllegalArgumentException("Unable to parse: " + value)
    );
  }

  static Optional<Presence> tryParse(String value) {

    var tags = getTags(value);
    var split = value.split("@");

    if (split.length != 2) {
      return Optional.empty();
    }

    var location = Locations.tryParse(split[0]);

    if (location.isEmpty()) {
      return Optional.empty();
    }

    var moment = Moments.parse(split[1].split(" ")[0]);

    if (moment == null) {
      return Optional.empty();
    }

    try {
      return Optional.of(Presences.presence(moment, location.get(), tags));
    } catch (Exception e) {
      // NOOP
    }

    return Optional.empty();
  }

  private static TagHolder getTags(String value) {
    var matcher = HAS_TAGS.matcher(value);
    if (matcher.find()) {
      var tags = matcher.group(0);
      return Tags.parseHolder(tags);
    }

    return Tags.holder();
  }


}
