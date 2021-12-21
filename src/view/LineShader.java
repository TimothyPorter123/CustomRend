package view;

import java.awt.Color;

import model.math.TransformMatrix;

public class LineShader implements Shader {

  Color color;
  TransformMatrix viewMatrix;
  TransformMatrix projectionMatrix;

  public LineShader(Color color) {
    this.color = color;
  }

  public void setColor(Color color) {
    this.color = color;
  }


  @Override
  public VertexToFragment vert(VertexData v) {
    VertexToFragment returnFrag = new VertexToFragment();
    returnFrag.uv = v.texCoord;
    returnFrag.normal = v.normal;
    returnFrag.clipPos = ShaderApplicator.worldToClipPos(v.objectPos, this.viewMatrix, this.projectionMatrix);
    return returnFrag;
  }

  @Override
  public Color frag(VertexToFragment o) {
    return color;
  }

  @Override
  public void setModelMatrix(TransformMatrix modelMatrix) {  }


  @Override
  public void setViewMatrix(TransformMatrix viewMatrix) {
    this.viewMatrix = viewMatrix;
  }

  @Override
  public void setProjectionMatrix(TransformMatrix projectionMatrix) { this.projectionMatrix = projectionMatrix; }
}
