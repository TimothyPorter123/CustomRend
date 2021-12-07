package view;

import java.awt.*;

import model.TransformMatrix;
import model.Vector3;

public class SimpleShader implements Shader {

  Color flatColor;
  TransformMatrix modelMatrix;
  TransformMatrix viewMatrix;
  TransformMatrix projectionMatrix;

  public SimpleShader(Color color) {
    this.flatColor = color;
    this.modelMatrix = new TransformMatrix();
  }

  @Override
  public VertexToFragment vert(VertexData v) {
    VertexToFragment v2f = new VertexToFragment();
    v2f.clipPos = ShaderApplicator.worldToClipPos(this.modelMatrix.apply(v.objectPos), this.viewMatrix, this.projectionMatrix);
    v2f.uv = v.texCoord;
    v2f.normal = v.normal;
    return v2f;
  }

  @Override
  public Color frag(VertexToFragment o) {
    Vector3 worldNormal = this.modelMatrix.apply(o.normal);
    return new Color(o.normal.x / 2 + 0.5f, o.normal.y / 2 + 0.5f, o.normal.z / 2 + 0.5f, 1);
    //return new Color(worldNormal.x / 2 + 0.5f, worldNormal.y / 2 + 0.5f, worldNormal.z / 2 + 0.5f, 1.0f);
    //return new Color(1 - o.clipPos.z, 1 - o.clipPos.z, 1 - o.clipPos.z, 1);
    //return new Color(o.clipPos.x, o.clipPos.y, 0, 1);
  }

  @Override
  public void setModelMatrix(TransformMatrix modelMatrix) {
    this.modelMatrix = modelMatrix;
  }

  @Override
  public void setViewMatrix(TransformMatrix viewMatrix) {
    this.viewMatrix = viewMatrix;
  }

  @Override
  public void setProjectionMatrix(TransformMatrix projectionMatrix) { this.projectionMatrix = projectionMatrix; }
}
