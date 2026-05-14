package com.fieldcode.tesseract.utils;

import java.util.Objects;

/**
 * Implements the <em>quick-union</em> algorithm for the
 * <em>union-find</em> data structure (also known as disjoint-set data structure).
 * This algorithm is often presented in the context of the work by Robert Sedgewick and Kevin Wayne.
 * <p>
 * This data structure supports operations on a collection of disjoint sets. Each set is represented by a tree, where each element points to its parent. The root of the tree serves
 * as the identifier for the set (component).
 * </p>
 * <p>
 * This implementation uses an integer array {@code parent[]} where {@code parent[i]} stores the parent of element {@code i}. If {@code parent[i] == i}, then {@code i} is a root.
 * </p>
 * <p>
 * The {@code find} operation determines the set identifier (root) for a given element by traversing the parent pointers upwards. The {@code union} operation merges two sets by
 * making the root of one tree point to the root of the other.
 * </p>
 * <p>
 * The time complexity for the constructor is O(n). In the worst case, the {@code find} and {@code union} operations can take O(n) time if the tree becomes tall and skinny. This
 * implementation does not include optimizations like weighting or path compression.
 * </p>
 * <p>
 * Note: This implementation does not track the number of disjoint sets. To check connectivity between sites {@code p} and {@code q}, one must manually compare the results of
 * {@code find(p) == find(q)}.
 * </p>
 *
 * @see <a href="https://algs4.cs.princeton.edu/15uf">Section 1.5</a> of <i>Algorithms, 4th Edition</i> by Robert Sedgewick and Kevin Wayne
 */
public class QuickUnion {

  /**
   * Stores the total number of sites.
   */
  private final int n;

  /**
   * parent[i] = parent of site i. If parent[i] == i, then i is a root.
   */
  private final int[] parent;

  /**
   * Initializes a union-find data structure with {@code n} sites {@code 0} through {@code n-1}. Each site is initially in its own component.
   *
   * @param n the number of sites
   * @throws NegativeArraySizeException if {@code n} is negative
   */
  public QuickUnion(int n) {
    parent = new int[n];
    this.n = n;
    for (int i = 0; i < n; i++) {
      parent[i] = i;
    }
  }

  /**
   * Static factory method to create a new {@code QuickUnion} instance. Initializes a union-find data structure with {@code n} sites {@code 0} through {@code n-1}.
   *
   * @param n the number of sites
   * @return a new {@code QuickUnion} instance with {@code n} sites.
   * @throws NegativeArraySizeException if {@code n} is negative
   */
  public static QuickUnion of(int n) {
    return new QuickUnion(n);
  }

  /**
   * Validates that the given site index {@code p} is within the valid range [0, n).
   *
   * @param p the site index to validate
   * @throws IndexOutOfBoundsException if {@code p} is not a valid index ({@code p < 0 || p >= n})
   */
  private void validate(int p) {
    Objects.checkIndex(p, n);
  }

  /**
   * Returns the identifier (root) of the component containing site {@code p}. Traverses the parent pointers until the root is reached.
   *
   * @param p the integer representing the site
   * @return the component identifier (root) for the site {@code p}
   * @throws IndexOutOfBoundsException if {@code p} is not a valid index
   */
  public int find(int p) {
    validate(p);
    int current = p;
    while (current != parent[current]) {
      current = parent[current];
    }
    return current;
  }

  /**
   * Merges the component containing site {@code p} with the component containing site {@code q}.
   * <p>
   * It finds the roots of both {@code p} and {@code q}. If they are different, it makes the root of {@code p}'s component point to the root of {@code q}'s component. If they are
   * already in the same component, this method does nothing.
   * </p>
   *
   * @param p the integer representing one site
   * @param q the integer representing the other site
   * @throws IndexOutOfBoundsException if {@code p} or {@code q} are not valid indices
   */
  public void union(int p, int q) {
    int rootP = find(p);
    int rootQ = find(q);

    if (rootP == rootQ) {
      return;
    }

    parent[rootP] = rootQ;
  }

}
