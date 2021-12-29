package model.objects;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import model.math.Vector2;
import model.math.Vector3;

public class Mesh {
  private List<Vertex> verts;
  private List<Integer> tris;

  public Mesh(Vertex[] verts, int[] tris) {
    this.verts = new ArrayList<Vertex>();
    for (Vertex vert : verts) {
      this.verts.add(vert);
    }
    this.tris = new ArrayList<Integer>();
    for (int i : tris) {
      this.tris.add(i);
    }
  }

  public Mesh(List<Vertex> verts, List<Integer> tris) {
    this.verts = verts;
    this.tris = tris;
  }

  public Vertex[] getVerts() {
    Vertex[] verts = new Vertex[this.verts.size()];
    for(int i = 0; i < this.verts.size(); i++) { verts[i] = this.verts.get(i); }
    return verts;
  }

  public int[] getTris() {
    int[] tris = new int[this.tris.size()];
    for(int i = 0; i < this.tris.size(); i++) { tris[i] = this.tris.get(i); }
    return tris;
  }

  public void clipAgainstPlane(Vector3 pos, Vector3 normal) {
    Vertex[] verts = this.getVerts();
    int[] tris = this.getTris();
    List<Integer> modTris = new ArrayList<Integer>();
    List<Vertex> modVerts = new ArrayList<Vertex>();
    modVerts.addAll(this.verts);

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

    this.verts = modVerts;
    this.tris = modTris;
    this.clean();
  }

  public void cullBackFacesPerspective(Vector3 cameraPos) {
    for(int f = 0; f < this.tris.size(); f += 3) {
      Vector3 faceNormal = Vector3.cross(this.verts.get(tris.get(f + 1)).position.minus(this.verts.get(tris.get(f)).position),
              this.verts.get(tris.get(f + 2)).position.minus(this.verts.get(tris.get(f)).position));
      Vector3 camToFaceVec = this.verts.get(tris.get(f)).position.minus(cameraPos);
      if(Vector3.dot(faceNormal, camToFaceVec) <= 0) {
        this.tris.remove(f); this.tris.remove(f); this.tris.remove(f);
        f -= 3;
      }
    }
  }

  public void cullBackFacesOrtho(Vector3 cameraNormal) {
    for(int f = 0; f < this.tris.size(); f += 3) {
      Vector3 faceNormal = Vector3.cross(this.verts.get(tris.get(f + 1)).position.minus(this.verts.get(tris.get(f)).position),
              this.verts.get(tris.get(f + 2)).position.minus(this.verts.get(tris.get(f)).position));
      if(Vector3.dot(faceNormal, cameraNormal) >= 0) {
        this.tris.remove(f); this.tris.remove(f); this.tris.remove(f);
        f -= 3;
      }
    }
  }

  //clear unused verts from the array
  public void clean() {
    for(int v = 0 ; v < this.verts.size(); v++) {
      if(!this.tris.contains(v)) {
        this.verts.remove(v);
        for(int i = 0; i < this.tris.size(); i++) {
          this.tris.set(i, this.tris.get(i) > v ? this.tris.get(i) - 1 : this.tris.get(i));
        }
        v--;
      }
    }
  }
}
