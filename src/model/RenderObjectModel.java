package model;

import model.math.Transform;
import model.math.TransformMatrix;
import model.math.Vector2;
import model.math.Vector3;
import model.objects.Mesh;
import model.objects.Vertex;

public interface RenderObjectModel {

  public void setVertices(Vector3[] vertices);

  public void setEdges(int[] edges);

  public void setFaces(int[][] faces);

  public void setUVs(Vector2[][] uvs);

  public Mesh getRenderableMesh();

  public Vector3[] getVertices();

  public int[] getEdges();

  public int[][] getFaces();

  public Vector2[][] getUVs();

  public Transform getTransform();
}
