package model;

public class Matrix {

  final float[][] values;

  public Matrix(int rows, int columns) {
    this.values = new float[rows][columns];
  }

  public Matrix(float[][] values) {
    this.values = values;
  }

  public Matrix(Matrix copyValues) {
    this.values = new float[copyValues.rows()][copyValues.columns()];
    for(int row = 0; row < this.rows(); row++) {
      for(int column = 0; column < this.columns(); column++) {
        this.assign(row, column, copyValues.get(row, column));
      }
    }
  }

  public Matrix multiply(Matrix other) throws IllegalArgumentException {
    if(this.columns() != other.rows()) {
      throw new IllegalArgumentException("Cannot multiply [" + this.rows() + ", " + this.columns() + "] matrix by ["
      + other.rows() + ", " + other.columns() + "] matrix");
    }

    Matrix returnMatrix = new Matrix(this.rows(), other.columns());

    for(int row = 0; row < this.rows(); row++) {
      for(int column = 0; column < other.columns(); column++) {
        float value = 0;
        for(int i = 0; i < this.columns(); i++) {
          value += this.get(row, i) * other.get(i, column);
        }
        returnMatrix.assign(row, column, value);
      }
    }

    return returnMatrix;
  }

  public Matrix add(Matrix other) throws IllegalArgumentException {
    if(this.rows() != other.rows() || this.columns() != other.columns()) {
      throw new IllegalArgumentException("Cannot add matrices of different dimensions");
    }

    Matrix returnMatrix = new Matrix(this.rows(), this.columns());

    for(int row = 0; row < this.rows(); row++) {
      for (int column = 0; column < this.columns(); column++) {
        returnMatrix.assign(row, column, this.get(row, column) + other.get(row, column));
      }
    }

    return returnMatrix;
  }

  public int rows() { return this.values.length; };

  public int columns() { return this.values[0].length; };

  public float get(int row, int column) {
    return this.values[row][column];
  }

  public void assign(int row, int column, float value) throws IllegalArgumentException {
    if(row < 0 || row >= this.rows() || column < 0 || column >= this.columns()) {
      throw new IllegalArgumentException("(" + row + ", " + column + ") outside bounds of matrix "
      + "of size [" + this.rows() + ", " + this.columns() + "]");
    }
    this.values[row][column] = value;
  }

  public void assignRow(int row, int[] values) throws IllegalArgumentException {
    if(row < 0 || row >= this.rows()) {
      throw new IllegalArgumentException("Row " + row + " outside bounds of matrix");
    }
    for(int i = 0; i < values.length; i++) {
      this.assign(row, i, values[i]);
    }
  }

  public void assignColumn(int column, int[] values) throws IllegalArgumentException {
    if(column < 0 || column >= this.columns()) {
      throw new IllegalArgumentException("Column " + column + " outside bounds of matrix");
    }
    for(int i = 0; i < values.length; i++) {
      this.assign(i, column, values[i]);
    }
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    builder.append("| ");
    for(int row = 0; row < this.rows(); row++) {
      for (int column = 0; column < this.columns(); column++) {
        builder.append(this.get(row, column));
        builder.append(" ");
      }
      if(row != this.rows() - 1) {
        builder.append("|\n| ");
      }
    }
    builder.append("|");
    return builder.toString();
  }
}
