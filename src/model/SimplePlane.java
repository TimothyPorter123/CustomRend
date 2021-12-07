package model;

public final class SimplePlane extends SimpleModel {

  public SimplePlane() {
    super();
  }

  @Override
  protected void construct() {
    Vector3 base = new Vector3(0, 0, 0);
    Vector3 base1 = new Vector3(1, 0, 0);
    Vector3 base2 = new Vector3(1, 1, 0);
    Vector3 base3 = new Vector3(0, 1, 0);

    Vector3 top = new Vector3(0, 0, 1);
    Vector3 top1 = new Vector3(1, 0, 1);
    Vector3 top2 = new Vector3(1, 1, 1);
    Vector3 top3 = new Vector3(0, 1, 1);

    this.setVertices(new Vector3[] {base, base1, base2, base3, top, top1, top2, top3});

    int[] tris = new int[12];

    tris[0] = 0;
    tris[1] = 1;
    tris[2] = 2;

    tris[3] = 0;
    tris[4] = 2;
    tris[5] = 3;

    tris[6] = 4;
    tris[7] = 5;
    tris[8] = 6;

    tris[9] = 4;
    tris[10] = 6;
    tris[11] = 7;

    this.setTriangles(tris);
  }
}
