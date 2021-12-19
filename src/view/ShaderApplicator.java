package view;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import model.Camera;
import model.RenderObjectModel;
import model.RenderOutput;
import model.SimpleModel;
import model.TransformMatrix;
import model.Vector2;
import model.Vector3;

public class ShaderApplicator {

  Shader shader;
  int screenWidth;
  int screenHeight;

  public ShaderApplicator(Shader shader, int width, int height) {
    this.shader = shader;
    screenHeight = height;
    screenWidth = width;
  }


  public RenderOutput renderObject(SimpleModel model, Camera camera) {
    return renderObjectOver(model, camera, blankScreenImage(), blankDepthBuffer());
  }

  public RenderOutput renderObjectOver(SimpleModel model, Camera camera, BufferedImage base, BufferedImage buffer) {
    Vector3[] verts = model.getVertices();
    int[] vertsToRender = model.getTriangles();
    VertexToFragment[] vertToFragData = new VertexToFragment[vertsToRender.length];
    TransformMatrix M = model.getTransform();
    TransformMatrix V = camera.getTransform().inverse();
    TransformMatrix P = camera.getProjectionMatrix();
    shader.setModelMatrix(M);
    shader.setViewMatrix(V);
    shader.setProjectionMatrix(P);
    //this.preformAllCulling(verts, vertsToRender, M, V, P);
    for (int i = 0; i < vertsToRender.length; i += 3) {
      Vector3 normal = Vector3.cross(verts[vertsToRender[i + 2]].minus(verts[vertsToRender[i]]),
              verts[vertsToRender[i + 1]].minus(verts[vertsToRender[i]])).normalized();


      for (int v = i; v < i + 3; v++) {
        VertexData data = new VertexData();
        data.objectPos = verts[vertsToRender[v]];
        data.texCoord = new Vector2(verts[vertsToRender[v]].x, verts[vertsToRender[v]].z);
        data.normal = normal;
        vertToFragData[v] = shader.vert(data);
      }
    }

    for (int i = 0; i < vertToFragData.length; i += 3) {
      Vector2 clipPos1 = vertToFragData[i].clipPos.xy();
      Vector2 clipPos2 = vertToFragData[i + 1].clipPos.xy();
      Vector2 clipPos3 = vertToFragData[i + 2].clipPos.xy();

      Vector2 rastStart = new Vector2(1, 1);
      Vector2 rastEnd = new Vector2(0, 0);

      rastStart.x = Math.max(Math.min(clipPos1.x, Math.min(clipPos2.x, Math.min(clipPos3.x, 1))), 0) * screenWidth;
      rastStart.y = Math.max(Math.min(clipPos1.y, Math.min(clipPos2.y, Math.min(clipPos3.y, 1))), 0) * screenHeight;
      rastEnd.x = Math.min(Math.max(clipPos1.x, Math.max(clipPos2.x, Math.max(clipPos3.x, 0))), 1) * screenWidth;
      rastEnd.y = Math.min(Math.max(clipPos1.y, Math.max(clipPos2.y, Math.max(clipPos3.y, 0))), 1) * screenHeight;

      for (int y = (int)rastStart.y; y < (int)rastEnd.y; y++) {
        for (int x = (int)rastStart.x; x < (int)rastEnd.x; x++) {
          Vector2 point = new Vector2((float) x / screenWidth, (float) y / screenHeight);
          if (barycentricInside(point,
                  clipPos1, clipPos2, clipPos3)) {
            VertexToFragment v2f = barycentricInterpolation(point, vertToFragData[i],
                    vertToFragData[i + 1], vertToFragData[i + 2], camera.getProjectionMatrix());
            if (v2f.clipPos.z < decodeDepth(buffer.getRGB(x, y)) && v2f.clipPos.z > 0) {
              buffer.setRGB(x, y, encodeDepth(v2f.clipPos.z));
              base.setRGB(x, y, shader.frag(v2f).getRGB());
            }
          }
        }
      }
    }
    return new RenderOutput(base, buffer);
  }

