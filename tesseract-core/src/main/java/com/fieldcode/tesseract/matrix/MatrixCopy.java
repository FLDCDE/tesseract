package com.fieldcode.tesseract.matrix;

class MatrixCopy {

  private final long[][] matrix;

  private MatrixCopy(long[][] matrix) {
    this.matrix = matrix;
  }

  static long[][] copy(long[][] matrix) {
    return new MatrixCopy(matrix).copy();
  }

  private long[][] copy() {

    var dimensions = MatrixDimensions.create(matrix);

    var rows = dimensions.getRows();
    var columns = dimensions.getColumns();

    var targetMatrix = new long[rows][columns];

    for (int i = 0; i < rows; i++) {
      System.arraycopy(matrix[i], 0, targetMatrix[i], 0, columns);
    }

    return targetMatrix;
  }

}
