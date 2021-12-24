package model;

import model.math.TransformMatrix;
import model.math.Vector3;
import model.objects.Mesh;
import model.objects.Vertex;

public interface RenderObjectModel {

  public void setVertices(Vertex[] vertices);

  public void setEdges(int[] edges);

  public void setFaces(int[][] faces);

  public Mesh getRenderableMesh();

  public Vertex[] getVertices();

  public int[] getEdges();

  public int[][] getFaces();

  public TransformMatrix getTransform();
}
