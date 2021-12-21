package model.objects;

import model.RenderObjectModel;
import model.math.TransformMatrix;
import model.math.Vector3;

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

  @Override
  public TransformMatrix getTransform() { return this.transform; }

  protected abstract void construct();

  public static SimpleModel empty() {
    return new SimpleModel() {
      @Override
      protected void construct() {
        this.setVertices(new Vector3[0]);
        this.setTriangles(new int[0]);
      }
    };
  }
}
