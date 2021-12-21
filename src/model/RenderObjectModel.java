package model;

import model.math.TransformMatrix;
import model.math.Vector3;

public interface RenderObjectModel {

  public void setVertices(Vector3[] vertices);

  public void setTriangles(int[] triangles) throws IllegalArgumentException;

  public Vector3[] getVertices();

  public int[] getTriangles();

  public TransformMatrix getTransform();
}
