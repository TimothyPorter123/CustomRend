package view;

import org.junit.Before;
import org.junit.Test;

import java.awt.*;

import model.math.TransformMatrix;
import model.math.Vector2;
import model.math.Vector3;
import model.math.Vector4;
import model.objects.Camera;
import model.objects.PerspectiveCamera;
import view.shaders.LineShader;
import view.shaders.Shader;

import static org.junit.Assert.*;

public class LineRendererMathTest {

  /*@Test
  public void testInterpolate() {
    Camera mainCam = new PerspectiveCamera(90, 0.1f, 1000f, (float)300 / 400);
    mainCam.transform(new TransformMatrix().move(new Vector3(3.0f, 3.0f, 3.0f)));
    mainCam.transform(new TransformMatrix().rotate(new Vector3(45, 225, 0)));

    Shader lineShade = new LineShader(new Vector4( 0, 0, 0, 1));

    VertexData v0 = new VertexData();
    v0.objectPos = new Vector3(3, 0, 0);
    v0.normal = new Vector3(-1, 0, 0);
    v0.texCoord = new Vector2(0, 0);

    VertexData v1 = new VertexData();
    v1.objectPos = new Vector3(-3, 0, 0);
    v1.normal = new Vector3(1, 0, 0);
    v1.texCoord = new Vector2(0, 0);

    VertexData v2 = new VertexData();
    v2.objectPos = new Vector3(0, 0, 0);
    v2.normal = new Vector3(1, 0, 0);
    v2.texCoord = new Vector2(0, 0);

    ShaderData s = new ShaderData(new TransformMatrix(), mainCam.getViewMatrix(), mainCam.getProjectionMatrix(), 400, 300);

    VertexToFragment top = lineShade.vert(v0, s);
    VertexToFragment bottom = lineShade.vert(v1, s);
    Vector3 middle = lineShade.vert(v2, s).clipPos;

    float middleDepth = LineRendererMath.onLineInterpolation(middle.xy(), top, bottom, s).clipPos.z;
    float middleActDepth = middle.z;

    assertEquals(middleActDepth, middleDepth, 0.001f);
  }*/
}