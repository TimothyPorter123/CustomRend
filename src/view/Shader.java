package view;

import java.awt.*;

import model.math.TransformMatrix;

public interface Shader {

  public VertexToFragment vert(VertexData v, ShaderData s);

  public Color frag(VertexToFragment o, ShaderData s);
}
