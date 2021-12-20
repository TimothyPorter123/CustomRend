package model;

public class Mesh {
  public Vector3[] verts;
  public int[] tris;

  public Mesh(Vector3[] verts, int[] tris) {
    this.verts = verts;
    this.tris = tris;
  }
}
