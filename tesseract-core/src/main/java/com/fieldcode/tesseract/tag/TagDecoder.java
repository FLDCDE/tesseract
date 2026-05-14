package com.fieldcode.tesseract.tag;

import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.commons.text.StringEscapeUtils;

import com.fieldcode.tesseract.Taggable.Tag;
import com.fieldcode.tesseract.Taggable.TagHolder;

class TagDecoder {

  public static final Pattern QUOTED_LIST = Pattern.compile("\"((?:[^\"\\\\]|\\\\.)*)\"(:\"((?:[^\"\\\\]|\\\\.)*)\")?");

  private TagDecoder() {
  }

  static String asString(TagHolder holder) {
    return holder.getTags()
        .stream()
        .map(TagDecoder::asString)
        .collect(Collectors.joining(",", "[", "]"));
  }

  static String asString(Tag tag) {
    var name = tag.getName();
    return tag.getValue()
        .map(value -> quote(name, value))
        .orElseGet(() -> quote(name));
  }

  static String quote(String name, String value) {
    return String.format("%s:%s", quote(name), quote(value));
  }

  static String quote(String value) {
    return String.format("\"%s\"", escape(value));
  }

  static Tag parseTag(String value) {
    var matcher = QUOTED_LIST.matcher(value.strip());
    if (!matcher.find()) {
      throw new IllegalArgumentException("Invalid quoted string: " + value);
    }

    if (matcher.group(3) != null) {
      return Tags.tag(unescape(matcher.group(1)), unescape(matcher.group(3)));
    }
    return Tags.tag(unescape(matcher.group(1)));
  }

  static String escape(String value) {
    return StringEscapeUtils.escapeJava(value);
  }

  static String unescape(String value) {
    return StringEscapeUtils.unescapeJava(value);
  }

  static TagHolder parseHolder(String value) {
    var builder = Tags.builder();
    var strip = value.strip();
    var matcher = QUOTED_LIST.matcher(strip);

    while (matcher.find()) {
      if (matcher.group(3) != null) {
        builder.tag(unescape(matcher.group(1)), unescape(matcher.group(3)));
      } else {
        builder.tag(unescape(matcher.group(1)));
      }
    }
    var tags = builder.build();

    if (tags.isEmpty() && !value.isBlank() && !strip.equals("[]")) {
      throw new IllegalArgumentException("Invalid tag holder: " + value);
    }
    return tags;
  }

}
