package view;

import org.junit.Test;

import model.math.Vector2;

import static org.junit.Assert.*;

public class RendererMathTest {

  @Test
  public void triangleInterpolationTest() {
    Vector2 vert1 = new Vector2(0, 0);
    Vector2 vert2 = new Vector2(2, 1);
    Vector2 vert3 = new Vector2(0, 2);
    Vector2 p = new Vector2(1, 1);
    float w0 = RendererMath.edgeFunction(vert2, vert3, p);
    float w1 = RendererMath.edgeFunction(vert3, vert1, p);
    float w2 = RendererMath.edgeFunction(vert1, vert2, p);



    assertEquals(2, RendererMath.triangleArea(vert1, vert2, vert3), 0.001f);
    assertEquals(RendererMath.triangleArea(vert2, vert3, p) / 2,
            w0 / 2f / 2f, 0.001f);
    assertEquals(RendererMath.triangleArea(vert3, vert1, p) / 2,
            w1 / 2f / 2f, 0.001f);
    assertEquals(RendererMath.triangleArea(vert1, vert2, p) / 2,
            w2 / 2f / 2f, 0.001f);
  }

}