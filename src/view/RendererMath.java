package view;
import java.awt.*;

import model.math.Vector2;
import model.math.Vector3;
import model.math.Vector4;

public class RendererMath {

  public static float edgeFunction(Vector2 A, Vector2 B, Vector2 P) {
    return (B.x - A.x) * (P.y - A.y) - (B.y - A.y) * (P.x - A.x);
  }


  public static boolean topLeftEdgeFunction(Vector2 v0, Vector2 v1, Vector2 v2, float w0, float w1, float w2) {
    Vector2 edge0 = v2.minus(v1);
    Vector2 edge1 = v0.minus(v2);
    Vector2 edge2 = v1.minus(v0);

    boolean overlaps = true;


    overlaps &= (w0 == 0 ? ((edge0.y == 0 && edge0.x > 0) || edge0.y > 0) : (w0 > 0));
    overlaps &= (w1 == 0 ? ((edge1.y == 0 && edge1.x > 0) || edge1.y > 0) : (w1 > 0));
    overlaps &= (w1 == 0 ? ((edge2.y == 0 && edge2.x > 0) || edge2.y > 0) : (w2 > 0));
    return overlaps;
  }

  //triple interpolation
  public static float terp(float value1, float value2, float value3, float a1, float a2,
                                     float a3, float depth1, float depth2, float depth3) {
    return (value1 * a1 / depth1 + value2 * a2 / depth2 + value3 * a3 / depth3)
            / (a1 / depth1 + a2 / depth2 + a3 / depth3);
  }

  public static float triangleArea(Vector2 point1, Vector2 point2, Vector2 point3) {
    return java.lang.Math.abs(0.5f * (point1.x * (point2.y - point3.y) + point2.x * (point3.y - point1.y)
            + point3.x * (point1.y - point2.y)));
  }

  public static Color blendColor(int c1, int c2, float ratio ) {
    if ( ratio > 1f ) ratio = 1f;
    else if ( ratio < 0f ) ratio = 0f;
    float iRatio = 1.0f - ratio;

    int i1 = c1;
    int i2 = c2;

    int a1 = (i1 >> 24 & 0xff);
    int r1 = ((i1 & 0xff0000) >> 16);
    int g1 = ((i1 & 0xff00) >> 8);
    int b1 = (i1 & 0xff);

    int a2 = (i2 >> 24 & 0xff);
    int r2 = ((i2 & 0xff0000) >> 16);
    int g2 = ((i2 & 0xff00) >> 8);
    int b2 = (i2 & 0xff);

    int a = (int)((a1 * iRatio) + (a2 * ratio));
    int r = (int)((r1 * iRatio) + (r2 * ratio));
    int g = (int)((g1 * iRatio) + (g2 * ratio));
    int b = (int)((b1 * iRatio) + (b2 * ratio));

    return new Color( a << 24 | r << 16 | g << 8 | b );
  }

  public static Vector4 blendColor(Vector4 c1, Vector4 c2, float ratio ) {
    if ( ratio > 1f ) ratio = 1f;
    else if ( ratio < 0f ) ratio = 0f;
    float iRatio = 1.0f - ratio;

    return new Vector4(c1.x * iRatio + c2.x * ratio, c1.y * iRatio + c2.y * ratio,
            c1.z * iRatio + c2.z * ratio,
            c1.w * iRatio + c2.w * ratio);
  }
}
