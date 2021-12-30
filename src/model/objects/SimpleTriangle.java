package model.objects;

import model.math.Vector2;
import model.math.Vector3;

public final class SimpleTriangle extends SimpleModel {

  public SimpleTriangle() {
    super();
  }

  @Override
  protected void construct() {
    Vector3 top = new Vector3(0, 2, 0);
    Vector3 left = new Vector3(2, 0, 0);
    Vector3 right = new Vector3(-2, 0, 0);
    this.setVertices(new Vector3[]{ right, left, top });
    int[][] faces = new int[1][2];
    faces[0] = new int[] { 0, 1, 2 };
    this.setFaces(faces);
    this.setEdges(new int[] { 0, 1, 1, 2, 2, 0});

    Vector2[][] uvs = new Vector2[1][2];
    uvs[0] = new Vector2[] { new Vector2(1, 1), new Vector2(0, 1), new Vector2(1f / 2, 1f / 2) };
    this.setUVs(uvs);
  }
}
