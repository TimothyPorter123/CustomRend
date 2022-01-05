package view.shaders;

import model.math.Vector4;
import view.ShaderData;
import view.VertexData;
import view.VertexToFragment;

public interface Shader {

  public VertexToFragment vert(VertexData v, ShaderData s);

  public Vector4 frag(VertexToFragment o, ShaderData s);
}
