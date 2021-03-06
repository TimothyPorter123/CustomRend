package view.shaders;

import java.awt.*;
import java.awt.image.BufferedImage;

import model.math.Vector2;
import model.math.Vector4;
import view.ShaderData;
import view.VertexData;
import view.VertexToFragment;

public class TextureShader implements Shader {

  BufferedImage mainTex;

  public TextureShader(BufferedImage mainTex) {
    this.mainTex = mainTex;
  }

  public void setMainTex(BufferedImage mainTex) {
    this.mainTex = mainTex;
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
  public Vector4 frag(VertexToFragment o, ShaderData s) {
    Vector2 pixelLoc = o.uv.scale(mainTex.getWidth()).minus(new Vector2(1, 1).scale(0.00001f));
    int imageRGB = mainTex.getRGB((int)pixelLoc.x, (int)pixelLoc.y);
    return new Vector4(imageRGB);
  }
}
