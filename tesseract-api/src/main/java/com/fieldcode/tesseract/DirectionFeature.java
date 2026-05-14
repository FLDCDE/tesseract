package com.fieldcode.tesseract;

import java.time.Duration;

public interface DirectionFeature extends Direction {

  Distance getDistance();

  Duration getDuration();

}
