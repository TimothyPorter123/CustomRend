package model;

import java.awt.*;
import java.awt.image.BufferedImage;

public class RenderOutput {

  public final BufferedImage image;
  public final BufferedImage depthBuffer;

  public RenderOutput(BufferedImage image, BufferedImage buffer) {
    this.image = image;
    this.depthBuffer = buffer;
  }

  public static RenderOutput blank(int width, int height) {
    BufferedImage depthBuffer = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
    BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
    for (int y = 0; y < height; y++) {
      for (int x = 0; x < width; x++) {
        depthBuffer.setRGB(x, y, Float.floatToIntBits(1.0f));
        image.setRGB(x, y, Color.darkGray.getRGB());
      }
    }
    return new RenderOutput(image, depthBuffer);
  }

}
