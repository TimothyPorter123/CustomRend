package view;

import model.math.TransformMatrix;
import model.math.Vector3;

public class ShaderData {
  TransformMatrix M;
  TransformMatrix V;
  TransformMatrix P;
  int screenWidth;
  int screenHeight;

  public ShaderData(TransformMatrix M, TransformMatrix V, TransformMatrix P, int screenWidth, int screenHeight) {
    this.M = M;
    this.V = V;
    this.P = P;
    this.screenWidth = screenWidth;
    this.screenHeight = screenHeight;
  }


  public Vector3 worldToClipPos(Vector3 worldPos) {
    Vector3 viewPos = this.V.apply(worldPos);
    Vector3 clipPos = this.P.apply(viewPos);
    return new Vector3(clipPos.x / 2 + 0.5f, clipPos.y / 2 + 0.5f, clipPos.z / 2 + 0.5f);
  }

  public float linearEyeDepth(float depth) {
    float near = -P.get(2, 3) / P.get(2, 2);
    float far = near * P.get(2, 2) / (P.get(2, 2) - 1);
    return (far * near) / (far - (depth * (far - near)));
  }

  public float nonlinearDepth(float eyeDepth) {
    float near = -P.get(2, 3) / P.get(2, 2);
    float far = near * P.get(2, 2) / (P.get(2, 2) - 1);
    return far / (far - near) - (far * near) / (eyeDepth * (far - near));
  }
}
