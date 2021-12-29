package model.objects;

import java.awt.image.BufferedImage;

import model.RenderObjectModel;
import model.RenderOutput;
import model.math.TransformMatrix;
import model.math.Vector2;
import model.math.Vector3;
import view.LineRendererMath;
import view.shaders.Shader;
import view.ShaderData;
import view.VertexData;
import view.VertexToFragment;

public abstract class Camera extends WorldObject {

  protected float nearClipPlane;
  protected float farClipPlane;
  protected TransformMatrix viewMatrix;

  public Camera() {
    this.viewMatrix = this.getTransform().inverse();
  }

  public abstract TransformMatrix getProjectionMatrix();

  public TransformMatrix getViewMatrix() { return this.viewMatrix; }

  public float getNearClipPlane() {
    return this.nearClipPlane;
  }

  public float getFarClipPlane() {
    return this.farClipPlane;
  }

  protected abstract boolean orthographic();

  @Override
  public void transform(TransformMatrix transform) {
    super.transform(transform);
    this.viewMatrix = this.getTransform().inverse();
  }

  public RenderOutput renderObjectSurfaceOver(RenderObjectModel model, Shader shader, RenderOutput previous) {
    BufferedImage buffer = previous.depthBuffer;
    BufferedImage base = previous.image;
    int screenWidth = base.getWidth();
    int screenHeight = base.getHeight();

    TransformMatrix M = model.getTransform();
    TransformMatrix V = this.getViewMatrix();
    TransformMatrix P = this.getProjectionMatrix();
    ShaderData sData = new ShaderData(M, V, P, screenWidth, screenHeight);

    Mesh toRender = model.getRenderableMesh();

    Vector3 viewPlanePosition = this.getTransform().apply(new Vector3(0, 0, 0));
    Vector3 viewPlaneNormal = this.getTransform().apply(new Vector3(0, 0, 1)).minus(viewPlanePosition).normalized();
    TransformMatrix IM = M.inverse();
    //backface culling
    if(this.orthographic()) {
      toRender.cullBackFacesOrtho(IM.apply(viewPlaneNormal));
    } else {
      toRender.cullBackFacesPerspective(IM.apply(viewPlanePosition));
    }
    //THIS DOES ALL BACK CAMERA CLIPPING
    viewPlanePosition = viewPlanePosition.plus(viewPlaneNormal.scale(this.getNearClipPlane()));
    toRender.clipAgainstPlane(IM.apply(viewPlanePosition), IM.apply(viewPlaneNormal));
    //clean mesh
    toRender.clean();

    Vertex[] verts = toRender.getVerts();
    int[] vertsToRender = toRender.getTris();
    VertexToFragment[] vertToFragData = new VertexToFragment[vertsToRender.length];

    for (int i = 0; i < vertsToRender.length; i += 3) {
      for (int v = i; v < i + 3; v++) {
        VertexData data = new VertexData();
        data.objectPos = verts[vertsToRender[v]].position;
        data.texCoord = verts[vertsToRender[v]].texCoord;
        data.normal = verts[vertsToRender[v]].normal;
        vertToFragData[v] = shader.vert(data, sData);
      }
    }

    for (int i = 0; i < vertToFragData.length; i += 3) {
      Vector2 clipPos1 = vertToFragData[i].clipPos.xy();
      Vector2 clipPos2 = vertToFragData[i + 1].clipPos.xy();
      Vector2 clipPos3 = vertToFragData[i + 2].clipPos.xy();

      Vector2 rastStart = new Vector2(1, 1);
      Vector2 rastEnd = new Vector2(0, 0);

      rastStart.x = Math.max(Math.min(clipPos1.x, Math.min(clipPos2.x, Math.min(clipPos3.x, 1))), 0) * screenWidth - 1;
      rastStart.y = Math.max(Math.min(clipPos1.y, Math.min(clipPos2.y, Math.min(clipPos3.y, 1))), 0) * screenHeight - 1;
      rastEnd.x = Math.min(Math.max(clipPos1.x, Math.max(clipPos2.x, Math.max(clipPos3.x, 0))), 1) * screenWidth + 1;
      rastEnd.y = Math.min(Math.max(clipPos1.y, Math.max(clipPos2.y, Math.max(clipPos3.y, 0))), 1) * screenHeight + 1;

      for (int y = (int)rastStart.y; y < (int)rastEnd.y; y++) {
        for (int x = (int)rastStart.x; x < (int)rastEnd.x; x++) {
          Vector2 point = new Vector2((float) x / screenWidth, (float) y / screenHeight);
          if (point.barycentricInside(clipPos1, clipPos2, clipPos3)) {
            VertexToFragment v2f = VertexToFragment.barycentricInterpolation(point, vertToFragData[i],
                    vertToFragData[i + 1], vertToFragData[i + 2], sData);
            if (v2f.clipPos.z < Float.intBitsToFloat(buffer.getRGB(x, y)) && v2f.clipPos.z > 0) {
              buffer.setRGB(x, y, Float.floatToIntBits(v2f.clipPos.z));
              base.setRGB(x, y, shader.frag(v2f, sData).getRGB());
            }
          }
        }
      }
    }
    return new RenderOutput(base, buffer);
  }

