package view;

import java.awt.*;

import model.math.TransformMatrix;

public interface Shader {

  public VertexToFragment vert(VertexData v);

  public Color frag(VertexToFragment o);

  public void setModelMatrix(TransformMatrix modelMatrix);

  public void setViewMatrix(TransformMatrix viewMatrix);

  public void setProjectionMatrix(TransformMatrix projectionMatrix);
}
