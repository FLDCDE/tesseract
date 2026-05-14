package com.fieldcode.tesseract.moment;

import com.fieldcode.tesseract.Moment;

import java.time.OffsetDateTime;
import java.util.Set;
import static com.fieldcode.tesseract.Constants.INFINITE_SIGN;
import static com.fieldcode.tesseract.Constants.NEGATIVE_INFINITE_SIGN;
import static com.google.common.base.Preconditions.checkNotNull;

final class MomentParser {

  private static final Set<String> INF_SIGNS = Set.of(
      "inf",
      "infinite",
      "+inf",
      "+infinite",
      INFINITE_SIGN,
      "+" + INFINITE_SIGN
  );

  private static final Set<String> NINF_SIGNS = Set.of(
      "ninf",
      "-infinite",
      "-inf",
      NEGATIVE_INFINITE_SIGN
  );

  private MomentParser() {}

  static Moment parse(String moment) {
    checkNotNull(moment, "moment cannot be null");
    var token = moment.strip();

    if (INF_SIGNS.contains(token)) {
      return Moments.inf();
    }

    if (NINF_SIGNS.contains(token)) {
      return Moments.ninf();
    }

    return Moments.moment(OffsetDateTime.parse(token));
  }

}
