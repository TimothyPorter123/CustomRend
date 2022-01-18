package model.objects;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import model.RenderObjectModel;
import model.math.Vector2;
import model.math.Vector3;

public abstract class SimpleModel extends WorldObject implements RenderObjectModel {

  Vector3[] vertices;
  int[] edges;
  int[][] faces;
  Vector2[][] faceVertUVs;
  Vector3[][] faceVertNormals;
  Vector3[] faceNormals;

  boolean smoothShading = false;

  public SimpleModel() {
    super();
    this.construct();
    this.recalculateNormals();
  }

  @Override
  public void setVertices(Vector3[] vertices) {
    this.vertices = vertices;
  }

  @Override
  public void setEdges(int[] edges) { this.edges = edges; }

  @Override
  public void setFaces(int[][] faces) { this.faces = faces; }

  @Override
  public void setUVs(Vector2[][] uvs) { this.faceVertUVs = uvs; }

  @Override
  public Vector3[] getVertices() { return this.vertices; }

  @Override
  public int[] getEdges() { return this.edges; }

  @Override
  public int[][] getFaces() { return this.faces; }

  @Override
  public Vector2[][] getUVs() { return this.faceVertUVs; }

  @Override
  public Mesh getRenderableMesh() {
    List<Vertex> meshVerts = new ArrayList<Vertex>();
    List<Integer> meshTris = new ArrayList<Integer>();
    for(int f = 0; f < this.faces.length; f++) {
      int meshVertsStartIndex = meshVerts.size();
      Vector3 netNormal = new Vector3(0, 0, 0);
      meshVerts.add(meshVertsStartIndex, new Vertex(this.vertices[this.faces[f][0]],
              this.faceVertNormals[f][0],
              this.faceVertUVs[f][0]));
      meshVerts.add(meshVertsStartIndex + 1, new Vertex(this.vertices[this.faces[f][1]],
              this.faceVertNormals[f][1],
              this.faceVertUVs[f][1]));
      for(int v = 2; v < this.faces[f].length; v++) {
        int currentIndex = meshVerts.size();
        meshVerts.add(currentIndex, new Vertex(this.vertices[this.faces[f][v]],
                this.faceVertNormals[f][v],
                this.faceVertUVs[f][v]));
        meshTris.add(currentIndex); meshTris.add(currentIndex - 1); meshTris.add(meshVertsStartIndex);
      }
    }
    Vertex[] rVerts = new Vertex[meshVerts.size()];
    int[] rTris = new int[meshTris.size()];
    rVerts = meshVerts.toArray(rVerts);
    for(int i = 0; i < meshTris.size(); i++) { rTris[i] = meshTris.get(i); }
    return new Mesh(rVerts, rTris);
  }

  public void setSmoothShading(boolean smooth) {
    this.smoothShading = smooth;
    this.recalculateNormals();
  }

  private void recalculateNormals() {
    this.recalculateFaceNormals();
    if(this.smoothShading) {
      this.recalculateVertexNormalsSmooth();
    } else {
      this.recalculateVertexNormalsFlat();
    }
  }

  private void recalculateFaceNormals() {
    this.faceNormals = new Vector3[faces.length];
    for(int f = 0; f < this.faces.length; f++) {
      Vector3 netNormal = new Vector3(0, 0, 0);
      for(int v = 2; v < this.faces[f].length; v++) {

        Vector3 triNormal = Vector3.cross(vertices[faces[f][v]].minus(vertices[faces[f][0]]),
                vertices[faces[f][v - 1]].minus(vertices[faces[f][0]])).normalized();
        netNormal = netNormal.plus(triNormal);
      }
      this.faceNormals[f] = netNormal.scale(-1.0f / (this.faces[f].length - 2));
    }
  }

  private void recalculateVertexNormalsFlat() {
    this.faceVertNormals = new Vector3[this.faces.length][];
    for(int f = 0; f < faces.length; f++) {
      this.faceVertNormals[f] = new Vector3[this.faces[f].length];
      for(int v = 0; v < faces[f].length; v++) {
        this.faceVertNormals[f][v] = this.faceNormals[f];
      }
    }
  }

  private void recalculateVertexNormalsSmooth() {
    Vector3[] vertexNormals = new Vector3[this.vertices.length];
    for(int v = 0; v < vertexNormals.length; v++) {
      int numVectors = 0;
      Vector3 sumNormal = new Vector3(0, 0, 0);
      for(int f = 0; f < this.faces.length; f++) {
        if(containsInt(faces[f], v)) {
          sumNormal = sumNormal.plus(this.faceNormals[f]);
          numVectors++;
        }
      }
      vertexNormals[v] = sumNormal.scale(1f / numVectors);
    }

    this.faceVertNormals = new Vector3[this.faces.length][];
    for(int f = 0; f < this.faces.length; f++) {
      this.faceVertNormals[f] = new Vector3[this.faces[f].length];
      for(int v = 0; v < this.faces[f].length; v++) {
        this.faceVertNormals[f][v] = vertexNormals[this.faces[f][v]];
      }
    }
  }

  //array contains method is going here because I don't know where else to put it, and it's honestly
  //absolute baffonery that java doesn't have this function prepackaged
  private boolean containsInt(int[] arr, int key) {
    for(int i : arr) {
      if(i == key) {
        return true;
      }
    }
    return false;
  }

  protected abstract void construct();

  public static SimpleModel empty() {
    return new SimpleModel() {
      @Override
      protected void construct() {
        this.setVertices(new Vector3[0]);
        this.setEdges(new int[0]);
        this.setFaces(new int[0][0]);
        this.setUVs(new Vector2[0][0]);
      }
    };
  }

  protected void generateUVs() {
    int[][] faces = this.getFaces();
    if(faces != null && faces.length > 0) {
      Vector2[][] uvs = new Vector2[faces.length][];
      for(int i = 0; i < faces.length; i++) {
        uvs[i] = new Vector2[faces[i].length];
        for(int o = 0; o < faces[i].length; o++) {
          uvs[i][o] = new Vector2(0, 0);
        }
      }
      this.setUVs(uvs);
    } else {
      this.setUVs(new Vector2[0][0]);
    }
  }

}
