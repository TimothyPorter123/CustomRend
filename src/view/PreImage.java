package view;

import java.awt.image.BufferedImage;

import model.math.Vector4;

public class PreImage {
  Vector4[][] pixels;
  public final int width;
  public final int height;

  public PreImage(int width, int height) {
    this(width, height, new Vector4(0, 0, 0, 1));
  }

  public PreImage(int width, int height, Vector4 background) {
    this.width = width;
    this.height = height;
    this.pixels = new Vector4[width][height];
    for(int y = 0; y < height; y++) {
      for(int x = 0; x < width; x++) {
        this.pixels[x][y] = background;
      }
    }
  }

  public Vector4 getPixel(int x, int y) {
    if(x >= 0 && y >= 0 && y < height && x < width) {
      return this.pixels[x][y];
    } else {
      throw new IllegalArgumentException("Invalid pixel selection: (" + x + ", " + y + ")");
    }
  }

  public void setPixel(int x, int y, Vector4 color) {
    if(x >= 0 && y >= 0 && y < height && x < width) {
      this.pixels[x][y] = color;
    } else {
      throw new IllegalArgumentException("Invalid pixel selection: (" + x + ", " + y + ")");
    }
  }

  public BufferedImage renderToImage() {
    BufferedImage render = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
    for(int y = 0; y < height; y++) {
      for(int x = 0; x < width; x++) {
        Vector4 c = this.getPixel(x, y);
        render.setRGB(x, y, this.getPixel(x, y).getRGB());
      }
    }
    return render;
  }
}
