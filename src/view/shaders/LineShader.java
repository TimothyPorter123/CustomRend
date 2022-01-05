package view.shaders;

import java.awt.Color;

import model.math.Vector4;
import view.ShaderData;
import view.VertexData;
import view.VertexToFragment;

public class LineShader implements Shader {

  Vector4 color;

  public LineShader(Vector4 color) {
    this.color = color;
  }

  public void setColor(Vector4 color) {
    this.color = color;
  }


  @Override
  public VertexToFragment vert(VertexData v, ShaderData s) {
    VertexToFragment returnFrag = new VertexToFragment();
    returnFrag.uv = v.texCoord;
    returnFrag.normal = v.normal;
    returnFrag.clipPos = s.worldToClipPos(v.objectPos);
    return returnFrag;
  }

  @Override
  public Vector4 frag(VertexToFragment o, ShaderData s) {
    return color;
  }
}
