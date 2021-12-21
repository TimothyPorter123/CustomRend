package model.objects;

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

    int[] tris = new int[36];

    tris[0] = 0;
    tris[1] = 4;
    tris[2] = 1;

    tris[3] = 1;
    tris[4] = 4;
    tris[5] = 5;

    tris[6] = 1;
    tris[7] = 5;
    tris[8] = 2;

    tris[9] = 2;
    tris[10] = 5;
    tris[11] = 6;

    tris[12] = 2;
    tris[13] = 6;
    tris[14] = 3;

    tris[15] = 3;
    tris[16] = 6;
    tris[17] = 7;

    tris[18] = 3;
    tris[19] = 7;
    tris[20] = 0;

    tris[21] = 0;
    tris[22] = 7;
    tris[23] = 4;

    tris[24] = 0;
    tris[25] = 1;
    tris[26] = 2;

    tris[27] = 0;
    tris[28] = 2;
    tris[29] = 3;

    tris[30] = 4;
    tris[31] = 6;
    tris[32] = 5;

    tris[33] = 4;
    tris[34] = 7;
    tris[35] = 6;

    this.setTriangles(tris);
  }
}
