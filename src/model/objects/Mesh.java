package model.objects;

import model.math.Vector2;
import model.math.Vector3;

public class Mesh {
  public Vertex[] verts;
  public int[] tris;

  public Mesh(Vertex[] verts, int[] tris) {
    this.verts = verts;
    this.tris = tris;
  }
}
