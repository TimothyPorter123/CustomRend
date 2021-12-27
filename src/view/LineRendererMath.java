package view;
import model.math.Vector2;
import model.math.Vector3;

public class LineRendererMath {

  public static VertexToFragment onLineInterpolation(Vector2 point, VertexToFragment v0, VertexToFragment v1, ShaderData s) {
    VertexToFragment returnFrag = new VertexToFragment();
    returnFrag.clipPos = new Vector3(point.x, point.y, 0);
    float vert1EyeDepth = s.linearEyeDepth(v0.clipPos.z);
    float vert2EyeDepth = s.linearEyeDepth(v1.clipPos.z);
    returnFrag.clipPos.z = s.nonlinearDepth(onLineInterpolate(vert1EyeDepth, vert2EyeDepth, point, v0.clipPos.xy(), v1.clipPos.xy()));
    returnFrag.normal = onLineInterpolate(v0.normal, v1.normal, point, v0.clipPos.xy(), v1.clipPos.xy());
    returnFrag.uv = onLineInterpolate(v0.uv, v1.uv, point, v0.clipPos.xy(), v1.clipPos.xy());
    return returnFrag;
  }

  private static float onLineInterpolate(float value1, float value2, Vector2 p, Vector2 v0, Vector2 v1) {
    Vector2 toPoint = p.minus(v0);
    Vector2 line = v1.minus(v0);
    float projection = Vector2.dot(toPoint, line) / line.magnitude();
    float factor = projection / line.magnitude();
    return value1 + ((value2 - value1) * factor);
  }

  private static Vector2 onLineInterpolate(Vector2 value1, Vector2 value2, Vector2 p, Vector2 v0, Vector2 v1) {
    return onLineInterpolate(new Vector3(value1.x, value1.y, 0.0f), new Vector3(value2.x, value2.y, 0.0f), p, v0, v1).xy();
  }

  private static Vector3 onLineInterpolate(Vector3 value1, Vector3 value2, Vector2 p, Vector2 v0, Vector2 v1) {
    Vector2 toPoint = p.minus(v0);
    Vector2 line = v1.minus(v0);
    float projection = Vector2.dot(toPoint, line) / line.magnitude();
    return Vector3.lerp(value1, value2, projection / line.magnitude());
  }
}
