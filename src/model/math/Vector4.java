package model.math;

public class Vector4 {

  public float x;
  public float y;
  public float z;
  public float w;

  public Vector4(float x, float y, float z, float w) {
    this.x = x;
    this.y = y;
    this.z = z;
    this.w = w;
  }

  public Vector4(float x, float y, float z) {
    this.x = x;
    this.y = y;
    this.z = z;
    this.w = 1f;
  }

  public Vector4(int RGB) {
    this.x = ((RGB & 0x00ff0000) >> 16) / 255f;
    this.y = ((RGB & 0x0000ff00) >> 8) / 255f;
    this.z = (RGB & 0x000000ff) / 255f;
    this.w = ((RGB & 0xff000000) >> 24) / 255f;
  }

  public Vector3 xyz() {
    return new Vector3(this.x, this.y, this.z);
  }

  public int getRGB() {
    return ((int)(this.w * 255) << 24)
            + ((int)(this.x * 255) << 16)
            + ((int)(this.y * 255) << 8)
            + (int)(this.z * 255);
  }

  public Vector4 scale(float factor) {
    return new Vector4(this.x * factor, this.y * factor, this.z * factor, this.w * factor);
  }

  public Vector4 plus(Vector4 other) {
    return new Vector4(this.x + other.x, this.y + other.y, this.z + other.z, this.w + other.w);
  }

  public float magnitude() {
    return (float)Math.sqrt(this.x * this.x + this.y * this.y + this.z * this.z + this.w * this.w);
  }

  public Vector4 normalized() {
    return new Vector4(x, y, z, w).scale(1f / this.magnitude());
  }

  @Override
  public String toString() {
    return "(" + this.x + ", " + this.y + ", " + this.z + ", " + this.w + ")";
  }
}
