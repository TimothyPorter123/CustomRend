package view;

import java.awt.image.BufferedImage;

import model.Camera;
import model.RenderOutput;
import model.TransformMatrix;
import model.Vector2;
import model.Vector3;

public class LineRenderer {

  Shader shader;
  int screenWidth;
  int screenHeight;

  public LineRenderer(Shader shader, int width, int height) {
    this.shader = shader;
    screenHeight = height;
    screenWidth = width;
  }

  public RenderOutput renderLineOver(Vector3 start, Vector3 end, float thickness, Camera camera, BufferedImage base, BufferedImage buffer) {
    shader.setModelMatrix(new TransformMatrix());
    shader.setViewMatrix(camera.getTransform().inverse());
    shader.setProjectionMatrix(camera.getProjectionMatrix());

    VertexData vert1Data = new VertexData();
    vert1Data.normal = start.minus(end).normalized();
    vert1Data.objectPos  = start;
    vert1Data.texCoord = new Vector2(0 ,0);

    VertexData vert2Data = new VertexData();
    vert2Data.normal = end.minus(start).normalized();
    vert2Data.objectPos = end;
    vert2Data.texCoord = new Vector2(1 ,1);

    VertexToFragment vert1v2f = shader.vert(vert1Data);
    VertexToFragment vert2v2f = shader.vert(vert2Data);

    System.out.println(vert1v2f.clipPos.toString() + " " + vert2v2f.clipPos.toString());


    for (int y = 0; y < screenHeight; y++) {
      for (int x = 0; x < screenWidth; x++) {
        Vector2 point = new Vector2((float) x / screenWidth, (float) y / screenHeight);
        if (onLine(point, vert1v2f.clipPos.xy(), vert2v2f.clipPos.xy(), thickness)) {
          VertexToFragment v2f = linearInterpolation(point, vert1v2f, vert2v2f, camera.getProjectionMatrix());
          if (v2f.clipPos.z < ShaderApplicator.decodeDepth(buffer.getRGB(x, y)) && v2f.clipPos.z > 0) {
            buffer.setRGB(x, y, ShaderApplicator.encodeDepth(v2f.clipPos.z));
            base.setRGB(x, y, shader.frag(v2f).getRGB());
          }
        }
      }
    }

    return new RenderOutput(base, buffer);
  }

  private static boolean onLine(Vector2 p, Vector2 v0, Vector2 v1, float lineThickness) {
    Vector2 toPoint = p.minus(v0);
    Vector2 line = v1.minus(v0);
    float projection = Vector2.dot(toPoint, line) / line.magnitude();
    if(projection >= 0 && projection <= line.magnitude()) {
      if(ShaderApplicator.triangleArea(p, v0, v1) < lineThickness * line.magnitude() / 4.0f) {
        return true;
      }
    }
    return false;
  }

  private static VertexToFragment linearInterpolation(Vector2 point, VertexToFragment v0, VertexToFragment v1, TransformMatrix projection) {
    VertexToFragment returnFrag = new VertexToFragment();
    returnFrag.clipPos = new Vector3(point.x, point.y, 0);
    float vert1EyeDepth = ShaderApplicator.linearEyeDepth(v0.clipPos.z, projection);
    float vert2EyeDepth = ShaderApplicator.linearEyeDepth(v1.clipPos.z, projection);
    returnFrag.clipPos.z = ShaderApplicator.nonlinearDepth(linearInterpolate(vert1EyeDepth, vert2EyeDepth, point, v0.clipPos.xy(), v1.clipPos.xy()), projection);
    returnFrag.normal = linearInterpolate(v0.normal, v1.normal, point, v0.clipPos.xy(), v1.clipPos.xy());
    returnFrag.uv = linearInterpolate(v0.uv, v1.uv, point, v0.clipPos.xy(), v1.clipPos.xy());
    return returnFrag;
  }

  private static float linearInterpolate(float value1, float value2, Vector2 p, Vector2 v0, Vector2 v1) {
    Vector2 toPoint = p.minus(v0);
    Vector2 line = v1.minus(v0);
    float projection = Vector2.dot(toPoint, line) / line.magnitude();
    float factor = projection / line.magnitude();
    return value1 + ((value2 - value1) * factor);
  }

  private static Vector2 linearInterpolate(Vector2 value1, Vector2 value2, Vector2 p, Vector2 v0, Vector2 v1) {
    return linearInterpolate(new Vector3(value1.x, value1.y, 0.0f), new Vector3(value2.x, value2.y, 0.0f), p, v0, v1).xy();
  }

  private static Vector3 linearInterpolate(Vector3 value1, Vector3 value2, Vector2 p, Vector2 v0, Vector2 v1) {
    Vector2 toPoint = p.minus(v0);
    Vector2 line = v1.minus(v0);
    float projection = Vector2.dot(toPoint, line) / line.magnitude();
    float factor = projection / line.magnitude();
    return value1.plus((value2.minus(value1)).scale(factor));
  }
}
