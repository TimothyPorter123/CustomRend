package model.math;

public class Vector3 {

  public float x, y, z;

  public Vector3(float x, float y, float z) {
    this.x = x;
    this.y = y;
    this.z = z;
  }

  public float magnitude() {
    return (float) Math.sqrt(this.x * this.x + this.y * this.y + this.z * this.z);
  }

  public Vector3 plus(Vector3 other) {
    return new Vector3(this.x + other.x, this.y + other.y, this.z + other.z);
  }

  public Vector3 minus(Vector3 other) {
    return new Vector3(this.x - other.x, this.y - other.y, this.z - other.z);
  }

  public Vector3 scale(float factor) {
    return new Vector3(this.x * factor, this.y * factor, this.z * factor);
  }

  public static float dot(Vector3 vOne, Vector3 vTwo) {
    return vOne.x * vTwo.x + vOne.y * vTwo.y + vOne.z * vTwo.z;
  }

  public static Vector3 cross(Vector3 vOne, Vector3 vTwo) {
    return new Vector3(vOne.y * vTwo.z - vOne.z * vTwo.y, vOne.z * vTwo.x - vOne.x * vTwo.z,
            vOne.x * vTwo.y - vOne.y * vTwo.x);
  }

  public Vector3 rotate(Vector4 quaternion) {
    Vector4 q = quaternion.normalized();
    Vector3 u = q.xyz();

    return u.scale(Vector3.dot(u, this) * 2).plus(this.scale(q.w * q.w - Vector3.dot(u, u))).plus(Vector3.cross(u, this).scale(2 * q.w));
  }

  public Vector3 normalized() {
    float mag = this.magnitude();
    return new Vector3(this.x / mag, this.y / mag, this.z / mag);
  }

  public Vector2 xy() {
    return new Vector2(this.x, this.y);
  }

  //linear interpolation
  public static Vector3 lerp(Vector3 v0, Vector3 v1, float factor) {
    return v0.plus((v1.minus(v0)).scale(factor));
  }

  //trilinear interpolation
  public static Vector3 terp(Vector3 value1, Vector3 value2, Vector3 value3, float a1, float a2,
                             float a3, float depth1, float depth2, float depth3) {
    float xNum = (value1.x * a1 / depth1 + value2.x * a2 / depth2 + value3.x * a3 / depth3);
    float yNum = (value1.y * a1 / depth1 + value2.y * a2 / depth2 + value3.y * a3 / depth3);
    float zNum = (value1.z * a1 / depth1 + value2.z * a2 / depth2 + value3.z * a3 / depth3);
    return new Vector3(xNum, yNum, zNum).scale(1 / (a1 / depth1 + a2 / depth2 + a3 / depth3));
  }

  @Override
  public String toString() {
    return "(" + this.x + ", " + this.y + ", " + this.z + ")";
  }
}
