package view;
import model.math.Vector2;
import model.math.Vector3;

public class LineRendererMath {

  public static VertexToFragment onLineInterpolation(Vector2 point, VertexToFragment v0, VertexToFragment v1, ShaderData s) {
    VertexToFragment returnFrag = new VertexToFragment();
    returnFrag.clipPos = new Vector3(point.x, point.y, 0);
    float vert1EyeDepth = s.linearEyeDepth(v0.clipPos.z);
    float vert2EyeDepth = s.linearEyeDepth(v1.clipPos.z);

    float distanceToStart = point.minus(v0.clipPos.xy()).magnitude();
    float distanceToEnd = point.minus(v1.clipPos.xy()).magnitude();

    returnFrag.clipPos.z = s.nonlinearDepth(onLineInterpolate(vert1EyeDepth, vert2EyeDepth, distanceToStart, distanceToEnd, vert1EyeDepth, vert2EyeDepth));
    returnFrag.normal = onLineInterpolate(v0.normal, v1.normal, distanceToStart, distanceToEnd, vert1EyeDepth, vert2EyeDepth);
    returnFrag.uv = onLineInterpolate(v0.uv, v1.uv, distanceToStart, distanceToEnd, vert1EyeDepth, vert2EyeDepth);
    return returnFrag;
  }

  private static float onLineInterpolate(float value1, float value2, float dist1, float dist2, float depth1, float depth2) {
    float denominator = dist2 / depth1 + dist1 / depth2;
    return ((dist2 * value1 / depth1) + (dist1 * value2 / depth2)) / denominator;
  }

  private static Vector2 onLineInterpolate(Vector2 value1, Vector2 value2, float dist1, float dist2, float depth1, float depth2) {
    return onLineInterpolate(new Vector3(value1.x, value1.y, 0.0f), new Vector3(value2.x, value2.y, 0.0f),
            dist1, dist2, depth1, depth2).xy();
  }

  private static Vector3 onLineInterpolate(Vector3 value1, Vector3 value2, float dist1, float dist2, float depth1, float depth2) {
    float denominator = dist2 / depth1 + dist1 / depth2;
    return new Vector3(dist2 * value1.x / depth1 + dist1 * value2.x / depth2,
            dist2 * value1.y / depth1 + dist1 * value2.y / depth2,
            dist2 * value1.z / depth1 + dist1 * value2.z / depth2).scale(1f / denominator);
  }
}
