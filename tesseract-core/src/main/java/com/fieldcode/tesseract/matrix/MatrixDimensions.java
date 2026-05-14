package com.fieldcode.tesseract.matrix;

public class MatrixDimensions {

  private final int columns;
  private final int rows;

  private MatrixDimensions(long[][] matrix) {
    rows = matrix.length;
    columns = rows == 0 ? 0 : matrix[0].length;
  }

  public static MatrixDimensions create(long[][] matrix) {
    return new MatrixDimensions(matrix);
  }

  public int getColumns() {
    return columns;
  }

  public int getRows() {
    return rows;
  }

  public boolean isNotSquare() {
    return !isSquare();
  }

  public boolean isSquare() {
    return columns == rows;
  }

  public boolean isEmpty() {
    return rows == 0;
  }

}
