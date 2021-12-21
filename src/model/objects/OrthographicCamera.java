package model.objects;

import model.math.TransformMatrix;
import model.objects.Camera;

public class OrthographicCamera extends Camera {

  float orthographicWidth;
  float orthographicHeight;

  TransformMatrix projectionMatrix;

  public OrthographicCamera(float width, float height, float near, float far) {
    super();
    this.orthographicWidth = width;
    this.orthographicHeight = height;
    this.nearClipPlane = near;
    this.farClipPlane = far;
    this.projectionMatrix = this.calculateProjectionMatrix();
  }

  @Override
  public TransformMatrix getProjectionMatrix() {
    return this.projectionMatrix;
  }

  private TransformMatrix calculateProjectionMatrix() {
    TransformMatrix projection = new TransformMatrix();
    float c = 1 / (this.farClipPlane - this.nearClipPlane);
    //scale x and y to fit width and height
    projection.assign(0 ,0, 2 / this.orthographicWidth);
    projection.assign(1 ,1, 2 / this.orthographicHeight);
    //scale z to fit clipping planes
    projection.assign(2, 2, 2 * c);
    projection.assign(2, 3, -(this.nearClipPlane + this.farClipPlane) * c);
    return projection;
  }
}
