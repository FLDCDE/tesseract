package com.fieldcode.tesseract.presence;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

import com.fieldcode.tesseract.Presence;
import com.fieldcode.tesseract.PresenceCollection;
import com.fieldcode.tesseract.PresenceSet;

public class PresenceSets {

  private static final ImmutableTreePresenceSet EMPTY_INSTANCE = ImmutableTreePresenceSet.of(List.of());

  private PresenceSets() {}

  /**
   * Creates a new empty presence set.
   *
   * @return a new presence set
   */
  public static PresenceSet presences() {
    return TreePresenceSet.of();
  }

  /**
   * Creates a new presence set from the given presence collection.
   *
   * @param presences the presence collection to copy
   * @return a new presence set
   */
  public static PresenceSet presences(Collection<Presence> presences) {
    var set = TreePresenceSet.of();
    presences.forEach(set::put);
    return set;
  }

  /**
   * Creates a new presence set from the given presences.
   *
   * @param presences the presences to copy
   * @return a new presence set
   */
  public static PresenceSet presences(Presence... presences) {
    var set = TreePresenceSet.of();
    for (var presence : presences) {
      set.put(presence);
    }
    return set;
  }

  /**
   * Creates a new presence set from the given presence stream.
   *
   * @param presences the presence stream to copy
   * @return a new presence set
   */
  public static PresenceSet presences(Stream<Presence> presences) {
    var set = TreePresenceSet.of();
    presences.forEach(set::put);
    return set;
  }

  /**
   * Creates a new presence set from the given presence set.
   *
   * @param presences the presence set to copy
   * @return a new presence set
   */
  public static PresenceSet presences(PresenceSet presences) {
    var set = TreePresenceSet.of();
    presences.presences().forEach(set::put);
    return set;
  }

  /**
   * Creates a new immutable presence set from the given presence collection.
   *
   * @param presences the presence collection to copy
   * @return a new immutable presence set
   */
  public static PresenceCollection immutable(PresenceSet presences) {
    return ImmutableTreePresenceSet.of(presences);
  }

  /**
   * Creates a new immutable presence set from the given presences.
   *
   * @param presences the presences to copy
   * @return a new immutable presence set
   */
  public static PresenceCollection immutable(Iterable<Presence> presences) {
    return ImmutableTreePresenceSet.of(presences);
  }

  /**
   * Creates a new immutable presence set from the given presence stream.
   *
   * @param presences the presence stream to copy
   * @return a new immutable presence set
   */
  public static PresenceCollection immutable(Stream<Presence> presences) {
    return ImmutableTreePresenceSet.of(presences);
  }

  /**
   * Creates a new immutable presence set from the given presence stream.
   *
   * @param presences the presence stream to copy
   * @return a new immutable presence set
   */
  public static PresenceCollection immutable(Presence... presences) {
    return ImmutableTreePresenceSet.of(Arrays.stream(presences));
  }

  /**
   * Creates a new empty immutable presence. The instance is singleton.
   *
   * @return a new empty immutable presence
   */
  public static PresenceSet empty() {
    return EMPTY_INSTANCE;
  }

}