  public RenderOutput renderLineOver(Vector3 start, Vector3 end, Shader shader, RenderOutput previous) {

    BufferedImage buffer = previous.depthBuffer;
    BufferedImage base = previous.image;
    int screenWidth = base.getWidth();
    int screenHeight = base.getHeight();

    TransformMatrix M = new TransformMatrix();
    TransformMatrix V = this.getViewMatrix();
    TransformMatrix P = this.getProjectionMatrix();
    ShaderData sData = new ShaderData(M, V, P, base.getWidth(), base.getHeight());

    Vector3 viewPlanePosition = this.getTransform().apply(new Vector3(0, 0, 0));
    Vector3 viewPlaneNormal = this.getTransform().apply(new Vector3(0, 0, 1)).minus(viewPlanePosition).normalized();
    viewPlanePosition = viewPlanePosition.plus(viewPlaneNormal.scale(this.getNearClipPlane()));

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
    VertexToFragment vert1v2f = shader.vert(vert1Data, sData);
    VertexToFragment vert2v2f = shader.vert(vert2Data, sData);

    //short circuit non-visible lines
    if((vert1v2f.clipPos.x < 0 && vert2v2f.clipPos.x < 0)
            || (vert1v2f.clipPos.x > 1 && vert2v2f.clipPos.x > 1)
            || (vert1v2f.clipPos.y < 0 && vert2v2f.clipPos.y < 0)
            || (vert1v2f.clipPos.y > 1 && vert2v2f.clipPos.y > 1)) {
      return previous;
    }

    //clip to screen
    Vector2 point1 = vert1v2f.clipPos.xy();
    Vector2 point2 = vert2v2f.clipPos.xy();
    for(int i = 0; i < 2; i++) {
      if(point1.x < 0 || point1.x > 1) {
        float factor = point1.x < 0 ? point1.x : point1.x - 1;
        point1 = Vector2.lerp(point1, point2, factor / (point1.x - point2.x));
      }
      if(point1.y < 0 || point1.y > 1) {
        float factor = point1.y < 0 ? point1.y : point1.y - 1;
        point1 = Vector2.lerp(point1, point2, factor / (point1.y - point2.y));
      }
      Vector2 hold = point1;
      point1 = point2;
      point2 = hold;
    }
    vert1v2f  = LineRendererMath.onLineInterpolation(point1, vert1v2f, vert2v2f, sData);
    vert2v2f  = LineRendererMath.onLineInterpolation(point2, vert1v2f, vert2v2f, sData);

    //rasterize
    Vector2 pixelStart;
    Vector2 pixelEnd;

    float xIncrement;
    float yIncrement;
    float incrementCount;

    //X-centric or Y-centric change
    boolean xCentric = Math.abs(vert1v2f.clipPos.x - vert2v2f.clipPos.x) > Math.abs(vert1v2f.clipPos.y - vert2v2f.clipPos.y);
    //choose rightmost or higher point depending on change direction
    if((xCentric && vert1v2f.clipPos.x < vert2v2f.clipPos.x)
    || (!xCentric && vert1v2f.clipPos.y < vert2v2f.clipPos.y)) {
      pixelStart = new Vector2((int)(vert1v2f.clipPos.x * screenWidth), (int)(vert1v2f.clipPos.y * screenHeight));
      pixelEnd = new Vector2((int)(vert2v2f.clipPos.x * screenWidth), (int)(vert2v2f.clipPos.y * screenHeight));
    } else {
      pixelStart = new Vector2((int)(vert2v2f.clipPos.x * screenWidth), (int)(vert2v2f.clipPos.y * screenHeight));
      pixelEnd = new Vector2((int)(vert1v2f.clipPos.x * screenWidth), (int)(vert1v2f.clipPos.y * screenHeight));
    }

    //set values for iteration over line
    if(xCentric) {
      incrementCount = pixelEnd.x - pixelStart.x;
      xIncrement = 1;
      yIncrement = (pixelStart.y - pixelEnd.y) / (pixelStart.x - pixelEnd.x);
    } else {
      incrementCount = pixelEnd.y - pixelStart.y;
      xIncrement = (pixelStart.x - pixelEnd.x) / (pixelStart.y - pixelEnd.y);
      yIncrement = 1;
    }

    for(int i = 0; i < incrementCount + 1; i++) {
      int x = Math.round(pixelStart.x + i * xIncrement);
      int y = Math.round(pixelStart.y + i * yIncrement);
      if(x >= 0 && x < screenWidth && y >= 0 && y < screenHeight) {
        Vector2 point = new Vector2((float) x / screenWidth, (float) y / screenHeight);
        VertexToFragment v2f = LineRendererMath.onLineInterpolation(point, vert1v2f, vert2v2f, sData);
        if (v2f.clipPos.z < Float.intBitsToFloat(buffer.getRGB(x, y)) && v2f.clipPos.z > 0) {
          buffer.setRGB(x, y, Float.floatToIntBits(v2f.clipPos.z));
          base.setRGB(x, y, shader.frag(v2f, sData).getRGB());
        }
      }
    }

    return new RenderOutput(base, buffer);
  }
}
