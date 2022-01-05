package model;

import java.awt.*;
import java.awt.image.BufferedImage;

import model.math.Vector4;
import view.PreImage;

public class RenderOutput {

  public final PreImage image;
  public final PreImage depthBuffer;

  public RenderOutput(PreImage image, PreImage buffer) {
    this.image = image;
    this.depthBuffer = buffer;
  }

  public static RenderOutput blank(int width, int height) {
    PreImage depthBuffer = new PreImage(width, height, new Vector4(1.0f, 1.0f, 1.0f, 1.0f));
    PreImage image = new PreImage(width, height, new Vector4(0.666f, 0.666f, 0.666f, 1.0f));
    return new RenderOutput(image, depthBuffer);
  }

}
