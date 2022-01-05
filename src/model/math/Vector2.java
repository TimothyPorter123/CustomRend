package model.math;

import view.RendererMath;

public class Vector2 {

  public float x, y;

  public Vector2(float x, float y) {
    this.x = x;
    this.y = y;
  }

  public float magnitude() {
    return (float) java.lang.Math.sqrt(this.x * this.x + this.y * this.y);
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

  //linear interpolation
  public static Vector2 lerp(Vector2 v0, Vector2 v1, float factor) {
    return v0.plus(v1.minus(v0).scale(factor));
  }


  //trilinear interpolation
  public static Vector2 terp(Vector2 value1, Vector2 value2, Vector2 value3, float a1, float a2,
                                              float a3, float depth1, float depth2, float depth3) {
    float xNum = (value1.x * a1 / depth1 + value2.x * a2 / depth2 + value3.x * a3 / depth3);
    float yNum = (value1.y * a1 / depth1 + value2.y * a2 / depth2 + value3.y * a3 / depth3);
    return new Vector2(xNum, yNum).scale(1 / (a1 / depth1 + a2 / depth2 + a3 / depth3));
  }


  /*public boolean barycentricInside(Vector2 v0, Vector2 v1, Vector2 v2) {
    float w0 = RendererMath.edgeFunction(v1, v2, this);
    float w1 = RendererMath.edgeFunction(v2, v0, this);
    float w2 = RendererMath.edgeFunction(v0, v1, this);

    Vector2 edge0 = v2.minus(v1);
    Vector2 edge1 = v0.minus(v2);
    Vector2 edge2 = v1.minus(v0);

    boolean overlaps = true;


    overlaps &= (w0 == 0 ? ((edge0.y == 0 && edge0.x > 0) || edge0.y > 0) : (w0 > 0));
    overlaps &= (w1 == 0 ? ((edge1.y == 0 && edge1.x > 0) || edge1.y > 0) : (w1 > 0));
    overlaps &= (w1 == 0 ? ((edge2.y == 0 && edge2.x > 0) || edge2.y > 0) : (w2 > 0));
    return overlaps;
  }*/

  @Override
  public String toString() {
    return  "(" + x + ", " + y + ")";
  }

}
