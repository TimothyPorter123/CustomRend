package model;

public interface RenderObjectModel {

  public void setVertices(Vector3[] vertices);

  public void setTriangles(int[] triangles) throws IllegalArgumentException;

  public Vector3[] getVertices();

  public int[] getTriangles();

  public void transform(TransformMatrix transformation);
}
