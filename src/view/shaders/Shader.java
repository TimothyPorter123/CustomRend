package view.shaders;

import java.awt.*;

import view.ShaderData;
import view.VertexData;
import view.VertexToFragment;

public interface Shader {

  public VertexToFragment vert(VertexData v, ShaderData s);

  public Color frag(VertexToFragment o, ShaderData s);
}
