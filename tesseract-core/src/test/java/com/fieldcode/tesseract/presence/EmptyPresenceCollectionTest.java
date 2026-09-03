package com.fieldcode.tesseract.presence;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.fieldcode.tesseract.QueryDirection.BACKWARD;
import static com.fieldcode.tesseract.QueryDirection.FORWARD;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("EmptyPresenceCollection")
class EmptyPresenceCollectionTest extends PresenceTestSupport {

  private final EmptyPresenceCollection empty = EmptyPresenceCollection.INSTANCE;

  @Test
  @DisplayName("should return null for floor, ceiling, lower and higher of any moment")
  void navigation_AnyMoment_ReturnsNull() {
    // Act & Assert: every navigation query resolves to null, since there is nothing to find
    assertThat(empty.floor(moment(0)))
        .isNull();
    assertThat(empty.ceiling(moment(0)))
        .isNull();
    assertThat(empty.lower(moment(0)))
        .isNull();
    assertThat(empty.higher(moment(0)))
        .isNull();
  }

  @Test
  @DisplayName("should report itself as empty and of size zero")
  void isEmptyAndSize_Always_EmptyAndZero() {
    // Act & Assert: the collection never holds any presences
    assertThat(empty.isEmpty())
        .isTrue();
    assertThat(empty.size())
        .isZero();
  }

  @Test
  @DisplayName("should have no presences when queried without a window")
  void presences_NoWindow_Empty() {
    // Act: iterate all presences
    var result = empty.presences();

    // Assert: there are none
    assertThat(result)
        .toIterable()
        .isEmpty();
  }

  @Test
  @DisplayName("should yield only the window's boundary presences in forward direction")
  void presences_WindowForward_BoundaryPresencesOnly() {
    // Arrange: a window from hour 2 to hour 8
    var window = interval(2, 8);

    // Act: query the presences within the window, forward
    var result = empty.presences(window, FORWARD);

    // Assert: only the lower and upper boundary presences are produced, at Locations.anywhere()
    assertThat(result)
        .toIterable()
        .containsExactly(
            presence(moment(2)),
            presence(moment(8))
        );
  }

  @Test
  @DisplayName("should yield only the window's boundary presences in backward direction")
  void presences_WindowBackward_BoundaryPresencesReversed() {
    // Arrange: a window from hour 2 to hour 8
    var window = interval(2, 8);

    // Act: query the presences within the window, backward
    var result = empty.presences(window, BACKWARD);

    // Assert: the upper boundary comes first, then the lower
    assertThat(result)
        .toIterable()
        .containsExactly(
            presence(moment(8)),
            presence(moment(2))
        );
  }

  @Test
  @DisplayName("should resolve a directed interval to the same result as its window and direction")
  void presences_DirectedInterval_MatchesWindowAndDirection() {
    // Arrange: a window from hour 2 to hour 8, directed backward
    var window = interval(2, 8);
    var directed = window.backward();

    // Act: query using the directed interval and, separately, its window and direction
    var viaDirected = empty.presences(directed).list();
    var viaWindowAndDirection = empty.presences(window, BACKWARD).list();

    // Assert: both queries produce the same presences
    assertThat(viaDirected)
        .containsExactlyElementsOf(viaWindowAndDirection);
  }

  @Test
  @DisplayName("should have no movements when queried without a window")
  void movements_NoWindow_Empty() {
    // Act: iterate all movements
    var result = empty.movements();

    // Assert: there are none
    assertThat(result)
        .toIterable()
        .isEmpty();
  }

  @Test
  @DisplayName("should yield a single movement between the window's boundaries in forward direction")
  void movements_WindowForward_SingleBoundaryMovement() {
    // Arrange: a window from hour 2 to hour 8
    var window = interval(2, 8);

    // Act: query the movements within the window, forward
    var result = empty.movements(window, FORWARD);

    // Assert: one movement from the lower to the upper boundary presence
    assertThat(result)
        .toIterable()
        .containsExactly(
            movement(presence(moment(2)), presence(moment(8)))
        );
  }

