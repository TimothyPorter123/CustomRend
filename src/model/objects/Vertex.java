package model.objects;

import model.math.Vector2;
import model.math.Vector3;

public class Vertex {
  public final Vector3 position;
  public final Vector3 normal;
  public final Vector2 texCoord;

  public Vertex(Vector3 pos) {
    this(pos, new Vector3(0, 0, 1), new Vector2(0, 0));
  }

  public Vertex(float x, float y, float z) {
    this(new Vector3(x, y, z), new Vector3(0, 0, 1), new Vector2(0, 0));
  }

  public Vertex(Vector3 pos, Vector3 normal) {
    this(pos, normal, new Vector2(0, 0));
  }

  public Vertex(Vector3 pos, Vector3 normal, Vector2 uv) {
    this.position = pos;
    this.normal = normal;
    this.texCoord = uv;
  }

  public static Vertex lerp(Vertex v0, Vertex v1, float factor) {
    return new Vertex(Vector3.lerp(v0.position, v1.position, factor),
            Vector3.lerp(v0.normal, v1.normal, factor),
            Vector2.lerp(v0.texCoord, v1.texCoord, factor));
  }
}
