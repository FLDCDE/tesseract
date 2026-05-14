package com.fieldcode.tesseract;

import java.time.Duration;

public interface FiniteMoment extends Moment {

  @Override
  FiniteMoment shift(Duration duration);

}
