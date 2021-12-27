package view;

import java.awt.*;

import model.math.Vector3;

public class SimpleShader implements Shader {

  Color flatColor;

  public SimpleShader(Color color) {
    this.flatColor = color;
  }

  @Override
  public VertexToFragment vert(VertexData v, ShaderData s) {
    VertexToFragment v2f = new VertexToFragment();
    v2f.clipPos = s.worldToClipPos(s.M.apply(v.objectPos));
    v2f.uv = v.texCoord;
    v2f.normal = v.normal;
    return v2f;
  }

  @Override
  public Color frag(VertexToFragment o, ShaderData s) {
    Vector3 worldNormal = s.M.apply(o.normal);
    return new Color(o.normal.x / 2 + 0.5f, o.normal.y / 2 + 0.5f, o.normal.z / 2 + 0.5f, 1);
    //return new Color(worldNormal.x / 2 + 0.5f, worldNormal.y / 2 + 0.5f, worldNormal.z / 2 + 0.5f, 1.0f);
    //return new Color(1 - o.clipPos.z, 1 - o.clipPos.z, 1 - o.clipPos.z, 1);
    //return new Color(o.clipPos.x, o.clipPos.y, 0, 1);
  }
}
