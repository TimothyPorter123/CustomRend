package model;

public class Vector2 {

  public float x, y;

  public Vector2(float x, float y) {
    this.x = x;
    this.y = y;
  }

  public float magnitude() {
    return (float) Math.sqrt(this.x * this.x + this.y * this.y);
  }

  public Vector2 plus(Vector2 other) {
    return new Vector2(this.x + other.x, this.y + other.y);
  }

  public Vector2 minus(Vector2 other) {
    return new Vector2(this.x - other.x, this.y - other.y);
  }

  public Vector2 scale(float factor) {
    return new Vector2(this.x * factor, this.y * factor);
  }

  public static float dot(Vector2 vOne, Vector2 vTwo) {
    return vOne.x * vTwo.x + vOne.y * vTwo.y;
  }

  public Vector2 normalized() {
    float mag = this.magnitude();
    return new Vector2(this.x / mag, this.y / mag);
  }

  public static Vector2 lerp(Vector2 v0, Vector2 v1, float factor) {
    return v0.plus(v1.minus(v0).scale(factor));
  }

  @Override
  public String toString() {
    return  "(" + x + ", " + y + ")";
  }

}