  private void preformAllCulling(Vector3[] verts, int[] tris, TransformMatrix M, TransformMatrix V, TransformMatrix P) {
    boolean[] vertexInFrustum = new boolean[verts.length];
    boolean[] needsClipping = new boolean[tris.length / 3];
    //assigned in bit order { discarded, discarded, near, far, bottom, top, left, right }
    byte[] planesToClipAgainst = new byte[tris.length / 3];

    TransformMatrix MVP = M.multiply(V.multiply(P));
    for(int i = 0; i < needsClipping.length; i ++) {
      Vector3 v0 = MVP.apply(verts[tris[i * 3]]);
      Vector3 v1 = MVP.apply(verts[tris[i * 3 + 1]]);
      Vector3 v2 = MVP.apply(verts[tris[i * 3 + 2]]);

      planesToClipAgainst[i] += (v0.z < 0 || v1.z < 0 || v2.z < 0) ? 32 : 0;
      planesToClipAgainst[i] += (v0.z > 1 || v1.z > 1 || v2.z > 1) ? 16 : 0;
      planesToClipAgainst[i] += (v0.y < -1 || v1.y < -1 || v2.y < -1) ? 8 : 0;
      planesToClipAgainst[i] += (v0.y > 1 || v1.y > 1 || v2.y > 1) ? 4 : 0;
      planesToClipAgainst[i] += (v0.x < -1 || v1.x < -1 || v2.x < -1) ? 2 : 0;
      planesToClipAgainst[i] += (v0.x > 1 || v1.x > 1 || v2.x > 1) ? 1 : 0;

      if(planesToClipAgainst[i] != 0) {
        needsClipping[i] = true;
      }
    }
  }

  public static boolean barycentricInsideOld(Vector2 point, Vector2 vert1, Vector2 vert2, Vector2 vert3) {
    float area = 0.5f * (-vert2.y * vert3.x + vert1.y * (-vert2.x + vert3.x) + vert1.x * (vert2.y - vert3.y) + vert2.x * vert3.y);
    float s = 1 / (2 * area) * (vert1.y * vert3.x - vert1.x * vert3.y + (vert3.y - vert1.y) * point.x + (vert1.x - vert3.x) * point.y);
    float t = 1 / (2 * area) * (vert1.x * vert2.y - vert1.y * vert2.x + (vert1.y - vert2.y) * point.x + (vert2.x - vert1.x) * point.y);
    return s > 0 && t > 0 && (1 - s - t) > 0;
  }

  public static boolean barycentricInside(Vector2 p, Vector2 v0, Vector2 v1, Vector2 v2) {
    float w0 = -edgeFunction(v1, v2, p);
    float w1 = -edgeFunction(v2, v0, p);
    float w2 = -edgeFunction(v0, v1, p);

    Vector2 edge0 = v2.minus(v1);
    Vector2 edge1 = v0.minus(v2);
    Vector2 edge2 = v1.minus(v0);

    boolean overlaps = true;


    overlaps &= (w0 == 0 ? ((edge0.y == 0 && edge0.x > 0) || edge0.y > 0) : (w0 > 0));
    overlaps &= (w1 == 0 ? ((edge1.y == 0 && edge1.x > 0) || edge1.y > 0) : (w1 > 0));
    overlaps &= (w1 == 0 ? ((edge2.y == 0 && edge2.x > 0) || edge2.y > 0) : (w2 > 0));
    return overlaps;
  }

  private static float edgeFunction(Vector2 A, Vector2 B, Vector2 P) {
    return (A.x - B.x) * (P.y - A.y) - (A.y - B.y) * (P.x - A.x);
  }

  private BufferedImage blankDepthBuffer() {
    BufferedImage depthBuffer = new BufferedImage(this.screenWidth, this.screenHeight, BufferedImage.TYPE_INT_ARGB);
    for (int y = 0; y < this.screenHeight; y++) {
      for (int x = 0; x < this.screenWidth; x++) {
        depthBuffer.setRGB(x, y, Float.floatToIntBits(1.0f));
      }
    }
    return depthBuffer;
  }

  private BufferedImage blankScreenImage() {
    BufferedImage depthBuffer = new BufferedImage(this.screenWidth, this.screenHeight, BufferedImage.TYPE_INT_ARGB);
    for (int y = 0; y < this.screenHeight; y++) {
      for (int x = 0; x < this.screenWidth; x++) {
        depthBuffer.setRGB(x, y, Color.gray.getRGB());
      }
    }
    return depthBuffer;
  }

