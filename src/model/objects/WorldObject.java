package model.objects;

import model.math.Transform;
import model.math.TransformMatrix;

public abstract class WorldObject {

  Transform transform;

  public WorldObject() {
    this.transform = new Transform();
  }

  public Transform getTransform() {
    return this.transform;
  }
}
