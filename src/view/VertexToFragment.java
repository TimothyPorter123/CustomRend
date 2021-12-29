package view;

import model.math.Vector2;
import model.math.Vector3;

public class VertexToFragment {
  public Vector3 normal;
  public Vector2 uv;
  public Vector3 clipPos;


  public static VertexToFragment barycentricInterpolation(Vector2 point,
                                                           VertexToFragment vert1, VertexToFragment vert2, VertexToFragment vert3,
                                                           ShaderData s) {
    float totalArea = RendererMath.triangleArea(vert1.clipPos.xy(), vert2.clipPos.xy(), vert3.clipPos.xy());
    float a1 = RendererMath.triangleArea(vert2.clipPos.xy(), vert3.clipPos.xy(), point) / totalArea;
    float a2 = RendererMath.triangleArea(vert1.clipPos.xy(), vert3.clipPos.xy(), point) / totalArea;
    float a3 = RendererMath.triangleArea(vert1.clipPos.xy(), vert2.clipPos.xy(), point) / totalArea;

    VertexToFragment resultant = new VertexToFragment();
    resultant.clipPos = new Vector3(point.x, point.y, 0.0f);
    float vert1EyeDepth = s.linearEyeDepth(vert1.clipPos.z);
    float vert2EyeDepth = s.linearEyeDepth(vert2.clipPos.z);
    float vert3EyeDepth = s.linearEyeDepth(vert3.clipPos.z);
    resultant.clipPos.z = s.nonlinearDepth(RendererMath.trilinearInterpolate(vert1EyeDepth, vert2EyeDepth, vert3EyeDepth,
            a1, a2, a3, vert1EyeDepth, vert2EyeDepth, vert3EyeDepth));
    resultant.normal = Vector3.terp(vert1.normal, vert2.normal, vert3.normal, a1, a2, a3,
            vert1EyeDepth, vert2EyeDepth, vert3EyeDepth);
    resultant.uv = Vector2.terp(vert1.uv, vert2.uv, vert3.uv, a1, a2, a3,
            vert1EyeDepth, vert2EyeDepth, vert3EyeDepth);
    return resultant;
  }
}
