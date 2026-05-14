package com.fieldcode.tesseract;

import java.time.OffsetDateTime;

public interface BoundedInterval extends Interval {

  OffsetDateTime getStart();

  OffsetDateTime getEnd();

}
