package model.objects;

import model.math.TransformMatrix;

public abstract class WorldObject {

  TransformMatrix transform;

  public WorldObject() {
    this.transform = new TransformMatrix();
  }

  public TransformMatrix getTransform() {
    return this.transform;
  }

  public void transform(TransformMatrix transformation) {
    this.transform = this.transform.multiply(transformation);
  }
}
