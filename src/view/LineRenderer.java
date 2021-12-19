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
    TransformMatrix M = new TransformMatrix();
    TransformMatrix V = camera.getTransform().inverse();
    TransformMatrix P = camera.getProjectionMatrix();
    shader.setModelMatrix(M);
    shader.setViewMatrix(V);
    shader.setProjectionMatrix(P);

    Vector3 viewPlanePosition = camera.getTransform().apply(new Vector3(0, 0, 0));
    Vector3 viewPlaneNormal = camera.getTransform().apply(new Vector3(0, 0, 1)).minus(viewPlanePosition).normalized();
    viewPlanePosition = viewPlanePosition.plus(viewPlaneNormal.scale(camera.getNearClipPlane()));

    //trim to camera forward plane
    float startDistance = Vector3.dot(viewPlaneNormal, start.minus(viewPlanePosition));
    float endDistance = Vector3.dot(viewPlaneNormal, end.minus(viewPlanePosition));
    if(startDistance < 0) {
      if(endDistance < 0) {
        return new RenderOutput(base, buffer);
      }
      start = Vector3.lerp(start, end, startDistance / (startDistance - endDistance));
    }
    else if (endDistance < 0) {
      end = Vector3.lerp(end, start, endDistance / (endDistance - startDistance));
    }

    //initialize vertex data
    VertexData vert1Data = new VertexData();
    vert1Data.normal = start.minus(end).normalized();
    vert1Data.objectPos  = start;
    vert1Data.texCoord = new Vector2(0 ,0);

    VertexData vert2Data = new VertexData();
    vert2Data.normal = end.minus(start).normalized();
    vert2Data.objectPos = end;
    vert2Data.texCoord = new Vector2(1 ,1);

    //get v2fs
    VertexToFragment vert1v2f = shader.vert(vert1Data);
    VertexToFragment vert2v2f = shader.vert(vert2Data);

    //short circuit non-visible lines
    if((vert1v2f.clipPos.x < 0 && vert2v2f.clipPos.x < 0)
    || (vert1v2f.clipPos.x > 1 && vert2v2f.clipPos.x > 1)
    || (vert1v2f.clipPos.y < 0 && vert2v2f.clipPos.y < 0)
    || (vert1v2f.clipPos.y > 1 && vert2v2f.clipPos.y > 1)) {
      return new RenderOutput(base, buffer);
    }

    Vector2 point1 = vert1v2f.clipPos.xy();
    Vector2 point2 = vert2v2f.clipPos.xy();
    //clip to screen
    if(vert1v2f.clipPos.x < 0) {
      Vector2 point = Vector2.lerp(vert1v2f.clipPos.xy(), vert2v2f.clipPos.xy(), -vert1v2f.clipPos.x / (vert2v2f.clipPos.x - vert1v2f.clipPos.x));
      point1 = point2.minus(point1).magnitude() > point2.minus(point).magnitude() ? point : point1;
    } else if (vert1v2f.clipPos.x > 1) {
      Vector2 point = Vector2.lerp(vert1v2f.clipPos.xy(), vert2v2f.clipPos.xy(), (vert1v2f.clipPos.x - 1) / (vert1v2f.clipPos.x - vert2v2f.clipPos.x));
      point1 = point2.minus(point1).magnitude() > point2.minus(point).magnitude() ? point : point1;
    }
    if(vert1v2f.clipPos.y < 0) {
      Vector2 point = Vector2.lerp(vert1v2f.clipPos.xy(), vert2v2f.clipPos.xy(), -vert1v2f.clipPos.y / (vert2v2f.clipPos.y - vert1v2f.clipPos.y));
      point1 = point2.minus(point1).magnitude() > point2.minus(point).magnitude() ? point : point1;
    } else if (vert1v2f.clipPos.y > 1) {
      Vector2 point = Vector2.lerp(vert1v2f.clipPos.xy(), vert2v2f.clipPos.xy(), (vert1v2f.clipPos.y - 1) / (vert1v2f.clipPos.y - vert2v2f.clipPos.y));
      point1 = point2.minus(point1).magnitude() > point2.minus(point).magnitude() ? point : point1;
    }
    if(vert2v2f.clipPos.x < 0) {
      Vector2 point = Vector2.lerp(vert2v2f.clipPos.xy(), vert1v2f.clipPos.xy(), -vert2v2f.clipPos.x / (vert1v2f.clipPos.x - vert2v2f.clipPos.x));
      point2 = point1.minus(point2).magnitude() > point1.minus(point).magnitude() ? point : point2;
    } else if (vert2v2f.clipPos.x > 1) {
      Vector2 point = Vector2.lerp(vert2v2f.clipPos.xy(), vert1v2f.clipPos.xy(), (vert2v2f.clipPos.x - 1) / (vert2v2f.clipPos.x - vert1v2f.clipPos.x));
      point2 = point1.minus(point2).magnitude() > point1.minus(point).magnitude() ? point : point2;
    }
    if(vert2v2f.clipPos.y < 0) {
      Vector2 point = Vector2.lerp(vert2v2f.clipPos.xy(), vert1v2f.clipPos.xy(), -vert2v2f.clipPos.y / (vert1v2f.clipPos.y - vert2v2f.clipPos.y));
      point2 = point1.minus(point2).magnitude() > point1.minus(point).magnitude() ? point : point2;
    } else if (vert2v2f.clipPos.y > 1) {
      Vector2 point = Vector2.lerp(vert2v2f.clipPos.xy(), vert1v2f.clipPos.xy(), (vert2v2f.clipPos.y - 1) / (vert2v2f.clipPos.y - vert1v2f.clipPos.y));
      point2 = point1.minus(point2).magnitude() > point1.minus(point).magnitude() ? point : point2;
    }

    vert1v2f  = linearInterpolation(point1, vert1v2f, vert2v2f, P);
    vert2v2f  = linearInterpolation(point2, vert1v2f, vert2v2f, P);

    //rasterize
    Vector2 pixelStart;
    Vector2 pixelEnd;

    //X-centric or Y-centric change
    if(Math.abs(vert1v2f.clipPos.x - vert2v2f.clipPos.x) > Math.abs(vert1v2f.clipPos.y - vert2v2f.clipPos.y)) {
      //choose rightmost
      if(vert1v2f.clipPos.x < vert2v2f.clipPos.x) {
        pixelStart = new Vector2((int)(vert1v2f.clipPos.x * screenWidth), (int)(vert1v2f.clipPos.y * screenHeight));
        pixelEnd = new Vector2((int)(vert2v2f.clipPos.x * screenWidth), (int)(vert2v2f.clipPos.y * screenHeight));
      } else {
        pixelStart = new Vector2((int)(vert2v2f.clipPos.x * screenWidth), (int)(vert2v2f.clipPos.y * screenHeight));
        pixelEnd = new Vector2((int)(vert1v2f.clipPos.x * screenWidth), (int)(vert1v2f.clipPos.y * screenHeight));
      }
      float yIncrement = (vert1v2f.clipPos.y - vert2v2f.clipPos.y) / (vert1v2f.clipPos.x - vert2v2f.clipPos.x);

      for(int x = (int)pixelStart.x; x < pixelEnd.x + 1; x++) {
        int y = Math.round(pixelStart.y + (x - pixelStart.x) * yIncrement);
        Vector2 point = new Vector2((float) x / screenWidth, (float) y / screenHeight);
        VertexToFragment v2f = linearInterpolation(point, vert1v2f, vert2v2f, P);
        if (v2f.clipPos.z < ShaderApplicator.decodeDepth(buffer.getRGB(x, y)) && v2f.clipPos.z > 0) {
          buffer.setRGB(x, y, ShaderApplicator.encodeDepth(v2f.clipPos.z));
          base.setRGB(x, y, shader.frag(v2f).getRGB());
        }
      }
    }
    else {
      //choose higher
      if(vert1v2f.clipPos.y < vert2v2f.clipPos.y) {
        pixelStart = new Vector2((int)(vert1v2f.clipPos.x * screenWidth), (int)(vert1v2f.clipPos.y * screenHeight));
        pixelEnd = new Vector2((int)(vert2v2f.clipPos.x * screenWidth), (int)(vert2v2f.clipPos.y * screenHeight));
      } else {
        pixelStart = new Vector2((int)(vert2v2f.clipPos.x * screenWidth), (int)(vert2v2f.clipPos.y * screenHeight));
        pixelEnd = new Vector2((int)(vert1v2f.clipPos.x * screenWidth), (int)(vert1v2f.clipPos.y * screenHeight));
      }

      float xIncrement = (vert1v2f.clipPos.x - vert2v2f.clipPos.x) / (vert1v2f.clipPos.y - vert2v2f.clipPos.y);

      for(int y = (int)pixelStart.y; y < pixelEnd.y + 1; y++) {
        int x = Math.round(pixelStart.x + (y - pixelStart.y) * xIncrement);
        if(x > 0 && x < screenWidth && y > 0 && y < screenHeight) {
          Vector2 point = new Vector2((float) x / screenWidth, (float) y / screenHeight);
          VertexToFragment v2f = linearInterpolation(point, vert1v2f, vert2v2f, P);
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
    return Vector3.lerp(value1, value2, projection / line.magnitude());
  }
}
