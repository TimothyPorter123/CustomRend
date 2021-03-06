package model.math;

public class Transform {
  public Vector3 position;
  public Vector4 rotation;
  public Vector3 scale;
  private TransformMatrix matrix;

  public Transform() {
    this.position = new Vector3(0, 0, 0);
    this.rotation = new Vector4(0 ,0, 0, 1);
    this.scale = new Vector3(1, 1, 1);
    this.updateMatrix();
  }

  public void translate(Vector3 displacement) {
    this.position = this.position.plus(displacement);
    this.updateMatrix();
  }

  public void scale(Vector3 scaleFactors) {
    this.scale = new Vector3(this.scale.x * scaleFactors.x, this.scale.y * scaleFactors.y, this.scale.x * scaleFactors.z);
    this.updateMatrix();
  }

  public void rotate(Vector4 quaternion) {
    float w = this.rotation.w * quaternion.w
            - this.rotation.x * quaternion.x
            - this.rotation.y * quaternion.y
            - this.rotation.z * quaternion.z;
    float x = this.rotation.w * quaternion.x
            + this.rotation.x * quaternion.w
            + this.rotation.y * quaternion.z
            - this.rotation.z * quaternion.y;
    float y = this.rotation.w * quaternion.y
            + this.rotation.y * quaternion.w
            + this.rotation.z * quaternion.x
            - this.rotation.x * quaternion.z;
    float z = this.rotation.w * quaternion.z
            + this.rotation.z * quaternion.w
            + this.rotation.x * quaternion.y
            - this.rotation.y * quaternion.x;
    this.rotation = new Vector4(x, y, z, w).normalized();
    this.updateMatrix();
  }

  public void rotate(Vector3 axis, float angle) {
    Vector3 a = axis.normalized();
    angle = angle / 180f * (float)Math.PI;
    float fac = (float)Math.sin(angle / 2);
    this.rotate(new Vector4(a.x * fac, a.y * fac, a.z * fac, (float)Math.cos(angle / 2)));
  }

  public void globalRotate(Vector3 axis, float angle) {
    axis = axis.rotate(new Vector4(this.rotation.xyz().scale(-1f), this.rotation.w));
    this.rotate(axis, angle);
  }

  public void orbit(Vector3 point, Vector3 axis, float angle) {
    Vector3 a = axis.normalized();
    this.globalRotate(a, angle);
    Vector3 posV = this.position.minus(point);
    angle = angle / 180f * (float)Math.PI;
    float fac = (float)Math.sin(angle / 2);
    Vector3 newV = posV.rotate(new Vector4(a.x * fac, a.y * fac, a.z * fac, (float)Math.cos(angle / 2)));
    this.position = point.plus(newV);
    this.updateMatrix();
  }

  protected void updateMatrix() {
    this.matrix = new TransformMatrix().move(this.position).rotate(this.rotation).scale(this.scale);
  }

  //better, but maybe not great
  public Vector3 forward() {
    return new Vector3(0, 0, 1).rotate(new Vector4(this.rotation.xyz().scale(1f), this.rotation.w));
    //return this.getMatrix().inverse().apply(new Vector3(0, 0, 1).plus(this.position));
  }
  public Vector3 up() {
    return new Vector3(0, 1, 0).rotate(new Vector4(this.rotation.xyz().scale(1f), this.rotation.w));
    //return this.getMatrix().inverse().apply(new Vector3(0, 1, 0).plus(this.position));
  }
  public Vector3 right() {
    return new Vector3(1, 0, 0).rotate(new Vector4(this.rotation.xyz().scale(1f), this.rotation.w));
    //return this.getMatrix().inverse().apply(new Vector3(1, 0, 0).plus(this.position));
  }

  public TransformMatrix getMatrix() {
    return this.matrix;
  }
}
