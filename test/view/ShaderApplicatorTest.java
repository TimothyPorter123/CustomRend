package view;

import org.junit.Test;

import model.Vector2;

import static org.junit.Assert.*;

public class ShaderApplicatorTest {



  @Test
  public void testTriangleArea() {
    assertEquals(0.5f, ShaderApplicator.triangleArea(
            new Vector2(0, 0), new Vector2(1, 0), new Vector2(0, 1)), 0.001f);
    assertEquals(1f, ShaderApplicator.triangleArea(
            new Vector2(0, 0), new Vector2(2, 0), new Vector2(0, 1)), 0.001f);
    assertEquals(1f, ShaderApplicator.triangleArea(
            new Vector2(0, 0), new Vector2(2, 0), new Vector2(1, 1)), 0.001f);
    assertEquals(2f, ShaderApplicator.triangleArea(
            new Vector2(0, 0), new Vector2(2, 0), new Vector2(1, 2)), 0.001f);
  }

  @Test
  public void testEncodeDecodeDepth() {
    assertEquals(0.71f, ShaderApplicator.decodeDepth(ShaderApplicator.encodeDepth(0.71f)), 0.01f);
    assertEquals(0.52f, ShaderApplicator.decodeDepth(ShaderApplicator.encodeDepth(0.52f)), 0.01f);
    assertEquals(0.02f, ShaderApplicator.decodeDepth(ShaderApplicator.encodeDepth(0.02f)), 0.01f);
    assertEquals(0.96f, ShaderApplicator.decodeDepth(ShaderApplicator.encodeDepth(0.96f)), 0.01f);
  }
}