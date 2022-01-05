package view;

import model.math.Vector2;
import model.math.Vector3;

public class VertexToFragment {
  public Vector3 normal;
  public Vector2 uv;
  public Vector3 clipPos;


  public static VertexToFragment barycentricInterpolation(Vector2 point, float totalArea,
                                                           VertexToFragment vert1, VertexToFragment vert2, VertexToFragment vert3,
                                                           float w0, float w1, float w2,
                                                           ShaderData s) {
    float a1 = w0 / totalArea;
    float a2 = w1 / totalArea;
    float a3 = w2 / totalArea;

    VertexToFragment resultant = new VertexToFragment();
    resultant.clipPos = new Vector3(point.x, point.y, 0.0f);
    float vert1EyeDepth = s.linearEyeDepth(vert1.clipPos.z);
    float vert2EyeDepth = s.linearEyeDepth(vert2.clipPos.z);
    float vert3EyeDepth = s.linearEyeDepth(vert3.clipPos.z);
    resultant.clipPos.z = s.nonlinearDepth(RendererMath.terp(vert1EyeDepth, vert2EyeDepth, vert3EyeDepth,
            a1, a2, a3, vert1EyeDepth, vert2EyeDepth, vert3EyeDepth));
    resultant.normal = Vector3.terp(vert1.normal, vert2.normal, vert3.normal, a1, a2, a3,
            vert1EyeDepth, vert2EyeDepth, vert3EyeDepth);
    resultant.uv = Vector2.terp(vert1.uv, vert2.uv, vert3.uv, a1, a2, a3,
            vert1EyeDepth, vert2EyeDepth, vert3EyeDepth);
    return resultant;
  }
}
