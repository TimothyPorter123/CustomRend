package model.math;

import model.Matrix;

public class TransformMatrix extends Matrix {

  public TransformMatrix() {
    super(4, 4);
    this.assign(0, 0, 1);
    this.assign(1, 1, 1);
    this.assign(2, 2, 1);
    this.assign(3, 3, 1);
  }

  public TransformMatrix(float[][] values) throws IllegalArgumentException {
    super(values);
    if(values.length != 4 || values[0].length != 4) {
      throw new IllegalArgumentException("Transform matrix must be 4x4");
    }
  }

  public TransformMatrix(Matrix values) {
    super(values);
    if(values.rows() != 4 || values.columns() != 4) {
      throw new IllegalArgumentException("Transform matrix must be 4x4");
    }
  }


  public TransformMatrix multiply(TransformMatrix other) throws IllegalArgumentException {

    TransformMatrix returnMatrix = new TransformMatrix();

    for(int row = 0; row < 4; row++) {
      for(int column = 0; column < 4; column++) {
        float value = 0;
        for(int i = 0; i < 4; i++) {
          value += this.get(row, i) * other.get(i, column);
        }
        returnMatrix.assign(row, column, value);
      }
    }
    return returnMatrix;
  }

  public Vector3 apply(Vector3 vector) {
    Matrix vectorMatrix = new Matrix(4, 1);
    vectorMatrix.assign(0, 0, vector.x);
    vectorMatrix.assign(1, 0, vector.y);
    vectorMatrix.assign(2, 0, vector.z);
    vectorMatrix.assign(3, 0, 1);
    Matrix returnVectorMatrix = this.multiply(vectorMatrix);
    return new Vector3(returnVectorMatrix.get(0, 0), returnVectorMatrix.get(1, 0),
            returnVectorMatrix.get(2, 0)).scale(1 / returnVectorMatrix.get(3, 0));
  }

  public TransformMatrix inverse() {
    TransformMatrix inverse = new TransformMatrix(this);
    for(int row = 0; row < 3; row++) {
      for (int col = 0; col < 3; col++) {
        inverse.assign(row, col, this.get(col, row));
      }
    }
    Vector3 t = new Vector3(this.get(0, 3), this.get(1, 3), this.get(2, 3));
    for(int row = 0; row < 3; row++) {
      Vector3 rowVector = new Vector3(inverse.get(row, 0), inverse.get(row, 1), inverse.get(row, 2));
      inverse.assign(row, 3, -Vector3.dot(rowVector, t));
    }
    return inverse;
  }


  public TransformMatrix move(Vector3 displacement) {
    TransformMatrix newMatrix = new TransformMatrix();
    newMatrix.assign(0, 3, displacement.x);
    newMatrix.assign(1, 3, displacement.y);
    newMatrix.assign(2, 3, displacement.z);
    return this.multiply(newMatrix);
  }

  public TransformMatrix scale(Vector3 scaleFactors) {
    TransformMatrix scaleMatrix = new TransformMatrix();
    scaleMatrix.assign(0, 0, this.get(0, 0) * scaleFactors.x);
    scaleMatrix.assign(1, 1, this.get(1, 1) + scaleFactors.y);
    scaleMatrix.assign(2, 2, this.get(2, 2) + scaleFactors.z);
    return this.multiply(scaleMatrix);
  }

  public TransformMatrix rotate(Vector3 eulerAngles) {
    Vector3 radianAngles = eulerAngles.scale((float)Math.PI / 180.0f);
    return this.rotateAboutZ(radianAngles.z).rotateAboutY(radianAngles.y).rotateAboutX(radianAngles.x);
  }

  private TransformMatrix rotateAboutX(float radianAngle) {
    TransformMatrix rotationMatrix = new TransformMatrix();
    rotationMatrix.assign(1, 1, (float)Math.cos(radianAngle));
    rotationMatrix.assign(2, 2, (float)Math.cos(radianAngle));

    rotationMatrix.assign(1, 2, (float)-Math.sin(radianAngle));
    rotationMatrix.assign(2, 1, (float)Math.sin(radianAngle));

    return this.multiply(rotationMatrix);
  }

  private TransformMatrix rotateAboutY(float radianAngle) {
    TransformMatrix rotationMatrix = new TransformMatrix();
    rotationMatrix.assign(0, 0, (float)Math.cos(radianAngle));
    rotationMatrix.assign(2, 2, (float)Math.cos(radianAngle));

    rotationMatrix.assign(2, 0, (float)-Math.sin(radianAngle));
    rotationMatrix.assign(0, 2, (float)Math.sin(radianAngle));

    return this.multiply(rotationMatrix);
  }

  private TransformMatrix rotateAboutZ(float radianAngle) {
    TransformMatrix rotationMatrix = new TransformMatrix();
    rotationMatrix.assign(0, 0, (float)Math.cos(radianAngle));
    rotationMatrix.assign(1, 1, (float)Math.cos(radianAngle));

    rotationMatrix.assign(0, 1, (float)-Math.sin(radianAngle));
    rotationMatrix.assign(1, 0, (float)Math.sin(radianAngle));

    return this.multiply(rotationMatrix);
  }
}
