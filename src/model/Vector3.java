package model;

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

  public Vector3 normalized() {
    float mag = this.magnitude();
    return new Vector3(this.x / mag, this.y / mag, this.z / mag);
  }

  public Vector2 xy() {
    return new Vector2(this.x, this.y);
  }

  @Override
  public String toString() {
    return "(" + this.x + ", " + this.y + ", " + this.z + ")";
  }
}
