package model;

public abstract class Camera extends WorldObject {

  protected float nearClipPlane;
  protected float farClipPlane;

  public abstract TransformMatrix getProjectionMatrix();

  public float getNearClipPlane() {
    return this.nearClipPlane;
  }

  public float getFarClipPlane() {
    return this.farClipPlane;
  }
}
