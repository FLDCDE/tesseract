package com.fieldcode.tesseract;

import java.time.Duration;

public interface InfiniteMoment extends Moment {

  @Override
  InfiniteMoment shift(Duration duration);

  int signum();

  InfiniteMoment abs();

  InfiniteMoment negate();

}
