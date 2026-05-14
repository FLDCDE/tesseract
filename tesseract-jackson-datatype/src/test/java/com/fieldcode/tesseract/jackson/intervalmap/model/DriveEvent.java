package com.fieldcode.tesseract.jackson.intervalmap.model;

import com.fieldcode.tesseract.Location;

public interface DriveEvent extends Event {

  Location getOrigin();

  Location getDestination();

}
