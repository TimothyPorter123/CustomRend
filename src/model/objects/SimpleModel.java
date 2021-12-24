package model.objects;

import java.util.ArrayList;
import java.util.List;

import model.RenderObjectModel;
import model.math.TransformMatrix;
import model.math.Vector3;

public abstract class SimpleModel extends WorldObject implements RenderObjectModel {

  Vertex[] vertices;
  int[] edges;
  int[][] faces;
  Vector3[] faceNormals;

  boolean smoothShading = false;

  public SimpleModel() {
    super();
    this.construct();
    this.recalculateFaceNormals();
  }

  @Override
  public void setVertices(Vertex[] vertices) {
    this.vertices = vertices;
  }

  @Override
  public void setEdges(int[] edges) { this.edges = edges; }

  @Override
  public void setFaces(int[][] faces) { this.faces = faces; }

  @Override
  public Vertex[] getVertices() {
    return this.vertices;
  }

  @Override
  public int[] getEdges() { return this.edges; }

  @Override
  public int[][] getFaces() { return this.faces; }

  @Override
  public Mesh getRenderableMesh() {
    List<Vertex> meshVerts = new ArrayList<Vertex>();
    List<Integer> meshTris = new ArrayList<Integer>();
    for(int f = 0; f < this.faces.length; f++) {
      int meshVertsStartIndex = meshVerts.size();
      Vector3 netNormal = new Vector3(0, 0, 0);
      meshVerts.add(meshVertsStartIndex, new Vertex(this.vertices[this.faces[f][0]].position,
              this.faceNormals[f],
              this.vertices[this.faces[f][0]].texCoord));
      meshVerts.add(meshVertsStartIndex + 1, new Vertex(this.vertices[this.faces[f][1]].position,
              this.faceNormals[f],
              this.vertices[this.faces[f][1]].texCoord));
      for(int v = 2; v < this.faces[f].length; v++) {
        int currentIndex = meshVerts.size();
        meshVerts.add(currentIndex, new Vertex(this.vertices[this.faces[f][v]].position,
                this.faceNormals[f],
                this.vertices[this.faces[f][v]].texCoord));
        meshTris.add(currentIndex); meshTris.add(currentIndex - 1); meshTris.add(meshVertsStartIndex);
      }
    }
    Vertex[] rVerts = new Vertex[meshVerts.size()];
    int[] rTris = new int[meshTris.size()];
    rVerts = meshVerts.toArray(rVerts);
    for(int i = 0; i < meshTris.size(); i++) { rTris[i] = meshTris.get(i); }
    return new Mesh(rVerts, rTris);
  }

  private void recalculateFaceNormals() {
    this.faceNormals = new Vector3[faces.length];
    for(int f = 0; f < this.faces.length; f++) {
      Vector3 netNormal = new Vector3(0, 0, 0);
      for(int v = 2; v < this.faces[f].length; v++) {

        Vector3 triNormal = Vector3.cross(vertices[faces[f][v]].position.minus(vertices[faces[f][0]].position),
                vertices[faces[f][v - 1]].position.minus(vertices[faces[f][0]].position)).normalized();
        netNormal = netNormal.plus(triNormal);
      }
      this.faceNormals[f] = netNormal.scale(-1.0f / (this.faces[f].length - 2));
    }
  }

  @Override
  public TransformMatrix getTransform() { return this.transform; }

  protected abstract void construct();

  public static SimpleModel empty() {
    return new SimpleModel() {
      @Override
      protected void construct() {
        this.setVertices(new Vertex[0]);
        this.setEdges(new int[0]);
        this.setFaces(new int[0][0]);
      }
    };
  }
}
