package model.objects;

import model.math.Vector3;

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


    int[][] faces = new int[4][3];

    faces[0][0] = 0;
    faces[0][1] = 1;
    faces[0][2] = 2;

    faces[1][0] = 0;
    faces[1][1] = 2;
    faces[1][2] = 3;

    faces[2][0] = 4;
    faces[2][1] = 5;
    faces[2][2] = 6;

    faces[3][0] = 4;
    faces[3][1] = 6;
    faces[3][2] = 7;

    this.setFaces(faces);
    this.generateUVs();
  }
}
