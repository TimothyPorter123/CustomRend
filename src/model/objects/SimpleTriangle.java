package model.objects;

import model.math.Vector3;

public class SimpleTriangle extends SimpleModel {

  public SimpleTriangle() {
    super();
  }

  @Override
  protected void construct() {
    Vertex top = new Vertex(0, 3, 0);
    Vertex left = new Vertex(1, 0, 1);
    Vertex right = new Vertex(-1, 0, -1);
    this.setVertices(new Vertex[]{ top, left, right });
    int[][] faces = new int[1][2];
    faces[0] = new int[] { 0, 1, 2 };
    this.setFaces(faces);
  }
}
