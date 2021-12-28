package view.shaders;

import java.awt.Color;

import view.ShaderData;
import view.VertexData;
import view.VertexToFragment;

public class LineShader implements Shader {

  Color color;

  public LineShader(Color color) {
    this.color = color;
  }

  public void setColor(Color color) {
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
  public Color frag(VertexToFragment o, ShaderData s) {
    return color;
  }
}
