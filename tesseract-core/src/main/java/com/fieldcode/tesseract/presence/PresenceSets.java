package com.fieldcode.tesseract.presence;

import com.fieldcode.tesseract.Presence;
import com.fieldcode.tesseract.PresenceCollection;
import com.fieldcode.tesseract.PresenceSet;

import java.util.Arrays;
import java.util.function.Function;
import java.util.stream.Stream;

public class PresenceSets {

  private static final PresenceCollection EMPTY_INSTANCE = EmptyPresenceCollection.INSTANCE;

  private PresenceSets() {}

  /**
   * Creates a new, empty, mutable presence set. Use this as the starting point when presences are added incrementally
   * over time (e.g. via {@link PresenceSet#put}), rather than known upfront as a batch.
   *
   * @return a new, empty presence set
   */
  public static PresenceSet presences() {
    return TreePresenceSet.of();
  }

  /**
   * Copies the given presences into a new, mutable presence set. Prefer this overload when the presences are already
   * held in an in-memory, re-iterable source (a {@code List}, {@code Set}, etc.) rather than a one-shot
   * {@link Stream}.
   *
   * @param presences the presence iterable to copy
   * @return a new presence set containing the given presences
   */
  public static PresenceSet presences(Iterable<Presence> presences) {
    var set = TreePresenceSet.of();
    presences.forEach(set::put);
    return set;
  }

  /**
   * Copies the given presences into a new, mutable presence set. Convenience overload for a small, literal set of
   * presences (e.g. in test setup), where wrapping them in a {@code List} or array first would just add noise.
   *
   * @param presences the presences to copy
   * @return a new presence set containing the given presences
   */
  public static PresenceSet presences(Presence... presences) {
    var set = TreePresenceSet.of();
    for (var presence : presences) {
      set.put(presence);
    }
    return set;
  }

  /**
   * Copies the given presences into a new, mutable presence set. Prefer this overload when the presences are
   * produced by a stream pipeline (e.g. filtered or transformed from another source) rather than already collected;
   * the stream is consumed exactly once by this call.
   *
   * @param presences the presence stream to copy
   * @return a new presence set containing the given presences
   */
  public static PresenceSet presences(Stream<Presence> presences) {
    var set = TreePresenceSet.of();
    presences.forEach(set::put);
    return set;
  }

  /**
   * Copies the given presence collection into a new, mutable presence set. Use this to obtain a mutable working copy
   * of another {@link PresenceCollection} (for instance, an immutable one returned by {@link #immutable}), leaving
   * the original untouched.
   *
   * @param presences the presence collection to copy
   * @return a new presence set containing the given presences
   */
  public static PresenceSet presences(PresenceCollection presences) {
    var set = TreePresenceSet.of();
    presences.presences().forEach(set::put);
    return set;
  }

  /**
   * Maps the given items to presences and collects them into a new, mutable presence set. Use this overload when
   * starting from domain items that aren't presences yet, instead of mapping them to presences yourself before
   * calling one of the other {@code presences} overloads.
   *
   * @param items the items to map to presences
   * @param mapper the function that maps each item to a presence
   * @param <T> the type of the items
   * @return a new presence set containing one mapped presence per item
   */
  public static <T> PresenceSet presences(Iterable<T> items, Function<T, Presence> mapper) {
    var set = TreePresenceSet.of();
    items.forEach(item -> set.put(mapper.apply(item)));
    return set;
  }

  /**
   * Creates an immutable snapshot of the given presence set. Use this to freeze a mutable set once it's been built
   * up (e.g. via {@link #presences()} and {@link PresenceSet#put}), so it can be shared or returned without callers
   * being able to mutate it; later changes to the source set have no effect on the returned collection.
   *
   * @param presences the presence collection to copy
   * @return a new immutable presence collection
   */
  public static PresenceCollection immutable(PresenceSet presences) {
    return ImmutableTreePresenceSet.of(presences);
  }

  /**
   * Creates an immutable presence collection from the given presences. Prefer this overload when the presences are
   * already held in an in-memory, re-iterable source rather than a one-shot {@link Stream}.
   *
   * @param presences the presences to copy
   * @return a new immutable presence collection
   */
  public static PresenceCollection immutable(Iterable<Presence> presences) {
    return ImmutableTreePresenceSet.of(presences);
  }

  /**
   * Creates an immutable presence collection from the given presence stream. Prefer this overload when the presences
   * are produced by a stream pipeline rather than already collected; the stream is consumed exactly once by this
   * call.
   *
   * @param presences the presence stream to copy
   * @return a new immutable presence collection
   */
  public static PresenceCollection immutable(Stream<Presence> presences) {
    return ImmutableTreePresenceSet.of(presences);
  }

  /**
   * Creates an immutable presence collection from the given presences. Convenience overload for a small, literal set
   * of presences, where wrapping them in a {@code List} or array first would just add noise.
   *
   * @param presences the presences to copy
   * @return a new immutable presence collection
   */
  public static PresenceCollection immutable(Presence... presences) {
    return ImmutableTreePresenceSet.of(Arrays.stream(presences));
  }

  /**
   * Returns the empty immutable presence set. Prefer this over calling {@link #immutable(Presence...)} with no
   * arguments: it returns a cached singleton instead of allocating a new instance.
   *
   * @return the singleton empty presence set
   */
  public static PresenceCollection empty() {
    return EMPTY_INSTANCE;
  }

}
