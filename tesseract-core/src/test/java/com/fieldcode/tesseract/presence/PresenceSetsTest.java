package com.fieldcode.tesseract.presence;

import com.fieldcode.tesseract.Presence;
import com.fieldcode.tesseract.PresenceSet;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("PresenceSets")
class PresenceSetsTest extends PresenceTestSupport {

  private final Presence p0 = presence(0, 1, 0);
  private final Presence p5 = presence(5, 2, 0);
  private final Presence p10 = presence(10, 3, 0);

  @Test
  @DisplayName("should create an empty presence set when no presences are given")
  void presences_NoArguments_Empty() {
    // Act: create a presence set without any presences
    var set = PresenceSets.presences();

    // Assert: the set is empty
    assertThat(set.isEmpty())
        .isTrue();
  }

  @Test
  @DisplayName("should copy presences from an iterable")
  void presences_FromIterable_CopiesPresences() {
    // Arrange: a plain list of presences
    var presences = List.of(p0, p5, p10);

    // Act: create a presence set from the iterable
    var set = PresenceSets.presences(presences);

    // Assert: the set contains the same presences
    assertThat(set.presences())
        .toIterable()
        .containsExactly(p0, p5, p10);
  }

  @Test
  @DisplayName("should copy presences from varargs")
  void presences_FromVarargs_CopiesPresences() {
    // Act: create a presence set from varargs presences
    var set = PresenceSets.presences(p0, p5, p10);

    // Assert: the set contains the same presences
    assertThat(set.presences())
        .toIterable()
        .containsExactly(p0, p5, p10);
  }

  @Test
  @DisplayName("should copy presences from a stream")
  void presences_FromStream_CopiesPresences() {
    // Arrange: a stream of presences
    var stream = Stream.of(p0, p5, p10);

    // Act: create a presence set from the stream
    var set = PresenceSets.presences(stream);

    // Assert: the set contains the same presences
    assertThat(set.presences())
        .toIterable()
        .containsExactly(p0, p5, p10);
  }

  @Test
  @DisplayName("should copy presences from another presence collection")
  void presences_FromPresenceCollection_CopiesPresences() {
    // Arrange: an existing presence set used as a presence collection
    var source = PresenceSets.presences(p0, p5, p10);

    // Act: create a new presence set from the source collection
    var set = PresenceSets.presences(source);

    // Assert: the set contains the same presences
    assertThat(set.presences())
        .toIterable()
        .containsExactly(p0, p5, p10);
  }

  @Test
  @DisplayName("should map items to presences and copy them into a new set")
  void presences_FromIterableWithMapper_MapsAndCopiesItems() {
    // Arrange: a list of hours mapped to presences at those hours
    List<Integer> hours = List.of(0, 5, 10);

    // Act: create a presence set from the items using the mapper
    var set = PresenceSets.presences(hours, hour -> presence(hour, 1, 0));

    // Assert: the set contains one mapped presence per item
    assertThat(set.presences())
        .toIterable()
        .containsExactly(
            presence(0, 1, 0),
            presence(5, 1, 0),
            presence(10, 1, 0)
        );
  }

  @Test
  @DisplayName("should create an immutable presence collection from a presence set")
  void immutable_FromPresenceSet_ReturnsImmutableCollection() {
    // Arrange: a mutable presence set with presences
    var mutable = PresenceSets.presences(p0, p5, p10);

    // Act: create an immutable presence collection from it
    var immutable = PresenceSets.immutable(mutable);

    // Assert: the immutable collection contains the same presences and rejects mutation
    assertThat(immutable.presences())
        .toIterable()
        .containsExactly(p0, p5, p10);
    assertThatThrownBy(() -> ((PresenceSet) immutable).put(p0))
        .isInstanceOf(UnsupportedOperationException.class);
  }

  @Test
  @DisplayName("should create an immutable presence collection from an iterable")
  void immutable_FromIterable_ReturnsImmutableCollection() {
    // Act: create an immutable presence collection from a list
    var immutable = PresenceSets.immutable(List.of(p0, p5, p10));

    // Assert: the immutable collection contains the same presences
    assertThat(immutable.presences())
        .toIterable()
        .containsExactly(p0, p5, p10);
  }

  @Test
  @DisplayName("should create an immutable presence collection from a stream")
  void immutable_FromStream_ReturnsImmutableCollection() {
    // Act: create an immutable presence collection from a stream
    var immutable = PresenceSets.immutable(Stream.of(p0, p5, p10));

    // Assert: the immutable collection contains the same presences
    assertThat(immutable.presences())
        .toIterable()
        .containsExactly(p0, p5, p10);
  }

  @Test
  @DisplayName("should create an immutable presence collection from varargs")
  void immutable_FromVarargs_ReturnsImmutableCollection() {
    // Act: create an immutable presence collection from varargs presences
    var immutable = PresenceSets.immutable(p0, p5, p10);

    // Assert: the immutable collection contains the same presences
    assertThat(immutable.presences())
        .toIterable()
        .containsExactly(p0, p5, p10);
  }

  @Test
  @DisplayName("should return the same singleton instance for the empty presence set")
  void empty_CalledTwice_ReturnsSameInstance() {
    // Act: obtain the empty presence set twice
    var first = PresenceSets.empty();
    var second = PresenceSets.empty();

    // Assert: both calls return the same singleton instance
    assertThat(first)
        .isSameAs(second);
    assertThat(first.isEmpty())
        .isTrue();
  }

}
