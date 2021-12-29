package view;
import model.math.Vector2;

public class RendererMath {

  public static float edgeFunction(Vector2 A, Vector2 B, Vector2 P) {
    return (A.x - B.x) * (P.y - A.y) - (A.y - B.y) * (P.x - A.x);
  }

  //triple interpolation
  public static float terp(float value1, float value2, float value3, float a1, float a2,
                                     float a3, float depth1, float depth2, float depth3) {
    return (value1 * a1 / depth1 + value2 * a2 / depth2 + value3 * a3 / depth3)
            / (a1 / depth1 + a2 / depth2 + a3 / depth3);
  }

  static float triangleArea(Vector2 point1, Vector2 point2, Vector2 point3) {
    return java.lang.Math.abs(0.5f * (point1.x * (point2.y - point3.y) + point2.x * (point3.y - point1.y)
            + point3.x * (point1.y - point2.y)));
  }
}
