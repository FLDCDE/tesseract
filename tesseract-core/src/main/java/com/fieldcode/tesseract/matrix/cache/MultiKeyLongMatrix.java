package com.fieldcode.tesseract.matrix.cache;

import com.fieldcode.tesseract.util.IndexedSortedSet;

import java.util.Collection;
import java.util.SortedSet;
import java.util.TreeSet;


public class MultiKeyLongMatrix<K extends Comparable<K>> {

  private final long[][] matrix;
  private final IndexedSortedSet<K> rows;
  private final IndexedSortedSet<K> columns;

  private MultiKeyLongMatrix(SortedSet<K> rows, SortedSet<K> columns) {
    this.rows = IndexedSortedSet.of(rows);
    this.columns = IndexedSortedSet.of(columns);
    this.matrix = new long[rows.size()][columns.size()];
  }

  public static <T extends Comparable<T>> MultiKeyLongMatrix<T> of(SortedSet<T> rows, SortedSet<T> columns) {
    return new MultiKeyLongMatrix<>(rows, columns);
  }

  public static <T extends Comparable<T>> MultiKeyLongMatrix<T> of(Collection<T> rows, Collection<T> columns) {
    return new MultiKeyLongMatrix<>(new TreeSet<>(rows), new TreeSet<>(columns));
  }

  public static <T extends Comparable<T>> MultiKeyLongMatrix<T> of(Collection<T> keys) {
    var sortedKeys = new TreeSet<>(keys);
    return new MultiKeyLongMatrix<>(sortedKeys, sortedKeys);
  }

  public void set(K row, K column, long value) {
    matrix[rows.indexOf(row)][columns.indexOf(column)] = value;
  }

  public long get(K row, K column) {
    return matrix[rows.indexOf(row)][columns.indexOf(column)];
  }

  public long[][] getMatrix() {
    return matrix;
  }

}
