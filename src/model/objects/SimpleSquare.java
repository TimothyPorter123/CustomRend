package model.objects;

import model.math.Vector2;
import model.math.Vector3;

public final class SimpleSquare extends SimpleModel {

  public SimpleSquare() {
    super();
  }

  @Override
  protected void construct() {
    Vector3 base = new Vector3(-0.5f, -0.5f, -0.5f);
    Vector3 base1 = new Vector3(0.5f, -0.5f, -0.5f);
    Vector3 base2 = new Vector3(0.5f, 0.5f, -0.5f);
    Vector3 base3 = new Vector3(-0.5f, 0.5f, -0.5f);

    Vector3 top = new Vector3(-0.5f, -0.5f, 0.5f);
    Vector3 top1 = new Vector3(0.5f, -0.5f, 0.5f);
    Vector3 top2 = new Vector3(0.5f, 0.5f, 0.5f);
    Vector3 top3 = new Vector3(-0.5f, 0.5f, 0.5f);

    this.setVertices(new Vector3[] {base, base1, base2, base3, top, top1, top2, top3});

    int[][] faces = new int[6][4];
    faces[0] = new int[] { 0, 1, 5, 4};
    faces[1] = new int[] { 0, 3, 2, 1};
    faces[2] = new int[] { 3, 7, 6, 2};
    faces[3] = new int[] { 7, 4, 5, 6};
    faces[4] = new int[] { 1, 2, 6, 5};
    faces[5] = new int[] { 0, 4, 7, 3};
    this.setFaces(faces);

    Vector2[][] uvs = new Vector2[6][4];
    uvs[0] = new Vector2[] { new Vector2(0, 0), new Vector2(1f / 3, 0), new Vector2(1f / 3, 1f / 3), new Vector2(0, 1f / 3) };
    uvs[1] = new Vector2[] { new Vector2(1f / 3, 0), new Vector2(2f / 3,  0), new Vector2(2f / 3, 1f / 3), new Vector2(1f / 3, 1f / 3) };
    uvs[2] = new Vector2[] { new Vector2(2f / 3, 0), new Vector2(1,  0), new Vector2(1, 1f / 3), new Vector2(2f / 3, 1f / 3) };
    uvs[3] = new Vector2[] { new Vector2(0, 1f / 3), new Vector2(1f / 3,  1f / 3), new Vector2(1f / 3, 2f / 3), new Vector2(0, 2f / 3) };
    uvs[4] = new Vector2[] { new Vector2(1f / 3, 1f / 3), new Vector2(2f / 3,  1f / 3), new Vector2(2f / 3, 2f / 3), new Vector2(1f / 3, 2f / 3) };
    uvs[5] = new Vector2[] { new Vector2(2f / 3, 1f / 3), new Vector2(1,  1f / 3), new Vector2(1, 2f / 3), new Vector2(2f / 3, 2f / 3) };

    this.setUVs(uvs);
  }
}
