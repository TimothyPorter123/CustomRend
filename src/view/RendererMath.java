package view;
import java.awt.*;

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
}
