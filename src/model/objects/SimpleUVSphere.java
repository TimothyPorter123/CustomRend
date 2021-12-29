package model.objects;

import model.math.Vector3;

public final class SimpleUVSphere extends SimpleModel {

  private final int segments = 16;
  private final int rings = 16;

  @Override
  protected void construct() {
    Vector3[] verts = new Vector3[(this.rings - 2) * this.segments + 2];
    int[][] faces = new int[(rings - 1) * segments][];
    verts[verts.length - 2] = new Vector3(0, 1, 0);
    verts[verts.length - 1] = new Vector3(0, -1, 0);

    float ringAngle = (float) (2 * Math.PI / this.segments);

    float height = (float)Math.cos(Math.PI / (this.rings - 1));
    float localRadius = (float) Math.sqrt(1 - (height * height));
    verts[0] = new Vector3(0, height, localRadius);

    verts[(this.rings - 3) * this.segments] = new Vector3(0, -height, localRadius);

    for (int i = 1; i < this.segments; i++) {
      verts[i] = new Vector3((float) Math.sin(ringAngle * i) * localRadius, height, (float) Math.cos(ringAngle * i) * localRadius);
      faces[i - 1] = new int[]{verts.length - 2, i - 1, i};

      verts[(this.rings - 3) * this.segments + i] = new Vector3((float) Math.sin(ringAngle * i) * localRadius, -height, (float) Math.cos(ringAngle * i) * localRadius);
      faces[(this.segments * (this.rings - 2)) + i - 1] = new int[]{verts.length - 1, (this.rings - 3) * this.segments + i, (this.rings - 3) * this.segments + i - 1};
    }
    faces[this.segments - 1] = new int[]{verts.length - 2, this.segments - 1, 0};

    faces[(this.segments * (this.rings - 1)) - 1] = new int[]{verts.length - 1, (this.rings - 3) * this.segments, (this.rings - 3) * this.segments + this.segments - 1};

    for (int r = 1; r < this.rings - 2; r++) {
      height = (float)Math.cos((r + 1) * Math.PI / (this.rings - 1));
      localRadius = (float) Math.sqrt(1 - (height * height));
      verts[this.segments * r] = new Vector3(0, height, localRadius);
      for (int i = 1; i < this.segments; i++) {
        verts[r * this.segments + i] = new Vector3((float) Math.sin(ringAngle * i) * localRadius, height, (float) Math.cos(ringAngle * i) * localRadius);
        faces[(this.segments * r) + i - 1] = new int[]{r * this.segments + i, (r - 1) * this.segments + i, (r - 1) * this.segments + i - 1, r * this.segments + i - 1};
      }
      faces[(this.segments * (r + 1)) - 1] = new int[]{this.segments * r, this.segments * (r - 1), this.segments * r - 1, this.segments * (r + 1) - 1};
    }

    this.setVertices(verts);
    this.setFaces(faces);
    this.generateUVs();
  }
}