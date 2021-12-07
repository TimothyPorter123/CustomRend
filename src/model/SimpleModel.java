package model;

public abstract class SimpleModel extends WorldObject implements RenderObjectModel {

  Vector3[] vertices;
  int[] triangles;

  public SimpleModel() {
    super();
    this.construct();
  }

  @Override
  public void setVertices(Vector3[] vertices) {
    this.vertices = vertices;
  }

  @Override
  public void setTriangles(int[] triangles) throws IllegalArgumentException {
    this.triangles = triangles;
  }

  @Override
  public Vector3[] getVertices() {
    return this.vertices;
  }

  @Override
  public int[] getTriangles() {
    return this.triangles;
  }

  protected abstract void construct();
}
