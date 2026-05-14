package com.fieldcode.tesseract.movement;

import com.fieldcode.tesseract.Location;
import com.fieldcode.tesseract.Moment;
import com.fieldcode.tesseract.Movement;
import com.fieldcode.tesseract.Presence;
import com.fieldcode.tesseract.presence.Presences;

public class Movements {

  private Movements() {}

  public static Movement movement(Presence origin, Presence destination) {
    return ImmutableMovement.of(origin, destination);
  }

  public static Movement movement(Moment start, Location startLocation, Moment end, Location endLocation) {
    var origin = Presences.presence(start, startLocation);
    var destination = Presences.presence(end, endLocation);
    return ImmutableMovement.of(origin, destination);
  }

}
