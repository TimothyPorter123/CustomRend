package model.objects;

import model.math.Vector3;

public class SimpleTriangle extends SimpleModel {

  public SimpleTriangle() {
    super();
  }

  @Override
  protected void construct() {
    Vector3 top = new Vector3(0, 3, 0);
    Vector3 left = new Vector3(1, 0, 1);
    Vector3 right = new Vector3(-1, 0, -1);
    this.setVertices(new Vector3[]{ top, left, right });
    this.setTriangles(new int[]{ 0, 1, 2 });
  }
}
