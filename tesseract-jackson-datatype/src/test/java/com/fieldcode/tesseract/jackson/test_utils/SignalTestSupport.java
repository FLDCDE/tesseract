package com.fieldcode.tesseract.jackson.test_utils;

import java.time.OffsetDateTime;

import com.fieldcode.tesseract.Moment;
import com.fieldcode.tesseract.Signal;
import com.fieldcode.tesseract.moment.Moments;
import com.fieldcode.tesseract.signal.Signals;

import static java.time.temporal.ChronoUnit.DAYS;

public class SignalTestSupport {

  private final OffsetDateTime reference = OffsetDateTime.now().truncatedTo(DAYS);

  protected <K extends Comparable<K>, V> Signal<K, V> signal(K key, V value) {
    return Signals.signal(key, value);
  }

  protected Moment moment(long hours) {
    return Moments.moment(reference.plusHours(hours));
  }

}