  @Test
  @DisplayName("should yield the same single boundary movement in backward direction")
  void movements_WindowBackward_SingleBoundaryMovement() {
    // Arrange: a window from hour 2 to hour 8
    var window = interval(2, 8);

    // Act: query the movements within the window, backward
    var result = empty.movements(window, BACKWARD);

    // Assert: with only one movement in the window, its origin still precedes its destination
    // chronologically, same as in the forward direction - only the traversal order would differ if there
    // were more than one
    assertThat(result)
        .toIterable()
        .containsExactly(
            movement(presence(moment(2)), presence(moment(8)))
        );
  }

  @Test
  @DisplayName("should resolve movements for a directed interval the same as its window and direction")
  void movements_DirectedInterval_MatchesWindowAndDirection() {
    // Arrange: a window from hour 2 to hour 8, directed forward
    var window = interval(2, 8);
    var directed = window.forward();

    // Act: query using the directed interval and, separately, its window and direction
    var viaDirected = empty.movements(directed).list();
    var viaWindowAndDirection = empty.movements(window, FORWARD).list();

    // Assert: both queries produce the same movements
    assertThat(viaDirected)
        .containsExactlyElementsOf(viaWindowAndDirection);
  }

  @Test
  @DisplayName("should return itself as its own snapshot")
  void snapshot_Always_ReturnsSameInstance() {
    // Act: take a snapshot
    var snapshot = empty.snapshot();

    // Assert: the snapshot is the same singleton instance
    assertThat(snapshot)
        .isSameAs(empty);
  }

  @Test
  @DisplayName("should equal itself")
  void equals_SameInstance_True() {
    // Act & Assert: the singleton is reflexively equal
    assertThat(empty)
        .isEqualTo(empty);
  }

  @Test
  @DisplayName("should equal any other empty presence collection")
  void equals_OtherEmptyPresenceCollection_True() {
    // Arrange: a different, mutable presence set that also happens to be empty
    var otherEmpty = PresenceSets.presences();

    // Act & Assert: equality is based on emptiness, not identity or concrete type
    assertThat(empty)
        .isEqualTo(otherEmpty);
  }

  @Test
  @DisplayName("should be symmetrically equal, with matching hash codes, to another empty presence collection")
  void equals_OtherEmptyPresenceCollection_SymmetricWithMatchingHashCode() {
    // Arrange: a different, mutable presence set that also happens to be empty
    var otherEmpty = PresenceSets.presences();

    // Act & Assert: equality holds in both directions and the equals/hashCode contract is honored,
    // even though the two are different concrete implementations
    assertThat(otherEmpty)
        .isEqualTo(empty);
    assertThat(empty.hashCode())
        .isEqualTo(otherEmpty.hashCode());
  }

  @Test
  @DisplayName("should not equal a non-empty presence collection")
  void equals_NonEmptyPresenceCollection_False() {
    // Arrange: a presence set with one presence
    var nonEmpty = PresenceSets.presences(presence(moment(0)));

    // Act & Assert: the empty collection is not equal to it
    assertThat(empty)
        .isNotEqualTo(nonEmpty);
  }

  @Test
  @DisplayName("should not equal an object that isn't a presence collection")
  void equals_UnrelatedType_False() {
    // Act & Assert: equality is scoped to presence collections
    assertThat(empty)
        .isNotEqualTo("not a presence collection");
  }

  @Test
  @DisplayName("should not equal null")
  void equals_Null_False() {
    // Act & Assert
    assertThat(empty)
        .isNotEqualTo(null);
  }

  @Test
  @DisplayName("should have a stable hash code across calls")
  void hashCode_CalledTwice_Stable() {
    // Act & Assert: repeated calls agree, satisfying the equals/hashCode contract for the singleton
    assertThat(empty.hashCode())
        .isEqualTo(empty.hashCode());
  }

}
