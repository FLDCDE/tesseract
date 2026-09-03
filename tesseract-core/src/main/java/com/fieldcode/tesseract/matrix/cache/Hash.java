package com.fieldcode.tesseract.matrix.cache;

import com.fieldcode.tesseract.Location;
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

  private static String getIdentifier(SortedSet<Location> locations) {
    return locations + "->" + locations;
  }

}