  private static VertexToFragment barycentricInterpolation(Vector2 point,
                                                           VertexToFragment vert1, VertexToFragment vert2, VertexToFragment vert3,
                                                           TransformMatrix projection) {
    float totalArea = triangleArea(vert1.clipPos.xy(), vert2.clipPos.xy(), vert3.clipPos.xy());
    float a1 = triangleArea(vert2.clipPos.xy(), vert3.clipPos.xy(), point) / totalArea;
    float a2 = triangleArea(vert1.clipPos.xy(), vert3.clipPos.xy(), point) / totalArea;
    float a3 = triangleArea(vert1.clipPos.xy(), vert2.clipPos.xy(), point) / totalArea;

    VertexToFragment resultant = new VertexToFragment();
    resultant.clipPos = new Vector3(point.x, point.y, 0.0f);
    float vert1EyeDepth = linearEyeDepth(vert1.clipPos.z, projection);
    float vert2EyeDepth = linearEyeDepth(vert2.clipPos.z, projection);
    float vert3EyeDepth = linearEyeDepth(vert3.clipPos.z, projection);
    resultant.clipPos.z = nonlinearDepth(trilinearInterpolate(vert1EyeDepth, vert2EyeDepth, vert3EyeDepth,
            a1, a2, a3, vert1EyeDepth, vert2EyeDepth, vert3EyeDepth), projection);
    resultant.normal = vert1.normal;
    resultant.uv = trilinearInterpolate(vert1.uv, vert2.uv, vert3.uv, a1, a2, a3,
            vert1EyeDepth, vert2EyeDepth, vert3EyeDepth);
    return resultant;
  }

  private static float trilinearInterpolate(float value1, float value2, float value3, float a1, float a2,
                                     float a3, float depth1, float depth2, float depth3) {
    return (value1 * a1 / depth1 + value2 * a2 / depth2 + value3 * a3 / depth3)
            / (a1 / depth1 + a2 / depth2 + a3 / depth3);
  }

  private static Vector2 trilinearInterpolate(Vector2 value1, Vector2 value2, Vector2 value3, float a1, float a2,
                                       float a3, float depth1, float depth2, float depth3) {
    float xNum = (value1.x * a1 / depth1 + value2.x * a2 / depth2 + value3.x * a3 / depth3);
    float yNum = (value1.y * a1 / depth1 + value2.y * a2 / depth2 + value3.y * a3 / depth3);
    return new Vector2(xNum, yNum).scale(1 / (a1 / depth1 + a2 / depth2 + a3 / depth3));
  }

  static float triangleArea(Vector2 point1, Vector2 point2, Vector2 point3) {
    return Math.abs(0.5f * (point1.x * (point2.y - point3.y) + point2.x * (point3.y - point1.y)
            + point3.x * (point1.y - point2.y)));
  }

  static float decodeDepth(int RGB) {
    return Float.intBitsToFloat(RGB);
    //return (RGB & 255) / 255.0f;
  }

  static int encodeDepth(float depth) {
    return Float.floatToIntBits(depth);
    //return new Color(depth, depth, depth, depth).getRGB();
  }

  public static float linearEyeDepth(float depth, TransformMatrix projectionMatrix) {
    float near = -projectionMatrix.get(2, 3) / projectionMatrix.get(2, 2);
    float far = near * projectionMatrix.get(2, 2) / (projectionMatrix.get(2, 2) - 1);
    return (far * near) / (far - (depth * (far - near)));
  }

  public static float nonlinearDepth(float eyeDepth, TransformMatrix projectionMatrix) {
    float near = -projectionMatrix.get(2, 3) / projectionMatrix.get(2, 2);
    float far = near * projectionMatrix.get(2, 2) / (projectionMatrix.get(2, 2) - 1);
    return far / (far - near) - (far * near) / (eyeDepth * (far - near));
  }

  public static Vector3 worldToClipPos(Vector3 worldPos, TransformMatrix viewMatrix, TransformMatrix projectionMatrix) {
    Vector3 viewPos = viewMatrix.apply(worldPos);
    Vector3 clipPos = projectionMatrix.apply(viewPos);
    return new Vector3(clipPos.x / 2 + 0.5f, clipPos.y / 2 + 0.5f, clipPos.z / 2 + 0.5f);
  }
}
