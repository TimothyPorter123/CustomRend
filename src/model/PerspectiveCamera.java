package model;

public class PerspectiveCamera extends Camera{

  float fieldOfView;
  float nearClipPlane;
  float farClipPlane;
  float aspectRatio;

  TransformMatrix projectionMatrix;

  public PerspectiveCamera(float FOV, float near, float far, float aspectRatio) {
    super();
    this.fieldOfView = FOV;
    this.nearClipPlane = near;
    this.farClipPlane = far;
    this.aspectRatio = aspectRatio;
    this.projectionMatrix = this.calculateProjectionMatrix();
  }

  @Override
  public TransformMatrix getProjectionMatrix() { return this.projectionMatrix; };

  private TransformMatrix calculateProjectionMatrix() {
    TransformMatrix projection = new TransformMatrix();
    float sx = 1 / (float)Math.tan(this.fieldOfView * Math.PI / 360.0);
    float verticalFOV = (float)Math.atan(Math.tan(this.fieldOfView * Math.PI / 360.0) * this.aspectRatio) * 2;
    float sy = 1 / (float)Math.tan(verticalFOV / 2);
    float c = this.farClipPlane / (this.farClipPlane - this.nearClipPlane);

    //x scale with FOV
    projection.assign(0, 0, sx);
    //y scale with FOV
    projection.assign(1, 1, sy);
    //assign w to pick up z component instead, so final vector will be divided by z
    projection.assign(3, 2, 1);
    projection.assign(3, 3, 0);
    //Assign scaling for z component
    projection.assign(2, 2, c);
    projection.assign(2, 3, -c * this.nearClipPlane);
    System.out.println(projection.toString());
    return projection;
  }

}
