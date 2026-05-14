package com.fieldcode.tesseract.utils; // Use the same package as QuickUnion

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("QuickUnion Tests")
class QuickUnionTest {

  private QuickUnion uf;
  private final int size = 10; // Default size for many tests

  @Nested
  @DisplayName("Constructor and Factory Method Tests")
  class CreationTests {

    @Test
    @DisplayName("Constructor initializes each site as its own component")
    void constructor_initializesComponentsCorrectly() {
      uf = new QuickUnion(size);
      for (int i = 0; i < size; i++) {
        // Initially, the root of site i should be i itself
        assertThat(uf.find(i))
            .as("Site %d should initially be its own root", i)
            .isEqualTo(i);
      }
    }

    @Test
    @DisplayName("of() factory method initializes each site as its own component")
    void of_initializesComponentsCorrectly() {
      uf = QuickUnion.of(size);
      for (int i = 0; i < size; i++) {
        assertThat(uf.find(i))
            .as("Site %d should initially be its own root via of()", i)
            .isEqualTo(i);
      }
    }

    @Test
    @DisplayName("Constructor handles size 0 correctly")
    void constructor_handlesZeroSize() {
      assertThatCode(() -> new QuickUnion(0))
          .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("of() factory method handles size 0 correctly")
    void of_handlesZeroSize() {
      assertThatCode(() -> QuickUnion.of(0))
          .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("Constructor throws NegativeArraySizeException for negative size")
    void constructor_throwsForNegativeSize() {
      assertThatThrownBy(() -> new QuickUnion(-1))
          .isInstanceOf(NegativeArraySizeException.class);
    }

    @Test
    @DisplayName("of() factory method throws NegativeArraySizeException for negative size")
    void of_throwsForNegativeSize() {
      assertThatThrownBy(() -> QuickUnion.of(-1))
          .isInstanceOf(NegativeArraySizeException.class);
    }
  }

  @Nested
  @DisplayName("find() Method Tests")
  class FindTests {

    @BeforeEach
    void setupUnionFind() {
      uf = new QuickUnion(size);
      // Create some components: (0, 1, 2), (3, 4), (5) ... (9)
      uf.union(0, 1);
      uf.union(1, 2);
      uf.union(3, 4);
    }

    @Test
    @DisplayName("find returns correct root after unions")
    void find_returnsCorrectRoot() {
      int root0 = uf.find(0);
      assertThat(uf.find(1)).isEqualTo(root0);
      assertThat(uf.find(2)).isEqualTo(root0);

      int root3 = uf.find(3);
      assertThat(uf.find(4)).isEqualTo(root3);

      // Root 0 and Root 3 should be different
      assertThat(root0).isNotEqualTo(root3);

      // Sites not involved in union remain their own roots
      assertThat(uf.find(5)).isEqualTo(5);
      assertThat(uf.find(9)).isEqualTo(9);
    }

    @Test
    @DisplayName("find returns correct root for longer chains")
    void find_returnsCorrectRootChain() {
      uf = new QuickUnion(5);
      uf.union(0, 1);
      uf.union(1, 2);
      uf.union(2, 3);
      uf.union(3, 4); // 0 -> 1 -> 2 -> 3 -> 4 (root could be 4)

      int root = uf.find(4); // Get the actual root (depends on implementation detail)
      assertThat(uf.find(0)).isEqualTo(root);
      assertThat(uf.find(1)).isEqualTo(root);
      assertThat(uf.find(2)).isEqualTo(root);
      assertThat(uf.find(3)).isEqualTo(root);
    }
  }

  @Nested
  @DisplayName("union() Method Tests")
  class UnionTests {

    @BeforeEach
    void setupUnionFind() {
      uf = new QuickUnion(size);
    }

    @Test
    @DisplayName("union connects sites in different components")
    void union_connectsDifferentComponents() {
      assertThat(uf.find(1)).isNotEqualTo(uf.find(2)); // Precondition: not connected

      uf.union(1, 2);

      assertThat(uf.find(1)).isEqualTo(uf.find(2)); // Postcondition: connected
    }

    @Test
    @DisplayName("union on sites already in the same component does nothing")
    void union_onSameComponent_doesNothing() {
      uf.union(1, 2);
      int initialRoot = uf.find(1);
      int rootOf3 = uf.find(3);

      // Union within the same component (1 and 2)
      uf.union(1, 2);
      assertThat(uf.find(1)).isEqualTo(initialRoot);
      assertThat(uf.find(2)).isEqualTo(initialRoot);

      // Union within the same component (2 and 1)
      uf.union(2, 1);
      assertThat(uf.find(1)).isEqualTo(initialRoot);
      assertThat(uf.find(2)).isEqualTo(initialRoot);

      // Ensure other components were not affected
      assertThat(uf.find(3)).isEqualTo(rootOf3);
    }

    @Test
    @DisplayName("union works correctly when forming complex structures")
    void union_formsComplexStructure() {
      // (0, 1)   (2, 3)   (5, 6)   (7, 8)   (4)   (9)
      uf.union(0, 1);
      uf.union(2, 3);
      uf.union(5, 6);
      uf.union(7, 8);

      // Connect (0,1) and (2,3) -> (0, 1, 2, 3)
      uf.union(1, 3);
      int root0 = uf.find(0);
      assertThat(uf.find(1)).isEqualTo(root0);
      assertThat(uf.find(2)).isEqualTo(root0);
      assertThat(uf.find(3)).isEqualTo(root0);

      // Connect (5,6) and (7,8) -> (5, 6, 7, 8)
      uf.union(6, 8);
      int root5 = uf.find(5);
      assertThat(uf.find(6)).isEqualTo(root5);
      assertThat(uf.find(7)).isEqualTo(root5);
      assertThat(uf.find(8)).isEqualTo(root5);

      // Ensure the two large components are separate
      assertThat(root0).isNotEqualTo(root5);
      // Ensure isolated components remain isolated
      assertThat(uf.find(4)).isEqualTo(4);
      assertThat(uf.find(9)).isEqualTo(9);

      // Connect the two large components -> (0, 1, 2, 3, 5, 6, 7, 8)
      uf.union(0, 8);
      int finalRoot = uf.find(0);
      assertThat(uf.find(1)).isEqualTo(finalRoot);
      assertThat(uf.find(2)).isEqualTo(finalRoot);
      assertThat(uf.find(3)).isEqualTo(finalRoot);
      assertThat(uf.find(5)).isEqualTo(finalRoot);
      assertThat(uf.find(6)).isEqualTo(finalRoot);
      assertThat(uf.find(7)).isEqualTo(finalRoot);
      assertThat(uf.find(8)).isEqualTo(finalRoot);

      // Check isolated ones again
      assertThat(uf.find(4)).isEqualTo(4);
      assertThat(uf.find(9)).isEqualTo(9);
      assertThat(finalRoot).isNotEqualTo(4);
      assertThat(finalRoot).isNotEqualTo(9);
    }
  }


  @Nested
  @DisplayName("Implicit connected() Behavior Tests")
  class ConnectedImplicitTests {

    @BeforeEach
    void setupUnionFind() {
      uf = new QuickUnion(size);
    }

    @Test
    @DisplayName("Initially, only a site is connected to itself")
    void connected_initiallyOnlySelf() {
      for (int i = 0; i < size; i++) {
        assertThat(uf.find(i)).isEqualTo(uf.find(i));
        if (i + 1 < size) {
          assertThat(uf.find(i) == uf.find(i + 1))
              .as("Site %d should not be connected to %d initially", i, i + 1)
              .isFalse();
        }
      }
    }

    @Test
    @DisplayName("After union, sites are connected")
    void connected_afterUnion() {
      uf.union(1, 5);
      uf.union(5, 8);

      assertThat(uf.find(1) == uf.find(5)).isTrue();
      assertThat(uf.find(1) == uf.find(8)).isTrue();
      assertThat(uf.find(5) == uf.find(8)).isTrue();
    }

    @Test
    @DisplayName("Sites in different components are not connected")
    void connected_differentComponents() {
      uf.union(1, 2);
      uf.union(3, 4);

      assertThat(uf.find(1) == uf.find(3)).isFalse();
      assertThat(uf.find(2) == uf.find(4)).isFalse();
      assertThat(uf.find(0) == uf.find(1)).isFalse(); // 0 is isolated initially
    }
  }


  @Nested
  @DisplayName("Exception Handling Tests")
  class ExceptionTests {

    @BeforeEach
    void setupUnionFind() {
      // Use a smaller size for easier boundary checks
      uf = new QuickUnion(5); // Valid indices are 0, 1, 2, 3, 4
    }

    @Test
    @DisplayName("find throws IndexOutOfBoundsException for negative index")
    void find_throwsForNegativeIndex() {
      assertThatThrownBy(() -> uf.find(-1))
          .isInstanceOf(IndexOutOfBoundsException.class);
    }

    @Test
    @DisplayName("find throws IndexOutOfBoundsException for index >= size")
    void find_throwsForIndexOutOfBounds() {
      assertThatThrownBy(() -> uf.find(5)) // size is 5, so index 5 is invalid
          .isInstanceOf(IndexOutOfBoundsException.class);
    }

    @Test
    @DisplayName("union throws IndexOutOfBoundsException for negative index p")
    void union_throwsForNegativeIndexP() {
      assertThatThrownBy(() -> uf.union(-1, 3))
          .isInstanceOf(IndexOutOfBoundsException.class);
    }

    @Test
    @DisplayName("union throws IndexOutOfBoundsException for index p >= size")
    void union_throwsForIndexPOutOfBounds() {
      assertThatThrownBy(() -> uf.union(5, 3))
          .isInstanceOf(IndexOutOfBoundsException.class);
    }

    @Test
    @DisplayName("union throws IndexOutOfBoundsException for negative index q")
    void union_throwsForNegativeIndexQ() {
      assertThatThrownBy(() -> uf.union(3, -1))
          .isInstanceOf(IndexOutOfBoundsException.class); // Exception comes from find(q) -> validate(q)
    }

    @Test
    @DisplayName("union throws IndexOutOfBoundsException for index q >= size")
    void union_throwsForIndexQOutOfBounds() {
      assertThatThrownBy(() -> uf.union(3, 5))
          .isInstanceOf(IndexOutOfBoundsException.class); // Exception comes from find(q) -> validate(q)
    }
  }

}
