package com.fieldcode.tesseract.matrix.cache;

import com.fieldcode.tesseract.Location;
import com.fieldcode.tesseract.PartialDirectionMatrix;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;

import java.util.SortedSet;
import java.util.function.Function;

import static java.nio.charset.StandardCharsets.UTF_8;

class Hash {

  private Hash() {
  }

  public static final HashFunction HASH_FUNCTION = Hashing.sha256();
  public static final Function<String, String> SHA256_HASH = s -> HASH_FUNCTION.hashBytes(s.getBytes(UTF_8)).toString();

  static String hash(SortedSet<Location> locations) {
    var joined = getIdentifier(locations);
    return SHA256_HASH.apply(joined);
  }

  static String hash(PartialDirectionMatrix matrix) {
    var locations = getIdentifier(matrix.getOrigins(), matrix.getDestinations());
    return SHA256_HASH.apply(locations);
  }

  private static String getIdentifier(SortedSet<Location> origins, SortedSet<Location> destinations) {
    return origins + "->" + destinations;
  }

  private static String getIdentifier(SortedSet<Location> locations) {
    return locations + "->" + locations;
  }

}
