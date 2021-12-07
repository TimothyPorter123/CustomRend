package model;

import java.awt.image.BufferedImage;

public class RenderOutput {

  public final BufferedImage image;
  public final BufferedImage depthBuffer;

  public RenderOutput(BufferedImage image, BufferedImage buffer) {
    this.image = image;
    this.depthBuffer = buffer;
  }

}
