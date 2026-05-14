package com.fieldcode.tesseract.presence;

import com.fieldcode.tesseract.Location;
import com.fieldcode.tesseract.Moment;
import com.fieldcode.tesseract.Presence;
import com.fieldcode.tesseract.location.Locations;
import com.fieldcode.tesseract.moment.Moments;
import com.fieldcode.tesseract.test_utils.TimeTestSupport;

public class PresenceTestSupport extends TimeTestSupport {

  /**
   * Creates a presence at the given hour of the day. The location is created by the provided longitude and latitude.
   *
   * @param hours the hour of the day
   * @param longitude the longitude of the location
   * @param latitude the latitude of the location
   * @return a presence at the given hour of the day
   */
  protected Presence presence(int hours, int longitude, int latitude) {
    return Presences.presence(Moments.moment(at(hours)), Locations.location(longitude, latitude));
  }

  /**
   * Creates a presence at the given moment. The location is set to {@link Locations#anywhere()}.
   *
   * @param moment the moment
   * @return a presence at the given hour of the day
   */
  protected Presence presence(Moment moment) {
    return Presences.presence(moment, Locations.anywhere());
  }

  /**
   * Creates a presence at the given moment and location.
   *
   * @param moment the moment
   * @param location the location
   * @return a presence at the given moment and location
   */
  protected Presence presence(Moment moment, Location location) {
    return Presences.presence(moment, location);
  }

}
