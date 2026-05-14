package com.fieldcode.tesseract;

import java.util.SortedSet;

public interface DirectionMatrix extends PartialDirectionMatrix {

  SortedSet<Location> getLocations();

}
