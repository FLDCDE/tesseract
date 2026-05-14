package com.fieldcode.tesseract.interval;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.fieldcode.tesseract.Constants;
import com.fieldcode.tesseract.Interval;
import com.fieldcode.tesseract.Moment;
import com.fieldcode.tesseract.moment.Moments;


final class IntervalParser {

  private static final Set<String> ALWAYS_SIGNS = Set.of(
      "always",
      Constants.INTERVAL_ALWAYS_SIGN
  );

  private IntervalParser() {
  }

  public static Interval parse(String value) {
    return tryParse(value).orElseThrow(() -> new IllegalArgumentException("Unable to parse interval:" + value));
  }

  public static Optional<Interval> tryParse(String value) {

    if (value == null) {
      return Optional.empty();
    }

    if (ALWAYS_SIGNS.contains(value)) {
      return Optional.of(Intervals.always());
    }

    return removeBrackets(value)
        .flatMap(IntervalParser::split)
        .flatMap(IntervalParser::convert);

  }

  private static Optional<String> removeBrackets(String value) {

    if (value.length() > 4 && (value.startsWith("[") || value.startsWith("(")) && value.endsWith(")")) {
      return Optional.of(value.substring(1, value.length() - 1));
    }

    return Optional.empty();
  }

  private static Optional<List<String>> split(String value) {
    var split = value.split("\\.\\.");
    if (split.length != 2) {
      return Optional.empty();
    }

    var lower = split[0].strip();
    var upper = split[1].strip();

    if (lower.length() == 0 || upper.length() == 0) {
      return Optional.empty();
    }

    return Optional.of(List.of(lower, upper));
  }

  private static Optional<Interval> convert(List<String> bounds) {
    try {
      var lower = Moments.parse(bounds.get(0));
      var upper = Moments.parse(bounds.get(1));
      return Optional.of(convert(lower, upper));
    } catch (Exception e) {
      return Optional.empty();
    }

  }

  private static Interval convert(Moment lower, Moment upper) {
    // -∞,∞ : 00 (0) -> always()
    // -∞,x : 01 (1) -> to(x)
    //  x,∞ : 10 (2) -> from(x)
    //  x,y : 11 (3) -> bounded(x,y)
    var bits = bit(upper.isFinite(), 0) | bit(lower.isFinite(), 1);

    switch (bits) {
      case 0:
        return Intervals.always();
      case 1:
        return Intervals.intervalTo(upper.getAt());
      case 2:
        return Intervals.intervalFrom(lower.getAt());
      default:
        return Intervals.interval(lower, upper);
    }

  }

  private static int bit(boolean value, int bitIndex) {
    return value ? 1 << bitIndex : 0;
  }

}
