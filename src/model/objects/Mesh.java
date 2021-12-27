package model.objects;

import java.util.ArrayList;
import java.util.List;

import model.math.Vector2;
import model.math.Vector3;

public class Mesh {
  public Vertex[] verts;
  public int[] tris;

  public Mesh(Vertex[] verts, int[] tris) {
    this.verts = verts;
    this.tris = tris;
  }


  public Mesh clipAgainstPlane(Vector3 pos, Vector3 normal) {
    Vertex[] verts = this.verts;
    int[] tris = this.tris;
    List<Integer> modTris = new ArrayList<Integer>();
    List<Vertex> modVerts = new ArrayList<Vertex>();
    for(Vertex v : verts) {
      modVerts.add(v);
    }

    //do clipping on each triangle
    for(int i = 0; i < tris.length; i += 3) {
      Vertex v0 = verts[tris[i]];
      float dis0 = Vector3.dot(normal, v0.position.minus(pos));
      Vertex v1 = verts[tris[i + 1]];
      float dis1 = Vector3.dot(normal, v1.position.minus(pos));
      Vertex v2 = verts[tris[i + 2]];
      float dis2 = Vector3.dot(normal, v2.position.minus(pos));

      int sig0 = Float.floatToIntBits(dis0)>>>31;
      int sig1 = Float.floatToIntBits(dis1)>>>31;
      int sig2 = Float.floatToIntBits(dis2)>>>31;
      dis0 = Math.abs(dis0);
      dis1 = Math.abs(dis1);
      dis2 = Math.abs(dis2);

      if(sig0 == sig1 && sig1 == sig2) {
        if(sig0 == 0) {
          modTris.add(tris[i]); modTris.add(tris[i + 1]); modTris.add(tris[i + 2]);
        }
      } else {
        int newVertsIndex = modVerts.size();
        int standOutSig = 0;
        int standOutIndex = 0;
        int nextVertIndex = 0;
        int lastVertIndex = 0;

        if (sig0 == sig1) {
          standOutSig = sig2;
          standOutIndex = tris[i + 2];
          nextVertIndex = tris[i];
          lastVertIndex = tris[i + 1];

          modVerts.add(newVertsIndex, Vertex.lerp(v2, v0, dis2 / (dis0 + dis2)));
          modVerts.add(newVertsIndex + 1, Vertex.lerp(v2, v1, dis2 / (dis1 + dis2)));
        } else if (sig0 == sig2) {
          standOutSig = sig1;
          standOutIndex = tris[i + 1];
          nextVertIndex = tris[i + 2];
          lastVertIndex = tris[i];

          modVerts.add(newVertsIndex, Vertex.lerp(v1, v2, dis1 / (dis2 + dis1)));
          modVerts.add(newVertsIndex + 1, Vertex.lerp(v1, v0, dis1 / (dis0 + dis1)));
        } else {
          standOutSig = sig0;
          standOutIndex = tris[i];
          nextVertIndex = tris[i + 1];
          lastVertIndex = tris[i + 2];

          modVerts.add(newVertsIndex, Vertex.lerp(v0, v1, dis0 / (dis1 + dis0)));
          modVerts.add(newVertsIndex + 1, Vertex.lerp(v0, v2, dis0 / (dis2 + dis0)));
        }

        if (standOutSig == 0) {
          modTris.add(standOutIndex);
          modTris.add(newVertsIndex);
          modTris.add(newVertsIndex + 1);
        } else {
          modTris.add(newVertsIndex + 1);
          modTris.add(newVertsIndex);
          modTris.add(nextVertIndex);
          modTris.add(newVertsIndex + 1);
          modTris.add(nextVertIndex);
          modTris.add(lastVertIndex);
        }
      }
    }

    //clear now unused verts from the array
    for(int v = 0 ; v < modVerts.size(); v++) {
      if(!modTris.contains(v)) {
        modVerts.remove(v);
        for(int i = 0; i < modTris.size(); i++) {
          modTris.set(i, modTris.get(i) > v ? modTris.get(i) - 1 : modTris.get(i));
        }
        v--;
      }
    }

    //convert lists to arrays because apparently java doesn't have a way to do this which isn't awful
    Vertex[] newVerts = new Vertex[modVerts.size()];
    for(int v = 0; v < modVerts.size(); v++) { newVerts[v] = modVerts.get(v); }
    int[] newTris = new int[modTris.size()];
    for(int i = 0; i < modTris.size(); i++) { newTris[i] = modTris.get(i); }
    return new Mesh(newVerts, newTris);
  }
}
